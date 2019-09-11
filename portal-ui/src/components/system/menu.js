export default [
    {
        title: "监控中心",
        key: "SystemMonitor",
        icon: "icon-monitor",
        children: [
            {
                title: "性能监控",
                icon: "icon-server",
                key: "/system/monitor/performance"
            },
            {
                title: "服务监控",
                icon: "icon-server",
                key: "/system/monitor/service"
            },
            {
                title: "日志监控",
                icon: "icon-pie-graph",
                key: "/system/monitor/logs"
            },
            {
                title: "异常监控",
                icon: "h-icon-bell",
                key: "/system/monitor/exception"
            }
        ]
    }
];