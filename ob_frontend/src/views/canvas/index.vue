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
    ElMessage.error({ message, grouping: true, })
    shareMode.value = !val
  }
}

async function endRoom() {
  canvasStore.over()
}

async function changeSheet(sheetId: string) {
  canvasStore.ws.sendRaw('/app/change_sheet', {}, JSON.stringify({
    sheetId,
    roomId: roomStore.roomId,
  }))
}

const readyAddSheet = ref(false)
const sheetName = ref<string>('')

async function addSheet() {
  canvasStore.ws.sendRaw('/app/create', {}, JSON.stringify({
    name: sheetName.value,
    roomId: roomStore.roomId,
  }))
  sheetName.value = ''
}
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
  <div class="sheets">
    <div v-for="sheet in canvasStore.sheets" :key="sheet.id" @click="changeSheet(sheet.id)"
        :class="{'sheet-now':sheet.id===roomStore.sheetId}">{{ sheet.name }}
    </div>
    <div style="display: inline-block;position: relative;bottom: 11px;">
      <div v-show="readyAddSheet" style="display: inline-block">
        <el-input style="width:150px;" v-model="sheetName" placeholder="new sheet name"/>
        <el-button @click="readyAddSheet=false;sheetName=''">取消</el-button>
        <el-button style="margin-left: 0" type="primary" @click="readyAddSheet=false;addSheet()">确定</el-button>
      </div>
      <el-button plain @click="readyAddSheet=!readyAddSheet;sheetName=''">
        <el-icon>
          <Plus/>
        </el-icon>
      </el-button>
    </div>
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

.sheets {
  position: absolute;
  bottom: 20px;
  left: 40px;
}

.sheets > *:not(:last-child) {
  max-width: 70px;
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  line-height: 30px;
  border-radius: 4px;
  margin-right: 5px;
  border: 1px solid lightgray;
  padding-left: 5px;
  padding-right: 5px;
}

.sheets > *:not(:last-child):hover {
  background-color: #dcefff;
  cursor: pointer;
}

.sheet-now {
  background-color: lightblue;
}
</style>
