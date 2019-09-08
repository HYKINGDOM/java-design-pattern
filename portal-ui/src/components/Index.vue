<template>
  <div>显示最近新增的5个任务、最近的5个订单、显示最近的5个文章{{User.name}}</div>
</template>

<script>

import { mapState } from 'vuex';
export default {
  computed: {
    ...mapState(["User"])
  }
};
</script>