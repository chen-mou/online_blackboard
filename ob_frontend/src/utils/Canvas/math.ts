/**
 * 计算画板相对于页面的绝对坐标
 * @param element
 * @returns
 */
const getElPagePos = (element: HTMLElement) => {
  //计算x坐标
  let actualLeft = element.offsetLeft
  let actualTop = element.offsetTop
  let current = element.offsetParent as HTMLElement
  while (current !== null) {
    actualLeft += current.offsetLeft
    actualTop += current.offsetTop + current.clientTop
    current = current.offsetParent as HTMLElement
  }
  //返回结果
  return { x: actualLeft, y: actualTop }
}

/**
 * 计算圆的半径
 */
const getRadiusByTwoPoints = (
  APosition: Array<number>,
  BPisition: Array<number>
) => {
  return Math.sqrt(
    Math.pow(APosition[0] - BPisition[0], 2) +
      Math.pow(APosition[1] - BPisition[1], 2)
  )/2
}
/**
 * 计算圆心坐标 
 */
const getCenterByTwoPoints = (
  BPosition: Array<number>,
  APosition: Array<number>
) => {
  return {
    x:(APosition[0] + BPosition[0])/2 ,
    y:(APosition[1] + BPosition[1]) /2
  }
}


export { getElPagePos, getCenterByTwoPoints, getRadiusByTwoPoints }
