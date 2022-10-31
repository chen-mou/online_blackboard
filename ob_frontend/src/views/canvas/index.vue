<script lang="ts">
import { defineComponent } from 'vue'
import { useUserStore } from '@/store/user'
import Canvas from '@/utils/Canvas/canvas'

export default defineComponent({
  name: 'BlackboardCanvas',

  watch: {
    'userStore.hasLogin' (newVal) {
      console.log(newVal)
      if (newVal) {
        return
      }
      //todo 登出处理
      return
    }
  }
})


</script>

<script setup lang="ts">
import NavBar from './components/NavBar.vue'
import { rectDraw } from '@/utils/Canvas/shapeType'
import { onMounted, ref ,reactive} from 'vue'
import { getElPagePos } from '../../utils/Canvas/math'

const IsDrawing=ref<Boolean>(false)
const rectDrawPositon=reactive<Array<rect>>([])
type rect ={
  x:number
  y:number
  w:number
  h:number
}
onMounted(() => {
  const canvas = new Canvas({
    canvas: 'canvas'
  })
  const { x, y } = getElPagePos(document.getElementById('canvas') as any)
  const beforePosition = ref<Array<number>>([0, 0])
  /**
   * 添加监听事件
   */
  canvas.canvas.addEventListener('mousedown', e => {
    /**
     * 创建点
     */
    console.log([e.pageX - x, e.pageY - y])
    beforePosition.value = [e.pageX - x, e.pageY - y]
    IsDrawing.value=true
  })
  canvas.canvas.addEventListener('mousemove', e => {
    if (IsDrawing.value) {
        /**
         * 清空之后全部重新绘制
         */

        canvas.context.clearRect(0,0,800,800)
        for(let i=0;i<rectDrawPositon.length;i++)
        {
          rectDraw(canvas,[ rectDrawPositon[i].x,rectDrawPositon[i].y], [
          rectDrawPositon[i].w,rectDrawPositon[i].h
        ])
        }
        console.log([e.pageX - x, e.pageY - y])
        rectDraw(canvas, beforePosition.value, [
          e.pageX - x - beforePosition.value[0],
          e.pageY - y - beforePosition.value[1]
        ])
    }

  })
  canvas.canvas.addEventListener("mouseup", e => {
      console.log([e.pageX - x, e.pageY - y])
            rectDraw(canvas, beforePosition.value, [
          e.pageX - x - beforePosition.value[0],
          e.pageY - y - beforePosition.value[1]
        ])
        /**
         * 储存标记点
         */
         rectDrawPositon.push({
          x:beforePosition.value[0],
          y:beforePosition.value[1],
          w:e.pageX - x - beforePosition.value[0],
          h:e.pageY - y - beforePosition.value[1],
         })
        IsDrawing.value=false
    })
})

const userStore = useUserStore()
/**
 * 绑定mouse事件
 */
</script>
<template>
  <div class="main">
    <nav-bar></nav-bar>
    <canvas id="canvas" width="800" height="800"></canvas>
  </div>
</template>

<style scoped lang="less">
.main {
  height: 100%;
  width: 100%;

  // #canvas {
  //   height: 90vh;
  //   width: 100%
  // }
}
</style>
