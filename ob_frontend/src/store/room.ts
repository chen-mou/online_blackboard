import { defineStore } from "pinia";
import request from "@/utils/request";

export const useRoomStore = defineStore('room', {
  state: () => ({
    roomId: '1000288',
    name: '',
    isShare: 0,
    creatorId: 0,
    startTime: '',
    endTime: '',
    allowAnonymous: 0,
  }),
  actions: {
    async joinRoom(data: any) {
      const res = await request({
        method: 'post',
        data,
        url: '/room/join'
      })
      if (res.code != 200) {
        return res.msg
      }
      this._loadRoom(res.data)
      return false
    },
    async createRoom(data: any) {
      const res = await request({
        method: 'post',
        data,
        url: '/room/create',
      })
      if (res.code != 200) {
        return res.msg
      }
      this._loadRoom(res.data)
      return false
    },
    _loadRoom(res: any) {
      console.log(res)
      this.name = res.name
      this.roomId = res.id
      this.isShare = res.setting.isShare
      this.endTime = res.setting.endTime
      this.creatorId = res.creatorId
      this.startTime = res.setting.startTime
      this.allowAnonymous = res.setting.allowAnonymous
    },
    //结束
    async overRoom() {
      const res = await request({
        method: 'post',
        url: '/room/over',
      })
      this.roomId = ''
      if (res.code != 200) {
        return res.msg
      }
    },
  },
  getters: {}
})