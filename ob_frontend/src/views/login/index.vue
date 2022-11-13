<script lang="ts">
import { defineComponent, ref } from "vue";
import { useUserStore } from "@/store/user";

export default defineComponent({
  name: "UserLogin",
  setup() {
    const username = ref('')
    const password = ref('')
    const userStore = useUserStore()
    const stat = ref<'登录' | '注册'>('登录')

    return {
      username,
      password,
      userStore,
      stat
    }
  },
  methods: {
    async login() {
      if (this.stat === '登录') {
        await this.userStore.login(this.username, this.password, (failData: any) => {
          this.promptMessage(failData.msg)
        })
      } else {
        await this.userStore.register(this.username, this.password, (failData: any) => {
          this.promptMessage(failData.msg)
        })
      }
      this.password = ''
    },
    promptMessage(msg: string) {
      this.$message({
        message: `登陆失败！${msg}`,
        type: 'error',
        grouping: true,
      })
    },
  },
  watch: {
    'userStore.hasLogin'(newValue: boolean) {
      if (!newValue) {
        return
      }
      if (this.$route.fullPath != '/login') {
        return
      }
      this.$message({
        type: 'success',
        message: '登录成功',
      })
      this.$router.replace('/')
    }
  }
})
</script>

<template>
  <div class="container">
    <h3>在线黑板 · {{ stat }}</h3>
    <el-input v-model="username" placeholder="输入账号" clearable></el-input>
    <el-input v-model="password" type="password" placeholder="输入密码" show-password clearable></el-input>
    <div style="float: right">
      <el-button @click="stat=stat==='登录'?'注册':'登录'">去{{ stat === '登录' ? '注册' : '登录' }}</el-button>
      <el-button type="primary" @click="login">{{ stat }}</el-button>
    </div>
  </div>
</template>

<style scoped>
.container {
  border: 1px solid lightgray;
  width: 500px;
  padding: 30px;
  border-radius: 30px;
  box-shadow: 0 0 20px 0 lightgray;
}

.container > *:not(:nth-child(1)) {
  margin-top: 20px;
}
</style>