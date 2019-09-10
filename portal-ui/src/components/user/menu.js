export default [
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
]