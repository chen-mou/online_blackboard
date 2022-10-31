<template>
    <div class="NavBar">
        <pen></pen>
        <shape></shape>

        <div>
            color
        </div>
        <operationVue></operationVue>
    </div>
</template>

<script lang="ts" >
import shape from "./shape.vue"
import pen from "./pen.vue"
import  {defineComponent} from "vue"
export default defineComponent({
    name:"NavBar",
    components:{
        shape,
        pen

    }
})

</script>

<script lang="ts" setup>
import operationVue from "./operation.vue";
</script>
<style lang="less" scoped>
.NavBar {
    height: 15vh;
    width: 100%;
    border-bottom: 2px solid rgb(217, 211, 211);
    display: flex;
    justify-content: space-evenly;
    gap: 0;
}
</style>