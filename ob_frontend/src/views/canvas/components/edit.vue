<template>
    <div class="edit" @mousemove="stopEvents">
        
    </div>
</template>

<script lang="ts">
import { defineComponent  } from 'vue';
export default defineComponent({
    name:"edit"
})
</script>
<script lang="ts" setup>
const stopEvents = (e:MouseEvent)=>{
    e.stopPropagation()
}
</script>

<style lang="less" scoped> 
.edit{
    position: absolute;
    height: 300px;
    width: 200px;
    border-radius: 12px;
    left: 50px;
    top: 150px;
    border: 1px solid  #eef1ff;
    background-color: #eef1ff;
    box-shadow: 10px 10px 20px 10px #c3cae7; 
    z-index: 100;
}
</style>