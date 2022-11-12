import { wsShapeToShape } from "@/utils/convert";

export const userInfoMessageResolver: { [k: string]: (store: any, data: any) => void } = {
  'room_info': (store: any, data: any) => {
    store.sheetId = data.sheet == 0 ? data.data.nowSheet : data.sheet
  },
  'voice_url': (store: any, data: any) => {
    return
  },
}

export const roomMessageResolver: { [k: string]: (store: any, data: any) => void } = {
  'add': (store: any, data: any) => {
    store.canvas.layers.data.push(wsShapeToShape(data.data))
  },
  'delete': (store: any, data: any) => {
    // @ts-ignore
    store.canvas.layers.data.splice(store.canvas.layers.data.findIndex(e => e.id === data.shapeId),1)
  },
}