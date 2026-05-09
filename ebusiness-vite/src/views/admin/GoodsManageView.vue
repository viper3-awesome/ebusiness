<template>
  <el-tabs type="border-card">
    <el-tab-pane label="商品管理">
      <el-form :inline="true" :model="searchParam" class="fl">
				<el-form-item label="商品类型">
					<el-select v-model="searchParam.goodstypeId" placeholder="商品类型">
						<el-option label="---" value="0"></el-option>
						<el-option v-for="item in typeoptions" :key="item.id" :label="item.typename" :value="item.id" />
					</el-select>
				</el-form-item>
        <el-form-item label="商品名称" prop="gname">
          <el-input v-model="searchParam.gname" placeholder="商品名称" />
        </el-form-item>
				<el-form-item>
					<el-button type="primary" @click="loadGoods()">查询</el-button>
				</el-form-item>
        <el-form-item>
				  <el-button size="medium" type="success" @click="openadd()">新增</el-button>
				</el-form-item>
			</el-form>
      <el-table :data="result" border :key="itemKey">
        <el-table-column type="index" label="序号" width="100"></el-table-column>
        <el-table-column prop="id" label="商品ID" width="150"></el-table-column>
        <el-table-column prop="gname" label="商品名称" width="200"></el-table-column>
        <el-table-column prop="typename" label="商品类型" width="150"></el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-row>
              <el-button size="small" type="success" @click="handleEdit(scope.row,'detail')">详情
              </el-button>
              <el-button size="small" type="primary" @click="handleEdit(scope.row, 'update')">编辑
              </el-button>
              <el-popconfirm confirm-button-text="是" cancel-button-text="否" :icon="InfoFilled" icon-color="#626AEF"
                title="真的删除吗？" @confirm="confirmEvent(scope.row, scope.$index)" @cancel="cancelEvent">
                <template #reference>
                  <el-button size="small" type="danger">删除
                  </el-button>
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
           :page-sizes="[5, 10, 20]"
           :total="total" />
      </div>
    </el-tab-pane>
  </el-tabs>
  <el-dialog v-model="addFormVisible" title="增加商品">
    <el-form :model="addForm" class="fl" ref="addFormRef" :rules="rules">
      <el-input v-model="addForm.act" type="hidden" />
      <el-form-item label="商品名称" prop="gname">
        <el-input v-model="addForm.gname" placeholder="请输入商品名称" />
      </el-form-item>
      <el-form-item label="商品原价" prop="goprice">
        <el-input v-model="addForm.goprice" placeholder="请输入商品价格"/>
      </el-form-item>
      <el-form-item label="商品折扣价" prop="grprice">
        <el-input v-model="addForm.grprice" placeholder="请输入商品折扣价"/>
      </el-form-item>
      <el-form-item label="商品库存" prop="gstore">
        <el-input v-model="addForm.gstore" placeholder="请输入商品库存" />
      </el-form-item>

      <el-form-item label="照片">
        <!--  eslint-disable  -->
          <template v-slot="scope">
            <el-upload
              action="http://localhost:8443/eBusiness/api/admin/goods/fileInit"
              list-type="picture-card"
              :on-change="changeFile"
              :file-list="myFileList"
              >
              <i class="el-icon-plus"></i>
              <template #tip>
                <div style="font-size: 12px;color: #919191;">
                  单次限制上传一张照片
                </div>
              </template>
            </el-upload>
          </template>
      </el-form-item>
      <el-form-item label="是否广告">
        <el-radio-group v-model="addForm.isAdvertisement">
          <el-radio label="1" size="large">是</el-radio>
          <el-radio label="2" size="large">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="商品类型" prop="goodstypeId">
        <el-select v-model="addForm.goodstypeId" placeholder="商品类型">
          <el-option label="---" value="0"></el-option>
          <el-option v-for="item in typeoptions" :key="item.id" :label="item.typename" :value="item.id" />
        </el-select>
      </el-form-item>
    </el-form>
    <el-button @click="resetForm(addFormRef)">取消</el-button>
    <el-button type="primary" @click="add(addFormRef)">新增</el-button>
  </el-dialog>

  <el-dialog title="商品修改" v-model="updateFormVisible">
    <el-form :model="detailData" class="fl" ref="detailDataRef" :rules="rules" >
      <el-input v-model="detailData.act" type="hidden" />
      <el-form-item label="商品ID">
        <el-input v-model="detailData.id" disabled/>
      </el-form-item>
      <el-form-item label="商品名称" prop="gname">
        <el-input v-model="detailData.gname"/>
      </el-form-item>
      <el-form-item label="商品原价" prop="goprice">
        <el-input v-model="detailData.goprice"/>
      </el-form-item>
      <el-form-item label="商品折扣价" prop="grprice">
        <el-input v-model="detailData.grprice"/>
      </el-form-item>
      <el-form-item label="商品库存" prop="gstore">
        <el-input v-model="detailData.gstore"/>
      </el-form-item>
      <el-input v-model="detailData.gpicture" type="hidden" />
        <el-form-item label="照片">
          <!--  eslint-disable  -->
          <template v-slot="scope">
            <el-image :src="imgurl" style="width: 200px; height: 200px; object-fit: contain; background: #f5f5f5;"/>
            <el-upload
              action="http://localhost:8443/eBusiness/api/admin/goods/fileInit"
              list-type="picture-card"
              :on-change="changeFile"
              :file-list="myFileList"
              >
              <i class="el-icon-plus"></i>
              <template #tip>
                <div style="font-size: 12px;color: #919191;">
                  单次限制上传一张照片
                </div>
              </template>
            </el-upload>
          </template>
        </el-form-item>        
      <el-form-item label="是否广告" prop="isAdvertisement">
        <el-radio-group v-model="detailData.isAdvertisement">
          <el-radio :label="1" size="large">是</el-radio>
          <el-radio :label="2" size="large">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="商品类型" prop="goodstypeId">
        <el-select v-model="detailData.goodstypeId" placeholder="商品类型">
          <el-option label="---" value="0"></el-option>
          <el-option v-for="item in typeoptions" :key="item.id" :label="item.typename" :value="item.id" />
        </el-select>
      </el-form-item>
        <el-button @click="updateCancel(detailDataRef)">取消</el-button>
        <el-button type="primary" @click="update(detailDataRef)">修改</el-button>
    </el-form>
  </el-dialog>

  <el-dialog title="商品详情" v-model="detailFormVisible">
    <el-form :model="detailData" class="fl" disabled >
      <el-form-item label="商品名称">
        <el-input v-model="detailData.gname"/>
      </el-form-item>
      <el-form-item label="商品原价">
        <el-input v-model="detailData.goprice"/>
      </el-form-item>
      <el-form-item label="商品折扣价">
        <el-input v-model="detailData.grprice"/>
      </el-form-item>
      <el-form-item label="商品库存">
        <el-input v-model="detailData.gstore"/>
      </el-form-item>
      <el-form-item label="商品图片">
        <el-image :src="imgurl" style="width: 200px; height: 200px; object-fit: contain; background: #f5f5f5;"/>
      </el-form-item>
      <el-form-item label="是否广告">
        <el-radio-group v-model="detailData.isAdvertisement">
          <el-radio :label="1" size="large">是</el-radio>
          <el-radio :label="2" size="large">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="商品类型">
        <el-select v-model="detailData.goodstypeId">
          <el-option v-for="item in typeoptions" :key="item.id" :label="item.typename" :value="item.id" />
        </el-select>
      </el-form-item>
    </el-form>
  </el-dialog>

