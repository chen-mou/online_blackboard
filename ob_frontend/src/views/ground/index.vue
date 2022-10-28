<script lang="ts">
import { useUserStore } from "@/store/user";
import { useRouter } from "vue-router";
import { defineComponent, ref } from "vue";
import { ElMessage } from "element-plus";

export default defineComponent({
  name: "BlackboardGround",
  setup() {
    const userStore = useUserStore()

    const newName = ref(userStore.nickname)
    const whiteboardCode = ref('')
    const packEnter = ref<'enter' | 'create'>('enter')

    return {
      newName,
      whiteboardCode,
      packEnter,
      userStore,
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
  },
  methods: {
    joinRoom() {

      return
    },
    createRoom() {
      return
    },
  }
})
</script>

<template>
  <div class="container">
    <div :class="{pack:packEnter==='enter'}" @click="packEnter='create'">
      <p>加入白板</p>
      <el-icon v-show="packEnter==='enter'" class="ground-icon">
        <DArrowRight/>
      </el-icon>
      <div v-show="packEnter==='create'" class="container-in">
        <el-input placeholder="输入白板号" v-model="whiteboardCode"/>
        <el-input placeholder="你的昵称" v-model="newName"/>
        <p class="buttons">
          <el-button type="primary" @click="joinRoom">加入</el-button>
        </p>
      </div>
    </div>
    <div :class="{pack:packEnter==='create'}" @click="packEnter='enter'">
      <p>创建白板</p>
      <el-icon v-show="packEnter==='create'" class="ground-icon">
        <DArrowLeft/>
      </el-icon>
      <div v-show="packEnter==='enter'" class="container-in">
        <el-input placeholder="你的昵称" v-model="newName"/>
        <p class="buttons">
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
  height: 200px;
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

.ground-icon {
  margin-top: 10px;
  font-size: 50px;
}

.container-in > * {
  margin-top: 12px;
}
</style>