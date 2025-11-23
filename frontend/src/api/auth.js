import request, {unwrapResponse} from "@/util/request.js";

export const sendVerificationCode = async (email) => {
  try {
    const response = await request.get(`/welcome/verify/${email}`)
    return unwrapResponse(response)
  } catch (error) {
    throw new Error(error.response?.data?.message || '发送验证码失败')
  }
}

export const loginWithCode = async (email, code) => {
  try {
    const response = await request.post('/welcome/login', {
      'email': email,
      'verificationCode': code
    })
    return unwrapResponse(response)
  } catch (error) {
    throw new Error(error.response?.data?.message || '登录失败')
  }
}

