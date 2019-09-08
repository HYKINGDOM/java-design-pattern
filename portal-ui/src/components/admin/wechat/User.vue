<template>
  <div>
    <Form :model="selectedAccount">
      <FormItem label="选择公众号">
        <Select v-model="selectedAccount" :datas="accounts" :deletable="false" type="object"></Select>
      </FormItem>
    </Form>
    
  </div>
</template>

<script>
// 异步HTTP请求的API，比jQuery要小，专注于做AJAX方面的操作
import axios from "axios";
export default {
  data() {
    return {
      // 所有的微信账号
      accounts: [{ title: "加载中..." }],
      // 选中的微信账号
      selectedAccount: { account: null },
      pageNumber: 0,
      keyword: null
    };
  },
  mounted() {
    this.loadAccounts();
  },
  watch: {
    selectedAccount(account) {
      if (account && account.key) {
      }
    }
  },
  methods: {
    /**
     * 先加载微信公众号配置，选择公众号以后才能加载标签和用户。因为不同的公众号里面标签和用户都不同。
     */
    loadAccounts() {
      let url = "/api/we-chat/account";
      axios.get(url).then(response => {
        this.accounts = response.data;
      });
    }
  }
};
</script>

<style scoped>
.tags {
  min-height: 340px;
  overflow: auto;
  margin-right: 5px;
}
.tags div {
  margin-bottom: 3px;
}
.tags .h-input-group div {
  height: 30px;
}
.h-transfer {
  width: 100%;
}
</style>