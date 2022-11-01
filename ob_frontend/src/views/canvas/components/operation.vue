<template>
    <div>
        <span @click="cleanCanvas">
            清空
        </span>
        <span>
            回退
        </span>
        <a  @click="exportAsPng">
            导出
        </a>
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

/**
 * 清空Canvas
 */
const cleanCanvas=()=>{
    if(canvas.value){
        canvas.value.data=[]
        canvas.value?.context.clearRect(0,0,1600,1600)
    }
    }
/**
 * 导出图片
 */
const exportAsPng=()=>{
   const el = document.createElement('a');
  // 设置 href 为图片经过 base64 编码后的字符串，默认为 png 格式
  el.href = (canvas.value as Canvas) .canvas.toDataURL()
  el.download = '项目名称';
  
  // 创建一个点击事件并对 a 标签进行触发
  const event = new MouseEvent('click');
  el.dispatchEvent(event);
}



</script>
<script lang="ts">
import { defineComponent } from 'vue';
export default defineComponent({
    name:"operation"
})
</script>