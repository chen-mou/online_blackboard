import { BaseShape, Rectangle,Line,Circle } from "./shape";

const ShapeMap =new Map<string,Rectangle>()
const rectangle =new Rectangle()
const shapeLine=new Line()
const shapeCicle =new Circle()
ShapeMap.set(rectangle.type,rectangle)
ShapeMap.set(shapeLine.type,shapeLine)
ShapeMap.set(shapeCicle.type,shapeCicle)
export default ShapeMap