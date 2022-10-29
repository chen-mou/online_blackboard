<script lang="ts">
import { defineComponent, ref } from "vue";
import { useUserStore } from "@/store/user";
import { useRoute, useRouter } from "vue-router";

export default defineComponent({
  name: "UserLogin",
  setup() {
    const username = ref('')
    const password = ref('')
    const userStore = useUserStore()

    return {
      username,
      password,
      userStore,
    }
  },
  methods: {
    async login() {
      await this.userStore.login(this.username, this.password, (failData: any) => {
        this.promptMessage(failData.msg)
      })
      this.password = ''
    },
    async register() {
      await this.userStore.register(this.username, this.password, (failData: any) => {
        this.promptMessage(failData.msg)
      })
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
    <el-input v-model="username" placeholder="输入账号" clearable></el-input>
    <el-input v-model="password" type="password" placeholder="输入密码" show-password clearable></el-input>
    <div style="float: right">
      <el-button @click="register">去注册</el-button>
      <el-button type="primary" @click="login">登录</el-button>
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