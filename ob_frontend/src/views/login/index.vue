<script>
import { ref } from "vue";

export default {
  name: "UserLogin",
  setup() {
    const username = ref('')
    const password = ref('')

    return {
      username,
      password,
    }
  }
}
</script>

<template>
  <div class="container">
    <el-input v-model="username" placeholder="输入账号" clearable></el-input>
    <el-input v-model="password" type="password" placeholder="输入密码" show-password clearable></el-input>
    <el-button>去注册</el-button>
    <el-button type="primary">登录</el-button>

    {{ username }}, {{ password }}
  </div>
</template>

<style scoped>

.container {
  border: 1px solid red;

}

.container > * {
  /*margin-bottom: 10px;*/
  margin-bottom: 10px;
}

</style>