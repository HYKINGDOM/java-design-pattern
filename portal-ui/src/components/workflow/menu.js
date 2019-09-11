export default [
    {
        title: "工作流",
        icon: "icon-shuffle",
        key: "Workflow",
        children: [
            {
                title: "收件箱",
                icon: "icon-inbox",
                key: "/workflow/task"
            },
            {
                title: "已处理",
                icon: "icon-outbox",
                key: "/workflow/history/task"
            },
            {
                title: "流程跟踪",
                icon: "icon-archive",
                key: "/workflow/history/process"
            }
        ]
    }
]