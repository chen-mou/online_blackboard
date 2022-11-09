import { defineStore } from "pinia";
import request from "@/utils/request";

export const useUserStore = defineStore('user', {
    state: () => ({
      hasLogin: true  ,
      userId: 0,
      nickname: '鲲鲲',
      myRooms: [],
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
        const roomRes = await request({
          method: 'get',
          url: '/room/my_room',
        })
        // console.log(roomRes)
        if (roomRes.code != 200) {
          return
        }
        this.myRooms = roomRes.data
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
      _loginSuccess(data: any, hasToken = true) {
        this.hasLogin = true
        if (hasToken) {
          this.nickname = data.data.user_data.nickname
          this.userId = data.data.user_data.userId
          localStorage.setItem('token', data.data.token)
          return
        }
        this.nickname = data.data.nickname
        this.userId = data.data.userId
      },
      async updateNickName(newNickname: string, onfail: (data: any) => void, onsuccess: () => void) {
        const data = await request({
          method: 'post',
          url: '/user/nickname',
          data: { nickname: newNickname },
        })
        if (data.code != 200) {
          onfail(data)
          return
        }
        this.nickname = newNickname
        onsuccess()
        return
      },
      async getUserData() {
        const data = await request({
          method: 'get',
          url: '/user/info',
        })
        if (data.code != 200) {
          return false
        }
        this._loginSuccess(data, false)
        const roomRes = await request({
          method: 'get',
          url: '/room/my_room',
        })
        // console.log(roomRes)
        if (roomRes.code != 200) {
          return true
        }
        this.myRooms = roomRes.data
        return true
      },
      async logout() {
        await request({
          method: 'post',
          url: '/user/logout',
        })
        this.hasLogin = false
        this.nickname = '鲲鲲'
        this.userId = 0
        localStorage.removeItem('token')
      },
    },
    getters: {},
  }
)