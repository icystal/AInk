import axios from "axios";
import {useUserStore} from "@/stores/user.js";
import router from "@/router/index.js";

const request = axios.create({
  baseURL: 'http://127.0.0.1:7717/aink',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

request.interceptors.request.use(
  (config) => {
    const userStore = useUserStore()
    const email = userStore.getEmail()
    const token = userStore.getToken()

    if (email && token) {
      config.headers['INK-EMAIL'] = email
      config.headers['INK-TOKEN'] = token
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

request.interceptors.response.use(
  (response) => {
    return response
  },
  (error) => {
    if (error.response?.status === 401) {
      const userStore = useUserStore()
      userStore.logout()
      router.push('login')
    }
    return Promise.reject(error)
  }
)

export const unwrapResponse = (response) => {
  console.log("unpacking response", response)
  if (response?.data?.code && response.data.code !== "0") {
    console.log("fail response")
    throw new Error(response?.data?.message || '请求异常')
  }
  return response.data.body
}

export default request
