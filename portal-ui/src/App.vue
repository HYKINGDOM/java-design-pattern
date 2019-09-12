<template>
  <div id="app">
    <PageLoading v-if="!showRouterView" />
    <router-view v-if="showRouterView" />
  </div>
</template>

<script>
import axios from "axios";
import { mapState } from "vuex";
import loadUserInfo from "./components/LoadUserInfo";
import PageLoading from "./components/PageLoading";
export default {
  components: { PageLoading },
  data() {
    return {};
  },
  computed: {
    ...mapState(["User"]),
    showRouterView() {
      if (this.$route.path === "/login") {
        // 登录名页面，直接显示router-view，用于显示界面。
        return true;
      } else if (this.User && this.User.name) {
        // 其他界面则判断是否有登录信息，在有登录信息的情况下才显示router-view
        return true;
      }
      return false;
    }
  },
  watch: {
    User(u) {}
  },
  mounted() {
    loadUserInfo(this.$store);
  }
};
</script>
