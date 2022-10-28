<script lang="ts">
import { defineComponent, ref } from "vue";
import { useUserStore } from "@/store/user";

export default defineComponent({
  name: "UserInfo",
  setup() {
    const userStore = useUserStore()
    const newName = ref(userStore.nickname)
    return {
      newName,
      userStore,
    }
  },
  methods: {
    async updateNickname() {
      if (this.newName === this.userStore.nickname) {
        return
      }
      await this.userStore.updateNickName(this.newName,
        (data) => {
          this.$message({
            type: 'error',
            message: `修改失败！${data.msg}`
          })
        }, () => {
          this.$message({
            type: 'success',
            message: '修改成功！',
          })
        })
      this.newName = this.userStore.nickname
    },
    async logout() {
      this.userStore.logout()
      this.$router.replace('/login')
    }
  }
})
</script>

<template>
  <div class="container">
    <p>个人信息</p>
    <el-input v-model="newName" placeholder="请输入新的昵称"/>
    <div style="float: right">
      <el-button type="primary" @click="updateNickname">修改昵称</el-button>
    </div>
    <el-button type="danger" style="margin-top: 50px" @click="logout">登出</el-button>
  </div>
</template>

<style scoped>
.container {
  border: 1px solid lightgray;
  border-radius: 30px;
  padding: 30px;
  box-shadow: 0 0 20px 0 lightgray;
}

.container > *:not(:nth-child(1)) {
  margin-top: 10px;
}
</style>