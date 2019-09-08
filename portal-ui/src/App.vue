<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
// 增加HTML 5存储API的调用，安装Vuex组件
// npm install --save vuex
import store from './store';
export default {
  data() {
    return {};
  },
  store
};
</script>
