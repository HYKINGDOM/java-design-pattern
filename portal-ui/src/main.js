// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
// import Welcome from './Welcome'
import router from './router'

// 引入HeyUI的less文件
import "heyui/themes/index.less";

// 引入HeyUI的组件，并注册到Vue里面
import HeyUI from 'heyui';
Vue.use(HeyUI);

Vue.config.productionTip = false;

// 增加HTML 5存储API的调用，安装Vuex组件
// npm install --save vuex
import store from './store';


import interceptors from './interceptors';
interceptors.addAxiosInterceptors(router);

// 创建Vue实例，并把Vue实例挂载到#app里面
// let vue = new Vue({
//   router,
//   store,
//   render: h => h(Welcome)
// });
// vue.$mount('#app');

// 创建Vue实例，并把<App/>元素插入#app里面
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
