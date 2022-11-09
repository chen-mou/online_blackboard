<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'BlackboardCanvas',
})
</script>

<script setup lang="ts">
import NavBar from './components/NavBar.vue'
import { onMounted, ref, reactive, provide, onBeforeUnmount } from 'vue'
import Canvas from '@/utils/Canvas/canvas'
import { useCanvasStore } from "@/store/canvas";
import { useRoomStore } from "@/store/room";

const canvasRef = ref(null)
const canvasProvide = ref<Canvas>()
const canvasStore = useCanvasStore();
onMounted(() => {
  canvasStore.initCanvas()
  canvasProvide.value = canvasStore.canvas
})
provide("canvas__", canvasProvide)
onBeforeUnmount(() => {
  canvasStore.cacheCanvasData()
})
const roomStore = useRoomStore()
canvasStore.connect(roomStore.roomId, roomStore.userAnonymous)
/**
 * 绑定mouse事件
 */
</script>
<template>
  <div class="main">
    <nav-bar></nav-bar>
    <canvas id="canvas" ref="canvasRef" width="1600" height="800"></canvas>
    <canvas id="canvas2" ref="canvas2Ref" width="1600" height="800"></canvas>
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

* {
  user-select: none;
}

#canvas {
  position: relative;
}

#canvas2 {
  position: absolute;
  top: 15vh;
  left: 0px;
}
</style>
