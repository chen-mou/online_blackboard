<script lang="ts">
import { useUserStore } from "../../../store/user";
import { useRouter } from "vue-router";
import { defineComponent } from "vue";

export default defineComponent({
  name: "BlackboardGround",
  setup() {
    const userStore = useUserStore()
    console.log(userStore.hasLogin)
    if (!userStore.hasLogin) {
      useRouter().replace('/login')
    }
  }
})
</script>

<template>
  <div>ground</div>
</template>

<style scoped>

</style>