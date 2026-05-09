<template>
    <el-dialog title="订单确认" v-model="myfocusVisible" width="55%" @close="goClose()">
     <el-table :data="goodslists"  border :key="itemKey">
       <el-table-column label="图片" width="100">
         <template #default="scope">
           <el-link @click="goToGoodsDetail(scope.row)">
               <el-image  :src="'/' + scope.row.gpicture" style="width: 60px; height: 50px;"/>
           </el-link>
         </template>
       </el-table-column>
       <el-table-column  label="商品名称" width="160">
           <template #default="scope">
           <el-link @click="goToGoodsDetail(scope.row)" :underline="false">
               <span>{{scope.row.gname}}</span>
           </el-link>
         </template>
       </el-table-column>
       <el-table-column label="商品实价" width="105">
         <template #default="scope">
           <span>{{scope.row.grprice.toFixed(1)}}</span>
         </template>
       </el-table-column>
       <el-table-column  label="购买量"  width="150">
           <template #default="scope">
               <span>&nbsp;{{scope.row.shoppingnum}}&nbsp;</span>
            </template>
       </el-table-column>
       <el-table-column  label="小计"  width="150">
            <template #default="scope">
               <span>{{(scope.row.grprice * scope.row.shoppingnum).toFixed(1)}}</span>
           </template>
       </el-table-column>
     </el-table>
     <br>
     <div>总价：¥ {{ totalPrice.toFixed(1) }} &nbsp;
      <el-button  type="success" :icon="Finished"  @click="submitOrder()">提交订单</el-button>
     </div>
 </el-dialog>
</template>
<script setup>
import { ref, computed, onMounted } from 'vue'
import {useRoute, useRouter} from 'vue-router'
import { Finished} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import myAxios from '../../axios/request.js'
const router = useRouter()
const route = useRoute() 
const myfocusVisible = ref(true) 
const goodslists = ref([])
let itemKey = ref(0)
//组件初始化
onMounted(() => {
 loadGoods()
})
//加载商品信息
const loadGoods = () => {
  myAxios.post('/api/before/cart/myCart',
 {
   busertableId: sessionStorage.getItem('bid')
 }, 
 {
   headers: {
     'Authorization': sessionStorage.getItem('buserauthtoken')
   }
 })
 .then(res => {
     goodslists.value =  res.data.result;
     itemKey.value = Math.random() //刷新表格数据
 })
 .catch((error) => {
     ElMessage.error(error)
 })
}
const goToGoodsDetail = (goods) => {
   router.push({name: 'goodsDetail', state: goods })
}
const goClose = () => {
   //跳转到前一个页面
   let path = route.query.redirect
   router.replace({ path: path === '/' || path === undefined ? '/' : path })
}
//使用计算属性计算总额
const totalPrice = computed( ()=> {
   let total = 0
   for (let i = 0; i < goodslists.value.length; i++) {
       let item = goodslists.value[i]
       total = total + item.grprice * item.shoppingnum
   }
   return total
})
const submitOrder = () =>{
  let gids = []
  let shoppingnums = []
  for (let i = 0; i < goodslists.value.length; i++) {
      let item = goodslists.value[i]
      gids[i] = item.id
      shoppingnums[i] = item.shoppingnum
  }
  myAxios.post('/api/admin/orders/submitOrder',
  {
    bgid: gids,
    bshoppingnum: shoppingnums,
    busertableId: sessionStorage.getItem('bid'),
    amount: totalPrice.value
  }, 
  {
    headers: {
      'Authorization': sessionStorage.getItem('buserauthtoken')
    }
  }).then(res => {
      if(res.data.result.id > 0){
        ElMessage.success({message: '订单提交成功，请付款！',type: 'success'})
        router.push({name: 'index'})
      } else
        ElMessage.error('订单提交失败！')
  })
  .catch((error) => {
      ElMessage.error(error)
  })
}
</script>