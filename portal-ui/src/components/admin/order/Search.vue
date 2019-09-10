<template>
  <div>
    <p>
    <span>开始时间：</span>
    <DatePicker v-model="value" type="datehour"></DatePicker>
    <span>结束时间：</span>
    <DatePicker v-model="value" type="datehour"></DatePicker>
    <Search @search="search" v-model="searchText3" v-width="300" showSearchButton placeholder="查询示例3"><i class="h-icon-search"></i></Search>
    </p>
    <p><h-switch v-model="show" small>订单操作</h-switch></p>
    <Table :datas="datas" :border="border" :checkbox="checkbox" :stripe="stripe" :loading="loading">
      <TableItem align="center" title="订单编号" prop="id"></TableItem>
      <TableItem align="center" title="用户名" prop="username"></TableItem>
      <TableItem align="center" title="用户手机" prop="phoneNum" sort="auto"></TableItem>
      <TableItem align="center" title="所属部门" prop="department" sort="auto"></TableItem>
      <TableItem align="center" title="礼品" prop="gift"></TableItem>
      <TableItem align="center" title="花费积分" prop="payJf"></TableItem>
      <TableItem align="center" title="兑换日期" prop="date"></TableItem>
      <TableItem align="center" title="收货地址" prop="address"></TableItem>
      <TableItem align="center" title="物流信息" prop="wlxx"></TableItem>
      <TableItem align="center" title="订单状态" prop="ddzt"></TableItem>
      <TableItem v-if="show" title="订单操作" align="center" prop="ddcz">
        <template slot-scope="{data}">
          <button class="h-btn h-btn-s h-btn-red" @click="remove(datas, data)"><i class="h-icon-trash"></i></button>
          <button class="h-btn h-btn-s h-btn-red" @click="remove(datas, data)"><i class="h-icon-edit"></i></button>
        </template>
      </TableItem>
      <div slot="empty">自定义提醒：暂时无数据</div>
    </Table>
    <Pagination v-model="pagination" align="center" @change="currentChange"></Pagination>
  </div>
</template>

<script>
    export default {
        data() {
            return {
                show: false,
                border: true,
                stripe: true,
                checkbox: false,
                serial: true,
                loading: false,
                datas: [
                    {id:1,username:'张三',phoneNum:'111111',department:"财务",gift:"太阳伞",payJf:12,date:'2017-xxx-xx', address:'xxxxxxxxx', wlxx:'正在出库',ddzt:'待出库'},
                    {id:1,username:'李四',phoneNum:'1111211',department:"财务",gift:"太阳伞",payJf:12,date:'2017-xxx-xx', address:'xxxxxxxxx', wlxx:'正在出库',ddzt:'待出库'},
                    {id:1,username:'王五',phoneNum:'1111111',department:"财务",gift:"太阳伞",payJf:12,date:'2017-xxx-xx', address:'xxxxxxxxx', wlxx:'正在出库',ddzt:'待出库'},
                    {id:1,username:'赵六',phoneNum:'1113111',department:"财务",gift:"太阳伞",payJf:12,date:'2017-xxx-xx', address:'xxxxxxxxx', wlxx:'正在出库',ddzt:'待出库'},
                    {id:1,username:'甲',phoneNum:'1111411',department:"财务",gift:"太阳伞",payJf:12,date:'2017-xxx-xx', address:'xxxxxxxxx', wlxx:'正在出库',ddzt:'待出库'},
                    {id:1,username:'张三',phoneNum:'111111',department:"财务",gift:"太阳伞",payJf:12,date:'2017-xxx-xx', address:'xxxxxxxxx', wlxx:'正在出库',ddzt:'待出库'}
                    ],
                pagination: {
                    page: 1,
                    size: 10,
                    total: 100
                },
                searchText3: ''
            };
        },
        methods: {
            currentChange(value) {
                console.log(value);
            },
            search(data) {
                this.$Message.info(`查询“${data}”`);
            }
        }
    };
</script>
