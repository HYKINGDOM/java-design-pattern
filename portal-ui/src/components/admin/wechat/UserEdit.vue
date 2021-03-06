<template>
  <div>
    <router-link to="/admin/wechat/user" style="float: right;">返回</router-link>
    <Row>
      <Cell width="12">
        <Form :readonly="true" v-if="userInfo">
          <FormItem label="昵称">{{userInfo.nickname}}</FormItem>
          <FormItem label="性别">{{userInfo.sex===1? '男' : userInfo.sex === 2? '女': '未知'}}</FormItem>
          <FormItem label="国家">{{userInfo.country}}</FormItem>
          <FormItem label="省份">{{userInfo.province}}</FormItem>
          <FormItem label="城市">{{userInfo.city}}</FormItem>
          <FormItem label="语言">{{userInfo.language}}</FormItem>
          <FormItem label="OpenID">{{userInfo.openid}}</FormItem>
          <FormItem label="UnionID">{{userInfo.unionid ? userInfo.unionid : '公众号未绑定开放平台账号'}}</FormItem>
          <FormItem label="关注时间">{{userInfo.subscribeDate}}</FormItem>
          <FormItem label="备注">
            <div class="h-input-group">
              <input
                type="text"
                v-model="userInfo.remark"
                maxlength="30"
                placeholder="备注信息，最长30个字符"
              />
              <Button color="primary" @click="updateRemark">修改</Button>
            </div>
          </FormItem>
        </Form>
      </Cell>
      <Cell width="12">
        <Form
          :readonly="true"
          v-if="user"
          ref="userForm"
          :model="user"
          :rules="userFormValidationRules"
          :showErrorTip="true"
        >
          <FormItem label="登录名" v-if="loginNameEditable">
            <div class="h-input-group">
              <input type="text" v-model="user.loginName" />
              <Button color="primary" @click="setLoginName">修改</Button>
            </div>
            <span>登录名只能修改一次，请慎重设置！</span>
          </FormItem>
          <FormItem label="登录名" v-if="!loginNameEditable">{{user.loginName}}</FormItem>
          <FormItem label="登录密码">
            <div class="h-input-group">
              <input type="text" v-model="user.password" placeholder="管理员重置用户密码，不推荐使用" />
              <Button color="primary" @click="changePassword">修改</Button>
            </div>
          </FormItem>
          <FormItem label="手机号码" prop="phone">
            <div class="h-input-group">
              <input type="text" v-model="user.phone" placeholder="输入正确的手机号码" />
              <Button
                color="yellow"
                v-bind:class="{'h-btn-loading': phone.verifyCodeSending}"
                @click="sendVerifyCode($event)"
                v-bind:disabled="phone.verifyCodeButtonDisabled"
              >
                <i class="h-icon-loading" v-if="phone.verifyCodeSending"></i>
                <span class="btn-txt">{{phone.verifyCodeButton}}</span>
              </Button>
            </div>
          </FormItem>
          <FormItem label="验证码" prop="verifyCode">
            <div class="h-input-group">
              <input
                type="text"
                v-model="phone.verifyCode"
                placeholder="输入手机收到的验证码"
                @keyup="phone.disabled = $event.target.value.length !== 4"
                @change="phone.disabled = $event.target.value.length !== 4"
              />
              <Button color="primary" @click="changePhone" v-bind:disabled="phone.disabled">修改</Button>
            </div>
          </FormItem>
          <FormItem label="注册时间">{{user.registeredTime}}</FormItem>
          <FormItem label="账号有效期">{{user.accountExpireTime}}</FormItem>
          <FormItem label="密码有效期">{{user.passwordExpireTime}}</FormItem>
          <FormItem label="是否锁定">
            <h-switch v-model="user.accountNonLocked" @change="changeAccountLockStatus">
              <span slot="open">开启</span>
              <span slot="close">锁定</span>
            </h-switch>
          </FormItem>
          <FormItem label="是禁用">
            <h-switch v-model="user.enabled" @change="changeEnableStatus">
              <span slot="open">开启</span>
              <span slot="close">禁用</span>
            </h-switch>
          </FormItem>
        </Form>
      </Cell>
    </Row>
    <Row>
      <Cell width="12">
        <Transfer
          style="width: 100%; margin-top: 10px;"
          v-model="selectedTags"
          :datas="allTags"
          :option="selectTagOption"
        >
          <template slot-scope="{option}" slot="item">{{option.text}}({{option.id}})</template>
        </Transfer>
        <div class="text-right">
          <Button color="primary" @click="changeTags">保存</Button>
        </div>
      </Cell>
    </Row>
  </div>
</template>

