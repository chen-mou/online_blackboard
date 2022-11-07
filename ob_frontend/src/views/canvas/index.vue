<script lang="ts">
import { defineComponent } from 'vue'
import { useWs } from "@/utils/ws";


export default defineComponent({
  name: 'BlackboardCanvas',
  watch: {
    'userStore.hasLogin' (newVal) {
      if (newVal) {
        return
      }
      //todo 登出处理

      return
    }
  },
  mounted() {
    // useWs()
  }
})
</script>

<script setup lang="ts">
import {deepCopy} from "@/utils/index"
import NavBar from './components/NavBar.vue'
import { onMounted, ref, reactive, provide } from 'vue'
import { getElPagePos } from '../../utils/Canvas/math'
import Canvas from '@/utils/Canvas/canvas'
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
    console.log(canvas.context.strokeStyle)
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
      canvas.drawData()
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
      AfterPosition:AfterPosition.value,
      pen:deepCopy(canvas.pen)
    })
    IsDrawing.value = false
  })

  /**
   * 监听双击事件可选中和可拖动
   */
  canvas.canvas.addEventListener("dblclick",(e)=>{
    /**
     * 判断点是否在data的图形里面在的话拿出那一个图形并绘制
     */
    canvas.drawControlBorder(e.pageX - x,e.pageY - y)
  })
})
  provide("canvas__",canvasProvide)

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
