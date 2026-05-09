<template>
  <el-dialog title="管理员登录" v-model="dialogVisible" width="30%">
    <div class="box">
      <el-form ref="loginFormRef" :model="loginForm" :rules="rules" style="width:100%;" label-width="20%">
        <el-form-item label="用户名" prop="aname">
          <el-input v-model="loginForm.aname" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="apwd">
          <el-input show-password v-model="loginForm.apwd" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="login(loginFormRef)">登录</el-button>
          <el-button type="danger" @click="cancel">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </el-dialog>
</template>
<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import myAxios from '../../axios/request.js'
const router = useRouter()
let loginForm = reactive({})
let loginFormRef = reactive({})
//验证规则
const  rules = reactive({
        aname: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        apwd: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      })
let dialogVisible = ref(true)

const  login = async (formEl) => {//formEl为ref值
  if (!formEl) return
  await formEl.validate((valid) => {
    if (valid) {
      myAxios.post('/api/admin/login', loginForm)
      .then(res => {
          if (res.data.msgId  === "A001"){
              ElMessage.error('用户名错误！')
          } else if (res.data.msgId  === "A002"){
              ElMessage.error('密码错误！')
          }else{
              ElMessage.success({message: '登录成功',type: 'success'})
              sessionStorage.setItem("authtoken", res.data.result.authtoken);
							sessionStorage.setItem("aname", res.data.result.aname);
              sessionStorage.setItem("aid", res.data.result.aid);
              router.replace('/home')
          }
      })
      .catch(() => {
          ElMessage.error('访问异常')
      })
    } else {
      ElMessage.error('表单验证失败')
    }
  })
}
const cancel = ()=> {
  loginFormRef.resetFields()
}
</script>
<style scoped>
.box {
  width: 100%;
  height: 150px;
}
</style>