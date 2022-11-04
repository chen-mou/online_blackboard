/**
 * 定义相关数据结构
 */

export type ShapeDataType ={
    type:string
    BeforePosition:Array<number> // mousedown position
    AfterPosition:Array<number>// mouseup position
    pen:Pen|null
    file?:File
}

/**
 * Pen
 */
export type Pen={
    icon:string
    linewidth:number
    strokeStyle:string
    fillStyle:string
}