<script setup lang="ts">
import edit from "@/views/canvas/components/edit.vue"
import NavBar from './components/NavBar.vue'
import { onMounted, ref, reactive, provide, onBeforeUnmount } from 'vue'
import Canvas from '@/utils/Canvas/canvas'
import { useCanvasStore } from "@/store/canvas";
import { useRoomStore } from "@/store/room";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import ChatRoom from "@/views/canvas/components/ChatRoom.vue";
import UserList from "@/views/canvas/components/UserList.vue";
import { useUserStore } from "@/store/user";

const IsEditShow = ref<boolean>(false)
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
  canvasStore.ws.close()
  router.replace('/')

}

function quitRoom() {
  canvasStore.ws.sendRaw('/app/quit', {}, JSON.stringify({ roomId: roomStore.roomId }))
  canvasStore.ws.close()
  roomStore.joinRoom(0, 0, 0)
  router.replace('/')
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

const userStore = useUserStore()
console.log(canvasProvide.value?.state)
</script>
<template>
  <div class="main">
    <nav-bar></nav-bar>
    <edit v-if="canvasProvide?.state"></edit>
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
      title="????????????"
      direction="rtl"
    >
      <el-tabs v-model="activeName">
        <el-tab-pane label="??????" name="chat">
          <chat-room/>
        </el-tab-pane>
        <el-tab-pane label="????????????" name="room">
          <user-list/>
        </el-tab-pane>
        <el-tab-pane label="????????????" name="settings">
          <div>
            ??????????????????
            <el-switch @change="changeShareMode" v-model="shareMode"/>
          </div>
          <el-button type="warning" @click="quitRoom">????????????</el-button>
          <el-button type="danger" @click="endRoom" v-if="roomStore.creatorId===userStore.userId">????????????</el-button>
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
        <el-button @click="readyAddSheet=false;sheetName=''">??????</el-button>
        <el-button style="margin-left: 0" type="primary" @click="readyAddSheet=false;addSheet()">??????</el-button>
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

.drawer /deep/ .el-drawer {
  width: 463px !important;
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
  white-space: nowrap;
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
