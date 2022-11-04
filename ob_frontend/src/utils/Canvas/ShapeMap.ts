import { BaseShape, Rectangle,Line,Circle,ShapeImg } from "./shape";

const ShapeMap =new Map<string,Rectangle|Line|Circle|ShapeImg>()
const rectangle =new Rectangle()
const shapeLine=new Line()
const shapeCicle =new Circle()
const shapeImg =new ShapeImg()
ShapeMap.set(rectangle.type,rectangle)
ShapeMap.set(shapeLine.type,shapeLine)
ShapeMap.set(shapeCicle.type,shapeCicle)
ShapeMap.set(shapeImg.type,shapeImg)
export default ShapeMap
