import axios, { AxiosRequestConfig } from 'axios'

export default async function request(config: AxiosRequestConfig) {
  const instance = axios.create({
    baseURL: '/api',
    timeout: 5000,
  })

  instance.interceptors.response.use(
    res => {
      return res.data
    },
    err => {
      //todo 单独处理 token 过期
      return err.response.data
    }
  )

  if (config.data != null) {
    const data = new FormData
    for (const key of Object.keys(config.data)) {
      data.append(key, config.data[key])
    }
    config.data = data
  }

  config.headers = {
    token: `Bearer ${localStorage.getItem('token')}`
  }

  return (await instance(config)) as any
}
