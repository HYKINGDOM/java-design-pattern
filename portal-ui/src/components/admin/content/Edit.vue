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
      <div ref="editorBar" class="editor editor-toolbar"></div>
      <div ref="editorContent" class="editor editor-content"></div>
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
    editor = new E(this.$refs.editorBar, this.$refs.editorContent);
    editor.customConfig.onchange = html => {
      // 当富文本编辑器的内容发生变化，同步到文章的内容属性里面
      this.article.content = html;
    };
    editor.customConfig.uploadImgServer = "/api/storage/file";
    // 最大文件限制
    editor.customConfig.uploadImgMaxSize = 10 * 1024 * 1024;
    // 上传文件的属性名称
    editor.customConfig.uploadFileName = "file";
    // 自定义文件上传事件监听
    editor.customConfig.uploadImgHooks = {
      before: function(xhr, editor, files) {
        // 图片上传之前触发
        // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象，files 是选择的图片文件
        // 如果返回的结果是 {prevent: true, msg: 'xxxx'} 则表示用户放弃上传
        // return {
        //     prevent: true,
        //     msg: '放弃上传'
        // }
      },
      success: function(xhr, editor, result) {
        // 图片上传并返回结果，图片插入成功之后触发
        // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象，result 是服务器端返回的结果
      },
      fail: function(xhr, editor, result) {
        // 图片上传并返回结果，但图片插入错误时触发
        // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象，result 是服务器端返回的结果
      },
      error: function(xhr, editor) {
        // 图片上传出错时触发
        // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象
      },
      timeout: function(xhr, editor) {
        // 图片上传超时时触发
        // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象
      },
      // 如果服务器端返回的不是 {errno:0, data: [...]} 这种格式，可使用该配置
      // （但是，服务器端返回的必须是一个 JSON 格式字符串！！！否则会报错）
      customInsert: function(insertImg, result, editor) {
        var url = `/api/storage/file/download/${result.attachment}`;
        insertImg(url);
      }
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

<style>
.editor {
  border: 1px solid #ccc;
}
.editor-content {
  border-top: none;
  height: 600px;
}
</style>