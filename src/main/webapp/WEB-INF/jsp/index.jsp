<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title}</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <style>
        body { font-family: 'Plus Jakarta Sans', sans-serif; background: #fafafa; color: #0f172a; }
        .gradient-primary { background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%); }
        .glass-card { background: rgba(255, 255, 255, 0.9); backdrop-filter: blur(12px); border: 1px solid rgba(226, 232, 240, 0.5); }
        .hero-gradient { background: radial-gradient(circle at top right, rgba(59, 130, 246, 0.05), transparent), radial-gradient(circle at bottom left, rgba(37, 99, 235, 0.05), transparent); }
    </style>
</head>
<body class="flex min-h-screen hero-gradient">
    <!-- Include Nav -->
    <jsp:include page="common/nav.jsp" />

    <main class="ml-64 flex-1 p-10">
        <div class="max-w-6xl mx-auto space-y-12">
            <!-- Header Section -->
            <header class="flex justify-between items-start">
                <div>
                    <h1 class="text-4xl font-extrabold tracking-tight text-slate-900">Welcome back, Admin</h1>
                    <p class="text-slate-500 mt-2 text-lg">Your academic coordination cockpit is ready.</p>
                </div>
                <div class="flex items-center gap-4">
                    <span class="px-4 py-2 bg-white border border-slate-200 rounded-full text-xs font-bold text-slate-500 shadow-sm flex items-center gap-2">
                        <span class="w-2 h-2 rounded-full bg-green-500 animate-pulse"></span>
                        System Online
                    </span>
                </div>
            </header>

            <!-- Stats/Quick Actions Grid -->
            <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
                <a href="/view/timetable" class="group glass-card p-8 rounded-3xl transition-all hover:shadow-2xl hover:shadow-blue-500/10 hover:-translate-y-1">
                    <div class="w-12 h-12 bg-blue-50 rounded-2xl flex items-center justify-center text-blue-500 mb-6 group-hover:scale-110 transition-transform">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                        </svg>
                    </div>
                    <h3 class="text-xl font-bold text-slate-900 mb-2">Weekly Timetable</h3>
                    <p class="text-slate-500 text-sm leading-relaxed">View and manage venue allocations and academic session schedules.</p>
                </a>

                <a href="/view/courserequest" class="group glass-card p-8 rounded-3xl transition-all hover:shadow-2xl hover:shadow-indigo-500/10 hover:-translate-y-1">
                    <div class="w-12 h-12 bg-indigo-50 rounded-2xl flex items-center justify-center text-indigo-500 mb-6 group-hover:scale-110 transition-transform">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 8h10M7 12h4m1 8l-4-4H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-3l-4 4z" />
                        </svg>
                    </div>
                    <h3 class="text-xl font-bold text-slate-900 mb-2">Course Requests</h3>
                    <p class="text-slate-500 text-sm leading-relaxed">Respond to inter-departmental course unit and resource requests.</p>
                </a>

                <a href="/view/equipment" class="group glass-card p-8 rounded-3xl transition-all hover:shadow-2xl hover:shadow-emerald-500/10 hover:-translate-y-1">
                    <div class="w-12 h-12 bg-emerald-50 rounded-2xl flex items-center justify-center text-emerald-500 mb-6 group-hover:scale-110 transition-transform">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 3v2m6-2v2M9 19v2m6-2v2M5 9H3m2 6H3m18-6h-2m2 6h-2M7 19h10a2 2 0 002-2V7a2 2 0 00-2-2H7a2 2 0 00-2 2v10a2 2 0 002 2zM9 9h6v6H9V9z" />
                        </svg>
                    </div>
                    <h3 class="text-xl font-bold text-slate-900 mb-2">Equipment Tracking</h3>
                    <p class="text-slate-500 text-sm leading-relaxed">Live status board for projectors, PA systems, and portable lab assets.</p>
                </a>
            </div>

            <!-- Recent Activity Section -->
            <div class="glass-card p-10 rounded-[2.5rem]">
                <div class="flex justify-between items-center mb-8">
                    <h2 class="text-2xl font-bold text-slate-900">Recent System Activity</h2>
                    <button class="text-blue-600 font-bold text-sm hover:underline">View Log Summary</button>
                </div>
                <div class="space-y-6">
                    <div class="flex items-center gap-6 p-4 rounded-2xl hover:bg-slate-50 transition-colors">
                        <div class="w-10 h-10 bg-slate-100 rounded-xl flex items-center justify-center font-bold text-slate-500 text-xs">08:30</div>
                        <div class="flex-1">
                            <p class="text-sm font-bold text-slate-900">Agriculture Faculty</p>
                            <p class="text-xs text-slate-500 mt-0.5">Submitted a new request for Calculus I (MAT101).</p>
                        </div>
                        <span class="px-3 py-1 bg-amber-50 text-amber-600 text-[10px] font-bold rounded-full uppercase tracking-widest border border-amber-100">Pending</span>
                    </div>
                    <div class="flex items-center gap-6 p-4 rounded-2xl hover:bg-slate-50 transition-colors">
                        <div class="w-10 h-10 bg-slate-100 rounded-xl flex items-center justify-center font-bold text-slate-500 text-xs">10:15</div>
                        <div class="flex-1">
                            <p class="text-sm font-bold text-slate-900">Projector A1 (PST 1)</p>
                            <p class="text-xs text-slate-500 mt-0.5">Status automatically updated to 'IN-USE' during CS201 session.</p>
                        </div>
                        <span class="px-3 py-1 bg-blue-50 text-blue-600 text-[10px] font-bold rounded-full uppercase tracking-widest border border-blue-100">Live</span>
                    </div>
                </div>
            </div>
        </div>
    </main>
</body>
</html>
