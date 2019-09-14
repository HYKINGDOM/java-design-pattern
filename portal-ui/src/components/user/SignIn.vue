<!-- 用户的每日签到功能的界面 -->
<template>
  <div>
    <div class="text-right sign-in-div">
      <Button :disabled="todayIsSignIn" color="blue" @click="signIn">{{signIngText}}</Button>
    </div>
    <Calendar
      ref="Calendar"
      @changeMonth="changeMonth"
      :markDateMore="today"
      :markDate="signInDays"
    ></Calendar>
  </div>
</template>

<script>
import axios from "axios";
import Calendar from "vue-calendar-component";
// npm install moment
var moment = require("moment");
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
      signInDays: []
    };
  },
  mounted() {
    this.loadData();
  },
  methods: {
    // date 是当前选中月份的当前天
    changeMonth(date) {
      //console.log(date);
      this.loadData(date);
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
          this.$Message({ type: "success", text: "签到成功" });
          let current = new Date();
          let today = moment(current).format("YYYY-MM-DD");
          this.$refs.Calendar.ChoseMonth(today);
          //this.loadData();
        } else {
          // 签到失败
          this.$Message({ type: "error", text: result.message });
        }
      });
    },
    loadData(date) {
      let data = {
        date: date,
        type: "ON_DUTY"
      };
      axios.get("/api/daily-sign-in/sign", { params: data }).then(response => {
        this.signInDays = response.data;
        let current = new Date();
        // npm install moment
        let today = moment(current).format("YYYY-MM-DD");
        let month = moment(current).format("YYYY-MM");
        let isSignIned = this.signInDays.some(day => {
          if (day.startsWith(month)) {
            return day === today;
          }
        });
        if (isSignIned) {
          this.todayIsSignIn = true;
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