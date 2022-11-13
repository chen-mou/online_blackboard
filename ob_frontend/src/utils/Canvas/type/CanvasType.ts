/**
 * 定义相关数据结构
 */

export type ShapeDataType ={
    id?: number
    type:string
    BeforePosition:Array<number> // mousedown position
    AfterPosition:Array<number>// mouseup position
    pen:Pen|null
    file?:ImageData
    text?:string
    data?:Array<Point>
}

/**
 * Pen
 */
export type Pen={
    icon:string
    linewidth:number
    strokeStyle:string | CanvasGradient | CanvasPattern
    fillStyle:string
    lineStyle?:number
}
export  type penNeed = Omit<Pen, 'icon' >
/**
 * Point
 */

export type Point = {
    x:number
    y:number
}