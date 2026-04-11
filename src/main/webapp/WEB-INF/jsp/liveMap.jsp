<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title != null ? title : 'Live Map - UniFlow'}</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <style>
        * { font-family: 'Plus Jakarta Sans', sans-serif; }
        body { background:
            radial-gradient(circle at top left, rgba(16,185,129,0.08), transparent 24%),
            radial-gradient(circle at top right, rgba(59,130,246,0.08), transparent 28%),
            #f8fafc;
            color:#0f172a;
            min-height:100vh;
            display:flex;
        }
        .card { background:rgba(255,255,255,0.94); backdrop-filter:blur(10px); border:1px solid rgba(226,232,240,0.9); box-shadow:0 18px 40px -18px rgba(15,23,42,0.18); }
        .venue-green { border-color:#16a34a; background:linear-gradient(180deg, rgba(34,197,94,0.18), rgba(255,255,255,0.98)); }
        .venue-neutral { border-color:#cbd5e1; background:rgba(255,255,255,0.98); }
        .badge { display:inline-flex; align-items:center; padding:0.2rem 0.55rem; border-radius:9999px; font-size:0.67rem; font-weight:800; letter-spacing:0.05em; text-transform:uppercase; }
        .nav-shell { width:240px; flex-shrink:0; }
    </style>
</head>
<body>
    <jsp:include page="common/nav.jsp" />

    <main style="margin-left:240px;flex:1;padding:1.5rem 1.5rem 2.5rem;">
        <div class="max-w-7xl mx-auto space-y-8">
            <header class="flex flex-col gap-4 lg:flex-row lg:items-end lg:justify-between">
                <div>
                    <p class="text-xs font-bold uppercase tracking-[0.24em] text-emerald-600">Live Map</p>
                    <h1 class="text-4xl font-black tracking-tight text-slate-900">Room availability at a glance</h1>
                    <p class="mt-2 text-slate-500 max-w-2xl">Venues released by lecturer trips are highlighted in green so students and CODs can quickly find open rooms for group work and coordination.</p>
                </div>
                <div class="card rounded-2xl px-5 py-4 min-w-[16rem]">
                    <p class="text-xs font-bold uppercase tracking-widest text-slate-400">Active Lecturer Trips</p>
                    <p class="text-4xl font-black text-slate-900 mt-1">${activeTrips != null ? activeTrips.size() : 0}</p>
                    <p class="text-sm text-slate-500 mt-1">Rooms released by active trips stay available for booking.</p>
                </div>
            </header>

            <c:if test="${not empty message}">
                <div class="card rounded-2xl px-5 py-4 border-emerald-200 bg-emerald-50 text-emerald-900">
                    <p class="font-semibold">${message}</p>
                </div>
            </c:if>

            <div class="grid grid-cols-1 xl:grid-cols-4 gap-6">
                <section class="xl:col-span-3 space-y-4">
                    <div class="flex items-center justify-between">
                        <h2 class="text-xl font-bold text-slate-900">Venues</h2>
                        <span class="text-sm text-slate-500">Green cards indicate available rooms</span>
                    </div>

                    <div class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4">
                        <c:forEach var="venue" items="${venues}">
                            <div class="card rounded-3xl p-5 transition-transform hover:-translate-y-1 hover:shadow-2xl ${venue.status != null && fn:toLowerCase(venue.status) eq 'available' ? 'venue-green' : 'venue-neutral'}">
                                <div class="flex items-start justify-between gap-4">
                                    <div>
                                        <h3 class="text-lg font-extrabold text-slate-900">${venue.name}</h3>
                                        <p class="text-sm text-slate-500 mt-1">${venue.building} · Floor ${venue.floor}</p>
                                    </div>
                                    <c:choose>
                                        <c:when test="${venue.status != null && fn:toLowerCase(venue.status) eq 'available'}">
                                            <span class="badge bg-emerald-100 text-emerald-700 border border-emerald-200">Green</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-slate-100 text-slate-600 border border-slate-200">${venue.status != null ? venue.status : 'UNKNOWN'}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <div class="mt-4 space-y-2 text-sm text-slate-600">
                                    <p><span class="font-semibold text-slate-900">Capacity:</span> ${venue.capacity}</p>
                                    <p><span class="font-semibold text-slate-900">Location:</span> ${venue.location}</p>
                                    <p><span class="font-semibold text-slate-900">Equipment office:</span> ${venue.equipmentOfficeName}</p>
                                    <p><span class="font-semibold text-slate-900">Distance:</span> ${venue.distanceFromOfficeMeters}m</p>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </section>

                <aside class="space-y-4">
                    <div class="card rounded-3xl p-5">
                        <h2 class="text-lg font-bold text-slate-900">Released by trips</h2>
                        <p class="text-sm text-slate-500 mt-1">These lecturers have active trips that may be freeing venues.</p>
                        <div class="mt-4 space-y-3">
                            <c:choose>
                                <c:when test="${empty activeTrips}">
                                    <p class="text-sm text-slate-500">No active lecturer trips right now.</p>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="trip" items="${activeTrips}">
                                        <div class="rounded-2xl border border-slate-200 bg-slate-50 px-4 py-3">
                                            <p class="font-bold text-slate-900">${trip.title}</p>
                                            <p class="text-sm text-slate-500 mt-1">${trip.cohort} · ${trip.department}</p>
                                            <p class="text-xs uppercase tracking-widest font-bold text-emerald-600 mt-2">${trip.status}</p>
                                        </div>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <div class="card rounded-3xl p-5">
                        <h2 class="text-lg font-bold text-slate-900">Session</h2>
                        <p class="text-sm text-slate-500 mt-1">${sessionScope.user != null ? sessionScope.user.name : 'Guest'}</p>
                        <p class="text-xs font-bold uppercase tracking-[0.22em] text-slate-400 mt-2">${sessionScope.userRole}</p>
                    </div>
                </aside>
            </div>
        </div>
    </main>
</body>
</html>
