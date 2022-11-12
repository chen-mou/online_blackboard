import { defineStore } from "pinia";
import request from "@/utils/request";

export const useRoomStore = defineStore('room', {
  state: () => ({
    roomId: 0,
    name: '',
    isShare: 0,
    creatorId: 0,
    startTime: '',
    endTime: '',
    allowAnonymous: 0,
    userAnonymous: 0,
    sheetId: 0,
  }),
  actions: {
    joinRoom(roomId: number, userAnonymous: number, creatorId: number) {
      // console.log(roomId,userAnonymous)
      this.roomId = roomId
      this.userAnonymous = userAnonymous
      this.creatorId = creatorId
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
      this.roomId = 0
      if (res.code != 200) {
        return res.msg
      }
      return false
    },
    async updateRoom(data: any) {
      const res = await request({
        data,
        url: '/room/update_setting',
        method: 'post',
      })
      if (res.code != 200) {
        return res.msg
      }
      return false
    },
    async deleteRoom(roomId: number) {
      const res = await request({
        data: { roomId },
        url: '/room/delete',
        method: 'post',
      })
      if (res.code != 200) {
        return res.msg
      }
      return false
    },
    setSheetIdAndIsShare(sheetId: number, isShare: number, creatorId: number) {
      this.sheetId = sheetId
      this.isShare = isShare
      this.creatorId = creatorId
    },
  },
  getters: {}
})