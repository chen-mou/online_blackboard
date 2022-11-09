<script lang="ts">
import { useUserStore } from "@/store/user";
import { defineComponent, ref } from "vue";
import { ElMessage } from "element-plus";
import { useRoomStore } from "@/store/room";
import dayjs from "dayjs";
import RoomEntry from './components/RoomEntry.vue'

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
    }
  },
  data() {
    return {
      newName: '',
      whiteboardCode: '',
      hideName: false,
      fromDate: dayjs().format('YYYY-MM-DD'),
      fromTime: dayjs().format('HH:mm'),
      toDate: '',
      toTime: '',
      roomName: (this.userStore as any).nickname + '的房间',
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
      let data: any = {
        isAnonymous: Number(this.hideName),
        roomId: this.whiteboardCode,
      }
      let msg: string = await this.roomStore.joinRoom(data);
      if (msg) {
        this.$message({
          type: 'error',
          message: msg,
        })
      }
    },
    async createRoom() {
      let data: any = {
        isShare: 0,
        allowAnonymous: Number(this.hideName),
        startTime: `${this.fromDate} ${this.fromTime}`,
        endTime: `${this.toDate} ${this.toTime}`,
      }
      let msg: string = await this.roomStore.createRoom(data)
      if (msg) {
        this.$message({
          type: 'error',
          message: msg,
        })
      }
    },
  },
  watch: {
    toTime() {
      console.log(this.fromDate, this.fromTime, '>>', this.toDate, this.toTime)
    }
  }
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
        <el-input placeholder="你的昵称" v-model="newName" :disabled="!hideName"/>
        <div>
          从
          <input type="date" class="date-picker" v-model="fromDate"/>
          <input type="time" class="date-picker" v-model="fromTime"/>
          <br/>
          到
          <input type="date" class="date-picker" v-model="toDate"/>
          <input type="time" class="date-picker" v-model="toTime"/>
        </div>
        <p class="buttons">
          <el-checkbox label="开启匿名" v-model="hideName" @click="newName=userStore.nickname"/>
          <el-button type="primary" @click="createRoom">创建</el-button>
        </p>
      </div>
    </div>
  </div>
  <div class="my-room">
    <p>我的房间</p>
    <div>
      <RoomEntry v-for="room in userStore.myRooms" :key="room.id" :room="room"/>
    </div>
  </div>
</template>

<style scoped>
.container {
  border: 1px solid lightgray;
  box-shadow: 0 0 20px 0 lightgray;
  border-radius: 30px;
  height: 270px;
  overflow: hidden;
  display: flex;
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

.container-in > * {
  margin-top: 12px;
  white-space: nowrap;
  overflow: hidden;
}

.date-picker {
  border: 1px solid lightgray;
  border-radius: 3px;
  outline: none;
  cursor: pointer;
  margin-right: 10px;
}

.my-room {
  margin-left: 20px;
  border: 1px solid lightgray;
  box-shadow: 0 0 20px 0 lightgray;
  border-radius: 30px;
  max-height: 80vh;
  padding: 30px;
  overflow-y: auto;
}
</style>