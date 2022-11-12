import { wsShapeToShape } from "@/utils/convert";
import { useRoomStore } from "@/store/room";
import { useCanvasStore } from "@/store/canvas";
import { useRouter } from "vue-router";
import router from "@/router";
import { useUserStore } from "@/store/user";

export const userInfoMessageResolver: { [k: string]: (data: any) => void } = {
  'room_info': (data: any) => {
    // console.log(data)
    useRoomStore().setSheetIdAndIsShare(
      data.sheet === 0 ? data.data.nowSheet : data.sheet,
      data.data.setting.isShare,
      data.data.setting.creatorId
    )
    const canvasStore = useCanvasStore();
    canvasStore.canvas.layers.data = []
    for (const shape of data.data.nowSheetEntity.shapeEntities) {
      canvasStore.canvas.layers.data.push(wsShapeToShape(shape))
    }
    canvasStore.sheets = data.data.sheetEntities
  },
  'voice_url': (data: any) => {
    return
  },
  'change_sheet': (data: any) => {
    const canvasStore = useCanvasStore()
    canvasStore.canvas.layers.data = []
    canvasStore.canvas.canvas.height = canvasStore.canvas.canvas.height
    for (const shape of data.data.shapeEntities) {
      canvasStore.canvas.layers.data.push(wsShapeToShape(shape))
    }
    const roomStore = useRoomStore();
    console.log(data)
    roomStore.setSheetIdAndIsShare(data.data.id, roomStore.isShare, roomStore.creatorId)
  },
  'message': (data) => {
    useCanvasStore().messageList.push(data.data)
  }
}

export const roomMessageResolver: { [k: string]: (data: any) => void } = {
  'add': (data: any) => {
    if (data.sheet !== useRoomStore().sheetId) {
      return
    }
    const canvasStore = useCanvasStore();
    // @ts-ignore
    if (canvasStore.canvas.layers.data.findIndex((i) => i.id === data.data.id) > -1) {
      return
    }
    canvasStore.canvas.layers.data.push(wsShapeToShape(data.data))
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
  },
  '/create_sheet': (data: any) => {
    useCanvasStore().sheets.push({ id: data.data.id, name: data.data.name })
  },
  'change_sheet': (data: any) => {
    const canvasStore = useCanvasStore()
    canvasStore.canvas.layers.data = []
    canvasStore.canvas.canvas.height = canvasStore.canvas.canvas.height
    for (const shape of data.data.shapeEntities) {
      canvasStore.canvas.layers.data.push(wsShapeToShape(shape))
    }
    const roomStore = useRoomStore();
    console.log(data)
    roomStore.setSheetIdAndIsShare(data.data.id, roomStore.isShare, roomStore.creatorId)
  },
  'user_join': (data: any) => {
    useCanvasStore().otherUsers.push(data.data)
  },
  'message': (data) => {
    const canvasStore = useCanvasStore();
    canvasStore.messageList.push(data.data)
    if (canvasStore.messageList.length > 30) {
      canvasStore.messageList.shift()
    }
  }
}