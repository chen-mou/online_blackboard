
// 生成一份object的key的类型，并将非 T 继承类型的key的值类型置为 never
type ExtractType<O, T> = { [K in keyof O]: O[K] extends T ? O[K] : unknown }
// 组合两个 object 的类型
type Diff<T extends string, U> = ({ [X in T]: X } &
  { [P in keyof U]: U[P] extends string ? string : never } & {
    [x: string]: never
  })[T]
// 这里我们只以 string 类型的key作为示例
type ExtractStringKey<A> = Diff<
  Extract<keyof A, string>,
  ExtractType<A, string>
>
import { Pen, ShapeDataType } from "./type/CanvasType"
type styleNameType = ExtractStringKey<CanvasRenderingContext2D>
import ShapeMap from "./ShapeMap"
type ShapeClassType=  ReturnType<typeof ShapeMap.get>
type GetMapKeyType<T> = T extends Map<any,infer I> ?I:never
export  type ShapeClassTypeT = GetMapKeyType<typeof ShapeMap>
class Canvas  {
  options: any
  canvas: HTMLCanvasElement
  target: HTMLElement // 接收事件的元素
  DrawClass:ShapeClassTypeT  // 默认类型为矩形// 默认类型为矩形
  after: any
  pen: Pen|null
  layers: any
  context: CanvasRenderingContext2D
  data : Array<ShapeDataType>
  constructor (options: Object) {
    this.options = options 
    const {
      canvas,
      target,
      after,
      data = [],
      list = null
    } = this.options
    if (canvas === null) {
      throw Error('请传入canvas元素对于的Id')
    }
    this.canvas =(document.getElementById(canvas) as HTMLCanvasElement) || canvas as HTMLCanvasElement
    this.target = document.getElementById(target) as HTMLElement
    this.after = after
    this.data = data
    this.DrawClass=ShapeMap.get("line") as ShapeClassTypeT
    this.layers = [] // 画布的层
    this.pen={
      icon:"",
      linewidth:1,
      strokeStyle:"#00",
      fillStyle:"#9f9"
    }
    if(!this.canvas){
        throw Error( `创建canvas失败,this.canvas=${this.canvas}`)
    }
    this.context = this.canvas.getContext('2d') as CanvasRenderingContext2D // 画布的上下文
    this.context.strokeStyle=this.pen.strokeStyle
    this.context.fillStyle=this.pen.fillStyle
  }
    // init(){

    // }
    drawData(){
      this.context.clearRect(0, 0, 1600, 1600) 
      for (let i = 0; i < this.data.length; i++) {
        if(this.data[i].type=="img"){
            console.log(this.data[i])
            ShapeMap.get(this.data[i].type)?.draw(this,this.data[i].file) 
        }else{
          const {strokeStyle,fillStyle} =this.data[i].pen as Pen
          this.context.strokeStyle=strokeStyle
          this.context.fillStyle=fillStyle
          this.context.lineWidth=4
          ShapeMap.get(this.data[i].type)!.BeforePosition=this.data[i].BeforePosition
          ShapeMap.get(this.data[i].type)!.AfterPosition=this.data[i].AfterPosition
          ShapeMap.get(this.data[i].type)?.draw(this) 
        }

      }

    }
} 

export default Canvas
