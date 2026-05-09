<template>
     <el-dialog title="我的收藏" v-model="myfocusVisible" width="46%" @close="goClose">
      <el-table :data="goodslists"  border :key="itemKey">
        <el-table-column label="图片" width="100">
          <template #default="scope">
            <el-image :src="'/' + scope.row.gpicture"  style="width: 50px; height: 50px;"/>
          </template>
        </el-table-column>
        <el-table-column prop="gname" label="商品名称" width="150"></el-table-column>
        <el-table-column label="商品实价" width="100">
          <template #default="scope">
            <span>{{scope.row.grprice.toFixed(1)}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="typename" label="商品类型"  width="100"></el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-row>
              <el-button size="small" type="primary"  @click="goToGoodsDetail(scope.row)">详情</el-button>
            </el-row>
          </template>
        </el-table-column>
      </el-table>
      <div>
        <div>
        <el-pagination background
           @current-change="handleCurrentChange"
           @size-change="handleSizeChange"
           layout="total, sizes, prev, pager, next"
           v-model:currentPage="currentPage"
           v-model:page-size="pageSize"
           :page-sizes="[5, 10, 20]" :total="total" />
      </div>
      </div>
  </el-dialog>
</template>
<script setup>
import { ElMessage } from 'element-plus'
import myAxios from '../../axios/request.js'
import { onMounted, ref } from 'vue'
import {useRoute, useRouter} from 'vue-router'

const router = useRouter()
const route = useRoute() 
const myfocusVisible = ref(true) 
const goodslists = ref([])
let total = ref(0)
let currentPage = ref(1)
let pageSize = ref(5)
let itemKey = ref(0)
//组件初始化
onMounted(() => {
  loadGoods()
})
//加载商品信息
const loadGoods = () => {
  myAxios.post('/api/before/focus/getMyFocusGoods',
  {
    currentPage: currentPage.value,
    pageSize: pageSize.value,
    busertableId: sessionStorage.getItem('bid')
  }, 
  {
    headers: {
      'Authorization': sessionStorage.getItem('buserauthtoken')
    }
  })
  .then(res => {
      goodslists.value =  res.data.result.allGoods;
      itemKey.value = Math.random() //刷新表格数据
      total.value = res.data.result.totalPage
  })
  .catch((error) => {
      ElMessage.error(error)
  })
}
//页码变换
const handleCurrentChange = (val) => {
    currentPage.value = val
    loadGoods()
}
const handleSizeChange = (val) => {
    pageSize.value = val
    currentPage.value = 1
    loadGoods()
}
const goToGoodsDetail = (goods) => {
    router.push({name: 'goodsDetail', state: goods })
}
const goClose = () => {
    //跳转到前一个页面
    let path = route.query.redirect
    router.replace({ path: path === '/' || path === undefined ? '/' : path })
}
</script>
<style scoped>
</style>