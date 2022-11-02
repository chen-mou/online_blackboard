import { BaseShape, Rectangle,ShapeLine } from "./shape";

const ShapeMap =new Map<string,Rectangle>()
const rectangle =new Rectangle()
const shapeLine=new ShapeLine()
ShapeMap.set(rectangle.type,rectangle)
ShapeMap.set(shapeLine.type,shapeLine)
export default ShapeMap