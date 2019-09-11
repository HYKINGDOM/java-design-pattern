<template>
  <div>
    <Row :space="30">
      <Cell :xs="24" :sm="24" :md="24" :lg="8" :xl="8">
        <div class="h-panel">
          <div class="h-panel-bar">
            <div class="h-panel-title">积分任务</div>
            <!-- <div class="h-panel-right">
              <span class="gray-color">总共达成</span>
              <i class="h-split"></i>
              <span class="font20 primary-color">200</span>
              <i class="h-split"></i>
              <span class="gray-color">W</span>
            </div>-->
          </div>
          <div class="h-panel-body progress-div home-part-body">
            <div>完善个人资料</div>
            <div>分享文章</div>
            <div>每日打卡</div>
            <div>炫耀积分</div>
          </div>
        </div>
      </Cell>
      <Cell :xs="24" :sm="24" :md="24" :lg="8" :xl="8">
        <div class="h-panel">
          <div class="h-panel-bar">
            <div class="h-panel-title">工作流任务</div>
          </div>
          <div class="h-panel-body progress-div home-part-body">
            <div>
              <div style="display: inline-block;">XX流程XX任务</div>
              <div style="float: right;">2019年08月01日12:01:02</div>
            </div>
            <div>
              <div style="display: inline-block;">XX流程XX任务</div>
              <div style="float: right;">2019年08月01日12:01:02</div>
            </div>
            <div>
              <div style="display: inline-block;">XX流程XX任务</div>
              <div style="float: right;">2019年08月01日12:01:02</div>
            </div>
            <div>
              <div style="display: inline-block;">XX流程XX任务</div>
              <div style="float: right;">2019年08月01日12:01:02</div>
            </div>
          </div>
        </div>
      </Cell>
      <Cell :xs="24" :sm="24" :md="24" :lg="8" :xl="8">
        <div class="h-panel">
          <div class="h-panel-bar">
            <div class="h-panel-title">今天计划</div>
          </div>
          <div class="h-panel-body progress-div home-part-body">
            <div>在12点之前完成定时任务调度服务的开发和测试</div>
            <div>下午2点组织项目讨论会议</div>
            <div>下午3点跟市场部沟通关于积分兑换活动的推广事宜</div>
          </div>
        </div>
      </Cell>
      <Cell :xs="24" :sm="24" :md="24" :lg="8" :xl="8">
        <div class="h-panel">
          <div class="h-panel-bar">
            <div class="h-panel-title">备忘录</div>
          </div>
          <div class="h-panel-body progress-div home-part-body">
            <div>在12点之前完成定时任务调度服务的开发和测试</div>
            <div>下午2点组织项目讨论会议</div>
            <div>下午3点跟市场部沟通关于积分兑换活动的推广事宜</div>
            <div>下午3点跟市场部沟通关于积分兑换活动的推广事宜</div>
            <div>下午3点跟市场部沟通关于积分兑换活动的推广事宜</div>
            <div>下午3点跟市场部沟通关于积分兑换活动的推广事宜</div>
          </div>
        </div>
      </Cell>
    </Row>
  </div>
</template>

<script>
import { mapState } from "vuex";
export default {
  computed: {
    ...mapState(["User"])
  }
};
</script>

<style scoped>
.h-panel {
  min-height: 181px;
  max-height: 181px;
}
.h-panel .h-panel-body{
  min-height: 124px;
  max-height: 124px;
  overflow: auto;
}
</style>