</template>
<script setup>
import { ElMessage } from 'element-plus'
import myAxios from '../../axios/request.js'
import { onMounted, reactive, ref } from 'vue'
let result = ref([])
let addFormVisible = ref(false)
let updateFormVisible = ref(false)
let detailFormVisible = ref(false)
let searchParam = reactive({})
let myFileList = ref([])
let addFormRef = reactive({})
let addForm= reactive({
        isrec: '2',
        isadv: '2'
    })
//验证规则
const rules = reactive({
        gname: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
        goprice: [{ required: true, message: '请输入商品原价', trigger: 'blur' }],
        grprice: [{ required: true, message: '请输入商品折扣价', trigger: 'blur' }],
        gstore: [{ required: true, message: '请输入商品库存', trigger: 'blur' }],
        typeid: [{ required: true, message: '请选择商品类型', trigger: 'change' }]
      })
let detailDataRef = reactive({})
//reactive只能是对象或数组，但修改时不能直接赋值
let detailData = ref({})
let imgurl = ref()
let total = ref(0)
let currentPage = ref(1)
let pageSize = ref(5)
let typeoptions = ref([])
let itemKey = ref(0)
//组件初始化
onMounted(() => {
  loadGoodsType()
  loadGoods()
})
//加载商品类型
const loadGoodsType = ()=>{
  //headers放在第三个参数
  myAxios.post('/api/admin/type/getAllGoodsType',{},
  {
    headers: {
      'Authorization': sessionStorage.getItem('authtoken')
    }
  })
  .then(res => {
      typeoptions.value =  res.data.result;
  })
  .catch((error) => {
      ElMessage.error(error)
  })
}
//加载商品信息
const loadGoods = () => {
  myAxios.post('/api/admin/goods/getGoods',
  {
    goodstypeId: searchParam.goodstypeId,
    gname: searchParam.gname,
    currentPage: currentPage.value,
    pageSize: pageSize.value
  },
  {
    headers: {
      'Authorization': sessionStorage.getItem('authtoken')
    }
  })
  .then(res => {
      result.value =  res.data.result.allGoods;
      itemKey.value = Math.random() //刷新表格数据
      total.value = res.data.result.totalPage
  })
  .catch((error) => {
      ElMessage.error(error)
  })
}
 //打开新增窗口
 const openadd = () => {
    //使用ref在setup读取的时候需要获取xxx.value，但在template中不需要
    addFormVisible.value = true
    addForm.act = 'add'
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
//选择图片时，保证只能选择一张
const changeFile = (file, fileList) =>{//file对象必须有，不然fileList找不到
    if(fileList.length > 1){
      fileList.splice(0,1);
    }
    myFileList = fileList;
}
//新增取消与重置表单
const resetForm = (formEl) => {
  if (!formEl) return
  formEl.resetFields()
  addFormVisible.value = false
}
//新增
const add = async (formEl) => {
  if (!formEl) return
  await formEl.validate((valid) => {
    if (valid) {
      myAxios.post('/api/admin/goods/add', addForm,
        {
          headers: {
            'Authorization': sessionStorage.getItem('authtoken')
          }
        }
      )
      .then(res => {
          if (res.data.msgId  === "A001") {
              //重置表单
              resetForm(formEl)
              ElMessage.success({message: '商品添加成功',type: 'success'})
              loadGoods()
          } else{
            ElMessage.error(res.data.msgId + ' ：添加失败')
          }
      })
      .catch((error) => {
          ElMessage.error(error)
      })
    } else {
      ElMessage.error('表单验证失败')
    }
  })
}
//编辑与详情
const handleEdit = (row, act) => {
  detailData.value = row
  imgurl.value = '/' + detailData.value.gpicture 
  if(act === 'update'){
    detailData.value.act = 'update'
    updateFormVisible.value = true
  } else{
    detailFormVisible.value  = true
  }
}
//修改取消与重置表单
const updateCancel  = (formEl) => {
  if (!formEl) return
  formEl.resetFields()
  updateFormVisible.value = false
}
const update =  async (formEl) => {
  if (!formEl) return
  await formEl.validate((valid) => {
    if (valid) {
      myAxios.post('/api/admin/goods/add', detailData.value,
          {
						headers: {
							'Authorization': sessionStorage.getItem('authtoken')
						}
					}
        )
        .then(res => {
            if (res.data.msgId  === "A001") {
                updateCancel(formEl)
                ElMessage.success({message: '商品修改成功',type: 'success'})
                loadGoods()
            } else{
              ElMessage.error(res.data.msgId + ' ：修改失败')
            }
        })
        .catch((error) => {
            ElMessage.error(error)
        })
      } else {
        ElMessage.error('表单验证失败')
      }
    })
}
const  confirmEvent = (row) => {
  myAxios.post('/api/admin/goods/delete', {id: row.id},
    {
      headers: {
        'Authorization': sessionStorage.getItem('authtoken')
      }
    })
    .then(res => {
      if (res.data.msgId === "A001") {
        ElMessage.error('商品有关联不能删除！')
      } else if (res.data.msgId === "A002") {
        ElMessage.success({message: '商品删除成功',type: 'success'})
        //删除成功后重新加载
        loadGoods()
      } else {
        ElMessage.error(res.data.msgId + ' ：删除失败')
      }
    })
    .catch((error) => {
        ElMessage.error(error)
    })
}
const cancelEvent = () => {
}
</script>