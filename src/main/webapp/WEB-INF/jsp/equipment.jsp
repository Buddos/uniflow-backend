<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Equipment Tracking - UniFlow</title>
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
        .badge { display:inline-flex; align-items:center; padding:0.2rem 0.6rem; border-radius:9999px; font-size:0.65rem; font-weight:700; text-transform:uppercase; letter-spacing:0.04em; border:1px solid; }
        .badge-available   { background:#dcfce7; color:#15803d; border-color:#bbf7d0; }
        .badge-in_use      { background:#dbeafe; color:#1e40af; border-color:#bfdbfe; }
        .badge-maintenance { background:#fef9c3; color:#854d0e; border-color:#fde68a; }
        .badge-other       { background:#f1f5f9; color:#475569; border-color:#e2e8f0; }
        table { width:100%; border-collapse:collapse; }
        thead tr { background:#f8fafc; border-bottom:1px solid #e2e8f0; }
        th { text-align:left; font-size:0.7rem; font-weight:500; color:#64748b; padding:0.75rem 0.875rem; }
        tbody tr { border-top:1px solid rgba(226,232,240,0.5); transition:background 0.15s; }
        tbody tr:hover { background:#f8fafc; }
        td { padding:0.75rem 0.875rem; }
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
        <a href="/view/courserequest" class="sidebar-link">
            <svg xmlns="http://www.w3.org/2000/svg" style="width:1rem;height:1rem;" viewBox="0 0 20 20" fill="currentColor"><path d="M7 3a1 1 0 000 2h6a1 1 0 100-2H7zM4 7a1 1 0 011-1h10a1 1 0 110 2H5a1 1 0 01-1-1zM2 11a2 2 0 012-2h12a2 2 0 012 2v4a2 2 0 01-2 2H4a2 2 0 01-2-2v-4z"/></svg>
            Course Requests
        </a>
        <a href="/view/equipment" class="sidebar-link active">
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

        <!-- Header -->
        <div>
            <h1 style="font-size:1.5rem;font-weight:800;margin:0 0 0.25rem;">Equipment Tracking</h1>
            <p style="color:#64748b;font-size:0.8125rem;margin:0;">Monitor equipment assignment and availability</p>
        </div>

        <!-- Table Card -->
        <div class="card" style="overflow:hidden;">
            <div style="overflow-x:auto;">
                <table>
                    <thead>
                        <tr>
                            <th>Equipment</th>
                            <th>Type</th>
                            <th>Assigned Venue</th>
                            <th>Resource Home</th>
                            <th>Status</th>
                            <th>Voucher</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty equipmentList}">
                                <c:forEach var="eq" items="${equipmentList}">
                                    <tr>
                                        <%-- Equipment name + serial --%>
                                        <td>
                                            <div style="display:flex;align-items:center;gap:0.5rem;">
                                                <svg xmlns="http://www.w3.org/2000/svg" style="width:1rem;height:1rem;color:#94a3b8;flex-shrink:0;" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M14.7 6.3a1 1 0 0 0 0 1.4l1.6 1.6a1 1 0 0 0 1.4 0l3.77-3.77a6 6 0 0 1-7.94 7.94l-6.91 6.91a2.12 2.12 0 0 1-3-3l6.91-6.91a6 6 0 0 1 7.94-7.94l-3.76 3.76z"/></svg>
                                                <span style="font-size:0.875rem;font-weight:500;">${eq.name}</span>
                                            </div>
                                        </td>
                                        <%-- Type --%>
                                        <td style="font-size:0.875rem;color:#64748b;">
                                            <c:choose>
                                                <c:when test="${not empty eq.type}">${eq.type}</c:when>
                                                <c:otherwise>—</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <%-- currentVenue = Assigned Venue --%>
                                        <td style="font-size:0.875rem;">
                                            <c:choose>
                                                <c:when test="${not empty eq.currentVenue}">${eq.currentVenue}</c:when>
                                                <c:otherwise>—</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <%-- homeDepartment = Resource Home --%>
                                        <td style="font-size:0.875rem;color:#64748b;">
                                            <c:choose>
                                                <c:when test="${not empty eq.homeDepartment}">${eq.homeDepartment}</c:when>
                                                <c:otherwise>—</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <%-- Status badge --%>
                                        <td>
                                            <c:choose>
                                                <c:when test="${eq.status == 'AVAILABLE'}">
                                                    <span class="badge badge-available">available</span>
                                                </c:when>
                                                <c:when test="${eq.status == 'IN_USE'}">
                                                    <span class="badge badge-in_use">in-use</span>
                                                </c:when>
                                                <c:when test="${eq.status == 'UNDER_MAINTENANCE'}">
                                                    <span class="badge badge-maintenance">maintenance</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-other">${eq.status}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <%-- QR / Digital Voucher --%>
                                        <td>
                                            <div style="width:2rem;height:2rem;border-radius:0.375rem;background:#f1f5f9;display:flex;align-items:center;justify-content:center;" title="Digital Equipment Voucher">
                                                <svg xmlns="http://www.w3.org/2000/svg" style="width:1rem;height:1rem;color:#64748b;" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/></svg>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="6" style="text-align:center;padding:4rem 1rem;color:#64748b;font-size:0.875rem;">
                                        No equipment registered yet. Data is fetched live from <strong>EquipmentServlet</strong> (/api/equipment).
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
</body>
</html>
