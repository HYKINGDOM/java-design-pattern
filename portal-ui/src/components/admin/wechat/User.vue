<template>
  <div>
    <Form :model="selectedAccount">
      <FormItem label="选择公众号">
        <Select v-model="selectedAccount" :datas="accounts" :deletable="false" type="object"></Select>
      </FormItem>
    </Form>
    <Table :datas="page.content" :stripe="true" :loading="loading">
      <TableItem title="昵称">
        <template slot-scope="{data}">
          <img style="width: 50px;" :src="data.headimgurl" />
          {{data.nickname}}
        </template>
      </TableItem>
      <TableItem title="OpenId" prop="openid"></TableItem>
      <TableItem title="国家" prop="country"></TableItem>
      <TableItem title="省份" prop="province"></TableItem>
      <TableItem title="城市" prop="city"></TableItem>
      <TableItem title="标签">
        <template slot-scope="{data}">
          <span class="h-tag" v-for="tag in data.tags" v-bind:key="tag.id">{{tag.name}}</span>
          <button class="h-btn h-btn-s h-btn-blue" style="float: right" @click="edit(data.openid)">
            <i class="h-icon-edit"></i>
          </button>
        </template>
      </TableItem>
      <div slot="empty">自定义提醒：暂时无数据</div>
    </Table>
    <Pagination
      align="center"
      :cur="page.pageable.pageNumber"
      :total="page.totalElements"
      :size="page.size"
      @change="changePage($event)"
    ></Pagination>
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
      selectedAccount: { key: null },
      keyword: null,

      loading: false,
      page: {
        pageable: {},
        size: 10
      }
    };
  },
  mounted() {
    this.loadAccounts();
  },
  watch: {
    selectedAccount(account) {
      if (account && account.key) {
        this.loadUserInfo(account.key);
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
    },
    loadUserInfo(account) {
      this.loading = true;
      let url = `/api/we-chat/user/users/${account}`;
      let data = {
        kw: this.keyword,
        pn: this.page.pageable.pageNumber
          ? this.page.pageable.pageNumber - 1
          : 0,
        ps: this.page.size ? this.page.size : 10
      };
      axios
        .get(url, { params: data })
        .then(response => {
          this.loading = false;
          this.page = response.data;
          this.page.pageable.pageNumber = this.page.pageable.pageNumber + 1;
        })
        .catch(error => (this.loading = false));
    },
    changePage(event) {
      this.page.pageable.pageNumber = event.page;
      this.page.size = event.size;
      this.loadUserInfo(this.selectedAccount.key);
    },
    edit(id) {
      // 切换到编辑页面
      this.$router.push("/admin/wechat/user/" + id);
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