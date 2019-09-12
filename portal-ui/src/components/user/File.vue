<template>
  <div>
    <div class="h-panel h-panel-no-border">
      <div class="h-panel-bar">
        <span class="h-panel-title"></span>
        <span class="h-panel-right">
          <Button @click="uploadModal.opened=true">
            <i class="icon-outbox"></i>
          </Button>
        </span>
      </div>
      <div class="h-panel-body">
        <Table :datas="page.content" :stripe="true" :loading="loading">
          <TableItem title="文件名" prop="name"></TableItem>
          <TableItem title="文件大小" prop="length"></TableItem>
          <TableItem title="文件类型" prop="contentType"></TableItem>
          <TableItem title="上传时间" prop="uploadTime"></TableItem>
          <TableItem title="操作">
            <template slot-scope="{data}">
              <a class="h-btn h-btn-s" v-bind:href="download(data.id)" target="_blank">
                <i class="icon-inbox"></i>
              </a>
              <Poptip content="确认删除吗?注意：删除后无法找回！" @confirm="remove(data.id)">
                <a class="h-btn h-btn-s h-btn-text-red">
                  <i class="h-icon-trash"></i>
                </a>
              </Poptip>
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
    <UploadModal :state="uploadModal" :callback="uploadCallback" />
  </div>
</template>

<script>
import axios from "axios";
import UploadModal from "../commons/UploadModal";
export default {
  components: { UploadModal },
  data() {
    return {
      loading: false,
      uploadModal: {
        opened: false
      },
      page: {
        size: 20
      }
    };
  },
  computed: {
    download(id) {
      return function(id) {
        return `/api/storage/file/download/${id}`;
      };
    }
  },
  mounted() {
    this.search();
  },
  methods: {
    search() {
      this.loading = true;
      let data = {
        pn: this.page.number ? this.page.number - 1 : 0,
        ps: this.page.size,
        kw: null,
        ob: null,
        d: null
      };
      let url = "/api/storage/file";
      axios
        .get(url, { params: data })
        .then(response => {
          this.page = response.data;
          this.page.number = this.page.number + 1;
          this.loading = false;
        })
        .catch(error => (this.loading = false));
    },
    changePage(event) {
      this.page.number = event.page;
      this.page.size = event.size;
      this.search();
    },
    uploadCallback(result) {
      if (result && result.code === 1) {
        this.$Message({
          type: "success",
          text: "文件上传成功"
        });
        this.search();
      }
    },
    remove(id) {
      let url = `/api/storage/file/${id}`;
      axios.delete(url).then(response => {
        this.$Message({
          type: "success",
          text: "文件删除成功"
        });
        this.search();
      });
    }
  }
};
</script>

<style scoped>
.h-panel-body {
  padding: 0px;
}
</style>