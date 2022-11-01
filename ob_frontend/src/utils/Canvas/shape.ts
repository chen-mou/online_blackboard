import Canvas from "./canvas"

/**
 * ShapeClass
 */
class BaseShape{
    icon:string|undefined
    type!: string
    positionX:number|undefined
    positionY:number|undefined
    width:number|undefined
    height:number|undefined
    BeforePosition:Array<number> // mousedown position
    AfterPosition:Array<number>// mouseup position
    constructor(BeforePosition:Array<number>=[0,0],AfterPosition:Array<number>=[0,0]){
        this.BeforePosition=BeforePosition,
        this.AfterPosition=AfterPosition
    }
}

class Rectangle extends BaseShape{
    constructor(BeforePosition:Array<number>=[0,0],AfterPosition:Array<number>=[0,0]){
        super(BeforePosition,AfterPosition)
        this.type="Rect"
        this.icon="/canvsShapeImg/rect.png"
    }
    draw(canvas:Canvas){
        if(this.BeforePosition&&this.AfterPosition){
            canvas.context.strokeStyle = '#00'
            canvas.context.fillStyle = '#9f9'
            canvas.context.beginPath()
            canvas.context.rect(
                this.BeforePosition[0] ,
                this.BeforePosition[1],
                this.AfterPosition[0]-this.BeforePosition[0] ,
                this.AfterPosition[1]-this.BeforePosition[1]
            )
            canvas.context.stroke()
        }else{
            throw Error(`this.BeforePosition:${this.BeforePosition}`)
        }

    }
}

export {
    BaseShape,
    Rectangle
}