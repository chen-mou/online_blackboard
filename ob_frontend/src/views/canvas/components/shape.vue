<template>
    <div class="shape" >
        <!-- 遍历icon及形状 -->
        <div v-for="item in (ShapeMap as any)" :key="item[1].type" @click="HandleSelect(item[1].type)">
          <img style="display: block" :src="item[1].icon" >
          <p style="display: block;width: 100%">{{item[1].type}}</p>
        </div>
    </div>
</template>

<script lang="ts" >
import {ShapeClassTypeT} from "@/utils/Canvas/canvas"
import  {defineComponent} from 'vue'
import Canvas from '@/utils/Canvas/canvas';
export default defineComponent({
    name:"shape"
})
</script>
<script lang="ts" setup>
import { ref, onMounted, inject } from 'vue';
import ShapeMap from '@/utils/Canvas/ShapeMap';

let canvas =ref()
onMounted( ()=>{
const canvasInjetct =inject("canvas__") as any
canvas=canvasInjetct
    // window.canvasInjetct=canvasInjetct
})
/**
 * 处理选中图形的事件
 */
const HandleSelect=(type:string)=>{
    /**(e.target as any).name as string
     * 事件代理触发的回调
     */
    (canvas.value as Canvas).DrawClass=ShapeMap.get(type) as ShapeClassTypeT
    console.log((canvas.value as Canvas).DrawClass)
}


</script>

<style lang="less" scoped>
.shape {
    width: 20vw;
    display: flex;
    border-left: 1px solid rgb(211, 201, 201);
    border-right: 1px solid rgb(211, 201, 201);
}
img{
    height: 30px;
    width: 30px;
}

.shape>* {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
}

.shape>*:hover {
  cursor: pointer;
  background-color: #dcefff;
}

</style>