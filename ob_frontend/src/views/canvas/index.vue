<script lang="ts">
import { defineComponent } from "vue";
import { useUserStore } from "@/store/user";

export default defineComponent({
  name: "BlackboardCanvas",
  setup() {
    const userStore = useUserStore()
    return {
      userStore
    }
  },
  watch: {
    'userStore.hasLogin'(newVal) {
      console.log(newVal)
      if (newVal) {
        return
      }
      //todo 登出处理
      return
    }
  }
})
</script>

<template>
  <div>
    canvas
  </div>
</template>

<style scoped>

</style>