import { defineStore } from 'pinia'
import Canvas from '@/utils/Canvas/canvas'
import { distance, getElPagePos } from '@/utils/Canvas/math'
import { deepCopy } from '@/utils'
import { useWs } from '@/utils/ws'
import { IFrame } from '@stomp/stompjs'
import { ElMessage } from 'element-plus'
import { Pen, ShapeDataType } from '@/utils/Canvas/type/CanvasType'
import ShapeMap from '@/utils/Canvas/ShapeMap'
import { FreeLine } from '@/utils/Canvas/shape'
import { userInfoMessageResolver } from "@/utils/messageResolver";
import { shapeToWSShape, wsShapeToShape } from "@/utils/convert";
import { useRoomStore } from "@/store/room";
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import snappy from 'snappyjs'

const roomStore = useRoomStore();

export const useCanvasStore = defineStore('canvas', {
  state: () => ({
    canvas: {} as Canvas,
    ws: { active: false } as {
      send: (id: string, data: unknown) => void
      close: () => void,
      active: boolean,
    },
    _otherUsers: [] as any[],
    sheetId: 0,
  }),
  actions: {
    connect(roomId: number, isAnonymous: number, onDisconnect: (frame: IFrame) => void) {
      this.ws = useWs(roomId, isAnonymous, [{
        id: 'room',
        channel: `/exchange/room/${roomId}`,
        callback: this._wsRoomReceive
      }, {
        id: 'user',
        channel: '/user/queue/info',
        callback: this._wsUserReceive
      }, {
        id: 'err',
        channel: '/user/queue/error',
        callback: this._wsErrReceive
      }], (frame) => {
        onDisconnect(frame)
        this.ws.active = false
      })
    },
    _wsRoomReceive(frame: IFrame) {
      const d = JSON.parse(frame.body);
      if (d.sheetId !== this.sheetId) {
        return
      }
      this.canvas.layers.data.push(wsShapeToShape(d))
      this.canvas.layers.drawData()
    },
    _wsUserReceive(frame: IFrame) {
      const d = JSON.parse(frame.body)
      if (d.zip) {
        d.data = snappy.uncompress(new TextEncoder().encode(d.data)).toString()
      }
      d.data = JSON.parse(d.data)
      userInfoMessageResolver[d.type](this, d)
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
      let showLine = false
      let prepareDrawing = false
      let canvas = {} as Canvas
      if (this.canvas.canvas) {
        canvas = this.canvas.reload()
        canvas.layers = canvas.layers.reload()
        this.canvas.layers.drawData()
      } else {
        canvas = new Canvas({ canvas: 'canvas' })
        canvas.layers = new Canvas({ canvas: 'canvas2' })
        this.canvas = canvas
        this.canvas.layers.context.globalCompositeOperation = 'destination-over'
        this.canvas.context.globalCompositeOperation = 'destination-over'
      }

      const { x, y } = getElPagePos(
        document.getElementById('canvas') as HTMLElement
      )

      canvas.canvas.addEventListener('mousedown', e => {
        /**
         * 传入相应的坐标
         */
        console.log(canvas.context.strokeStyle)
        beforePosition = [e.pageX - x, e.pageY - y]
        canvas.DrawClass.BeforePosition = beforePosition
        prepareDrawing = true
        showLine = true
        // if (canvas.DrawClass.type === 'freeLine') {
        //     (canvas.DrawClass as FreeLine).data.push({
        //       x:e.pageX - x,
        //       y:e.pageY - y
        //     })
        // }
      })

      canvas.canvas.addEventListener('mousemove', e => {
        AfterPosition = [e.pageX - x, e.pageY - y]
        // 距离超过一定值就开始画
        if (distance(AfterPosition, beforePosition) > 10 && prepareDrawing) {
          IsDrawing = true
          showLine = true
        } else {
          IsDrawing = false
          // 如果距离小于一定值且显示实时线条，则擦除线条
          if (showLine) {
            showLine = false
          }
        }
        if (IsDrawing) {
          /**
           * 清空之后全部重新绘制
           */
          // canvas.drawData()
          if (canvas.DrawClass.type !== 'freeLine') {
            canvas.context.clearRect(0, 0, 1600, 1600)
            canvas.DrawClass.BeforePosition = beforePosition
            canvas.DrawClass.AfterPosition = AfterPosition
          } else {
            /**
             * 存入data
             */
            (canvas.DrawClass as FreeLine).data.push({
              x: e.pageX - x,
              y: e.pageY - y
            })
          }

          canvas.DrawClass.draw(canvas)
        }
      })
      canvas.canvas.addEventListener('mouseup', e => {
        /**
         * 储存标记点type和相应的坐标点
         */
        prepareDrawing = false
        showLine = false
        if (!IsDrawing) {
          return
        }

        if (canvas.DrawClass.type !== 'freeLine') {
          // canvas.layers.data.push({
          //   type: canvas.DrawClass.type,
          //   BeforePosition: beforePosition,
          //   AfterPosition: AfterPosition,
          //   pen: deepCopy(canvas.pen)
          // })
          this.ws.send('room', shapeToWSShape({
            type: canvas.DrawClass.type,
            BeforePosition: beforePosition,
            AfterPosition: AfterPosition,
            pen: deepCopy(canvas.pen)
          }, this.sheetId, roomStore.roomId))
        } else {
          canvas.layers.data.push({
            type: canvas.DrawClass.type,
            BeforePosition: beforePosition,
            AfterPosition: AfterPosition,
            pen: deepCopy(canvas.pen),
            data: (canvas.DrawClass as FreeLine).data
          });

        }
        IsDrawing = false
        /**
         * 鼠标画完之后画入第二层
         * 画入后清空上一层画布
         */
        const { strokeStyle, fillStyle, linewidth } = canvas.pen as Pen
        canvas.layers.context.strokeStyle = strokeStyle
        canvas.layers.context.fillStyle = fillStyle
        canvas.layers.context.lineWidth = linewidth
        if (canvas.DrawClass.type !== 'freeLine') {
          ShapeMap.get(canvas.DrawClass.type)?.draw(canvas.layers)
        } else {

          ShapeMap.get(canvas.DrawClass.type)?.draw(canvas.layers);
          console.log("aaaaaaaaaaaaaa", (canvas.DrawClass as FreeLine).data);
          (canvas.DrawClass as FreeLine).data = []
        }
        canvas.context.clearRect(0, 0, 1600, 1600)
        console.log(canvas)
      })
      /**
       * 监听双击事件可选中和可拖动
       */
      canvas.canvas.addEventListener('dblclick', e => {
        /**
         * 判断点是否在data的图形里面在的话拿出那一个图形并绘制
         */
        canvas.data = canvas.layers.data
        canvas.drawControlBorder(e.pageX - x, e.pageY - y)
        canvas.data = []
      })
    },
  },
  getters: {}
})
