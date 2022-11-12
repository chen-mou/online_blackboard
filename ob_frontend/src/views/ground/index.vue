<script lang="ts">
import { useUserStore } from "@/store/user";
import { defineComponent, ref } from "vue";
import { ElMessage } from "element-plus";
import { useRoomStore } from "@/store/room";
import dayjs from "dayjs";
import RoomEntry from './components/RoomEntry.vue'
import request from "@/utils/request";
// @ts-ignore
import snappy from 'snappyjs'
import axios from "axios";

export default defineComponent({
  name: "BlackboardGround",
  components: {
    RoomEntry,
  },

  setup() {
    const userStore = useUserStore()
    const roomStore = useRoomStore()
    const packEnter = ref<'enter' | 'create'>('enter')
    return {
      userStore,
      roomStore,
      packEnter,
      dayjs,
    }
  },
  data() {
    return {
      newName: '',
      whiteboardCode: '',
      hideName: false,
      fromDatetime: dayjs().format('YYYY-MM-DD HH:mm'),
      toDatetime: '',
      roomName: (this.userStore as any).nickname + '的房间',
      startNow: false,
    }
  },
  mounted() {
    if (!this.userStore.hasLogin) {
      this.userStore.getUserData().then((res) => {
        if (!res) {
          ElMessage({
            message: '请先登录',
            type: 'info',
          })
          this.$router.replace('/login')
          return
        }
        this.newName = this.userStore.nickname
      })
    }
    this.newName = this.userStore.nickname
  },
  methods: {
    async joinRoom() {
      // console.log(this.whiteboardCode, this.hideName)
      this.roomStore.joinRoom(Number(this.whiteboardCode), Number(this.hideName), 0);
      await this.$router.replace('/canvas')
    },
    async createRoom() {
      let data: any = {
        isShare: 0,
        allowAnonymous: Number(this.hideName),
        startTime: this.fromDatetime,
        endTime: this.toDatetime,
        startNow: this.startNow,
        name: this.roomName,
        now: this.startNow,
      }
      let msg: string = await this.roomStore.createRoom(data)
      if (msg) {
        this.$message({
          type: 'error',
          message: msg,
        })
      } else {
        await this.userStore.getUserRooms()
      }
    },
  },
})
</script>

<template>
  <div class="container">
    <div :class="{pack:packEnter==='enter'}" @click="packEnter='create'">
      <p>加入房间</p>
      <el-icon v-show="packEnter==='enter'" class="ground-icon">
        <DArrowRight/>
      </el-icon>
      <div v-show="packEnter==='create'" class="container-in">
        <el-input placeholder="输入房间号" v-model="whiteboardCode"/>
        <el-input placeholder="你的昵称" v-model="newName" :disabled="!hideName"/>
        <p class="buttons">
          <el-checkbox label="开启匿名" v-model="hideName" @click="newName=userStore.nickname" title="只有在房间开启匿名功能时生效"/>
          <el-button type="primary" @click="joinRoom">加入</el-button>
        </p>
      </div>
    </div>
    <div :class="{pack:packEnter==='create'}" @click="packEnter='enter'">
      <p>创建房间</p>
      <el-icon v-show="packEnter==='create'" class="ground-icon">
        <DArrowLeft/>
      </el-icon>
      <div v-show="packEnter==='enter'" class="container-in">
        <el-input placeholder="房间名" v-model="roomName"/>
        <br/>
        <el-input style="width: 200px;margin-right: 5px" placeholder="你的昵称" v-model="newName"
                  :disabled="!hideName"/>
        <el-checkbox style="position: relative;top:7px" label="开启匿名" v-model="hideName"
                     @click="newName=userStore.nickname"/>
        <div>
          从
          <el-date-picker style="width: 180px;margin-right: 5px" type="datetime" class="date-picker"
                          v-model="fromDatetime" :disabled="startNow" format="YYYY-MM-DD HH:mm"
                          value-format="YYYY-MM-DD HH:mm"/>
          <el-checkbox label="从现在开始" v-model="startNow"/>
          <br/>
          到
          <el-date-picker style="width: 180px;margin-top: 10px" type="datetime" class="date-picker"
                          v-model="toDatetime" format="YYYY-MM-DD HH:mm" value-format="YYYY-MM-DD HH:mm"/>
        </div>
        <p class="buttons">
          <el-button type="primary" @click="createRoom">创建</el-button>
        </p>
      </div>
    </div>
  </div>
  <div class="my-room">
    <p>我的房间</p>
    <div style="margin-top: 20px"
         v-show="userStore.myRooms.filter((r)=>dayjs().isBefore(r.setting.endTime)).length===0">还没有创建房间哦
    </div>
    <div>
      <RoomEntry
        v-for="room in userStore.myRooms.filter((r)=>dayjs().isBefore(r.setting.endTime))"
        :key="room.id" :room="room"/>
    </div>
  </div>
</template>

<style scoped>
.container {
  border: 1px solid lightgray;
  box-shadow: 0 0 20px 0 lightgray;
  border-radius: 30px;
  height: 300px;
  overflow: hidden;
  display: flex;
  min-width: 490px;
}

.container > * {
  width: 300px;
  height: 100%;
  padding: 30px;
  transition: 0.3s ease-out;
}

.container > .pack {
  cursor: pointer;
  width: 70px;
  background-color: dodgerblue;
  color: white;
}

.container > .pack * {
  cursor: pointer;
}

.buttons {
  float: right;
}

.buttons > *:not(:nth-child(1)) {
  margin-left: 10px;
}

.ground-icon {
  margin-top: 10px;
  font-size: 50px;
}

.container-in {
  white-space: nowrap;
  overflow: hidden;
}

.container-in > * {
  margin-top: 12px;
}

.date-picker {
  border: 1px solid lightgray;
  border-radius: 3px;
  outline: none;
  cursor: pointer;
  margin-right: 10px;
}

.my-room {
  margin-left: 60px;
  border: 1px solid lightgray;
  box-shadow: 0 0 20px 0 lightgray;
  border-radius: 30px;
  max-height: 80vh;
  padding: 30px;
  overflow-y: auto;
  min-width: 400px;
}
</style>