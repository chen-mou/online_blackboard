import { defineStore } from "pinia";
import request from "@/utils/request";

export const useUserStore = defineStore('user', {
    state: () => ({
      hasLogin: false,
      nickname: '鲲鲲',
    }),
    actions: {
      async login(username: string, password: string, onfail: (...args: any[]) => void): Promise<unknown> {
        const userData = await request({
          url: '/user/login',
          data: { username, password }
        })
        if (userData.code != 200) {
          onfail(userData)
          return null
        }
        this.hasLogin = true
        this.nickname = userData.data.data.nickname
        //todo jwt 解析 token
        localStorage.setItem('token', '')
        return userData.data.id
      },
      async register(username: string, password: string, onfail: (...args: any[]) => void): Promise<unknown> {
        const userData = await request({
          url: '/user/register',
          data: { username, password }
        })
        //todo
        return
      },
      async logout() {
        return
      }
    },
    getters: {},
  }
)