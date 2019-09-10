import Vue from 'vue'
import Router from 'vue-router'
import Layout from '@/components/Layout'
import EmptyRouterView from '@/components/EmptyRouterView'
import WeChatUsers from '@/components/admin/wechat/Users'

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
                  name: '微信用户管理',
                  component: (resolve) => require(['@/components/admin/wechat/User'], resolve)
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
        },
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
            }
          ]
        },
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

import HeyUI from 'heyui';

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


// 注册AXIOS的拦截器，这里跟路由没有关系，只是正好在这里配置路由的进度条，所以一并在这里配置AXIOS的进度条
import axios from "axios";
// 添加一个请求拦截器，用于在发送请求之前修改请求相关的参数
axios.interceptors.request.use(config => {
  // console.log("开始")
  HeyUI.$LoadingBar.start();
  // 可以根据实际的要求、需求修改请求参数
  return config;
}, (error) => {
  // 当出现问题的时候，调用此函数
  // 一旦调用Promise.reject表示访问失败、结束后续的请求啊哦做
  return Promise.reject(error);
});

// 响应拦截器，用于处理响应的结果，比如401
axios.interceptors.response.use(response => {
  HeyUI.$LoadingBar.success();
  // console.log("结束")
  // 正常的返回，直接把响应返回即可
  return response;
}, (error) => {
  HeyUI.$LoadingBar.fail();
  // 错误信息返回
  if (error.response.status === 401) {
    if (error.request.responseURL.endsWith("/do-login")) {
      // 本身就是登录页面，不处理
    } else {
      // 未登录
      // 401需要服务器配合，在未登录的时候返回401错误码
      router.push("/login");
    }
  } else {
    //router.push("/error");
    console.error(error)
    HeyUI.$Message({ type: "error", text: "出现了未知问题" });
  }
  return Promise.reject(error);
});

export default router;
