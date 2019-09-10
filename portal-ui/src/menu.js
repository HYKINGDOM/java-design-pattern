import SystemMenu from "./components/system/menu";
import UserMenu from "./components/user/menu";
import AdminMenu from "./components/admin/menu";
export default [
    { title: "首页", key: "/", icon: "h-icon-home" },
    ...AdminMenu,
    ...UserMenu,
    ...SystemMenu
];