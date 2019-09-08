<template>
  <div>
    <Form ref="form" :model="article" :rules="validationRules">
      <div>
        <FormItem label="标题" prop="title">
          <input type="text" v-model="article.title" placeholder="标题，最多30个字符" />
        </FormItem>
      </div>
      <div>
        <FormItem label="摘要" prop="summary">
          <textarea v-model="article.summary" rows="5"></textarea>
        </FormItem>
      </div>
      <div>
        <FormItem label="标签/分类" prop="tagNames">
          <TagInput v-model="article.tagNames" :limit="10" :wordlimit="20"></TagInput>
        </FormItem>
      </div>
      <div ref="content"></div>
    </Form>
    <div class="text-right">
      <Button @click="resetForm">重置</Button>
      <Button v-on:click="submit" color="primary">提交</Button>
    </div>
  </div>
</template>

<script>
import E from "wangeditor";
import axios from "axios";
var editor;
export default {
  data() {
    return {
      article: {
        id: null,
        title: null,
        content: null,
        summary: null,
        tagNames: []
      },
      validationRules: { required: ["title"] }
    };
  },
  mounted() {
    // 创建富文本编辑器的实例
    editor = new E(this.$refs.content);
    editor.customConfig.onchange = html => {
      // 当富文本编辑器的内容发生变化，同步到文章的内容属性里面
      this.article.content = html;
    };
    editor.create();

    let id = this.$route.params.id;
    if (id) {
      axios.get(`/api/content/article/${id}`).then(response => {
        this.article = response.data;
        this.article.tagNames = this.article.tags.map(tag => tag.name);
        editor.txt.html(this.article.content);
      });
    }
  },
  methods: {
    // 重置表单
    resetForm() {
      this.article = {
        id: null,
        title: null,
        content: null,
        summary: null,
        tagNames: []
      };
      // 重置验证
      this.$refs.form.resetValid();
      // 清空富文本编辑器的内容
      editor.txt.clear();
    },
    submit() {
      let valid = this.$refs.form.valid();
      let msg = "验证失败:<br/>";
      let hasErrors = false;
      if (!valid.result) {
        // 验证失败
        hasErrors = true;
        valid.messages.forEach(m => {
          msg = msg + m.label + m.message + "<br/>";
        });
        // 弹出错误提示
        //this.$Message({ type: "error", text: msg });
      }

      // 验证富文本编辑器是否有输入内容
      // 所有变量，如果是""、''、null、0为值，那么就是false
      if (!this.article.content) {
        hasErrors = true;
        //文本框的内容会同步到article.content属性里面，如果article.content是空的
        //this.$Message({ type: "error", text: "正文不能为空，请正确填写正文！" });
        msg = msg + "正文不能为空，请正确填写正文！<br/>";
      }

      if (hasErrors) {
        this.$Message({ type: "error", text: msg });
        return;
      }

      // 如果没有摘要，从内容中截取一段
      if (!this.article.summary) {
        let text = editor.txt.text();
        //console.log(text);
        text = text.replace("&nbsp;", " ");
        if (text.length > 100) {
          text = text.substring(0, 100);
          text = text + "...";
        }
        this.article.summary = text;
      }
      // 提交数据到后台服务里面
      let url = "/api/content/article";
      if (this.article.id) {
        url = url + "/" + this.article.id;
      }
      axios.post(url, this.article, {}).then(response => {
        let result = response.data;
        if (result.code === 1) {
          // 回到搜索页面
          this.$router.push("/admin/content");
        } else {
          this.$Message({
            type: "error",
            text: result.message ? result.message : "保存内容出现问题，原因未知"
          });
        }
      });
    }
  }
};
</script>

<style scoped>
</style>