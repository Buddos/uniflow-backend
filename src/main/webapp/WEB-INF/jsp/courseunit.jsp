<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title}</title>
    <!-- Tailwind CSS for modern styling -->
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Inter', sans-serif;
            background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%);
            color: #f8fafc;
            min-height: 100vh;
        }
        .glass-card {
            background: rgba(30, 41, 59, 0.7);
            backdrop-filter: blur(12px);
            border: 1px solid rgba(255, 255, 255, 0.1);
            box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.37);
        }
        .gradient-text {
            background: linear-gradient(90deg, #38bdf8, #818cf8);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }
        .status-pill {
            padding: 2px 10px;
            border-radius: 9999px;
            font-size: 0.75rem;
            font-weight: 600;
        }
        .status-core { background: rgba(56, 189, 248, 0.2); color: #38bdf8; }
        .status-elective { background: rgba(148, 163, 184, 0.2); color: #94a3b8; }
    </style>
</head>
<body class="p-8">
    <div class="max-w-6xl mx-auto">
        <!-- Header -->
        <header class="flex justify-between items-center mb-10">
            <div>
                <h1 class="text-4xl font-extrabold tracking-tight mb-2">
                    <span class="gradient-text">UniFlow</span> Course Units
                </h1>
                <p class="text-slate-400">Manage academic modules and department curricula effortlessly.</p>
            </div>
            <button class="bg-sky-500 hover:bg-sky-600 text-white px-6 py-2 rounded-lg font-semibold transition-all shadow-lg shadow-sky-500/20">
                + Add Course Unit
            </button>
        </header>

        <!-- Stats Overview -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-10">
            <div class="glass-card p-6 rounded-2xl">
                <p class="text-slate-400 text-sm mb-1">Total Units</p>
                <p class="text-2xl font-bold">${units.size()}</p>
            </div>
            <div class="glass-card p-6 rounded-2xl border-l-4 border-sky-500">
                <p class="text-slate-400 text-sm mb-1">Core Modules</p>
                <p class="text-2xl font-bold text-sky-400">12</p>
            </div>
            <div class="glass-card p-6 rounded-2xl">
                <p class="text-slate-400 text-sm mb-1">Departments</p>
                <p class="text-2xl font-bold">5</p>
            </div>
        </div>

        <!-- Course Units Table -->
        <div class="glass-card rounded-2xl overflow-hidden">
            <table class="w-full text-left">
                <thead>
                    <tr class="bg-slate-800/50 border-b border-white/5">
                        <th class="px-6 py-4 font-semibold text-slate-300">Code</th>
                        <th class="px-6 py-4 font-semibold text-slate-300">Title</th>
                        <th class="px-6 py-4 font-semibold text-slate-300">Department</th>
                        <th class="px-6 py-4 font-semibold text-slate-300 text-center">Type</th>
                        <th class="px-6 py-4 font-semibold text-slate-300 text-center">Credits</th>
                        <th class="px-6 py-4 font-semibold text-slate-300 text-right">Actions</th>
                    </tr>
                </thead>
                <tbody class="divide-y divide-white/5">
                    <c:forEach var="unit" items="${units}">
                        <tr class="hover:bg-white/5 transition-colors group">
                            <td class="px-6 py-4 font-mono text-sky-400 font-medium">${unit.code}</td>
                            <td class="px-6 py-4 font-semibold">${unit.name}</td>
                            <td class="px-6 py-4 text-slate-400">${unit.department}</td>
                            <td class="px-6 py-4 text-center">
                                <span class="status-pill ${unit.isCore ? 'status-core' : 'status-elective'}">
                                    ${unit.isCore ? "CORE" : "ELECTIVE"}
                                </span>
                            </td>
                            <td class="px-6 py-4 text-center font-bold text-slate-200">${unit.credits} CU</td>
                            <td class="px-6 py-4 text-right">
                                <button class="text-slate-500 hover:text-white transition-colors">
                                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                                        <path d="M10 6a2 2 0 110-4 2 2 0 010 4zM10 12a2 2 0 110-4 2 2 0 010 4zM10 18a2 2 0 110-4 2 2 0 010 4z" />
                                    </svg>
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty units}">
                        <tr>
                            <td colspan="6" class="px-6 py-12 text-center text-slate-500 italic">
                                No course units found in the repository.
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>

        <footer class="mt-8 text-center text-slate-500 text-sm">
            &copy; 2024 UniFlow System &bull; Collaborative Academic Requirements System (CARS 2.0)
        </footer>
    </div>
</body>
</html>
