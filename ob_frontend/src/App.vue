<script lang="ts" setup>
import { ref } from 'vue'
import { useUserStore } from "./store/user";

const isCollapse = ref(true)
const userStore = useUserStore()
</script>

<template>
  <el-menu
    v-if="userStore.hasLogin"
    default-active="2"
    class="menu"
    :collapse="isCollapse"
    :router="true"
    @mouseenter="isCollapse=false"
    @mouseleave="isCollapse=true"
  >
    <div class="welcome">
      <p v-if="userStore.hasLogin">欢迎</p>
      <p v-if="!userStore.hasLogin">请登录</p>
      <p v-show="!isCollapse && userStore.hasLogin">`{{ userStore.nickname }}</p>
    </div>
    <el-menu-item index="/">
      <el-icon>
        <House/>
      </el-icon>
      <template #title>主页</template>
    </el-menu-item>
    <el-menu-item index="/canvas">
      <el-icon>
        <Edit/>
      </el-icon>
      <template #title>白板</template>
    </el-menu-item>
    <el-menu-item index="/user">
      <el-icon>
        <setting/>
      </el-icon>
      <template #title>个人信息</template>
    </el-menu-item>
  </el-menu>
  <div class="views">
    <router-view/>
  </div>
</template>

<style>
.menu:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}

.menu {
  z-index: 100;
  height: 100vh;
}

.views {
  z-index: 1;
  position: absolute;
  top: 0;
  left: 64px;
  width: calc(100vw - 64px);
  height: 100vh;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
}

.welcome {
  padding-top: 10px;
  margin-bottom: 10px;
  margin-left: 15px;
  overflow: hidden;
  white-space: nowrap;
}

.welcome > * {
  display: inline;
}
</style>
