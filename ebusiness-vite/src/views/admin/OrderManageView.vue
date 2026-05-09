<template>
  <el-tabs type="border-card">
    <el-tab-pane label="订单管理">
       <el-form :inline="true" :model="searchParam" class="fl">
				<el-form-item label="订单编号">
          <el-input v-model="searchParam.id" placeholder="请输入订单编号" />
				</el-form-item>
				<el-form-item>
					<el-button type="primary" @click="loadOrders()">查询</el-button>
				</el-form-item>
			</el-form>
      <el-table :data="result"  :default-sort="{ prop: 'orderdate', order: 'descending' }" border :key="itemKey">
        <el-table-column prop="id" label="订单编号" width="100"></el-table-column>
        <el-table-column label="订单金额" width="150">
          <template #default="scope">
            <span>{{scope.row.amount.toFixed(1)}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="orderdate" label="下单时间" sortable width="200"></el-table-column>
        <el-table-column prop="status" :formatter="stateFormat" label="订单状态" width="150"></el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-row>
              <el-button size="small" type="primary"  @click="handleDetail(scope.row)">详情</el-button>
              <el-popconfirm v-if="scope.row.status === 0" confirm-button-text="是" cancel-button-text="否" :icon="InfoFilled" icon-color="#626AEF"
                title="真的删除吗？" @confirm="confirmEvent()" @cancel="cancelEvent">
                <template #reference>
                  <el-button size="small" type="danger">删除</el-button>
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
           v-model:page-size="pageSize"
           v-model:currentPage="currentPage"
           :page-sizes="[5, 10, 20]" :total="total" />
      </div>
    </el-tab-pane>
  </el-tabs>
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
        <el-table-column  label="小计" width="120">
           <template #default="scope">
            <span>{{scope.row.smallTotal.toFixed(1)}}</span>
          </template>
        </el-table-column>
    </el-table>
  </el-dialog>
</template>
<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import myAxios from '../../axios/request.js'
let result = ref([])
let detailResult = ref([])
let orderDetailVisible = ref(false)
let searchParam = reactive({})
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
  myAxios.post('/api/admin/orders/getAllOrders',
  {
    id: searchParam.id,
    currentPage: currentPage.value,
    pageSize: pageSize.value
  },
  {
    headers: {
      'Authorization': sessionStorage.getItem('authtoken')
    }
  })
  .then(res => {
      result.value =  res.data.result.allOrders;
      itemKey.value = Math.random() //刷新表格数据
      total.value = res.data.result.totalPage
  })
  .catch((error) => {
      ElMessage.error(error)
  })
}
//根据订单状态显示是否支付
const stateFormat = (row) => {
  if (row.status === 0) {
    return '未支付'
  } else  {
    return '已支付'
  } 
}
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
//详情
const handleDetail = (row) => {
  myAxios.post('/api/admin/orders/getOrdersDetail',
  {
    id: row.id
  },
  {
    headers: {
      'Authorization': sessionStorage.getItem('authtoken')
    }
  })
  .then(res => {
      detailResult.value =  res.data.result;
      itemKey.value = Math.random() //刷新表格数据
      orderDetailVisible.value = true
  })
  .catch((error) => {
      ElMessage.error(error)
  })
}
//删除
const confirmEvent = () => {
  ElMessage.error('不要轻易删除订单！')
  //ElMessage({message: '删除成功',type: 'success'})
}
const cancelEvent = () => {
}
</script>
