<script lang="ts" setup>
import { computed } from "vue";
import dayjs from "dayjs";

const props = defineProps({
  room: Object
})
const isOpen = computed(() => {
  return !dayjs().isBetween(props.room?.setting.startTime, props.room?.setting.endTime)
})
</script>

<template>
  <div class="room">
    <div class="room-info">
      <p style="max-width: 120px;
        max-height:30px;
        overflow: hidden;
        text-overflow: ellipsis;
        line-break: anywhere;
        -webkit-line-clamp: 1;
        -webkit-box-orient: vertical;
        display: -webkit-box;">
        {{ room.name }}</p>
      <el-button :disabled="isOpen">复制房间号</el-button>
      <el-button type="primary" :disabled="isOpen">进入房间</el-button>
    </div>
    <div>{{ room.setting.startTime }} ~ {{ room.setting.endTime }}</div>
  </div>
</template>


<style scoped>
.room {
  padding: 10px;
  user-select: auto;
  margin-top: 10px;
  border: 1px solid lightgray;
  box-shadow: 0 0 10px 0 lightgray;
  border-radius: 10px;
  text-align: center;
}

.room-info {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 30px;
  margin-bottom: 10px;
}

.room-info > * {
  display: inline-block;
  margin-right: 10px;
}
</style>