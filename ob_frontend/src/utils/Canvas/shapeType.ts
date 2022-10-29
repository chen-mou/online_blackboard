/**
 *
 * 存放需要绘制的图形
 */

/**
 *
 * @param canvas
 * @param positionsBefore
 * @param positionsAfter
 */
export function rectDraw (
  canvas: HTMLCanvasElement,
  positionsBefore: Array<number>,
  positionsAfter: Array<number>
) {
  /**
   *
   */
  const context = canvas.getContext('2d') as CanvasRenderingContext2D
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