<script>
// 异步HTTP请求的API，比jQuery要小，专注于做AJAX方面的操作
import axios from "axios";
export default {
  data() {
    return {
      userInfo: null,
      user: null,
      account: null,
      loginNameEditable: false,
      phone: {
        disabled: true,
        verifyCode: null,
        verifyCodeSending: false,
        verifyCodeButton: "发送短信验证码",
        verifyCodeButtonDisabled: false
      },
      userFormValidationRules: {
        required: ["phone"],
        mobile: ["phone"]
      },
      selectedTags: [],
      allTags: [],
      selectTagOption: {
        ltHeadText: "未选择标签",
        rtHeadText: "已选择标签"
      }
    };
  },
  mounted() {
    this.getUserInfo();
  },
  methods: {
    getUserInfo() {
      let url = `/api/we-chat/user/${this.$route.params.id}`;
      axios.get(url).then(response => {
        this.userInfo = response.data;
        // 加载用户信息
        this.getUser(this.userInfo.userId);
        // 读取公众号信息
        this.getAccount(this.userInfo.account);
        // 读取公众号里面的用户标签
        this.getAllTags(this.userInfo.account);
      });
    },
    getUser(id) {
      let url = `/api/user-center/user/byId?id=${id}`;
      axios.get(url).then(response => {
        this.user = response.data;
        this.loginNameEditable = this.user.loginName === this.user.openId;
      });
    },
    getAccount(account) {
      let url = "/api/we-chat/account/" + account;
      axios.get(url).then(response => {
        this.account = response.data;
      });
    },
    getAllTags(account) {
      let url = "/api/we-chat/tag/non-users/" + account;
      axios.get(url).then(response => {
        this.getUserTags(this.userInfo.id);
        this.allTags = response.data.map(tag => {
          return {
            key: tag.tagId,
            id: tag.id,
            text: tag.name
          };
        });
      });
    },
    getUserTags(id) {
      let url = "/api/we-chat/user/tags/" + id;
      axios.get(url).then(response => {
        this.selectedTags = response.data.map(tag => {
          return tag.tagId;
        });
      });
    },
    setLoginName() {
      // 修改登录名
      // let data = {
      //   id: this.user.id,
      //   loginName: this.user.loginName
      // };
      let data = new FormData();
      data.append("id", this.user.id);
      data.append("loginName", this.user.loginName);
      axios
        .post("/api/user-center/user/set-login-name", data)
        .then(response => {
          this.$Message({ type: "success", text: "登录名更新成功" });
          this.getUser(this.user.id);
        });
    },
    sendVerifyCode(event) {
      // 发送验证码到对应的手机号码上面
      let validResult = this.$refs.userForm.validFieldJs("phone");
      if (validResult.valid) {
        this.phone.verifyCodeSending = true;
        this.phone.verifyCodeButtonDisabled = true;
        axios
          .get("/api/verify-code/send?to=" + this.user.phone)
          .then(response => {
            this.phone.verifyCodeSending = false;
            let phone = this.phone;
            let times = 120;
            function verifyCodeTimer() {
              phone.verifyCodeButton = "发送成功，重新发送(" + times + ")";
              times--;
              if (times < 0) {
                phone.verifyCodeButtonDisabled = false;
                return;
              }
              setTimeout(verifyCodeTimer, 1000);
            }
            verifyCodeTimer();
          })
          .catch(error => {
            this.phone.verifyCodeButtonDisabled = false;
            this.phone.verifyCodeSending = false;
          });
      } else {
        this.$Message({
          type: "error",
          text: `${validResult.label}${validResult.message}`
        });
      }
    },
    changePhone() {
      // 修改手机号码
      let url = "/api/user-center/user/update-phone";
      // let data = {
      //   id: this.user.id,
      //   phone: this.user.phone,
      //   verifyCode: this.phone.verifyCode
      // };
      let data = new FormData();
      data.append("id", this.user.id);
      data.append("phone", this.user.phone);
      data.append("verifyCode", this.phone.verifyCode);
      axios.post(url, data).then(response => {
        let result = response.data;
        if (result.code === 1) {
          this.$Message({ type: "success", text: "手机号码修改成功" });
          // 重新加载用户数据
          this.getUserInfo();
        } else {
          this.$Message({ type: "error", text: result.message });
        }
      });
    },
    changeAccountLockStatus() {
      // 修改账号锁定状态
      // let data = {
      //   id: this.user.id,
      //   accountNonLocked: this.user.accountNonLocked
      // };
      let data = new FormData();
      data.append("id", this.user.id);
      data.append("accountNonLocked", this.user.accountNonLocked);
      axios
        .post("/api/user-center/user/account-non-locked", data)
        .then(response => {
          this.$Message({ type: "success", text: "修改账号锁定状态成功" });
        });
    },
    changeEnableStatus() {
      // 修改账号禁用状态
      // let data = {
      //   id: this.user.id,
      //   enable: this.user.enable
      // };
      let data = new FormData();
      data.append("id", this.user.id);
      data.append("enabled", this.user.enabled);
      axios.post("/api/user-center/user/enabled", data).then(response => {
        this.$Message({ type: "success", text: "修改禁用状态成功" });
      });
    },
    changePassword() {
      let data = new FormData();
      data.append("id", this.user.id);
      data.append("password", this.user.password);
      axios
        .post("/api/user-center/user/update-password", data)
        .then(response => {
          this.$Message({ type: "success", text: "修改密码成功" });
          this.getUser(this.user.id);
        });
    },
    updateRemark() {
      let data = new FormData();
      data.append("id", this.userInfo.id);
      data.append("remark", this.userInfo.remark);
      axios.post("/api/we-chat/user/update-remark", data).then(response => {
        let result = response.data;
        if (result.code === 1) {
          this.$Message({ type: "success", text: result.message });
        } else {
          this.$Message({ type: "error", text: result.message });
        }
      });
    },
    changeTags() {
      if (this.selectedTags && this.selectedTags.length > 0) {
        let data = new FormData();
        data.append("id", this.userInfo.id);
        this.selectedTags.forEach(tagId => {
          data.append("tagId", tagId);
        });
        axios.post("/api/we-chat/user/update-tags", data).then(response => {
          let result = response.data;
          let type = "success";
          if (result.code !== 1) {
            type = "error";
          }
          this.$Message({ type: type, text: result.message });
        });
      }
    }
  }
};
</script>

<style scoped>
</style>