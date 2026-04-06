<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="fixed left-0 top-0 h-full w-64 bg-slate-900 text-slate-300 p-6 flex flex-col gap-8 z-50">
    <div class="flex items-center gap-3 px-2">
        <div class="w-8 h-8 gradient-primary rounded-lg flex items-center justify-center text-white">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                <path d="M10.394 2.08a1 1 0 00-.788 0l-7 3a1 1 0 000 1.84L5.25 8.051a.999.999 0 01.356-.257l4-1.714a1 1 0 11.788 1.838L7.667 9.088l1.94.831a1 1 0 00.787 0l7-3a1 1 0 000-1.838l-7-3zM3.31 9.397L5 10.12V11.75a1 1 0 00.658.94l4 1.5a1 1 0 00.684 0l4-1.5a1 1 0 00.658-.94V10.12l1.306-.559a1 1 0 011.384 1.173l-1.5 6A1 1 0 0115.112 18H4.888a1 1 0 01-.972-.787l-1.5-6A1 1 0 013.31 9.397z" />
            </svg>
        </div>
        <span class="text-xl font-bold text-white tracking-tight">UniFlow</span>
    </div>

    <div class="flex-1 flex flex-col gap-2">
        <a href="/view/dashboard" class="flex items-center gap-3 px-3 py-2 rounded-xl hover:bg-slate-800 hover:text-white transition-all">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                <path d="M10.707 2.293a1 1 0 00-1.414 0l-7 7a1 1 0 001.414 1.414L4 10.414V17a1 1 0 001 1h2a1 1 0 001-1v-2a1 1 0 011-1h2a1 1 0 011-1v2a1 1 0 001 1h2a1 1 0 001-1v-6.586l.293.293a1 1 0 001.414-1.414l-7-7z" />
            </svg>
            <span class="text-sm font-medium">Dashboard</span>
        </a>
        <a href="/view/timetable" class="flex items-center gap-3 px-3 py-2 rounded-xl hover:bg-slate-800 hover:text-white transition-all">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z" clip-rule="evenodd" />
            </svg>
            <span class="text-sm font-medium">Timetable</span>
        </a>
        <a href="/view/courserequest" class="flex items-center gap-3 px-3 py-2 rounded-xl hover:bg-slate-800 hover:text-white transition-all">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                <path d="M7 3a1 1 0 000 2h6a1 1 0 100-2H7zM4 7a1 1 0 011-1h10a1 1 0 110 2H5a1 1 0 01-1-1zM2 11a2 2 0 012-2h12a2 2 0 012 2v4a2 2 0 01-2 2H4a2 2 0 01-2-2v-4z" />
            </svg>
            <span class="text-sm font-medium">Requests</span>
        </a>
        <a href="/view/equipment" class="flex items-center gap-3 px-3 py-2 rounded-xl hover:bg-slate-800 hover:text-white transition-all">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M11.49 3.17c-.38-1.56-2.6-1.56-2.98 0a1.532 1.532 0 01-2.286.948c-1.372-.836-2.942.734-2.106 2.106.54.886.061 2.042-.947 2.287-1.561.379-1.561 2.6 0 2.978a1.532 1.532 0 01.947 2.287c-.836 1.372.734 2.942 2.106 2.106a1.532 1.532 0 012.287.947c.379 1.561 2.6 1.561 2.978 0a1.533 1.533 0 012.287-.947c1.372.836 2.942-.734 2.106-2.106a1.533 1.533 0 01.947-2.287c1.561-.379 1.561-2.6 0-2.978a1.532 1.532 0 01-.947-2.287c.836-1.372-.734-2.942-2.106-2.106a1.532 1.532 0 01-2.287-.947zM10 13a3 3 0 100-6 3 3 0 000 6z" clip-rule="evenodd" />
            </svg>
            <span class="text-sm font-medium">Equipment</span>
        </a>
    </div>

    <div class="mt-auto pt-6 border-t border-slate-800">
        <div class="flex items-center gap-3 px-2">
            <div class="w-10 h-10 bg-slate-800 rounded-full flex items-center justify-center font-bold text-white">UP</div>
            <div>
                <p class="text-xs font-bold text-white">UniFlow Prof</p>
                <p class="text-[10px] text-slate-500">Administrator</p>
            </div>
        </div>
    </div>
</nav>
