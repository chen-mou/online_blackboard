import { wsShapeToShape } from "@/utils/convert";
import { useRoomStore } from "@/store/room";
import { useCanvasStore } from "@/store/canvas";
import { useRouter } from "vue-router";
import router from "@/router";
import { useUserStore } from "@/store/user";

export const userInfoMessageResolver: { [k: string]: (data: any) => void } = {
  'room_info': (data: any) => {
    // console.log(data)
    useRoomStore().setSheetIdAndIsShare(data.sheet === 0 ? data.data.nowSheet : data.sheet, data.data.setting.isShare)
  },
  'voice_url': (data: any) => {
    return
  },
}

export const roomMessageResolver: { [k: string]: (data: any) => void } = {
  'add': (data: any) => {
    if (data.sheet !== useRoomStore().sheetId) {
      return
    }
    console.log(data.data)
    useCanvasStore().canvas.layers.data.push(wsShapeToShape(data.data))
  },
  'delete': (data: any) => {
    if (data.sheet !== useRoomStore().sheetId) {
      return
    }
    // @ts-ignore
    useCanvasStore().canvas.layers.data.splice(useCanvasStore().canvas.layers.data.findIndex(e => e.id === data.data), 1)
  },
  'over': (data: any) => {
    useCanvasStore().ws.close()
    useUserStore().getUserRooms()
    router.replace('/')
  }
}