<script lang="ts" setup>
import { ref } from "vue";
import { useCanvasStore } from "@/store/canvas";
import { useUserStore } from "@/store/user";
import ChatMessage from './ChatMessage.vue'

const inputStr = ref<string>('')
const target = ref<number>(0)
const canvasStore = useCanvasStore()
const userStore = useUserStore()

function send() {
  canvasStore.ws.sendRaw('/app/send', {}, JSON.stringify({
    sender: userStore.userId,
    getter: target.value,
    type: 'message',
    msg: inputStr.value,
    broadcast: target.value === 0,
  }))
}
</script>

<template>
  <div>
    <div class="chat-list">
      <chat-message v-for="item of canvasStore.messageList" :key="item.id" :msg="item" @privateChat="(v: number)=>{target=v}"/>
    </div>
    <div style="position: fixed;bottom: 20px;width: 420px">
      <el-input
        type="textarea"
        class="input-area"
        placeholder="进行一个天的聊"
        rows="5"
        :autosize="false"
        resize="none"
        v-model="inputStr"
      >
      </el-input>
      <div class="btns">
        <el-button plain type="primary" @click="target=0" v-show="target!==0">取消私聊</el-button>
        <el-button type="primary" @click="send();inputStr=''">发送</el-button>
      </div>
    </div>
  </div>
</template>


<style scoped>
.chat-list {
  height: calc(100vh - 300px);
  border: 1px solid lightgray;
  border-radius: 5px;
  overflow-x: hidden;
  overflow-y: auto;
  width: 420px;
}

.chat-list > * {
  display: block;
  width: 420px;
}

.btns {
  float: right;
  margin-top: 13px;
}
</style>