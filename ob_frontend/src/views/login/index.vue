<script lang="ts">
import { defineComponent, ref } from "vue";
import { useUserStore } from "../../../store/user";
import { ElMessage } from "element-plus";

export default defineComponent({
  name: "UserLogin",
  setup() {
    const username = ref('')
    const password = ref('')
    const userStore = useUserStore()
    return {
      username,
      password,
      userStore
    }
  },
  methods: {
    login() {
      this.userStore.login(this.username, this.password, (failData: any) => {
        this.$message({
          message: `登陆失败！${failData.msg}`,
          type: 'error',
          grouping: true,
        })
      })
    },
    register() {
      this.userStore.register(this.username,this.password, ())
    }
  }
})
</script>

<template>
  <div class="container">
    <el-input v-model="username" placeholder="输入账号" clearable></el-input>
    <el-input v-model="password" type="password" placeholder="输入密码" show-password clearable></el-input>
    <div style="float: right">
      <el-button>去注册</el-button>
      <el-button type="primary" @click="login">登录</el-button>
    </div>

  </div>
</template>

<style scoped>
.container {
  border: 1px solid lightgray;
  width: 500px;
  padding: 30px;
  position: absolute;
  left: calc(50vw - 350px);
  top: 30vh;
  border-radius: 30px;
  box-shadow: 0 0 20px 0 lightgray;
}

.container > *:not(:nth-child(1)) {
  margin-top: 20px;
}
</style>