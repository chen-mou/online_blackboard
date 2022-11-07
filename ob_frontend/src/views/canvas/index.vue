<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'BlackboardCanvas',
})
</script>

<script setup lang="ts">
import NavBar from './components/NavBar.vue'
import { onMounted, ref, reactive, provide } from 'vue'
import Canvas from '@/utils/Canvas/canvas'
import { useCanvasStore } from "@/store/canvas";
const canvasRef = ref(null)
const canvasProvide=ref<Canvas>()
const canvasStore = useCanvasStore();
onMounted(() => {
  canvasStore.initCanvas()
  canvasProvide.value=canvasStore.canvas
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
