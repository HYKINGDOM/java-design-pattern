<template>
  <div class="text-right user-info">
    <router-link to="/user/profile">{{name}}</router-link>
    <a v-if="User.name" @click="logout">退出登录</a>
  </div>
</template>

<script>
import axios from "axios";
import { mapState } from "vuex";
export default {
  data() {
    return {
      retry: false,
      name: "登录中..."
    };
  },
  methods: {
    logout() {
      //this.$router.push("/login");
      axios.post("/api/logout").then(response => {
        this.$router.replace("/login");
        this.$store.commit("updateAccount", {});
      });
    },
    loadUserInfo() {
      axios.get("/api/user-center/user").then(response => {
        let user = response.data;
        if (user.name) {
          this.$store.commit("updateAccount", response.data);
          this.name = response.data.name;
        } else if (this.retry === false) {
          this.retry = true;
          this.loadUserInfo();
        }
      });
    }
  },
  mounted() {
    this.loadUserInfo();
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