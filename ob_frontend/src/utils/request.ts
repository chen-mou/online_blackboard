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
      console.error(err)
      return err
    }
  )

  if (config.data != null) {
    const data = new FormData
    for (const key of Object.keys(config.data)) {
      data.append(key, config.data[key])
    }
    config.data = data
  }

  return (await instance(config)) as any
}
