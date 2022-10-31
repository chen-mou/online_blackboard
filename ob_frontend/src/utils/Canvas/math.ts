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

export { getElPagePos }
