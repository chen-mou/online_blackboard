<script lang="ts">
import { defineComponent } from 'vue'
import { useUserStore } from '@/store/user'
import Canvas from '@/utils/Canvas/canvas'
import { de } from 'element-plus/es/locale'

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
import { onMounted, ref, reactive, provide } from 'vue'
import { getElPagePos } from '../../utils/Canvas/math'
import ShapeMap from '@/utils/Canvas'
const IsDrawing = ref<Boolean>(false)
const canvasRef = ref(null)
const canvasProvide=ref<Canvas>()
const beforePosition = ref<Array<number>>([0, 0])
const AfterPosition = ref<Array<number>>([0, 0])
onMounted(() => {
  const canvas = new Canvas({
    canvas: 'canvas'
  })
  canvasProvide.value=canvas

  const { x, y } = getElPagePos(document.getElementById('canvas') as any)

  canvas.canvas.addEventListener('mousedown', e => {
    /**
     * 传入相应的坐标
     */
    beforePosition.value = [e.pageX - x, e.pageY - y]
    canvas.DrawClass.BeforePosition=beforePosition.value
    IsDrawing.value = true
  })
  canvas.canvas.addEventListener('mousemove', e => {
    if (IsDrawing.value) {
      /**
       * 清空之后全部重新绘制
       */
       canvas.context.clearRect(0, 0, 1600, 1600) 
      console.log(canvas.data)
      for (let i = 0; i < canvas.data.length; i++) {
        ShapeMap.get(canvas.data[i].type)!.BeforePosition=canvas.data[i].BeforePosition
        ShapeMap.get(canvas.data[i].type)!.AfterPosition=canvas.data[i].AfterPosition
        ShapeMap.get(canvas.data[i].type)?.draw(canvas)
      }
      AfterPosition.value = [e.pageX - x, e.pageY - y] 
      canvas.DrawClass.BeforePosition= beforePosition.value
      canvas.DrawClass.AfterPosition= AfterPosition.value 
  
      canvas.DrawClass.draw(canvas)
    }
  })
  canvas.canvas.addEventListener('mouseup', e => {
    AfterPosition.value = [e.pageX - x, e.pageY - y] 
    canvas.DrawClass.AfterPosition= AfterPosition.value 
    canvas.DrawClass.draw(canvas)
    /**
     * 储存标记点type和相应的坐标点
     */
    canvas.data.push({
      type:canvas.DrawClass.type,
      BeforePosition:beforePosition.value,
      AfterPosition:AfterPosition.value
    })
    IsDrawing.value = false
  })
})
  provide("canvas__",canvasProvide)

const userStore = useUserStore()
/**
 * 绑定mouse事件
 */
</script>
<template>
  <div class="main">
    <nav-bar></nav-bar>
    <canvas id="canvas" ref="canvasRef" width="1600" height="800"></canvas>
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
