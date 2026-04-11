package com.uniflow.servlet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.uniflow.dto.CourseRequestDTO;
import com.uniflow.dto.RealtimeMessage;
import com.uniflow.dto.RequestResubmissionDTO;
import com.uniflow.model.CourseUnit;
import com.uniflow.model.CourseUnitRequest;
import com.uniflow.repository.CourseUnitRepository;
import com.uniflow.service.RealtimeService;
import com.uniflow.service.RequestService;
import com.uniflow.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class RequestServlet extends HttpServlet {

    private static final String VIEW_PATH = "/WEB-INF/jsp/courserequest.jsp";

    @Autowired
    private RequestService requestService;

    @Autowired
    private CourseUnitRepository courseUnitRepository;

    @Autowired
    private RealtimeService realtimeService;

    private final ObjectMapper objectMapper;

    public RequestServlet() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");

        String pathInfo = normalizePath(request.getPathInfo());

        try {
            if ("/stats".equals(pathInfo)) {
                writeJson(response, requestService.getRequestStatistics());
                return;
            }

            if (pathInfo.startsWith("/pending/")) {
                String department = pathInfo.substring("/pending/".length());
                writeJson(response, requestService.getPendingRequestsForDepartment(department));
                return;
            }

            List<CourseUnitRequest> requests = requestService.getAllRequests();
            String viewMode = Optional.ofNullable(request.getParameter("view")).orElse("");
            boolean wantsHtml = wantsHtml(request) || "handshake".equalsIgnoreCase(viewMode);

            if (wantsHtml) {
                prepareHandshakeAttributes(request, requests);
                request.setAttribute("requests", requests);
                request.setAttribute("title", "Course Unit Requests - UniFlow");
                request.getRequestDispatcher(VIEW_PATH).forward(request, response);
                return;
            }

            writeJson(response, requests);
        } catch (Exception e) {
            writeError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");

        String pathInfo = normalizePath(request.getPathInfo());

        try {
            if (isCreatePath(pathInfo)) {
                handleCreate(request, response);
                return;
            }

            if (isResubmitPath(pathInfo) || isProcessPath(pathInfo) || isLegacyDecisionPath(pathInfo)) {
                handleWorkflowAction(request, response, pathInfo);
                return;
            }

            writeError(response, HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
        } catch (IllegalStateException e) {
            writeError(response, HttpServletResponse.SC_CONFLICT, e.getMessage());
        } catch (RuntimeException e) {
            writeError(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            writeError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void handleCreate(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        CourseRequestDTO requestDTO = readCreateRequest(request);
        enforceSubmissionLock(requestDTO.getRequestingDepartmentCode());

        CourseUnit courseUnit = courseUnitRepository.findById(requestDTO.getCourseUnitId())
            .orElseThrow(() -> new RuntimeException("Course unit not found"));

        CourseUnitRequest courseUnitRequest = new CourseUnitRequest();
        courseUnitRequest.setCourseUnit(courseUnit);
        courseUnitRequest.setRequestingDepartment(requestDTO.getRequestingDepartmentCode());
        courseUnitRequest.setExpectedStudents(requestDTO.getExpectedStudents());
        courseUnitRequest.setProvidingDepartment(courseUnit.getDepartment());
        courseUnitRequest.setComments(requestDTO.getComments());

        CourseUnitRequest savedRequest = requestService.createRequest(courseUnitRequest);
        realtimeService.broadcastRequestChange(RealtimeMessage.OperationType.CREATE, savedRequest);

        if (isJsonRequest(request)) {
            writeJson(response, savedRequest);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/view/courserequest?success=1");
    }

    private void handleWorkflowAction(HttpServletRequest request, HttpServletResponse response, String pathInfo) throws IOException, ServletException {
        if (isResubmitPath(pathInfo)) {
            Long requestId = extractRequestId(request, pathInfo);
            RequestResubmissionDTO resubmissionDTO = readResubmissionRequest(request);
            String requestingDepartment = findRequestById(requestId)
                .map(CourseUnitRequest::getRequestingDepartment)
                .orElseThrow(() -> new RuntimeException("Request not found"));

            enforceSubmissionLock(requestingDepartment);

            CourseUnitRequest resubmittedRequest = requestService.resubmitRequest(
                requestId,
                resubmissionDTO.getExpectedStudents(),
                resubmissionDTO.getComments()
            );
            realtimeService.broadcastRequestChange(RealtimeMessage.OperationType.UPDATE, resubmittedRequest);
            writeActionResponse(request, response, resubmittedRequest);
            return;
        }

        Long requestId = extractRequestId(request, pathInfo);
        String decision = normalizeDecision(request, pathInfo);

        CourseUnitRequest processedRequest;
        if ("REJECT".equals(decision)) {
            String reason = Optional.ofNullable(request.getParameter("reason"))
                .orElseGet(() -> request.getParameter("rejectionReason"));
            processedRequest = requestService.rejectRequest(requestId, reason);
        } else {
            processedRequest = requestService.acceptRequest(requestId);
        }

        realtimeService.broadcastRequestChange(RealtimeMessage.OperationType.UPDATE, processedRequest);
        writeActionResponse(request, response, processedRequest);
    }

    private void writeActionResponse(HttpServletRequest request, HttpServletResponse response, CourseUnitRequest processedRequest) throws IOException, ServletException {
        if (isJsonRequest(request)) {
            writeJson(response, processedRequest);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/view/courserequest?success=1");
    }

    private void prepareHandshakeAttributes(HttpServletRequest request, List<CourseUnitRequest> requests) {
        String department = Optional.ofNullable(SessionUtil.getDepartment(request)).orElse(request.getParameter("department"));
        List<CourseUnitRequest> departmentRequests = filterByDepartment(requests, department);

        request.setAttribute("pendingIncoming", filterIncomingRequests(requests, department));
        request.setAttribute("pendingOutgoing", departmentRequests.stream()
            .filter(courseRequest -> "PENDING".equalsIgnoreCase(courseRequest.getStatus()))
            .sorted(byRequestedAt())
            .collect(Collectors.toList()));
        request.setAttribute("settledRequests", departmentRequests.stream()
            .filter(courseRequest -> isSettled(courseRequest.getStatus()))
            .sorted(byRequestedAt())
            .collect(Collectors.toList()));
    }

    private List<CourseUnitRequest> filterByDepartment(List<CourseUnitRequest> requests, String department) {
        if (department == null || department.isBlank()) {
            return List.of();
        }

        return requests.stream()
            .filter(courseRequest -> department.equalsIgnoreCase(courseRequest.getRequestingDepartment())
                || department.equalsIgnoreCase(courseRequest.getProvidingDepartment()))
            .sorted(byRequestedAt())
            .collect(Collectors.toList());
    }

    private List<CourseUnitRequest> filterIncomingRequests(List<CourseUnitRequest> requests, String department) {
        if (department == null || department.isBlank()) {
            return List.of();
        }

        return requests.stream()
            .filter(courseRequest -> department.equalsIgnoreCase(courseRequest.getProvidingDepartment()))
            .filter(courseRequest -> "PENDING".equalsIgnoreCase(courseRequest.getStatus()))
            .sorted(byRequestedAt())
            .collect(Collectors.toList());
    }

    private Comparator<CourseUnitRequest> byRequestedAt() {
        return Comparator.comparing(CourseUnitRequest::getRequestedAt, Comparator.nullsLast(Comparator.naturalOrder())).reversed();
    }

    private boolean isSettled(String status) {
        if (status == null) {
            return false;
        }

        return "ACCEPTED".equalsIgnoreCase(status) || "REJECTED".equalsIgnoreCase(status);
    }

    private void enforceSubmissionLock(String department) {
        if (department == null || department.isBlank()) {
            throw new IllegalStateException("Submitting department is required");
        }

        if (!requestService.getPendingRequestsForDepartment(department).isEmpty()) {
            throw new IllegalStateException("Cannot submit to DET: You have unacknowledged Service Requests.");
        }
    }

    private CourseRequestDTO readCreateRequest(HttpServletRequest request) throws IOException {
        if (isJsonRequest(request)) {
            JsonNode root = objectMapper.readTree(readBody(request));
            CourseRequestDTO requestDTO = new CourseRequestDTO();
            requestDTO.setCourseUnitId(root.path("courseUnitId").asLong());
            requestDTO.setExpectedStudents(root.path("expectedStudents").asInt(root.path("cohortSize").asInt()));
            requestDTO.setRequestingDepartmentCode(readTextNode(root, "requestingDepartmentCode", "requestingDept"));
            requestDTO.setComments(readTextNode(root, "comments", "courseUnit"));
            return requestDTO;
        }

        CourseRequestDTO requestDTO = new CourseRequestDTO();
        requestDTO.setCourseUnitId(parseLongParameter(request, "courseUnitId"));
        requestDTO.setExpectedStudents(parseIntParameter(request, "expectedStudents", "cohortSize"));
        requestDTO.setRequestingDepartmentCode(firstNonBlank(request.getParameter("requestingDepartmentCode"), request.getParameter("requestingDept")));
        requestDTO.setComments(firstNonBlank(request.getParameter("comments"), request.getParameter("courseUnit")));
        return requestDTO;
    }

    private RequestResubmissionDTO readResubmissionRequest(HttpServletRequest request) throws IOException {
        if (isJsonRequest(request)) {
            JsonNode root = objectMapper.readTree(readBody(request));
            RequestResubmissionDTO requestDTO = new RequestResubmissionDTO();
            requestDTO.setExpectedStudents(root.path("expectedStudents").asInt(root.path("cohortSize").asInt()));
            requestDTO.setComments(readTextNode(root, "comments", "courseUnit"));
            return requestDTO;
        }

        RequestResubmissionDTO requestDTO = new RequestResubmissionDTO();
        requestDTO.setExpectedStudents(parseIntParameter(request, "expectedStudents", "cohortSize"));
        requestDTO.setComments(firstNonBlank(request.getParameter("comments"), request.getParameter("courseUnit")));
        return requestDTO;
    }

    private String normalizeDecision(HttpServletRequest request, String pathInfo) {
        String decision = firstNonBlank(request.getParameter("decision"), request.getParameter("action"), request.getParameter("status"));
        if (decision == null) {
            if (pathInfo.endsWith("/reject")) {
                return "REJECT";
            }

            return "ACCEPT";
        }

        return decision.trim().toUpperCase();
    }

    private void writeJson(HttpServletResponse response, Object payload) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), payload);
    }

    private void writeError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");

        Map<String, String> error = new HashMap<>();
        error.put("error", message == null || message.isBlank() ? "Request failed" : message);
        objectMapper.writeValue(response.getWriter(), error);
    }

    private boolean wantsHtml(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        return accept != null && accept.contains("text/html");
    }

    private boolean isJsonRequest(HttpServletRequest request) {
        String contentType = request.getContentType();
        return contentType != null && contentType.toLowerCase().contains("application/json");
    }

    private boolean isCreatePath(String pathInfo) {
        return pathInfo.isEmpty() || "/".equals(pathInfo) || "/create".equals(pathInfo);
    }

    private boolean isResubmitPath(String pathInfo) {
        return pathInfo.endsWith("/resubmit");
    }

    private boolean isProcessPath(String pathInfo) {
        return pathInfo.endsWith("/process");
    }

    private boolean isLegacyDecisionPath(String pathInfo) {
        return pathInfo.endsWith("/accept") || pathInfo.endsWith("/reject");
    }

    private Long extractRequestId(HttpServletRequest request, String pathInfo) {
        String normalized = normalizePath(pathInfo);
        String[] segments = normalized.split("/");
        if (segments.length >= 2) {
            String candidate = segments[1];
            if (candidate.matches("\\d+")) {
                return Long.parseLong(candidate);
            }
        }

        String requestIdParameter = request.getParameter("requestId");
        if (requestIdParameter != null && requestIdParameter.matches("\\d+")) {
            return Long.parseLong(requestIdParameter);
        }

        throw new IllegalStateException("Request ID is required");
    }

    private Optional<CourseUnitRequest> findRequestById(Long requestId) {
        return requestService.getAllRequests().stream()
            .filter(courseRequest -> requestId.equals(courseRequest.getId()))
            .findFirst();
    }

    private String normalizePath(String pathInfo) {
        if (pathInfo == null || pathInfo.isBlank()) {
            return "";
        }

        return pathInfo.startsWith("/") ? pathInfo : "/" + pathInfo;
    }

    private String readBody(HttpServletRequest request) throws IOException {
        StringBuilder body = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }
        return body.toString();
    }

    private String readTextNode(JsonNode root, String primaryField, String fallbackField) {
        String primaryValue = root.path(primaryField).asText(null);
        if (primaryValue != null && !primaryValue.isBlank()) {
            return primaryValue;
        }

        String fallbackValue = root.path(fallbackField).asText(null);
        if (fallbackValue != null && !fallbackValue.isBlank()) {
            return fallbackValue;
        }

        return null;
    }

    private Long parseLongParameter(HttpServletRequest request, String parameterName) {
        String parameterValue = request.getParameter(parameterName);
        if (parameterValue == null || parameterValue.isBlank()) {
            throw new IllegalStateException(parameterName + " is required");
        }

        return Long.parseLong(parameterValue.trim());
    }

    private Integer parseIntParameter(HttpServletRequest request, String primaryName, String fallbackName) {
        String parameterValue = firstNonBlank(request.getParameter(primaryName), request.getParameter(fallbackName));
        if (parameterValue == null) {
            throw new IllegalStateException(primaryName + " is required");
        }

        return Integer.parseInt(parameterValue.trim());
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }
        return null;
    }
}