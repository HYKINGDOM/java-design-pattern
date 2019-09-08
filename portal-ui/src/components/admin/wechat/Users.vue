<template>
  <div>
    <Form :model="selectedAccount">
      <FormItem label="选择公众号">
        <Select v-model="selectedAccount" :datas="accounts" :deletable="false" type="object"></Select>
      </FormItem>
    </Form>
    <Row>
      <Cell width="6">
        <div class="tags">
          <div
            v-for="tag in tags"
            v-bind:key="tag.tagId"
            @click="selectTag(tag)"
            @mouseover="overTag(tag)"
            @mouseout="mouseoverTag = null"
            class="h-input-group"
            v-bind:class="{
                'bg-blue-color': selectedTag && tag.tagId === selectedTag.tagId, 
                'bg-gray3-color': mouseoverTag && tag.tagId === mouseoverTag.tagId}"
          >
            <div
              v-bind:class="{'black-color': selectedTag && tag.tagId === selectedTag.tagId}"
            >{{tag.name}}</div>
            <Button v-if="mouseoverTag && tag.tagId === mouseoverTag.tagId" icon="h-icon-close"></Button>
          </div>
        </div>
      </Cell>
      <Cell width="18">
        <Form :model="selectedTag">
          <FormItem label="名称" prop="name">
            <input type="text" v-model="selectedTag.name" />
          </FormItem>
        </Form>
        <Transfer
          v-model="selectedTag.users"
          :datas="sourceDatas"
          keyName="id"
          :option="transferOptions"
        >
          <template slot-scope="{option}" slot="item">{{option.nickname}}</template>
        </Transfer>
      </Cell>
    </Row>
    <div class="text-right">
      <Button @click="reloadTags(selectedTag.tagId)">重置</Button>
      <Button color="blue" @click="save">保存</Button>
      <Button color="green" @click="selectedTag = {tagId:null, name: null, users: []}">新增</Button>
    </div>
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
      keyword: null,
      // 选中的组、标签，当点击的时候选中
      selectedTag: {
        tagId: null,
        name: null,
        users: []
      },
      // 鼠标放在某个Tag上面的时候，表示当前Tag
      mouseoverTag: null,
      tags: [],
      transferOptions: {
        ltHeadText: "未选择",
        rtHeadText: "已选择",
        filterable: true,
        placeholder: "关键字搜索"
        // ,
        // // 负责如何显示文本内容
        // render: function(op) {
        //   return `${op.nickname}`;
        // }
      },
      // 应该是在页面加载后，从服务器查询得到的
      // 而且在搜索的时候，也要到服务器查询
      sourceDatas: []
    };
  },
  mounted() {
    this.loadAccounts();
  },
  watch: {
    selectedAccount(account) {
      if (account && account.key) {
        // 到服务器查询所有的分组，查询结果需要分页显示。
        this.loadTags(account.key);
        // 加载所有的用户
        this.loadUsers(account.key);
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
    /**
     * 到服务器加载所有的标签、分组
     */
    loadTags(account) {
      let url = "/api/we-chat/tag/" + account;
      axios
        .get(url, {
          params: { pageNumber: this.pageNumber, keyword: this.keyword }
        })
        .then(response => {
          let page = response.data;
          let content = page.content;
          this.tags = content;
        });
    },
    loadUsers(account) {
      let url = "/api/we-chat/user/" + account;
      axios
        .get(url)
        .then(response => {
          let page = response.data;
          let content = page.content;
          this.sourceDatas = content;
        })
        .catch(ex => {
          console.error(ex);
        });
    },
    /**
     * 选中某个分组以后，需要在右边显示详情，并且加载分组对应的用户列表。
     */
    selectTag(tag) {
      this.selectedTag = tag;
    },
    overTag(tag) {
      this.mouseoverTag = tag;
    },
    reloadTags(id) {
      // 重置表单，从服务器重新加载数据
    },
    save() {
      let tag = this.selectedTag;
      tag.account = this.selectedAccount.key;
      let url = "/api/we-chat/tag";
      axios
        .post(url, tag)
        .then(response => {
          this.$Message("标签保存成功");
          // 重新加载标签的数据，更新标签列表
          this.loadTags(tag.account);
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