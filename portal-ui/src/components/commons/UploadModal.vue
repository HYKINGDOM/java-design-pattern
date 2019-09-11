<template>
  <div>
    <Modal v-model="state.opened" v-bind:hasCloseIcon="true" :closeOnMask="false">
      <div slot="header">请选择文件上传</div>
      <div>
        <form ref="uploadForm">
          <Form>
            <FormItem label="请选择文件">
              <input type="file" name="file" />
            </FormItem>
          </Form>
        </form>
      </div>
      <div slot="footer">
        <Button class="h-btn" @click="upload" :loading="uploading">确定</Button>
        <Button class="h-btn" @click="show=false">取消</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
import axios from "axios";
export default {
  /**
   * state是一个对象，里面必须有opened属性
   */
  props: ["state", "callback"],
  data() {
    return {
      uploading: false
    };
  },
  watch: {},
  computed: {
    show: {
      get() {
        return this.state.opened;
      },
      set(newValue) {
        this.state.opened = newValue;
      }
    }
  },
  methods: {
    upload() {
      this.uploading = true;
      let data = new FormData(this.$refs.uploadForm);
      let url = "/api/storage/file";
      axios
        .post(url, data)
        .then(response => {
          this.uploading = false;
          let result = response.data;
          this.state.opened = false;
          this.callback(result);
        })
        .catch(error => {
          this.$Message({
            type: "error",
            text: "上传文件失败，请检查服务器日志！"
          });
          this.uploading = false;
        });
    }
  }
};
</script>