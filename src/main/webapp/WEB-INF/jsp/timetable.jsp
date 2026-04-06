<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Timetable - UniFlow</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <style>
        * { font-family: 'Plus Jakarta Sans', sans-serif; }
        body { background: #f8fafc; color: #0f172a; display: flex; min-height: 100vh; }
        /* ── Sidebar ── */
        .sidebar { width: 240px; flex-shrink: 0; background: #0f172a; display: flex; flex-direction: column; padding: 1.25rem; gap: 1.5rem; position: fixed; left: 0; top: 0; height: 100vh; z-index: 50; overflow-y: auto; }
        .sidebar-link { display: flex; align-items: center; gap: 0.6rem; padding: 0.55rem 0.75rem; border-radius: 0.625rem; color: #94a3b8; text-decoration: none; font-size: 0.8125rem; font-weight: 500; transition: all 0.15s; }
        .sidebar-link:hover { background: #1e293b; color: #e2e8f0; }
        .sidebar-link.active { background: #1e293b; color: #ffffff; }
        /* ── Card ── */
        .card { background: #fff; border-radius: 1rem; border: 1px solid #e2e8f0; box-shadow: 0 1px 8px -2px rgba(0,0,0,0.05); }
        /* ── Slot button ── */
        .slot-btn { width: 100%; text-align: left; padding: 0.5rem 0.625rem; border-radius: 0.375rem; border: 1px solid transparent; font-size: 0.72rem; transition: box-shadow 0.15s, transform 0.15s; cursor: pointer; background: none; }
        .slot-btn:hover { box-shadow: 0 4px 12px -2px rgba(0,0,0,0.12); transform: translateY(-1px); }
        .empty-slot { height: 4rem; border-radius: 0.375rem; background: #f1f5f9; }
        /* ── Dept color presets ── */
        .dept-cs   { background:#dbeafe; color:#1e40af; border-color:#bfdbfe; }
        .dept-math { background:#dcfce7; color:#15803d; border-color:#bbf7d0; }
        .dept-phy  { background:#fef9c3; color:#854d0e; border-color:#fde68a; }
        .dept-eng  { background:#fee2e2; color:#b91c1c; border-color:#fecaca; }
        .dept-bus  { background:#ede9fe; color:#5b21b6; border-color:#ddd6fe; }
        .dept-def  { background:#f1f5f9; color:#475569; border-color:#e2e8f0; }
        /* ── Badge ── */
        .badge { display:inline-flex; align-items:center; padding:0.15rem 0.55rem; border-radius:9999px; font-size:0.65rem; font-weight:700; text-transform:uppercase; letter-spacing:0.04em; border:1px solid; }
        /* ── Modal ── */
        .modal-bg { display:none; position:fixed; inset:0; background:rgba(15,23,42,0.5); z-index:200; align-items:center; justify-content:center; padding:1rem; }
        .modal-bg.open { display:flex; }
        .modal-box { background:#fff; border-radius:1rem; width:100%; max-width:26rem; box-shadow:0 20px 40px -8px rgba(0,0,0,0.2); padding:1.5rem; }
        .detail-row { display:flex; flex-direction:column; gap:0.15rem; }
        .detail-label { font-size:0.7rem; color:#94a3b8; }
        .detail-val { font-size:0.875rem; font-weight:600; color:#0f172a; }
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
        <a href="/view/timetable" class="sidebar-link active">
            <svg xmlns="http://www.w3.org/2000/svg" style="width:1rem;height:1rem;" viewBox="0 0 20 20" fill="currentColor"><path fill-rule="evenodd" d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z" clip-rule="evenodd"/></svg>
            Timetable
        </a>
        <a href="/view/courserequest" class="sidebar-link">
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

        <!-- Header -->
        <div>
            <h1 style="font-size:1.5rem;font-weight:800;margin:0 0 0.25rem;">Timetable</h1>
            <p style="color:#64748b;font-size:0.8125rem;margin:0;">Weekly schedule overview — click a slot for details</p>
        </div>

        <!-- Grid Card -->
        <div class="card" style="overflow:hidden;">
            <div style="overflow-x:auto;">
                <table style="width:100%;border-collapse:collapse;min-width:700px;">
                    <thead>
                        <tr style="background:#f8fafc;border-bottom:1px solid #e2e8f0;">
                            <th style="text-align:left;font-size:0.7rem;font-weight:500;color:#64748b;padding:0.75rem 0.875rem;width:6rem;">Time</th>
                            <th style="text-align:left;font-size:0.7rem;font-weight:500;color:#64748b;padding:0.75rem 0.875rem;">Monday</th>
                            <th style="text-align:left;font-size:0.7rem;font-weight:500;color:#64748b;padding:0.75rem 0.875rem;">Tuesday</th>
                            <th style="text-align:left;font-size:0.7rem;font-weight:500;color:#64748b;padding:0.75rem 0.875rem;">Wednesday</th>
                            <th style="text-align:left;font-size:0.7rem;font-weight:500;color:#64748b;padding:0.75rem 0.875rem;">Thursday</th>
                            <th style="text-align:left;font-size:0.7rem;font-weight:500;color:#64748b;padding:0.75rem 0.875rem;">Friday</th>
                        </tr>
                    </thead>
                    <tbody id="timetableBody">
                        <%-- Rows built by JavaScript from server-injected JSON --%>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Legend -->
        <div style="display:flex;flex-wrap:wrap;gap:0.5rem;">
            <span class="badge dept-cs">Computer Science</span>
            <span class="badge dept-math">Mathematics</span>
            <span class="badge dept-phy">Physics</span>
            <span class="badge dept-eng">Engineering</span>
            <span class="badge dept-bus">Business</span>
            <span class="badge dept-def">Other</span>
        </div>

    </div>
</main>

<!-- ═══ SLOT DETAIL MODAL ═══ -->
<div id="slotModal" class="modal-bg" onclick="closeModalOnBg(event)">
    <div class="modal-box">
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:1.25rem;">
            <h2 id="modalTitle" style="font-size:1rem;font-weight:800;margin:0;"></h2>
            <button onclick="closeModal()" style="background:none;border:none;cursor:pointer;color:#94a3b8;font-size:1.25rem;line-height:1;">&times;</button>
        </div>
        <div style="display:grid;grid-template-columns:1fr 1fr;gap:0.875rem;" id="modalBody"></div>
    </div>
</div>

<!-- Inject server data as JSON for JavaScript to consume -->
<script id="ttData" type="application/json">
[
<c:forEach var="e" items="${entries}" varStatus="loop">
    {
        "id":        ${e.id},
        "day":       "${e.dayOfWeek}",
        "startTime": "${e.startTime}",
        "endTime":   "${e.endTime}",
        "code":      "${e.courseUnit != null ? e.courseUnit.code : ''}",
        "name":      "${e.courseUnit != null ? e.courseUnit.name : 'Session'}",
        "dept":      "${e.courseUnit != null ? e.courseUnit.department : ''}",
        "venue":     "${e.venue != null ? e.venue.name : ''}",
        "lecturer":  "${e.lecturer != null ? e.lecturer.name : ''}",
        "cohort":    "${e.cohort != null ? e.cohort : ''}",
        "students":  ${e.expectedStudents != null ? e.expectedStudents : 0},
        "color":     "${e.colorCode != null ? e.colorCode : '#3b82f6'}"
    }${!loop.last ? ',' : ''}
</c:forEach>
]
</script>

<script>
(function() {
    var TIME_SLOTS = ['7:00-9:00','9:00-11:00','11:00-13:00','14:00-16:00','16:00-18:00'];
    var DAYS       = ['MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY'];
    var DAY_LABELS = {MONDAY:'Monday',TUESDAY:'Tuesday',WEDNESDAY:'Wednesday',THURSDAY:'Thursday',FRIDAY:'Friday'};

    var deptClass = function(dept) {
        if (!dept) return 'dept-def';
        var d = dept.toLowerCase();
        if (d.includes('computer') || d.includes('cs'))  return 'dept-cs';
        if (d.includes('math'))  return 'dept-math';
        if (d.includes('phys'))  return 'dept-phy';
        if (d.includes('eng'))   return 'dept-eng';
        if (d.includes('bus'))   return 'dept-bus';
        return 'dept-def';
    };

    var entries = [];
    try { entries = JSON.parse(document.getElementById('ttData').textContent); } catch(e) {}

    function getSlot(day, time) {
        var startHour = parseInt(time.split(':')[0]);
        return entries.find(function(e) {
            var eHour = e.startTime ? parseInt(e.startTime.split(':')[0]) : -1;
            return e.day === day && eHour === startHour;
        });
    }

    var tbody = document.getElementById('timetableBody');
    TIME_SLOTS.forEach(function(time) {
        var tr = document.createElement('tr');
        tr.style.borderTop = '1px solid #e2e8f080';

        var tdTime = document.createElement('td');
        tdTime.style.cssText = 'padding:0.375rem 0.875rem;font-size:0.72rem;font-weight:500;color:#64748b;white-space:nowrap;';
        tdTime.textContent = time;
        tr.appendChild(tdTime);

        DAYS.forEach(function(day) {
            var td = document.createElement('td');
            td.style.cssText = 'padding:0.375rem;';
            var slot = getSlot(day, time);
            if (slot) {
                var btn = document.createElement('button');
                btn.className = 'slot-btn ' + deptClass(slot.dept);
                btn.innerHTML =
                    '<p style="font-weight:600;margin:0 0 0.1rem;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">' + (slot.code || '—') + '</p>' +
                    '<p style="margin:0 0 0.1rem;opacity:0.8;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">' + (slot.name || '') + '</p>' +
                    '<p style="margin:0;opacity:0.6;">' + (slot.venue || '') + '</p>';
                btn.onclick = function() { openModal(slot); };
                td.appendChild(btn);
            } else {
                var empty = document.createElement('div');
                empty.className = 'empty-slot';
                td.appendChild(empty);
            }
            tr.appendChild(td);
        });
        tbody.appendChild(tr);
    });

    // Modal
    var currentSlot = null;
    window.openModal = function(slot) {
        currentSlot = slot;
        document.getElementById('modalTitle').textContent = slot.name || 'Session';
        var fields = [
            ['Code',       slot.code],
            ['Day',        DAY_LABELS[slot.day] || slot.day],
            ['Time',       slot.startTime + (slot.endTime ? ' – ' + slot.endTime : '')],
            ['Venue',      slot.venue],
            ['Lecturer',   slot.lecturer || '—'],
            ['Cohort',     slot.cohort || '—'],
            ['Students',   slot.students],
            ['Department', slot.dept || '—']
        ];
        var body = document.getElementById('modalBody');
        body.innerHTML = '';
        fields.forEach(function(f, i) {
            var div = document.createElement('div');
            div.className = 'detail-row';
            if (i === fields.length - 1) div.style.gridColumn = '1 / -1';
            div.innerHTML = '<span class="detail-label">' + f[0] + ':</span><span class="detail-val">' + (f[1] || '—') + '</span>';
            body.appendChild(div);
        });
        document.getElementById('slotModal').classList.add('open');
    };
    window.closeModal = function() { document.getElementById('slotModal').classList.remove('open'); };
    window.closeModalOnBg = function(e) { if (e.target.id === 'slotModal') closeModal(); };
})();
</script>
</body>
</html>
