
import WeChatUsers from '@/components/admin/wechat/Users'
import EmptyRouterView from '@/components/EmptyRouterView'
export default [
    {
        path: "/admin",
        name: "后台管理",
        component: EmptyRouterView,
        children: [
            {
                path: "wechat",
                component: EmptyRouterView,
                children: [
                    {
                        path: 'users',
                        name: '微信用户标签管理',
                        component: WeChatUsers
                    },
                    {
                        path: 'user',
                        component: EmptyRouterView,
                        children: [
                            {
                                path: '',
                                name: '微信用户管理',
                                component: (resolve) => require(['@/components/admin/wechat/User'], resolve)
                            },
                            {
                                path: ':id',
                                name: '修改微信用户信息',
                                component: (resolve) => require(['@/components/admin/wechat/UserEdit'], resolve)
                            }
                        ]
                    }
                ]
            },
            {
                path: 'content',
                name: '内容管理',
                component: (resolve) => require(['@/components/admin/content/Search'], resolve)
            },
            {
                path: 'content/edit',
                name: '新增文章',
                component: (resolve) => require(['@/components/admin/content/Edit'], resolve)
            },
            {
                path: 'content/:id',
                name: '编辑文章',
                component: (resolve) => require(['@/components/admin/content/Edit'], resolve)
            },
            {
                path: 'gift',
                name: '礼品管理',
                component: (resolve) => require(['@/components/admin/gift/Search'], resolve)
            },
            {
                path: 'gift/add',
                name: '添加礼品',
                component: (resolve) => require(['@/components/admin/gift/Edit'], resolve)
            },
            {
                path: 'gift/:id',
                name: '编辑礼品',
                component: (resolve) => require(['@/components/admin/gift/Edit'], resolve)
            },
            {
                path: 'integral',
                name: '积分活动',
                component: (resolve) => require(['@/components/admin/integral/Search'], resolve)
            },
            {
                path: 'integral/add',
                name: '添加积分活动',
                component: (resolve) => require(['@/components/admin/integral/Edit'], resolve)
            },
            {
                path: 'integral/:id',
                name: '修改积分活动',
                component: (resolve) => require(['@/components/admin/integral/Edit'], resolve)
            },
            {
                path: 'order',
                name: '订单管理',
                component: (resolve) => require(['@/components/admin/order/Search'], resolve)
            },
            {
                path: '/order/:id',
                name: '订单明细',
                component: (resolve) => require(['@/components/admin/order/Detail'], resolve)
            }
        ]
    }
]