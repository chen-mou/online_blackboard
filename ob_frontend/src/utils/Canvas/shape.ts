import Canvas from "./canvas"
import {getRadiusByTwoPoints} from "./math"
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
/**
 * 矩形类
 */
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

/**
 * 自由线条
 */
class Line extends BaseShape{
    constructor(BeforePosition:Array<number>=[0,0],AfterPosition:Array<number>=[0,0]){
        super(BeforePosition,AfterPosition)
        this.type="line"
        this.icon="/canvsShapeImg/rect.png"
    }

    draw(canvas:Canvas){
        canvas.context.strokeStyle = '#00'
        canvas.context.fillStyle = '#9f9'
        canvas.context.beginPath()
        canvas.context.moveTo(this.BeforePosition[0],this.BeforePosition[1])
        canvas.context.lineTo(this.AfterPosition[0],this.AfterPosition[1])
        canvas.context.stroke()
        
    }
}
class Circle extends BaseShape{
    constructor(BeforePosition:Array<number>=[0,0],AfterPosition:Array<number>=[0,0]){
        super(BeforePosition,AfterPosition)
        this.type="circle"
        this.icon="/canvsShapeImg/rect.png"
    }

    draw(canvas:Canvas){
        canvas.context.strokeStyle = '#00'
        canvas.context.fillStyle = '#9f9'
        canvas.context.beginPath()
        canvas.context.arc(this.BeforePosition[0],this.BeforePosition[1],
        getRadiusByTwoPoints(this.BeforePosition,this.AfterPosition),0,2*Math.PI);//arc 的意思是“弧”
        canvas.context.stroke()
        
    }
}

export {
    BaseShape,
    Rectangle,
    Line,
    Circle
}