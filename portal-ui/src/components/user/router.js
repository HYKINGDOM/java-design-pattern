import EmptyRouterView from '@/components/EmptyRouterView'
export default [
    {
        path: "user",
        name: '用户中心',
        component: EmptyRouterView,
        children: [
            {
                path: 'profile',
                name: '个人信息',
                component: (resolve) => require(['@/components/user/UserProfile'], resolve)
            },
            {
                path: 'password',
                name: '修改密码',
                component: (resolve) => require(['@/components/user/ChangePassword'], resolve)
            },
            {
                path: 'exchange',
                name: '积分兑换',
                component: (resolve) => require(['@/components/user/IntegralExchange'], resolve)
            },
            {
                path: 'order',
                name: '我的订单',
                component: (resolve) => require(['@/components/user/ExpenseOrder'], resolve)
            },
            {
                path: 'order/:id',
                name: '订单跟踪',
                component: (resolve) => require(['@/components/user/ExpenseOrderDetail'], resolve)
            },
            {
                path: 'address',
                name: '配送地址',
                component: (resolve) => require(['@/components/user/DistributionAddress'], resolve)
            },
            {
                path: 'trace/:id',
                name: '配送物流跟踪',
                component: (resolve) => require(['@/components/user/DistributionTrace'], resolve)
            },
            {
                path: 'file',
                name: '我的文件',
                component: (resolve) => require(['@/components/user/File.vue'], resolve)
            },
        ]
    }
]