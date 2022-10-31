/**
 *
 * 存放需要绘制的图形
 */
import Canvas  from "./canvas"
/**
 *
 * @param canvas
 * @param positionsBefore
 * @param positionsAfter
 */
export function rectDraw (
  canvas: Canvas,
  positionsBefore: Array<number>,
  positionsAfter: Array<number>
) {
  /**
   *
   */
  const context = canvas.context as CanvasRenderingContext2D
  context.strokeStyle = '#00'
  context.fillStyle = '#9f9'
  context.beginPath()
  context.rect(
    positionsBefore[0],
    positionsBefore[1],
    positionsAfter[0],
    positionsAfter[1]
  )
  context.stroke()
}
// 矩形
// class Rectangle {
//   type: string
//   x: any
//   y: any
//   width: any
//   fillStyle: any
//   height: any
//   zIndex: number
//   constructor (x, y, width, height, fillStyle, zIndex = 0) {
//       this.type = 'rectangle'
//       this.x = x
//       this.y = y
//       this.width = width
//       this.height = height
//       this.fillStyle = fillStyle
//       this.zIndex  = zIndex 
//   }
// }