<!-- 用户的每日签到功能的界面 -->
<template>
  <div>
    <div class="text-right sign-in-div">
      <Button :disabled="todayIsSignIn" color="blue" @click="signIn">{{signIngText}}</Button>
    </div>
    <Calendar @changeMonth="changeMonth" :markDateMore="today" :markDate="signInDays"></Calendar>
  </div>
</template>

<script>
import axios from "axios";
import Calendar from "vue-calendar-component";
export default {
  components: { Calendar },
  data() {
    return {
      // 今天是否签到，要从数据库取出来
      signIngText: "签到",
      todayIsSignIn: false,
      today: [
        {
          // markDateMore 选中之后，自定义样式
          date: new Date(),
          className: "today"
        }
      ],
      // markDate 选中之后使用默认颜色，标记哪天选中
      // 已经签到的记录，也需要从数据库取出来，并且返回Date部分即可，不需要时分秒。
      signInDays: ["2019-09-01", "2019/09/02", "2019/9/3"]
    };
  },
  methods: {
    // date 是当前选中月份的当前天
    changeMonth(date) {
      console.log(date);
    },
    signIn() {
      // 执行签到
      let url = "/api/daily-sign-in/sign";
      let data = {
        type: "ON_DUTY"
      };
      axios.post(url, data).then(response => {
        let result = response.data;
        if (result.code === 1) {
          // 签到成功
        } else {
          // 签到失败
        }
      });
    }
  }
};
</script>
<style scoped>
.sign-in-div {
  max-width: 410px;
  margin-left: auto;
  margin-right: auto;
  margin-bottom: 5px;
}
.wh_container >>> .today {
  background-color: #ccc;
}
</style>