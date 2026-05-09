<template>
  <div>
    <HeaderView @goIndex="goToIndex" @searchIndex="searchToIndex"/>
  </div>
  <div>
    <el-row>
        <el-col
        v-for="(item, index) in goodslists"
        :key="item.id"
        :span="4"
        :offset="index > 0 && (index ==1 || (index !=1 && index % 5 != 0))? 1 : 0">
        <!--offset左侧的间隔栅数，一共24栅-->
            <el-card :body-style="{ padding: '0px' }">
                <el-link :underline="false" @click="goToGoodsDetail(item)">
                  <img :src="'/' + item.gpicture" class="image"/>
                </el-link>
                <div style="padding: 5px">
                    <el-link :underline="false" @click="goToGoodsDetail(item)"><span class="myfont">{{item.gname}}</span></el-link>
                    <br>
                    <span class="myfont">&yen;<s>{{item.goprice.toFixed(1)}}</s></span> &nbsp; 
                    <span class="yourfont">&yen;{{item.grprice.toFixed(1)}}</span>
                </div>
            </el-card>
        </el-col>
    </el-row>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import HeaderView from '../../components/HeaderView.vue'
import {useRouter} from 'vue-router'
import { ElMessage} from 'element-plus'
import myAxios from '../../axios/request.js'
const router = useRouter()
let goodslists = ref([])
onMounted (()=> {
    goToIndex(0)
})
//typeid子组件传递过来的数据
const goToIndex = (typeid) => {
  myAxios.post('/api/admin/goods/getGoodsIndex',
  {
    goodstypeId: typeid
  }
  ).then(res => {
      goodslists.value =  res.data.result;
  })
  .catch((error) => {
      ElMessage.error(error)
  })
}
//searchV子组件传递过来的数据
const searchToIndex = (searchV) => {
  myAxios.post('/api/admin/goods/getGoodsIndex',
  {
    gname: searchV
  }
  ).then(res => {
      goodslists.value =  res.data.result;
  })
  .catch((error) => {
      ElMessage.error(error)
  })
}
const goToGoodsDetail = (goods) => {
    //从Vue Router的2022-8-22 更新后，弃用params传参
    //使用 History API 方式传递和接收， 在跳转前的页面使用 state 参数
    router.push({name: 'goodsDetail', state: goods})
}
</script>
<style scoped>
.myfont {
  font-size: 10pt;
  color: rgb(125, 123, 123);
}
.yourfont {
  font-size: 11pt;
  color: rgb(249, 7, 7);
}
.image {
  width: 100%;
  height: 180px;
  display: block;
  object-fit: contain;
  background: #f5f5f5;
}
.el-col {
  margin-bottom: 10px;
}
</style>