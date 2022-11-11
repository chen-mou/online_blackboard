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
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";

const canvasRef = ref(null)
const canvasProvide = ref<Canvas>()
const canvasStore = useCanvasStore();
const router = useRouter()
onMounted(() => {
  canvasStore.initCanvas()
  canvasProvide.value = canvasStore.canvas
})
provide("canvas__", canvasProvide)
const roomStore = useRoomStore()
if (!canvasStore.ws.active) {
  canvasStore.connect(roomStore.roomId, roomStore.userAnonymous, (frame) => {
    console.log('ws disconnected:', frame)
    ElMessage.error({
      message: frame.headers.message
    })
    router.replace('/')
  })
}
/**
 * 绑定mouse事件
 */
</script>
<template>
  <div class="main">
    <nav-bar></nav-bar>
    <canvas id="canvas2" ref="canvas2Ref" width="1600" height="800"></canvas>
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

* {
  user-select: none;
}

#canvas {
  position: relative;
}

#canvas2 {
  position: absolute;
  top: 70px;
  left: 0px;
}
</style>
