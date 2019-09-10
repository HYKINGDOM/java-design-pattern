<template>
  <div>
    <Table :datas="datas" :border="border" :checkbox="checkbox" :stripe="stripe" :loading="loading">
      <TableItem align="center" title="订单编号" prop="id"></TableItem>
      <TableItem align="center" title="用户名" prop="username"></TableItem>
      <TableItem align="center" title="用户手机" prop="phoneNum" sort="auto"></TableItem>
      <TableItem align="center" title="礼品" prop="gift"></TableItem>
      <TableItem align="center" title="花费积分" prop="payJf"></TableItem>
      <TableItem align="center" title="兑换日期" prop="date"></TableItem>
      <TableItem align="center" title="收货地址" prop="address"></TableItem>
      <TableItem align="center" title="物流信息" prop="wlxx"></TableItem>
      <TableItem align="center" title="订单状态" prop="ddzt"></TableItem>
      <TableItem title="订单操作" align="center" prop="ddcz">
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
                    {id:1,username:'张三',phoneNum:'111111',gift:"太阳伞",payJf:12,date:'2017-xxx-xx', address:'xxxxxxxxx', wlxx:'正在出库',ddzt:'待出库'},
                    {id:2,username:'张三',phoneNum:'111111',gift:"太阳伞",payJf:12,date:'2017-xxx-xx', address:'xxxxxxxxx', wlxx:'正在出库',ddzt:'待出库'},
                    {id:3,username:'张三',phoneNum:'111111',gift:"太阳伞",payJf:12,date:'2017-xxx-xx', address:'xxxxxxxxx', wlxx:'正在出库',ddzt:'待出库'},
                    {id:4,username:'张三',phoneNum:'111111',gift:"太阳伞",payJf:12,date:'2017-xxx-xx', address:'xxxxxxxxx', wlxx:'正在出库',ddzt:'待出库'},
                    {id:5,username:'张三',phoneNum:'111111',gift:"太阳伞",payJf:12,date:'2017-xxx-xx', address:'xxxxxxxxx', wlxx:'正在出库',ddzt:'待出库'},
                    {id:6,username:'张三',phoneNum:'111111',gift:"太阳伞",payJf:12,date:'2017-xxx-xx', address:'xxxxxxxxx', wlxx:'正在出库',ddzt:'待出库'},
                    {id:7,username:'张三',phoneNum:'111111',gift:"太阳伞",payJf:12,date:'2017-xxx-xx', address:'xxxxxxxxx', wlxx:'正在出库',ddzt:'待出库'},
                    {id:8,username:'张三',phoneNum:'111111',gift:"太阳伞",payJf:12,date:'2017-xxx-xx', address:'xxxxxxxxx', wlxx:'正在出库',ddzt:'待出库'},
                    {id:9,username:'张三',phoneNum:'111111',gift:"太阳伞",payJf:12,date:'2017-xxx-xx', address:'xxxxxxxxx', wlxx:'正在出库',ddzt:'待出库'},
                    {id:10,username:'张三',phoneNum:'111111',gift:"太阳伞",payJf:12,date:'2017-xxx-xx', address:'xxxxxxxxx', wlxx:'正在出库',ddzt:'待出库'},
                    {id:11,username:'张三',phoneNum:'111111',gift:"太阳伞",payJf:12,date:'2017-xxx-xx', address:'xxxxxxxxx', wlxx:'正在出库',ddzt:'待出库'},
                    {id:12,username:'张三',phoneNum:'111111',gift:"太阳伞",payJf:12,date:'2017-xxx-xx', address:'xxxxxxxxx', wlxx:'正在出库',ddzt:'待出库'},
                    {id:13,username:'张三',phoneNum:'111111',gift:"太阳伞",payJf:12,date:'2017-xxx-xx', address:'xxxxxxxxx', wlxx:'正在出库',ddzt:'待出库'},
                    {id:14,username:'张三',phoneNum:'111111',gift:"太阳伞",payJf:12,date:'2017-xxx-xx', address:'xxxxxxxxx', wlxx:'正在出库',ddzt:'待出库'},
                    {id:15,username:'张三',phoneNum:'111111',gift:"太阳伞",payJf:12,date:'2017-xxx-xx', address:'xxxxxxxxx', wlxx:'正在出库',ddzt:'待出库'},
                    {id:16,username:'张三',phoneNum:'111111',gift:"太阳伞",payJf:12,date:'2017-xxx-xx', address:'xxxxxxxxx', wlxx:'正在出库',ddzt:'待出库'},
                    {id:17,username:'张三',phoneNum:'111111',gift:"太阳伞",payJf:12,date:'2017-xxx-xx', address:'xxxxxxxxx', wlxx:'正在出库',ddzt:'待出库'},
                    {id:18,username:'张三',phoneNum:'111111',gift:"太阳伞",payJf:12,date:'2017-xxx-xx', address:'xxxxxxxxx', wlxx:'正在出库',ddzt:'待出库'},
                    {id:19,username:'张三',phoneNum:'111111',gift:"太阳伞",payJf:12,date:'2017-xxx-xx', address:'xxxxxxxxx', wlxx:'正在出库',ddzt:'待出库'},
                    {id:20,username:'张三',phoneNum:'111111',gift:"太阳伞",payJf:12,date:'2017-xxx-xx', address:'xxxxxxxxx', wlxx:'正在出库',ddzt:'待出库'},
                    {id:21,username:'张三',phoneNum:'111111',gift:"太阳伞",payJf:12,date:'2017-xxx-xx', address:'xxxxxxxxx', wlxx:'正在出库',ddzt:'待出库'},
                    {id:22,username:'张三',phoneNum:'111111',gift:"太阳伞",payJf:12,date:'2017-xxx-xx', address:'xxxxxxxxx', wlxx:'正在出库',ddzt:'待出库'},
                    {id:23,username:'张三',phoneNum:'111111',gift:"太阳伞",payJf:12,date:'2017-xxx-xx', address:'xxxxxxxxx', wlxx:'正在出库',ddzt:'待出库'},
                    {id:24,username:'张三',phoneNum:'111111',gift:"太阳伞",payJf:12,date:'2017-xxx-xx', address:'xxxxxxxxx', wlxx:'正在出库',ddzt:'待出库'}
                ],
                pagination: {
                    page: 1,
                    size: 10,
                    total: 100
                }
            };
        },
        methods: {
            currentChange(value) {
                console.log(value);
            }
        }
    };
</script>
