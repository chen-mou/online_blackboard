<script lang="ts" setup>
import { computed, ref } from "vue";
import dayjs from "dayjs";
import { useRoomStore } from "@/store/room";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
import { useUserStore } from "@/store/user";

const props = defineProps({
  room: Object
})
const isOpen = computed(() => {
  return !dayjs().isBetween(props.room?.setting.startTime, props.room?.setting.endTime)
})

const roomStore = useRoomStore()
const userStore = useUserStore()
const router = useRouter()

function copyId() {
  let s = document.createElement('input')
  s.style.position = 'absolute'
  s.style.top = '-300px'
  s.value = props.room?.id
  let body = document.getElementsByTagName('body')[0]
  body.appendChild(s)
  s.select()
  document.execCommand("copy")
  s.blur()
  body.removeChild(s)
}

async function joinRoom() {
  roomStore.joinRoom(props.room?.id, props.room?.setting.allowAnonymous)
  await router.replace('/canvas')
}

async function editRoom() {
  let data: any = {
    roomId: props.room?.id,
    allowAnonymous: allowAnonymous.value,
    startTime: startTime.value,
    endTime: endTime.value,
    name: roomName.value,
  }
  let msg = await roomStore.updateRoom(data)
  if (msg) {
    ElMessage.error({
      message: msg
    })
  } else {
    openDialog.value = false
    userStore.getUserRooms()
  }
}

const roomName = ref<string>(props.room?.setting.name)
const startTime = ref<string>(props.room?.setting.startTime)
const endTime = ref<string>(props.room?.setting.endTime)
const allowAnonymous = ref<number>(props.room?.setting.allowAnonymous)

const changeSth = computed(() => {
  return roomName.value != props.room?.setting.name ||
    startTime.value != props.room?.setting.startTime ||
    endTime.value != props.room?.setting.endTime ||
    allowAnonymous.value != props.room?.setting.allowAnonymous
})

const openDialog = ref(false)

function deleteRoom() {
  roomStore.deleteRoom(props.room?.id)
}
</script>

<template>
  <div class="room">
    <div class="room-info">
      <p style="max-width: 150px;
        max-height:30px;
        overflow: hidden;
        text-overflow: ellipsis;
        line-break: anywhere;
        -webkit-line-clamp: 1;
        -webkit-box-orient: vertical;
        display: -webkit-box;">
        {{ room.setting.name }}</p>
      <div style="position: absolute;right: 0;top: 0;">
        <el-button @click="copyId" :disabled="isOpen">复制房间号</el-button>
        <el-button @click="joinRoom" type="primary" :disabled="isOpen">进入房间</el-button>
      </div>
    </div>
    <div>
      <span>{{ room.setting.startTime }} ~ {{ room.setting.endTime }}</span>
      <el-button style="margin-left: 10px;font-size: 24px;width: 30px;height: 30px;" type="primary" plain
                 @click="openDialog=true">
        <el-icon>
          <Setting/>
        </el-icon>
      </el-button>
    </div>
  </div>
  <el-dialog style="width: 280px;text-align: center;border-radius: 20px" v-model="openDialog"
             title="编辑房间">
    <el-input style="display: inline-block" placeholder="房间名" v-model="roomName"/>
    <div>
      从
      <el-date-picker style="width: 180px;margin-right: 5px;margin-top: 10px" type="datetime" class="date-picker"
                      v-model="startTime" format="YYYY-MM-DD HH:mm"
                      value-format="YYYY-MM-DD HH:mm"/>
      <br/>
      到
      <el-date-picker style="width: 180px;margin-top: 10px" type="datetime" class="date-picker"
                      v-model="endTime" format="YYYY-MM-DD HH:mm" value-format="YYYY-MM-DD HH:mm"/>
    </div>
    <div style="margin-top: 10px">
      <el-checkbox style="position: relative;top:5px;margin-right: 10px" label="开启匿名" v-model="allowAnonymous"/>
      <el-button type="primary" :disabled="!changeSth" @click="editRoom">确定</el-button>
    </div>
    <div>
      <el-button type="danger" style="margin-top: 10px" @click="deleteRoom">删除房间</el-button>
    </div>
  </el-dialog>
</template>


<style scoped>
.room {
  padding: 10px;
  user-select: auto;
  margin-top: 10px;
  border: 1px solid lightgray;
  box-shadow: 0 0 10px 0 lightgray;
  border-radius: 10px;
  text-align: center;
}

.room-info {
  height: 30px;
  margin-bottom: 10px;
  position: relative;
}

.room-info > * {
  display: inline-block;
  margin-right: 10px;
}
</style>