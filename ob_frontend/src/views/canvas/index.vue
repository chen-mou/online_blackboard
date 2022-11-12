<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'BlackboardCanvas',
})
</script>

<script setup lang="ts">
import edit from "@/views/canvas/components/edit.vue"
import NavBar from './components/NavBar.vue'
import { onMounted, ref, reactive, provide, onBeforeUnmount } from 'vue'
import Canvas from '@/utils/Canvas/canvas'
import { useCanvasStore } from "@/store/canvas";
import { useRoomStore } from "@/store/room";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
const IsEditShow =ref<boolean>(false)
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

canvasStore.connect(roomStore.roomId, roomStore.userAnonymous, (frame) => {
  ElMessage.error({
    message: frame.headers.message,
    grouping: true,
  })
  router.replace('/')
})


const drawer = ref(true)
const activeName = ref<'chat' | 'room' | 'settings'>('chat')
const shareMode = ref<boolean>(Boolean(roomStore.isShare))

async function changeShareMode(val: boolean) {
  let message = await roomStore.updateRoom({ roomId: roomStore.roomId, isShare: Number(val) })
  if (message) {
    ElMessage.error({ message,grouping: true, })
    shareMode.value = !val
  }
}

async function endRoom() {
  canvasStore.over()
}
/**
 * 控制是否显示编辑
 */

</script>
<template>
  <div class="main">
    <nav-bar></nav-bar>
    <edit v-show="canvasProvide?.state"></edit>
    <canvas id="canvas2" ref="canvas2Ref" width="1600" height="800"></canvas>
    <canvas id="canvas" ref="canvasRef" width="1600" height="800"></canvas>

  </div>
  <div class="drawer-button" @click="drawer=true">
    <el-icon>
      <DArrowLeft/>
    </el-icon>
  </div>
  <div class="drawer">
    <el-drawer
      v-model="drawer"
      title="房间信息"
      direction="rtl"
    >
      <el-tabs v-model="activeName">
        <el-tab-pane label="聊天" name="chat">111</el-tab-pane>
        <el-tab-pane label="房间用户" name="room">222</el-tab-pane>
        <el-tab-pane label="房间设置" name="settings">
          <div>
            所有人可编辑
            <el-switch @change="changeShareMode" v-model="shareMode"/>
          </div>
          <el-button type="danger" @click="endRoom">结束房间</el-button>
        </el-tab-pane>
      </el-tabs>
    </el-drawer>
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

.drawer-button {
  height: 50px;
  width: 30px;
  position: absolute;
  right: 0;
  border: 1px solid lightgray;
  cursor: pointer;
}

.drawer /deep/ .el-drawer__header {
  margin-bottom: 0;
}

.drawer /deep/ .el-drawer__body {
  padding-top: 10px;
}
</style>
