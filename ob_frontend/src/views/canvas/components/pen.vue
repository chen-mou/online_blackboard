<template>
    <div>
        <input type="color" v-model="penColor">
    </div>
</template>

<script lang="ts">
import { defineComponent, inject, onMounted, reactive, ref, watch } from 'vue';
export default defineComponent({
    name:"pen"
}) 
</script>

<script lang="ts" setup>
import {Pen} from "@/utils/Canvas/type/CanvasType"
const PenList=reactive<Array<Pen>>([])
let canvas =ref()
const penColor =ref<string>("a42d2d")
onMounted( ()=>{
    
const canvasInjetct =inject("canvas__") as any
    canvas=canvasInjetct
})

watch(penColor,(newValue,oldValue)=>{
    canvas.value.context.strokeStyle=newValue
    canvas.value.pen.strokeStyle=newValue
})
</script>   

<style lang="less" scoped>

input{
    border: solid 2px gray;
}
</style>