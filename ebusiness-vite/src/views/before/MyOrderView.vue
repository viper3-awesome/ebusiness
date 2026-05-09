<template>
    <el-dialog title="我的订单" v-model="myordeVisible" width="60%" @close="goClose">
      <el-table :data="result"  :default-sort="{ prop: 'orderdate', order: 'descending' }" border :key="itemKey">
        <el-table-column prop="id" label="订单编号" width="100"></el-table-column>
        <el-table-column label="订单金额" width="100">
          <template #default="scope">
            <span>{{scope.row.amount.toFixed(1)}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="orderdate" label="下单时间" sortable width="200"></el-table-column>
        <el-table-column prop="status"  label="订单状态" width="150">
          <template #default="scope">
            <el-row>
              <el-button v-if="scope.row.status === 0" size="small" type="success"  @click="goPay(scope.row)">去支付</el-button>
              <span v-if="scope.row.status === 1">已支付</span>
            </el-row>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-row>
              <el-button size="small" type="primary"  @click="handleDetail(scope.row)">详情</el-button>
              <el-popconfirm v-if="scope.row.status === 0" confirm-button-text="是" cancel-button-text="否" :icon="InfoFilled" icon-color="#626AEF"
                title="真的取消吗？" @confirm="confirmEvent()" @cancel="cancelEvent">
                <template #reference>
                  <el-button size="small" type="danger">取消</el-button>
                </template>
              </el-popconfirm>
            </el-row>
          </template>
        </el-table-column>
      </el-table>
      <div>
        <el-pagination background
           @current-change="handleCurrentChange"
           @size-change="handleSizeChange"
           layout="total, sizes, prev, pager, next"
           v-model:currentPage="currentPage"
           v-model:page-size="pageSize"
           :page-sizes="[5, 10, 20]" :total="total" />
      </div>
  </el-dialog>
  <el-dialog title="订单详情" v-model="orderDetailVisible">
    <el-table :data="detailResult" border :key="itemKey">
        <el-table-column prop="id" label="商品编号" width="120"></el-table-column>
        <el-table-column prop="gname" label="商品名称" width="120"></el-table-column>
        <el-table-column  label="单价" width="120">
          <template #default="scope">
            <span>{{scope.row.grprice.toFixed(1)}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="shoppingnum" label="数量" width="120"></el-table-column>
        <el-table-column  label="小计" >
           <template #default="scope">
            <span>{{scope.row.smallTotal.toFixed(1)}}</span>
          </template>
        </el-table-column>
    </el-table>
  </el-dialog>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import {useRoute, useRouter} from 'vue-router'
import { ElMessage } from 'element-plus'
import myAxios from '../../axios/request.js'
const router = useRouter()
const route = useRoute() 
let result = ref([])
let detailResult = ref([])
let myordeVisible = ref(true)
let orderDetailVisible = ref(false)
let total = ref(0)
let currentPage = ref(1)
let pageSize = ref(5)
let itemKey = ref(0)
//组件初始化
onMounted(() => {
  loadOrders()
})
//加载订单
const loadOrders = ()=>{
  //headers放在第三个参数
  myAxios.post('/api/admin/orders/getOrdersByUid',
  {
    busertableId: sessionStorage.getItem('bid'),
    currentPage: currentPage.value,
    pageSize: pageSize.value
  },
  {
    headers: {
      'Authorization': sessionStorage.getItem('buserauthtoken')
    }
  })
  .then(res => {
      result.value =  res.data.result.ordersByUid;
      itemKey.value = Math.random() //刷新表格数据
      total.value = res.data.result.totalPage
  })
  .catch((error) => {
      ElMessage.error(error)
  })
}
//根据订单状态显示是否支付<el-table-column prop="status" :formatter="stateFormat"  label="订单状态" width="150">
// const stateFormat = (row) => {
//   if (row.status === 0) {
//     return '未支付'
//   } else  {
//     return '已支付'
//   } 
// }
//页码变换
const handleCurrentChange = (val) => {
    currentPage.value = val
    loadOrders()
}
const handleSizeChange = (val) => {
    pageSize.value = val
    currentPage.value = 1
    loadOrders()
}
//支付订单
const goPay = (row) => {
  myAxios.post('/api/admin/orders/goPay',
  {
    id: row.id
  },
  {
    headers: {
      'Authorization': sessionStorage.getItem('buserauthtoken')
    }
  })
  .then(res => {
      if(res.data.msgId === 'A001'){
        ElMessage.success({message: '订单支付成功！',type: 'success'})
        //支付成功加载订单
        loadOrders()
      }
  })
  .catch((error) => {
      ElMessage.error(error)
  })
}
//详情
const handleDetail = (row) => {
  myAxios.post('/api/admin/orders/getOrdersDetail',
  {
    id: row.id
  },
  {
    headers: {
      'Authorization': sessionStorage.getItem('buserauthtoken')
    }
  })
  .then(res => {
      detailResult.value =  res.data.result
      itemKey.value = Math.random() //刷新表格数据
      orderDetailVisible.value = true
  })
  .catch((error) => {
      ElMessage.error(error)
  })
}
const goClose = () => {
    //跳转到前一个页面
    let path = route.query.redirect
    router.replace({ path: path === '/' || path === undefined ? '/' : path })
}
</script>
