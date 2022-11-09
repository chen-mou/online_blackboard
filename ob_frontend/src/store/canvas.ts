import { defineStore } from 'pinia'
import Canvas from '@/utils/Canvas/canvas'
import { distance, getElPagePos } from '@/utils/Canvas/math'
import { deepCopy } from '@/utils'
import { useWs } from '@/utils/ws'
import { IFrame } from '@stomp/stompjs'
import { ElMessage } from 'element-plus'
import { ShapeDataType } from '@/utils/Canvas/type/CanvasType'
import { FreeLine } from '@/utils/Canvas/shape'

export const useCanvasStore = defineStore('canvas', {
  state: () => ({
    canvas: {} as Canvas,
    ws: {} as {
      send: (channel: string, data: unknown) => void
      close: () => void
    },
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
      // let PointData = []
      let beforePosition = [0, 0]
      let AfterPosition = [0, 0]
      let IsDrawing = false
      let showLine = false
      let prepareDrawing = false
      let canvas = {} as Canvas
      if (this.canvas.canvas) {
        canvas = this.canvas.reload()
        this.canvas.drawData()
      } else {
        canvas = new Canvas({ canvas: 'canvas' })
        this.canvas = canvas
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
            // console.log('nonono')
            canvas.drawData()
          }
        }
        if (IsDrawing) {
          if (canvas.DrawClass.type == 'freeLine') {
            /**
             * 传入点并lineto
             */
            (canvas.DrawClass as FreeLine).data.push({
              x: AfterPosition[0],
              y: AfterPosition[1]
            })
          }
          /**
           * 清空之后全部重新绘制
           */
          canvas.drawData()
          canvas.DrawClass.BeforePosition = beforePosition
          canvas.DrawClass.AfterPosition = AfterPosition
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
      canvas.canvas.addEventListener('dblclick', e => {
        /**
         * 判断点是否在data的图形里面在的话拿出那一个图形并绘制
         */
        canvas.drawControlBorder(e.pageX - x, e.pageY - y)
      })
    },
    cacheCanvasData() {
      // console.log('cache')
      this._cacheData = this.canvas.data
    }
  },
  getters: {}
})
