<template>
    <div class="shape" @click="HandleSelect">
        <!-- 遍历icon及形状 -->
        <div v-for="item in ShapeMap" :key="item[1].type">
            <img :src="item[1].icon" :name="item[1].type">
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
import { ref,reactive, onMounted, inject } from 'vue';
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
const HandleSelect=(e:MouseEvent)=>{
    /**(e.target as any).name as string
     * 事件代理触发的回调
     */
    (canvas.value as Canvas).DrawClass=ShapeMap.get((e.target as any).name as string) as ShapeClassTypeT
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
</style>