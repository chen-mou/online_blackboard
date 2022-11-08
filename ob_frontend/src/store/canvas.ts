import { defineStore } from "pinia";
import Canvas from "@/utils/Canvas/canvas";
import { getElPagePos } from "@/utils/Canvas/math";
import { deepCopy } from "@/utils";
import { useWs } from "@/utils/ws";
import { IFrame } from "@stomp/stompjs";
import { ElMessage } from "element-plus";
import { ShapeDataType } from "@/utils/Canvas/type/CanvasType";


export const useCanvasStore = defineStore('canvas', {
  state: () => ({
    canvas: {} as Canvas,
    ws: {} as { send: (channel: string, data: unknown) => void, close: () => void },
    _cacheData: [] as Array<ShapeDataType>
  }),
  actions: {
    connect(roomId: string, isAnonymous: number) {
      this.ws = useWs(roomId, isAnonymous, [{
        channel: `/exchange/room/${roomId}`,
        callback: this._wsRoomReceive
      }, {
        channel: '/user/queue/info',
        callback: this._wsUserReceive
      }, {
        channel: '/user/queue/error',
        callback: this._wsErrReceive
      }])
      this.ws.send('/app/room_info', { roomId })
    },
    _wsRoomReceive(frame: IFrame) {
      console.log(frame)
    },
    _wsUserReceive(frame: IFrame) {
      console.log(frame)
    },
    _wsErrReceive(frame: IFrame) {
      ElMessage.error({
        message: frame.body
      })
    },
    initCanvas() {
      let beforePosition = [0, 0]
      let AfterPosition = [0, 0]
      let IsDrawing = false
      let canvas = {} as Canvas
      if (this.canvas.canvas) {
        canvas = this.canvas.reload()
        this.canvas.drawData()
      } else {
        canvas = new Canvas({ canvas: 'canvas' })
        this.canvas = canvas
      }

      const { x, y } = getElPagePos(document.getElementById('canvas') as HTMLElement)

      canvas.canvas.addEventListener('mousedown', e => {
        /**
         * 传入相应的坐标
         */
        console.log(canvas.context.strokeStyle)
        beforePosition = [e.pageX - x, e.pageY - y]
        canvas.DrawClass.BeforePosition = beforePosition
        IsDrawing = true
      })
      canvas.canvas.addEventListener('mousemove', e => {
        if (IsDrawing) {
          /**
           * 清空之后全部重新绘制
           */
          canvas.drawData()
          AfterPosition = [e.pageX - x, e.pageY - y]
          canvas.DrawClass.BeforePosition = beforePosition
          canvas.DrawClass.AfterPosition = AfterPosition
          canvas.DrawClass.draw(canvas)
        }
      })
      canvas.canvas.addEventListener('mouseup', e => {
        // AfterPosition = [e.pageX - x, e.pageY - y]
        // canvas.DrawClass.AfterPosition = AfterPosition
        // canvas.DrawClass.draw(canvas)
        /**
         * 储存标记点type和相应的坐标点
         */
        canvas.data.push({
          type: canvas.DrawClass.type,
          BeforePosition: beforePosition,
          AfterPosition: AfterPosition,
          pen: deepCopy(canvas.pen)
        })
        IsDrawing = false
      })
      /**
       * 监听双击事件可选中和可拖动
       */
      canvas.canvas.addEventListener("dblclick", (e) => {
        /**
         * 判断点是否在data的图形里面在的话拿出那一个图形并绘制
         */
        canvas.drawControlBorder(e.pageX - x, e.pageY - y)
      })
    },
    cacheCanvasData() {
      console.log('cache')
      this._cacheData = this.canvas.data
    }
  },
  getters: {},
})