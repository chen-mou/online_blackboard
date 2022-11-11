import Canvas from './canvas'
import { getRadiusByTwoPoints, getCenterByTwoPoints } from './math'
import { Point } from './type/CanvasType'
/**
 * ShapeClass
 */
class BaseShape {
  icon: string | undefined
  type!: string
  positionX: number | undefined
  positionY: number | undefined
  width: number | undefined
  height: number | undefined
  BeforePosition: Array<number> // mousedown position
  AfterPosition: Array<number> // mouseup position
  constructor (
    BeforePosition: Array<number> = [0, 0],
    AfterPosition: Array<number> = [0, 0]
  ) {
    (this.BeforePosition = BeforePosition),
      (this.AfterPosition = AfterPosition)
  }
}
/**
 * 矩形类
 */
class Rectangle extends BaseShape {
  constructor (
    BeforePosition: Array<number> = [0, 0],
    AfterPosition: Array<number> = [0, 0]
  ) {
    super(BeforePosition, AfterPosition)
    this.type = 'Rect'
    this.icon = '/canvsShapeImg/rect.png'
  }
  draw (canvas: Canvas) {
    if (this.BeforePosition && this.AfterPosition) {
      canvas.context.beginPath()
      canvas.context.rect(
        this.BeforePosition[0],
        this.BeforePosition[1],
        this.AfterPosition[0] - this.BeforePosition[0],
        this.AfterPosition[1] - this.BeforePosition[1]
      )

      canvas.context.stroke()
    } else {
      throw Error(`this.BeforePosition:${this.BeforePosition}`)
    }
  }
}

/**
 * 自由线条
 */
class Line extends BaseShape {

  constructor (
    BeforePosition: Array<number> = [0, 0],
    AfterPosition: Array<number> = [0, 0]
  ) {
    super(BeforePosition, AfterPosition)
    this.type = 'line'
    this.icon = '/canvsShapeImg/rect.png'
  }

  draw (canvas: Canvas) {
    canvas.context.beginPath()
    canvas.context.moveTo(this.BeforePosition[0], this.BeforePosition[1])
    canvas.context.lineTo(this.AfterPosition[0], this.AfterPosition[1])
    canvas.context.stroke()
  }
}
class Circle extends BaseShape {
  constructor (
    BeforePosition: Array<number> = [0, 0],
    AfterPosition: Array<number> = [0, 0]
  ) {
    super(BeforePosition, AfterPosition)
    this.type = 'circle'
    this.icon = '/canvsShapeImg/rect.png'
  }

  draw (canvas: Canvas) {
    canvas.context.beginPath()
    const { x, y } = getCenterByTwoPoints(
      this.BeforePosition,
      this.AfterPosition
    )
    canvas.context.arc(
      x,
      y,
      getRadiusByTwoPoints(this.BeforePosition, this.AfterPosition),
      0,
      2 * Math.PI
    ) //arc 的意思是“弧”
    canvas.context.stroke()
  }
}

/**
 * 导入图片
 */
class ShapeImg extends BaseShape {
  constructor (
    BeforePosition: Array<number> = [0, 0],
    AfterPosition: Array<number> = [0, 0]
  ) {
    super(BeforePosition, AfterPosition)
    this.type = 'img'
  }
  draw (canvas: Canvas, file?: any) {
    if (window.FileReader) {
      //检测浏览器是否支持
      const reader = new FileReader() //构造FileReader对象
      reader.addEventListener(
        'load',
        () => {
          const buffer = reader.result as ArrayBuffer
          const blob = new Blob([buffer], { type: 'image/jpg' })
          const imgObj = new Image()
          imgObj.src = URL.createObjectURL(blob)
          imgObj.onload = async () => {
            const w = imgObj.width,
              h = imgObj.height
            canvas.context.drawImage(
              imgObj,
              100,
              100,
              (w + 100) / 3,
              (h + 100) / 3
            )
            debugger
          }
        },
        false
      )
      reader.readAsArrayBuffer(file)
    } else {
      throw Error('浏览器不支持图片预览')
    }
  }
}

class FreeLine extends BaseShape {
  data: Array<Point> = []
  constructor (
    BeforePosition: Array<number> = [0, 0],
    AfterPosition: Array<number> = [0, 0]
  ) {
    super(BeforePosition, AfterPosition)
    this.type = 'freeLine'
    this.icon = '/canvsShapeImg/rect.png'
  }
  draw (canvas: Canvas ) {
      /**
       * 从data中读取数据
       */
      for(let i=1;i<this.data.length;i++)
      {
        canvas.context.beginPath()
        canvas.context.moveTo(this.data[i-1].x, this.data[i-1].y)
        canvas.context.lineTo(this.data[i].x, this.data[i].y)
        canvas.context.stroke()
        canvas.context.closePath()
      }
  }
}
class Ellipse extends BaseShape {
  constructor (
    BeforePosition: Array<number> = [0, 0],
    AfterPosition: Array<number> = [0, 0]
  ) {
    super(BeforePosition, AfterPosition)
    this.type = 'ellipse'
    this.icon = '/canvsShapeImg/rect.png'
  }
  draw (canvas: Canvas) {
    canvas.context.beginPath()
    canvas.context.ellipse(
      this.BeforePosition[0],
      this.BeforePosition[1],
      Math.abs(   this.AfterPosition[1]-this.BeforePosition[1]),
      Math.abs(this.AfterPosition[0]-this.BeforePosition[0]),

      (90 * Math.PI) / 180,
      0,
      2 * Math.PI
    )
    canvas.context.stroke()
  }
}
export { BaseShape, Rectangle, Line, Circle, ShapeImg, FreeLine, Ellipse }
