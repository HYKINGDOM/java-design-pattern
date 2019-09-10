import EmptyRouterView from '@/components/EmptyRouterView'

export default [
    {
        path: "system",
        name: "系统管理",
        component: EmptyRouterView,
        children: [
            {
                path: "monitor",
                name: "监控中心",
                component: EmptyRouterView,
                children: [
                    {
                        path: "service",
                        name: "服务监控",
                        component: (resolve) => require(['@/components/system/monitor/Service.vue'], resolve)
                    }
                ]
            }
        ]
    }
]