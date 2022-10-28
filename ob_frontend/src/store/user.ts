import { defineStore } from "pinia";
import request from "@/utils/request";

export const useUserStore = defineStore('user', {
    state: () => ({
      hasLogin: false,
      userId: 0,
      nickname: '鲲鲲',
    }),
    actions: {
      async login(username: string, password: string, onfail: (...args: any[]) => void): Promise<void> {
        const data = await request({
          method: 'post',
          url: '/user/login',
          data: { username, password }
        })
        console.log(data)
        if (data.code != 200) {
          onfail(data)
          return
        }
        this._loginSuccess(data)
      },
      async register(username: string, password: string, onfail: (...args: any[]) => void): Promise<void> {
        const data = await request({
          method: 'post',
          url: '/user/register',
          data: { username, password }
        })
        if (data.code != 200) {
          onfail(data)
          return
        }
        this._loginSuccess(data)
      },
      _loginSuccess(data: any) {
        this.hasLogin = true
        this.nickname = data.data.user_data.nickname
        this.userId = data.data.user_data.userId
        localStorage.setItem('token', data.data.token)
      },
      async updateNickName(newNickname: string, onfail: (data: any) => void) {

        return
      },
      async logout() {
        return
      },

    },
    getters: {},
  }
)