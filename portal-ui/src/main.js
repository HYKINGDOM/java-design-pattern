// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'

// 引入HeyUI的less文件
import "heyui/themes/index.less";

// 引入HeyUI的组件，并注册到Vue里面
import HeyUI from 'heyui';
Vue.use(HeyUI);

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
