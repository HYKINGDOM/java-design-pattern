import Vue from 'vue'
import Router from 'vue-router'
import Layout from '@/components/Layout'
import SystemRouterConfig from "../components/system/router"
import UserRouterConfig from "../components/user/router"
import AdminRouterConfig from "../components/admin/router"

Vue.use(Router)

let error = {
  path: "/error",
  name: "Error",
  component: (resolve) => require(['@/components/Error'], resolve)
};
let router = new Router({
  // 使用HTML 5的History API来实现前端路由，可以避免URL后面携带#号
  mode: "history",
  routes: [
    // 把需要先匹配的路径放到前面
    {
      path: "/login",
      name: "Login",
      component: (resolve) => require(['@/components/Login'], resolve)
    },
    {
      path: "/registry",
      name: "Registry",
      component: (resolve) => require(['@/components/Registry'], resolve)
    },
    {
      path: "/user/complete",
      name: "RegistryComplete",
      component: (resolve) => require(['@/components/user/Complete'], resolve)
    },

    // 如果有其他的布局，在这里加

    // 下面一个局部，是最后一个布局
    // 嵌套布局一般在后面一些
    {
      path: '/',
      component: Layout,
      children: [
        ...AdminRouterConfig,
        ...UserRouterConfig,
        ...SystemRouterConfig,
        // 嵌套的路由中，也需要没有找到页面的路由
        error,
        {
          path: "",
          name: "首页",
          component: (resolve) => require(['@/components/Index'], resolve)
        },
        {
          path: "*",
          name: "未找到页面",
          component: (resolve) => require(['@/components/NotFound'], resolve)
        }
      ]
    },
    {
      path: "*",
      name: "NotFound",
      component: (resolve) => require(['@/components/NotFound'], resolve)
    }
  ]
});
// 路由的前后过滤器、拦截器、处理器
router.beforeEach((to, from, next) => {
  // 启动浏览器顶部的进度条
  //HeyUI.$LoadingBar.start();

  // 执行下一个操作，比如正常的路由访问
  next();
});
router.afterEach((to, from) => {
  // 路由完成以后，把进度条取消，标记为已经成功
  //HeyUI.$LoadingBar.success();
});

export default router;