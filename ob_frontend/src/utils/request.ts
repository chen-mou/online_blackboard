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
      return err.response.data
    }
  )

  config.data = JSON.stringify(config.data)

  config.headers = {
    token: localStorage.getItem('token'),
    'Content-Type': 'application/json;charset=utf8',
  }

  return (await instance(config)) as any
}
