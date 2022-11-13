<template>
  <div class="NavBar">
    <pen></pen>
    <shape></shape>
    <operationVue></operationVue>
  </div>
</template>

<script lang="ts" setup>
import operationVue from "./operation.vue";
import shape from "./shape.vue"
import pen from "./pen.vue"
</script>
<style lang="less" scoped>
.NavBar {
  box-sizing: border-box;
  height: 70px;
  width: 100%;
  border-bottom: 2px solid rgb(217, 211, 211);
  display: flex;
  gap: 0;
}

.NavBar > * {
  flex: 1;
}
</style>