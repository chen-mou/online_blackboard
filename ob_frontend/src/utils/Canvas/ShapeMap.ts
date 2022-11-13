import {
  BaseShape,
  Rectangle,
  Line,
  Circle,
  ShapeImg,
  FreeLine,
  Ellipse
} from './shape'

const ShapeMap = new Map<
  string,
  Rectangle | Line | Circle | ShapeImg | FreeLine | Ellipse
>()
const rectangle = new Rectangle()
const shapeLine = new Line()
const shapeCicle = new Circle()
const shapeImg = new ShapeImg()
const freeLine = new FreeLine()
const ellipse = new Ellipse()
ShapeMap.set(rectangle.type, rectangle)
ShapeMap.set(shapeLine.type, shapeLine)
ShapeMap.set(shapeCicle.type, shapeCicle)
ShapeMap.set(shapeImg.type, shapeImg)
ShapeMap.set(freeLine.type, freeLine)
ShapeMap.set(ellipse.type, ellipse)
export default ShapeMap
