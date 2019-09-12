<template>
  <div class="text-right user-info">
    <router-link to="/user/profile">{{User.name}}</router-link>
    <a v-if="User.name" @click="logout">退出登录</a>
  </div>
</template>

<script>
import axios from "axios";
import { mapState } from "vuex";
import loadUserInfo from "./LoadUserInfo";
export default {
  data() {
    return {
      retry: false
    };
  },
  methods: {
    logout() {
      this.$store.commit("updateAccount", {});
      axios.post("/api/logout").then(response => {
        this.$router.replace("/login");
      });
    }
  },
  mounted() {
    if (!this.User.name) {
      loadUserInfo(this.$store);
    }
  },
  computed: {
    ...mapState(["User"])
  }
};
</script>

<style scoped>
.user-info {
  padding-right: 10px;
}
</style>