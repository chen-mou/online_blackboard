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

type styleNameType = ExtractStringKey<CanvasRenderingContext2D>

class canvas {
  options: any
  canvas: HTMLCanvasElement
  height: number
  width: number
  target: HTMLElement // 接收事件的元素
  before: any
  after: any
  layers: any
  context: CanvasRenderingContext2D
  data = []
  constructor (options: Object) {
    this.options = options
    const {
      canvas,
      height,
      width,
      target,
      before,
      after,
      data = [],
      list = null
    } = this.options
    if (canvas === null) {
      throw Error('请传入canvas元素对于的Id')
    }
    this.canvas =
      (document.getElementById(canvas) as HTMLCanvasElement) ||
      (canvas as HTMLCanvasElement) // 画布
    this.height = height // 画布的宽高
    this.width = width
    this.target = document.getElementById(target) as HTMLElement
    this.before = before
    this.after = after
    this.data = data
    this.layers = [] // 画布的层
    this.context = this.canvas.getContext('2d') as CanvasRenderingContext2D // 画布的上下文
  }

  changeContextStyle (
    styleName: styleNameType,
    styleValue: string
  ) {
    (this.context[styleName] as string) = styleValue
  }
}

export default canvas
