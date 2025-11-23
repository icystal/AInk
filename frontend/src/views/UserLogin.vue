<template>
  <div class="login-container">
    <div class="login-card">
      <h1 class="login-title">欢迎登录</h1>
      <p class="login-subtitle">请输入您的邮箱和验证码</p>

      <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" class="login-form" @submit.prevent="handleLogin">
        <el-form-item prop="email">
          <el-input v-model="loginForm.email" placeholder="请输入邮箱" size="large" clearable :validate-event="false">
            <template #prefix>
              <el-icon class="input-icon"><Message /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="code">
          <div class="code-input-group">
            <el-input v-model="loginForm.code" placeholder="请输入验证码" size="large" clearable :validate-event="false" class="code-input">
              <template #prefix>
                <el-icon class="input-icon"><Key /></el-icon>
              </template>
            </el-input>
            <el-button :disabled="isCounting || !isEmailValid"
                       :loading="sendingCode"
                       size="large" class="send-code-btn" @click="handleSendCode">
              {{ codeButtonText }}
            </el-button>
          </div>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" size="large" class="login-btn"
                     :loading="loggingIn" :disabled="!canLogin" native-type="submit">
            登录
          </el-button>
        </el-form-item>
      </el-form>

      <!--    错误提示    -->
      <el-alert v-if="errorMessage" :title="errorMessage" type="error" show-icon closable class="error-alert" @close="errorMessage = ''"/>
    </div>
  </div>
</template>

<script setup>

import {useRouter} from "vue-router";
import {useUserStore} from "@/stores/user.js";
import {computed, onUnmounted, reactive, ref} from "vue";
import {loginWithCode, sendVerificationCode} from "@/api/auth.js";
import {ElMessage} from "element-plus";
import {Key, Message} from "@element-plus/icons-vue";

const router = useRouter();
const userStore = useUserStore();

// 表单引用
const loginFormRef = ref();
// 表单数据
const loginForm = reactive({
  email: '',
  code: ''
})

// 表单状态
const isCounting = ref(false)   // 是否在倒计时
const countDown = ref(60)   // 倒计时
const sendingCode = ref(false)    // 已发送验证码
const loggingIn = ref(false)
const errorMessage = ref('')

let countDownTimer = null   // 定时器

// 邮箱规则
const emailRegExp = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
const isEmailValid = computed(() => {
  return emailRegExp.test(loginForm.email)
})

const canLogin = computed(() => {
  return isEmailValid && loginForm.code.length >= 4
})

const codeButtonText = computed(() => {
  if (isCounting.value) {
    return `${countDown.value}秒后重试`
  }
  if (!isEmailValid.value) {
    return '请输入邮箱'
  }
  if (sendingCode.value) {
    return '发送中...'
  }
  return '发送验证码'
})

const loginRules = {
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { pattern: emailRegExp, message: '请输入有效的邮箱地址', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { min: 4, max: 8, message: '请输入有效的验证码', trigger: 'blur' }
  ]
}

// 发送验证码
const handleSendCode = async () => {
  if (!isEmailValid.value || isCounting.value) return

  try {
    sendingCode.value = true
    await sendVerificationCode(loginForm.email)
    ElMessage.success('验证码发送成功, 请查收邮件')
    startCountDown()
  } catch (error) {
    errorMessage.value = error.message || '发送失败, 请稍后再试'
  } finally {
    sendingCode.value = false
  }
}

// 发送验证码后开始计时
const startCountDown = () => {
  isCounting.value = true
  countDown.value = 60

  countDownTimer = setInterval(() => {
    countDown.value--
    if (countDown.value <= 0) {
      stopCountDown()
    }
  }, 1000)
}

const stopCountDown = () => {
  if (countDownTimer) {
    clearInterval(countDownTimer)
    countDownTimer = null
  }
  isCounting.value = false
  countDown.value = 60
}

// 登录处理
const handleLogin = async () => {
  if (!canLogin.value) return

  try {
    loggingIn.value = true
    errorMessage.value = ''

    // 表单验证
    await loginFormRef.value.validate()

    const { email, token } = await loginWithCode(loginForm.email, loginForm.code)

    // 存储用户信息
    userStore.login(email, token)
    ElMessage.success('登录成功')

    // 登录成功后自动跳转页面
    setTimeout(() => {
      router.replace('/')
    }, 1000)

  } catch (error) {
    if (error.name === 'ValidationError') {
      // 表单验证错误
      return
    }
    errorMessage.value = error.message || '登录失败'
    loginForm.code = ''
  } finally {
    loggingIn.value = false
  }
}

// 清理定时器
onUnmounted(() => {
  stopCountDown()
})
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e7f1 100%);
  padding: 20px;
}

.login-card {
  width: 100%;
  max-width: 400px;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 40px;
  box-shadow: 0 8px 32px rgba(31, 38, 135, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.5);
}

.login-title {
  font-size: 28px;
  font-weight: 500;
  color: #2c3e50;
  margin: 0 0 8px 0;
  text-align: center;
}

.login-subtitle {
  font-size: 14px;
  color: #7f8c8d;
  margin: 0 0 32px 0;
  text-align: center;
}

.login-form {
  width: 100%;
}

.input-icon {
  color: #95a5a6;
  margin-right: 8px;
}

.code-input-group {
  display: flex;
  gap: 12px;
  align-items: center;
}

.code-input {
  flex: 1;
}

.send-code-btn {
  min-width: 120px;
  height: 40px;
  font-size: 14px;
  background: rgba(52, 152, 219, 0.1);
  color: #3498db;
  border: 1px solid rgba(52, 152, 219, 0.3);
  transition: all 0.3s ease;
}

.send-code-btn:hover:not(:disabled) {
  background: rgba(52, 152, 219, 0.2);
  border-color: rgba(52, 152, 219, 0.5);
}

.send-code-btn:disabled {
  background: rgba(149, 165, 166, 0.1);
  color: #95a5a6;
  border-color: rgba(149, 165, 166, 0.2);
}

.login-btn {
  width: 100%;
  height: 42px;
  font-size: 16px;
  background: linear-gradient(135deg, #3498db 0%, #2ecc71 100%);
  border: none;
  color: white;
  border-radius: 8px;
  transition: all 0.3s ease;
  letter-spacing: 2px;
}

.login-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(52, 152, 219, 0.3);
}

.login-btn:disabled {
  background: rgba(149, 165, 166, 0.3);
  color: rgba(255, 255, 255, 0.7);
}

.error-alert {
  margin-top: 20px;
  border-radius: 8px;
}

:deep(.el-form-item) {
  margin-bottom: 24px;
}

:deep(.el-input__wrapper) {
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.7);
  box-shadow: none;
  border: 1px solid rgba(189, 195, 199, 0.3);
  transition: all 0.3s ease;
}

:deep(.el-input__wrapper:hover) {
  border-color: rgba(52, 152, 219, 0.5);
}

:deep(.el-input__wrapper:focus) {
  border-color: #3498db;
  box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.1);
}

@media (max-width: 480px) {
  .login-card {
    padding: 30px 20px;
  }

  .login-title {
    font-size: 24px;
  }

  .code-input-group {
    flex-direction: column;
  }

  .send-code-btn {
    width: 100%;
  }
}

</style>
