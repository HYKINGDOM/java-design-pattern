<template>
  <div class="text-right user-info">
    <router-link to="/user/profile">{{User.name}}</router-link>
    <a v-if="User.name" @click="logout">退出登录</a>
  </div>
</template>

<script>
import axios from "axios";
import { mapState } from "vuex";
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
    },
    loadUserInfo() {
      axios.get("/api/user-center/user").then(response => {
        let user = response.data;
        if (user.name) {
          this.$store.commit("updateAccount", response.data);
        } else if (this.retry === false) {
          this.retry = true;
          this.loadUserInfo();
        }
      });
    }
  },
  mounted() {
    if (!this.User.name) {
      this.loadUserInfo();
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