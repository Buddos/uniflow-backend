<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Course Unit Requests - UniFlow</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <style>
        * { font-family: 'Plus Jakarta Sans', sans-serif; }
        body { background: #f8fafc; color: #0f172a; display: flex; min-height: 100vh; }
        .sidebar { width: 240px; flex-shrink: 0; background: #0f172a; display: flex; flex-direction: column; padding: 1.25rem; gap: 1.5rem; position: fixed; left: 0; top: 0; height: 100vh; z-index: 50; overflow-y: auto; }
        .sidebar-link { display: flex; align-items: center; gap: 0.6rem; padding: 0.55rem 0.75rem; border-radius: 0.625rem; color: #94a3b8; text-decoration: none; font-size: 0.8125rem; font-weight: 500; transition: all 0.15s; }
        .sidebar-link:hover { background: #1e293b; color: #e2e8f0; }
        .sidebar-link.active { background: #1e293b; color: #ffffff; }
        .card { background: #fff; border-radius: 1rem; border: 1px solid #e2e8f0; box-shadow: 0 1px 8px -2px rgba(0,0,0,0.05); }
        .btn-primary { display:inline-flex; align-items:center; gap:0.4rem; background:linear-gradient(135deg,#3b82f6,#2563eb); color:#fff; padding:0.5rem 1rem; border-radius:0.5rem; font-size:0.8125rem; font-weight:600; border:none; cursor:pointer; text-decoration:none; transition:opacity 0.15s; }
        .btn-primary:hover { opacity:0.9; }
        .badge { display:inline-flex; align-items:center; padding:0.18rem 0.55rem; border-radius:9999px; font-size:0.65rem; font-weight:700; text-transform:uppercase; letter-spacing:0.04em; border:1px solid; }
        .badge-accepted   { background:#dcfce7; color:#15803d; border-color:#bbf7d0; }
        .badge-pending    { background:#fef9c3; color:#854d0e; border-color:#fde68a; }
        .badge-rejected   { background:#fee2e2; color:#b91c1c; border-color:#fecaca; }
        table { width:100%; border-collapse:collapse; }
        thead tr { background:#f8fafc; border-bottom:1px solid #e2e8f0; }
        th { text-align:left; font-size:0.7rem; font-weight:500; color:#64748b; padding:0.75rem 0.875rem; }
        tbody tr { border-top:1px solid rgba(226,232,240,0.5); transition:background 0.15s; }
        tbody tr:hover { background:#f8fafc; }
        td { padding:0.75rem 0.875rem; }
        /* Modal */
        .modal-bg { display:none; position:fixed; inset:0; background:rgba(15,23,42,0.5); z-index:200; align-items:center; justify-content:center; padding:1rem; }
        .modal-bg.open { display:flex; }
        .modal-box { background:#fff; border-radius:1rem; width:100%; max-width:28rem; box-shadow:0 20px 40px -8px rgba(0,0,0,0.2); padding:1.5rem; }
        .form-label { display:block; font-size:0.75rem; font-weight:500; color:#374151; margin-bottom:0.3rem; }
        .form-input { width:100%; padding:0.5rem 0.75rem; border:1px solid #e2e8f0; border-radius:0.5rem; font-size:0.875rem; outline:none; background:#fff; box-sizing:border-box; font-family:inherit; transition:border-color 0.15s; }
        .form-input:focus { border-color:#3b82f6; box-shadow:0 0 0 3px rgba(59,130,246,0.1); }
        select.form-input { appearance:none; background-image:url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='10' height='10' viewBox='0 0 24 24' fill='none' stroke='%2394a3b8' stroke-width='2'%3E%3Cpolyline points='6 9 12 15 18 9'/%3E%3C/svg%3E"); background-repeat:no-repeat; background-position:right 0.75rem center; }
        .form-grid-2 { display:grid; grid-template-columns:1fr 1fr; gap:0.75rem; }
    </style>
</head>
<body>

<!-- ═══ SIDEBAR ═══ -->
<nav class="sidebar">
    <div style="display:flex;align-items:center;gap:0.6rem;padding-bottom:0.5rem;border-bottom:1px solid #1e293b;">
        <div style="width:1.75rem;height:1.75rem;background:linear-gradient(135deg,#3b82f6,#2563eb);border-radius:0.375rem;display:flex;align-items:center;justify-content:center;">
            <svg xmlns="http://www.w3.org/2000/svg" style="width:1rem;height:1rem;fill:white;" viewBox="0 0 20 20"><path d="M10.394 2.08a1 1 0 00-.788 0l-7 3a1 1 0 000 1.84L5.25 8.051a.999.999 0 01.356-.257l4-1.714a1 1 0 11.788 1.838L7.667 9.088l1.94.831a1 1 0 00.787 0l7-3a1 1 0 000-1.838l-7-3z"/></svg>
        </div>
        <span style="color:#fff;font-weight:800;font-size:1rem;">UniFlow</span>
    </div>
    <div style="display:flex;flex-direction:column;gap:0.2rem;">
        <a href="/view/dashboard" class="sidebar-link">
            <svg xmlns="http://www.w3.org/2000/svg" style="width:1rem;height:1rem;" viewBox="0 0 20 20" fill="currentColor"><path d="M10.707 2.293a1 1 0 00-1.414 0l-7 7a1 1 0 001.414 1.414L4 10.414V17a1 1 0 001 1h2a1 1 0 001-1v-2a1 1 0 011-1h2a1 1 0 011 1v2a1 1 0 001 1h2a1 1 0 001-1v-6.586l.293.293a1 1 0 001.414-1.414l-7-7z"/></svg>
            Dashboard
        </a>
        <a href="/view/timetable" class="sidebar-link">
            <svg xmlns="http://www.w3.org/2000/svg" style="width:1rem;height:1rem;" viewBox="0 0 20 20" fill="currentColor"><path fill-rule="evenodd" d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z" clip-rule="evenodd"/></svg>
            Timetable
        </a>
        <a href="/view/courserequest" class="sidebar-link active">
            <svg xmlns="http://www.w3.org/2000/svg" style="width:1rem;height:1rem;" viewBox="0 0 20 20" fill="currentColor"><path d="M7 3a1 1 0 000 2h6a1 1 0 100-2H7zM4 7a1 1 0 011-1h10a1 1 0 110 2H5a1 1 0 01-1-1zM2 11a2 2 0 012-2h12a2 2 0 012 2v4a2 2 0 01-2 2H4a2 2 0 01-2-2v-4z"/></svg>
            Course Requests
        </a>
        <a href="/view/equipment" class="sidebar-link">
            <svg xmlns="http://www.w3.org/2000/svg" style="width:1rem;height:1rem;" viewBox="0 0 20 20" fill="currentColor"><path fill-rule="evenodd" d="M11.49 3.17c-.38-1.56-2.6-1.56-2.98 0a1.532 1.532 0 01-2.286.948c-1.372-.836-2.942.734-2.106 2.106.54.886.061 2.042-.947 2.287-1.561.379-1.561 2.6 0 2.978a1.532 1.532 0 01.947 2.287c-.836 1.372.734 2.942 2.106 2.106a1.532 1.532 0 012.287.947c.379 1.561 2.6 1.561 2.978 0a1.533 1.533 0 012.287-.947c1.372.836 2.942-.734 2.106-2.106a1.533 1.533 0 01.947-2.287c1.561-.379 1.561-2.6 0-2.978a1.532 1.532 0 01-.947-2.287c.836-1.372-.734-2.942-2.106-2.106a1.532 1.532 0 01-2.287-.947zM10 13a3 3 0 100-6 3 3 0 000 6z" clip-rule="evenodd"/></svg>
            Equipment
        </a>
    </div>
    <div style="margin-top:auto;padding-top:1rem;border-top:1px solid #1e293b;display:flex;align-items:center;gap:0.6rem;">
        <div style="width:2rem;height:2rem;background:#1e293b;border-radius:9999px;display:flex;align-items:center;justify-content:center;font-weight:700;color:#fff;font-size:0.65rem;">UP</div>
        <div><p style="color:#fff;font-weight:700;font-size:0.75rem;margin:0;">Admin</p><p style="color:#64748b;font-size:0.65rem;margin:0;">Coordinator</p></div>
    </div>
</nav>

<!-- ═══ MAIN ═══ -->
<main style="margin-left:240px;flex:1;padding:2rem;min-height:100vh;">
    <div style="max-width:1280px;margin:0 auto;display:flex;flex-direction:column;gap:1.5rem;">

        <!-- Header row -->
        <div style="display:flex;align-items:flex-start;justify-content:space-between;flex-wrap:wrap;gap:0.75rem;">
            <div>
                <h1 style="font-size:1.5rem;font-weight:800;margin:0 0 0.25rem;">Course Unit Requests</h1>
                <p style="color:#64748b;font-size:0.8125rem;margin:0;">Inter-departmental course coordination</p>
            </div>
            <button class="btn-primary" onclick="openModal()">
                <svg xmlns="http://www.w3.org/2000/svg" style="width:1rem;height:1rem;" viewBox="0 0 20 20" fill="currentColor"><path fill-rule="evenodd" d="M10 3a1 1 0 011 1v5h5a1 1 0 110 2h-5v5a1 1 0 11-2 0v-5H4a1 1 0 110-2h5V4a1 1 0 011-1z" clip-rule="evenodd"/></svg>
                New Request
            </button>
        </div>

        <!-- Success toast -->
        <c:if test="${not empty param.success}">
            <div style="background:#dcfce7;border:1px solid #bbf7d0;color:#15803d;padding:0.75rem 1rem;border-radius:0.625rem;font-size:0.8125rem;font-weight:600;">
                ✓ Course unit request submitted successfully!
            </div>
        </c:if>

        <!-- Requests table -->
        <div class="card" style="overflow:hidden;">
            <div style="overflow-x:auto;">
                <table>
                    <thead>
                        <tr>
                            <th>Course</th>
                            <th>From &#8594; To</th>
                            <th>Students</th>
                            <th>Date</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty requests}">
                                <c:forEach var="r" items="${requests}">
                                    <tr>
                                        <%-- Course name + code (mirrors React: courseUnit + courseCode) --%>
                                        <td>
                                            <p style="font-size:0.875rem;font-weight:500;margin:0 0 0.15rem;">${r.courseUnit != null ? r.courseUnit.name : r.comments}</p>
                                            <p style="font-size:0.72rem;color:#64748b;font-family:monospace;margin:0;">${r.courseUnit != null ? r.courseUnit.code : ''}</p>
                                        </td>
                                        <%-- requestingDept → providingDept --%>
                                        <td style="font-size:0.875rem;color:#64748b;">${r.requestingDepartment} &#8594; ${r.providingDepartment}</td>
                                        <%-- expectedStudents = cohortSize --%>
                                        <td style="font-size:0.875rem;">${r.expectedStudents}</td>
                                        <%-- requestedAt date --%>
                                        <td style="font-size:0.875rem;color:#64748b;">
                                            <c:choose>
                                                <c:when test="${r.requestedAt != null}">${r.requestedAt}</c:when>
                                                <c:otherwise>—</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <%-- Status badge --%>
                                        <td>
                                            <c:choose>
                                                <c:when test="${r.status == 'ACCEPTED'}">
                                                    <span class="badge badge-accepted">Accepted</span>
                                                </c:when>
                                                <c:when test="${r.status == 'REJECTED'}">
                                                    <span class="badge badge-rejected">Rejected</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-pending">Pending</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="5" style="text-align:center;padding:4rem 1rem;color:#64748b;font-size:0.875rem;">
                                        No requests yet. Click <strong>New Request</strong> above to submit the first one.
                                        Data is managed by <strong>RequestServlet</strong> (/api/requests).
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>

    </div>
</main>

<!-- ═══ NEW REQUEST MODAL ═══ -->
<%-- Form POST goes directly to RequestServlet.doPost() at /api/requests --%>
<div id="requestModal" class="modal-bg" onclick="closeModalOnBg(event)">
    <div class="modal-box">
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:1.25rem;">
            <h2 style="font-size:1rem;font-weight:800;margin:0;">Submit Course Request</h2>
            <button onclick="closeModal()" style="background:none;border:none;cursor:pointer;color:#94a3b8;font-size:1.25rem;line-height:1;">&times;</button>
        </div>
        <form action="/api/requests" method="POST" style="display:flex;flex-direction:column;gap:1rem;">
            <%-- Row 1: Course Unit + Course Code --%>
            <div class="form-grid-2">
                <div>
                    <label class="form-label" for="courseUnit">Course Unit</label>
                    <input id="courseUnit" name="courseUnitId" class="form-input" required placeholder="e.g. Calculus I">
                </div>
                <div>
                    <label class="form-label" for="cohortSize">Expected Cohort Size</label>
                    <input id="cohortSize" name="cohortSize" type="number" min="1" class="form-input" required placeholder="200">
                </div>
            </div>
            <%-- Row 2: Requesting + Providing dept --%>
            <div class="form-grid-2">
                <div>
                    <label class="form-label">Requesting Dept</label>
                    <select name="requestingDept" required class="form-input">
                        <option value="">Select</option>
                        <option>Computer Science</option>
                        <option>Mathematics</option>
                        <option>Physics</option>
                        <option>Agriculture</option>
                        <option>Engineering</option>
                        <option>Education</option>
                        <option>Business Administration</option>
                    </select>
                </div>
                <div>
                    <label class="form-label">Providing Dept</label>
                    <select name="providingDept" required class="form-input">
                        <option value="">Select</option>
                        <option>Mathematics</option>
                        <option>Physics</option>
                        <option>Chemistry</option>
                        <option>Computer Science</option>
                        <option>Statistics</option>
                    </select>
                </div>
            </div>
            <button type="submit" class="btn-primary" style="width:100%;justify-content:center;padding:0.625rem;">Submit Request</button>
        </form>
    </div>
</div>

<script>
    function openModal()  { document.getElementById('requestModal').classList.add('open'); }
    function closeModal() { document.getElementById('requestModal').classList.remove('open'); }
    function closeModalOnBg(e) { if (e.target.id === 'requestModal') closeModal(); }
</script>
</body>
</html>
