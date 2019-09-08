<template>
  <div>
    <div class="search-bar">
      <div class="h-input-group" v-width="500">
        <input v-model="keyword" type="text" placeholder="输入关键字进行搜索" />
        <Button color="primary" @click="search">搜索</Button>
        <router-link to="/admin/content/edit" class="h-btn h-btn-default add-btn">新增</router-link>
      </div>
    </div>
    <div>
      通过标签搜索:
      <span class="h-tag" v-for="tag in tags" v-bind:key="tag.id">
        <label>
          <input
            @click="selectTag($event)"
            v-bind:value="tag.id"
            type="checkbox"
            v-if="multiTags===true"
          />
          <input
            @click="selectTag($event)"
            v-bind:value="tag.id"
            type="radio"
            name="tags"
            v-if="multiTags===false"
          />
          {{tag.name}}
        </label>
      </span>
      <label>
        <input type="checkbox" @click="multiTags=!multiTags" />多选
      </label>
    </div>
    <div>
      <Table :datas="articles" :border="false" :stripe="true" :loading="tableLoading">
        <TableItem title="标题" prop="title"></TableItem>
        <TableItem title="摘要" prop="summary"></TableItem>
        <TableItem title="发布时间" prop="publishTime"></TableItem>
        <TableItem title="状态">
          <template slot-scope="{data}">
            <span class="h-tag">{{data.status}}</span>
            <button class="h-btn h-btn-s h-btn-blue" @click="edit(data)">
              <i class="h-icon-edit"></i>
            </button>
            <button class="h-btn h-btn-s h-btn-red" @click="remove(data)">
              <i class="h-icon-trash"></i>
            </button>
          </template>
        </TableItem>
        <div slot="empty">暂无数据</div>
      </Table>
      <Pagination
        align="center"
        :cur="pageNumber"
        :total="page.totalElements"
        :size="page.size"
        @change="changePage($event)"
      ></Pagination>
    </div>
  </div>
</template>

<script>
import axios from "axios";
export default {
  data() {
    return {
      pageSize: 10,
      pageNumber: 0, // 页码
      multiTags: false, //标签是否可以复选
      tagsId: [], //搜索哪些标签的文章

      // 从服务器加载得到的标签列表，用于显示标签进行搜索
      tags: [],

      keyword: null, // 搜索关键字
      tableLoading: false, // 是否正在加载数据
      page: {}, // 分页条件
      articles: [] // 远程返回的数据内容
    };
  },
  methods: {
    selectTag(event) {
      let input = event.target;
      if (this.multiTags === true) {
        // 检查input的checked状态，如果勾选则表示要选择；不勾选则表示要移除
        let checked = input.checked;
        if (checked) {
          // 加入
          // 检查input的值是否在tagsId里面，如果不在则新加入
          let exists = this.tagsId.some(v => {
            return v === input.value;
          });
          if (!exists) {
            this.tagsId.push(input.value);
          }
        } else {
          // 移除
          let index = this.tagsId.findIndex(v => v === input.value);
          if (index != -1) {
            this.tagsId.splice(index, 1);
          }
        }
      } else {
        // 直接把tagsId替换成一个新的数组
        this.tagsId = [input.value];
      }
    },
    search() {
      this.tableLoading = true;
      // 查找数据
      let data = {
        pn: !this.pageNumber ? 0 : this.pageNumber - 1,
        ps: this.pageSize,
        kw: this.keyword
      };
      let url = "/api/content/article";
      if (this.tagsId) {
        url = url + "?";
        this.tagsId.forEach((v, index) => {
          url = url + "tagsId=" + v + "&";
        });
        url = url.substring(0, url.length - 1);
      }
      axios
        .get(url, { params: data })
        .then(response => {
          let page = response.data;
          this.articles = page.content;
          this.page = page;
          this.tableLoading = false;
          this.pageNumber = page.number + 1;
          this.pageSize = page.size;
        })
        .catch(ex => {
          this.tableLoading = false;
        });
    },
    changePage(event) {
      this.pageSize = event.size;
      this.pageNumber = event.page;
      this.search();
    },
    remove(data) {
      this.$Confirm(
        `确定要【${data.title}】删除吗？删除以后无法恢复！`,
        "确定删除？"
      ).then(() => {
        let id = data.id;
        axios.delete("/api/content/article/" + id).then(response => {
          this.$Message("删除成功");
          this.search();
        });
      });
    },
    edit(data){
      this.$router.replace(`/admin/content/${data.id}`);
    }
  },
  mounted() {
    // 此函数是在模板生成HTML，并且挂载到html文件中以后调用的函数
    // 此处去加载标签的信息
    axios.get("/api/content/tag").then(response => {
      // 返回的数据是一个List，其实就是数组
      this.tags = response.data;
    });
  }
};
</script>

<style scoped>
.search-bar .h-input-group {
  margin-left: auto;
  margin-right: auto;
}
div.h-input-group > .h-btn.add-btn {
  border-radius: 4px;
}
</style>