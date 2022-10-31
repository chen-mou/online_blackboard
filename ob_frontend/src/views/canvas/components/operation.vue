<template>
    <div>
        <span @click="cleanCanvas">
            清空
        </span>
    </div>
</template>

<script lang="ts" setup>
import Canvas from "@/utils/Canvas/canvas";
import {inject, nextTick, onMounted, reactive, ref} from "vue"
let canvas =ref()
onMounted( ()=>{
    
const canvasInjetct =inject("canvas__") as any
canvas=canvasInjetct
    console.log(canvas.value)
})



const cleanCanvas=()=>{
    if(canvas.value){
        canvas.value.data=[]
        canvas.value?.context.clearRect(0,0,1600,1600)
    }else{
        throw Error("wait ................")
    }
    }






</script>
<script lang="ts">
import { defineComponent } from 'vue';
export default defineComponent({
    name:"operation"
})
</script>