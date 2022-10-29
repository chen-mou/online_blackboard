<script lang="ts">
import { defineComponent,onMounted } from "vue";
import { useUserStore } from "@/store/user";
import Canvas from "@/utils/Canvas/canvas";
import NavBar from './components/NavBar.vue'
export default defineComponent({
  components: { NavBar },
  name: "BlackboardCanvas",
  setup() {
    const userStore = useUserStore()
    onMounted(()=>{
    const canvas = new Canvas({canvas:"canvas"})
    console.log(canvas)
  })

    return {
      userStore,
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
  <div class="main">
    <nav-bar></nav-bar>
    <canvas id="canvas"></canvas>
  </div>
</template> 

<style scoped lang="less">
.main {
  height: 100%;
  width: 100%;

  #canvas {
    height: 90vh;
    width: 100%
  }
}
</style>