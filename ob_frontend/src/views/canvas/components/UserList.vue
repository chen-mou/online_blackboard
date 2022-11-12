<script lang="ts" setup>
import { useCanvasStore } from "@/store/canvas";

const canvasStore = useCanvasStore()
</script>

<template>
  <div class="users">
    <div v-for="shit of canvasStore.otherUsers" :key="shit.userId" class="one-user">
      <p class="user-name">{{ shit.nickname }}</p>
      <div style="position: absolute;bottom: 0;right: 0">
        <el-tag type="success">在线</el-tag>
      </div>
    </div>
  </div>
  <div v-show="canvasStore.otherUsers.length===0">
    当前没有其他用户哦
  </div>
</template>

<style scoped>
.users {
  display: grid;
  grid-template-columns: 101px 101px 101px 101px;
}

.one-user {
  border: 1px solid lightgray;
  margin: 10px;
  height: 81px;
  display: flex;
  justify-content: center;
  align-items: center;
  line-break: anywhere;
  position: relative;
  border-radius: 10px;
  overflow: hidden;
}

.user-name {
  line-break: anywhere;
  overflow: hidden;
  text-overflow: ellipsis;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  display: -webkit-box;
  margin-left: 3px;
  margin-right: 3px;
}
</style>
