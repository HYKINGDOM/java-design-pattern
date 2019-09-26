import EmptyRouterView from '@/components/EmptyRouterView'
export default [
    {
        path: "workflow",
        name: '工作流',
        component: EmptyRouterView,
        children: [
            {
                path: "process",
                name: "流程",
                component: EmptyRouterView,
                children: [
                    {
                        path: "definition",
                        name: "流程定义管理",
                        component: (resolve) => require(['@/components/workflow/process/Definition.vue'], resolve)
                    }
                ]
            }
        ]
    }
]