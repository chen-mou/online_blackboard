<script setup>
import { computed, defineProps } from "vue";
import { useUserStore } from "@/store/user";
import dayjs from "dayjs";

const userStore = useUserStore()

let props = defineProps({
  msg: Object
})

const msgStyle = computed(() => {
  if (props.msg.sender === userStore.userId) {
    return {
      float: 'right',
      border: '1px solid rgb(32,128,240)',
      color: 'white',
      background: 'rgb(32,128,240)',
    }
  }
  return {
    float: 'left',
    border: '1px solid lightgray',
    color: 'black',
    background: 'white',
  }
})
</script>

<template>
  <div class="chat-message__out">
    <div
      class="chat-avatar"
      :style="{float:msg.sender === userStore.userId?'right':'left'}"
      @click="$emit('privateChat',msg.sender===userStore.userId?0:msg.sender)"
    >{{ msg.senderName.charAt(0) }}
    </div>
    <div
      class="chat-message"
      :style="msgStyle"
    >
      {{ msg.msg }}
    </div>
  </div>
  <p class="message-time" :style="{float:msg.sender === userStore.userId?'right':'left'}">
    {{ dayjs(msg.time).format('YYYY-MM-DD HH:mm') }}</p>
</template>

<style scoped>
.chat-message {
  white-space: pre-wrap;
  max-width: 250px;
  word-break: normal;
  word-wrap: break-word;
  padding: 5px;
  border-radius: 5px;
  user-select: text;
}

.chat-message__out {
  width: 100%;
  overflow: hidden;
  margin: 10px 0 10px;
}

.chat-avatar {
  margin: 0 10px 10px;
  border: 1px solid lightgray;
  height: 40px;
  width: 40px;
  border-radius: 5px;
  line-height: 40px;
  font-size: 20px;
  text-align: center;
  cursor: pointer;
  user-select: none;
}

.message-time {
  margin-left: 10px;
  margin-right: 10px;
  font-size: 10px;
  color: gray;
  margin-bottom: 13px;
}
</style>