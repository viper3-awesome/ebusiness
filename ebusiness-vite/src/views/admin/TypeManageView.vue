<template>
  <el-tabs type="border-card">
    <el-tab-pane label="类型管理">
      <div class="fl" style="margin-top: -10px;margin-bottom: 10px;">
        <el-button size="medium" type="success" @click="openadd()">增加</el-button>
      </div>
      <el-table :data="result" border :key="itemKey">
        <el-table-column type="index" label="序号" width="150"></el-table-column>
        <el-table-column prop="id" label="类型ID" width="150"></el-table-column>
        <el-table-column prop="typename" label="类型名称" width="150"></el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-row>
              <el-button size="small" type="primary"  @click="handleEdit(scope.row)">编辑
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
           :page-sizes="[5, 10, 20]" :total="total" />
      </div>
    </el-tab-pane>
  </el-tabs>
  <el-dialog v-model="addFormVisible" title="新增类型">
    <el-form :model="addForm" ref="addFormRef" :rules="rules">
      <el-form-item label="类型名称" prop="typename">
        <el-input v-model="addForm.typename" placeholder="请输入类型名称" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="addCancel()">取消</el-button>
        <el-button type="primary" @click="add(addFormRef)">新增</el-button>
      </span>
    </template>
  </el-dialog>
  <el-dialog title="类型修改" v-model="updateFormVisible">
    <el-form ref="detailDataRef" :model="detailData" :rules="rules">
      <el-form-item label="类型ID" prop="id">
        <el-input v-model="detailData.id" disabled></el-input>
      </el-form-item>
      <el-form-item label="类型名称" prop="typename">
        <el-input v-model="detailData.typename"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="updateFormVisible = false">取消</el-button>
        <el-button type="primary" @click="update(detailDataRef)">修改</el-button>
      </span>
    </template>
  </el-dialog>
</template>
<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import myAxios from '../../axios/request.js'
//ref创建一个具有响应式的数组，使用.value读数据，但在template中不需要（自动拆箱）
let result = ref([])
//let result = reactive([])
//result.push(...newArr)
//let result = reactive({list:[]})
//result.list = newArr
//ref创建一个具有响应式的基本数据类型的数据
let addFormVisible = ref(false)
let updateFormVisible = ref(false)
let addFormRef = reactive({})
let addForm = reactive({})
//验证规则
const rules = reactive({
  typename: [
    { required: true, message: '请输入类型名称', trigger: 'blur' },
    { min: 2, max: 5, message: '类型名称长度为2到5', trigger: 'blur' }
  ]
})
let detailDataRef = reactive({})
let detailData = reactive({})
let total = ref(0)
let currentPage = ref(1)
let pageSize = ref(5)
let itemKey = ref(0)
onMounted(() => {
  loadGoodsType()
})
//加载类型查询信息
const loadGoodsType = () => {
  myAxios.post('/api/admin/type/getGoodsType',
  {
    currentPage: currentPage.value,
    pageSize: pageSize.value
  },
  {
    headers: {
      'Authorization': sessionStorage.getItem('authtoken')
    }
  })
  .then(res => {
      result.value =  res.data.result.allGoodsTypes;
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
    loadGoodsType()
}
const handleSizeChange = (val) => {
    pageSize.value = val
    currentPage.value = 1
    loadGoodsType()
}
//打开新增窗口
const openadd = () => {
  //使用ref在setup读取的时候需要获取xxx.value，但在template中不需要
  addFormVisible.value = true
  addForm.id = ''
  addForm.typename = ''
}
//新增
const add = async (formEl) => {
  if (!formEl) return
  await formEl.validate((valid) => {
    if (valid) {
      myAxios.post('/api/admin/type/add', addForm,
          {
						headers: {
							'Authorization': sessionStorage.getItem('authtoken')
						}
					}
        )
        .then(res => {
            if (res.data.msgId  === "A001") {
                addFormVisible.value = false
                ElMessage.success({message: '类型添加成功',type: 'success'})
                loadGoodsType()
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
//新增对话框取消
const addCancel = () => {
    addFormVisible.value = false
}
//修改按钮
const update =  async (formEl) => {
  if (!formEl) return
  await formEl.validate((valid) => {
    if (valid) {
      myAxios.post('/api/admin/type/update', detailData,
          {
						headers: {
							'Authorization': sessionStorage.getItem('authtoken')
						}
					}
        )
        .then(res => {
            if (res.data.msgId  === "A001") {
                updateFormVisible.value = false
                ElMessage.success({message: '类型修改成功',type: 'success'})
                loadGoodsType()
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
//编辑按钮
const handleEdit = (row) => {
  detailData.id = row.id
  detailData.typename =  row.typename
  updateFormVisible.value = true
}
//删除
const confirmEvent = (row) => {
  myAxios.post('/api/admin/type/delete', {id: row.id},
      {
        headers: {
          'Authorization': sessionStorage.getItem('authtoken')
        }
      })
      .then(res => {
        if (res.data.msgId === "A001") {
          ElMessage.error('类型有关联不能删除！')
        } else if (res.data.msgId === "A002") {
          ElMessage.success({message: '类型删除成功',type: 'success'})
          //删除成功后重新加载
          loadGoodsType()
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
