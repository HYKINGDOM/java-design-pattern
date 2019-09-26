<template>
  <div>
    <div class="h-panel">
      <div class="h-panel-bar">
        <span class="h-panel-title">流程定义管理</span>
        <span class="h-panel-right">
          <a>添加</a>
        </span>
      </div>
      <div class="h-panel-body">
        <Table
          :datas="page.content"
          :border="true"
          :checkbox="false"
          :stripe="true"
          :loading="status.loading"
        >
          <TableItem title="分类" prop="catalog"></TableItem>
          <TableItem title="名称" prop="name"></TableItem>
          <TableItem title="KEY" prop="key"></TableItem>
          <TableItem title="版本" prop="version"></TableItem>
          <TableItem title="操作">
            <template slot-scope="{data}">
              <button class="h-btn h-btn-s h-btn-red">禁用{{data.name}}</button>
              <button class="h-btn h-btn-s h-btn-red">激活{{data.name}}</button>
            </template>
          </TableItem>
          <div slot="empty">自定义提醒：暂时无数据</div>
        </Table>

        <Pagination
          align="center"
          :cur="page.number"
          :total="page.totalElements"
          :size="page.size"
          @change="changePage($event)"
        ></Pagination>
      </div>
    </div>
  </div>
</template>

<script>
import search from "../../commons/search";
export default {
  data() {
    return {
      status: {
        loading: false
      },
      page: {
        content: []
      }
    };
  },
  mounted() {
    this.search();
  },
  methods: {
    search() {
      let data = {
        // 页码
        pn: this.page.number ? this.page.number - 1 : 0,
        // 每页的大小
        ps: this.page.size,
        // 关键字
        kw: null,
        // 排序的属性
        ob: null,
        // 排序的方向
        d: null
      };
      let url = "/api/workflow/process/definition";
      search(url, data, this.status, page => (this.page = page));
    },
    changePage(event) {
      this.page.number = event.page;
      this.page.size = event.size;
      this.search();
    }
  }
};
</script>