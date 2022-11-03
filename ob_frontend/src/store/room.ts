import { defineStore } from "pinia";
import request from "@/utils/request";
import { Participate } from "@/store/interface/room";

export const useRoomStore = defineStore('room', {
  state: () => ({
    roomId: 0,
    name: '',
    loaded: 0,
    nowSheet: 0,
    participants: [{
      userId: 123,
      nickname: '123',
      isAnonymous: 0,
    }] as Participate[],
    setting: {
      allowAnonymous: 0,
      endTime: '',
      startTime: '',
      isShare: 0,
    },
    sheets: [],
    status: '',
    timeout: 0,
  }),
  actions: {
    async joinRoom(data: any) {
      const res = await request({
        method: 'post',
        data,
        url: '/room/join'
      })

      return
    },
    async createRoom(data: any) {
      const res = await request({
        method: 'post',
        data,
        url: '/room/create',
      })
      console.log(res)
      return
    },
    //结束
    async overRoom() {
      const res = await request({
        method: 'post',
        url: '/room/over',
      })
      console.log(res)
    }
  },
  getters: {}
})