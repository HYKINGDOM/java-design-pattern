<template>
  <div class="loginForm">
    <Form
      ref="form"
      :validOnChange="validOnChange"
      :showErrorTip="showErrorTip"
      :labelPosition="labelPosition"
      :labelWidth="110"
      :rules="validationRules"
      :model="model"
    >
      <FormItem prop="phone" label="手机号码">
        <template v-slot:label>
          <i class="h-icon-user"></i>手机号码
        </template>
        <input type="text" v-model="model.phone" />
      </FormItem>
      <FormItem label="密码" icon="h-icon-lock" prop="password">
        <input type="password" v-model="model.password"
       @keypress.enter="submit"/>
      </FormItem>
      <FormItem>
        <Button color="primary" :loading="isLoading" @click="submit">提交</Button>&nbsp;&nbsp;&nbsp;
        <Button @click="reset">重置</Button>&nbsp;&nbsp;&nbsp;
        <router-link to="/registry">注册</router-link>
        <a href="/api/oauth-server/wechat/oauth2/authorization/gh_f21f1485101e">微信登录</a>
      </FormItem>
    </Form>
  </div>
</template>

<script>
import axios from "axios";
export default {
  data() {
    return {
      isSendCodeDisabled: false,
      isCodeSending: false,
      isLoading: false,
      labelPosition: "left",
      model: {
        phone: "",
        password: ""
      },
      validationRules: {
        required: ["phone", "password"],
        mobile: ["phone"]
      },
      showErrorTip: true,
      validOnChange: true
    };
  },
  methods: {
    submit() {
      this.isLoading = true;
      let validResult = this.$refs.form.valid();
      if (validResult.result) {
        this.isLoading = true;
        let url = "/api/oauth-server/uc/do-login";
        let user = {
          phone: this.model.phone,
          password: this.model.password
        };
        // let data = new FormData();
        // data.append("username", this.model.phone);
        // data.append("password", this.model.password);
        let data =
          "username=" + this.model.phone + "&password=" + this.model.password;
        axios
          .post(url, data, {
            headers: { accept: "text/html", XMLHttpRequest: "axios" }
          })
          .then(result => {
            this.isLoading = false;
            this.$router.replace("/");
          })
          .catch(e => {
            this.isLoading = false;
            if (e.response.status === 401) {
              this.$Message({ type: "error", text: "登录失败，可能是用户名、密码有错" });
            } else {
              // this.$Message("发送登录请求出现故障");
              this.$Message({ type: "error", text: "发送登录请求出现故障" });
            }
          });
      } else {
        this.isLoading = false;
      }
    },
    reset() {
      this.$refs.form.resetValid();
      this.model = {
        phone: "",
        password: ""
      };
    }
  }
};
</script>

<style scoped>
.loginForm {
  width: 400px;
  margin-right: auto;
  margin-left: auto;
  margin-top: 20px;
}

div.h-input-group > .h-input-addon {
  padding: 0px 0px;
  color: #000;
}
.h-input-group .h-btn {
  padding: 6px 15px;
  width: 100%;
}
</style>

