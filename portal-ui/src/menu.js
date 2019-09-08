export default [
    { title: "首页", key: "/", icon: "h-icon-home" },
    {
        title: "后台管理",
        icon: "icon-cog",
        key: "Admin",
        children: [
            {
                title: "微信公众号",
                key: "WeChat",
                icon: "h-icon-message",
                children: [
                    {
                        title: "分组管理",
                        icon: "h-icon-users",
                        key: "/admin/wechat/users"
                    },
                    {
                        title: "用户管理",
                        icon: "h-icon-user",
                        key: "/admin/wechat/user"
                    }
                ]
            },
            {
                title: "内容管理",
                icon: "icon-file",
                key: "/admin/content",
                children: [
                    {
                        title: "新增",
                        icon: "h-icon-edit",
                        key: "/admin/content/edit"
                    }
                ]
            },
            {
                title: "礼品管理",
                icon: "icon-file",
                key: "/admin/gift",
                children: [
                    {
                        title: "新增礼品",
                        icon: "icon-file",
                        key: "/admin/gift/add"
                    }
                ]
            },
            {
                title: "积分活动",
                icon: "icon-file",
                key: "/admin/integral",
                children: [
                    {
                        title: "新增积分活动",
                        icon: "icon-file",
                        key: "/admin/integral/add"
                    }
                ]
            },
            {
                title: "订单管理",
                icon: "icon-file",
                key: "/admin/order"
            }
        ]
    },
    {
        title: "用户中心",
        icon: "h-icon-user",
        key: "User",
        children: [
            {
                title: "个人信息",
                icon: "h-icon-user",
                key: "/user/profile"
            },
            {
                title: "修改密码",
                icon: "icon-lock",
                key: "/user/password"
            },
            {
                title: "积分兑换",
                icon: "icon-shuffle",
                key: "/user/exchange"
            },
            {
                title: "订单查询",
                icon: "icon-lock",
                key: "/user/order"
            },
            {
                title: "配送地址",
                icon: "icon-paper",
                key: "/user/address"
            }
        ]
    }
];