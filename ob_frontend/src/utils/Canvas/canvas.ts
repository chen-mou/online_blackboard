class canvas {
  options: any
  canvas: any
  height: any
  width: any
  target: any
  before: any
  after: any
  layers:any
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
    this.canvas = null // 画布
    this.height = height // 画布的宽高
    this.width = width
    this.target = target
    this.before = before
    this.after = after
    this.data = data
    this.layers = [] // 画布的层
  }
}
