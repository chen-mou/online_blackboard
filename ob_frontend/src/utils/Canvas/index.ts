import { BaseShape, Rectangle } from "./shape";

const ShapeMap =new Map<string,Rectangle>()
const rectangle =new Rectangle()
ShapeMap.set(rectangle.type,rectangle)

export default ShapeMap