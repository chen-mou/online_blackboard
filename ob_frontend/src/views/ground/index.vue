<script>
import { useUserStore } from "../../../store/user";
import { useRouter } from "vue-router";

export default {
  name: "BlackboardGround",
  setup() {
    const userStore = useUserStore()
    console.log(userStore.login)
    if (!userStore.login) {
      useRouter().replace('/login')
    }
  }
}
</script>

<template>
  <div>ground</div>
</template>

<style scoped>

</style>