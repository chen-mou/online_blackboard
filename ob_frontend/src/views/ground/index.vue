<script lang="ts">
import { useUserStore } from "@/store/user";
import { defineComponent, ref } from "vue";
import { ElMessage } from "element-plus";

export default defineComponent({
  name: "BlackboardGround",
  setup() {
    const userStore = useUserStore()
    const packEnter = ref<'enter' | 'create'>('enter')
    return {
      userStore,
      packEnter,
    }
  },
  data() {
    return {
      newName: '',
      whiteboardCode: '',
      hideName: false,
      orderTime: false,
      roomName: '',
    }
  },
  mounted() {
    if (!this.userStore.hasLogin) {
      this.userStore.getUserData().then((res) => {
        if (!res) {
          this.$router.replace('/login')
          ElMessage({
            message: '请先登录',
            type: 'info',
          })
        }
        this.newName = this.userStore.nickname
      })
    }
    this.newName = this.userStore.nickname
  },
  methods: {
    joinRoom() {
      return
    },
    createRoom() {
      return
    },
  },
  computed: {
    startTime() {
      return 0
    },
    endTime() {
      return 0
    },

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
        <el-input placeholder="输入白板号" v-model="whiteboardCode"/>
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
        <div v-show="orderTime">
          从
          <input type="date" class="date-picker"/>
          <input type="time" class="date-picker"/>
          <br/>
          到
          <input type="date" class="date-picker"/>
          <input type="time" class="date-picker"/>
        </div>
        <p class="buttons">
          <el-checkbox label="预约时间" v-model="orderTime"/>
          <el-checkbox label="开启匿名" v-model="hideName" @click="newName=userStore.nickname"/>
          <el-button type="primary" @click="createRoom">创建</el-button>
        </p>
      </div>
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
</style>