<template>
    <div>
        <span @click="cleanCanvas">
            清空
        </span>
        <span>
            回退
        </span>
        <a @click="exportAsPng">
            导出
        </a>
        <div>
            <button>上传图片</button>
            <input type="file" @change="importPng" accept="image/*">
        </div>
        <img :src="ImageSrc" alt="没有图片哦" style="width:60px;height: 60px;" id="img">
    </div>
</template> 

<script lang="ts" setup>
import ShapeMap from "@/utils/Canvas/ShapeMap";
import Canvas from "@/utils/Canvas/canvas";
import {inject, nextTick, onMounted, reactive, ref} from "vue"
let canvas =ref<Canvas>()
onMounted( ()=>{
    
const canvasInjetct =inject("canvas__") as any
    canvas=canvasInjetct
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


/**
 * 
 * @param e 
 */
const ImageSrc=ref("")
const importPng=(e: any)=>{
    const  file = e.target.files[0] 
    ImageSrc.value = URL.createObjectURL(file)
    console.log(e.target.files);

    (canvas.value as Canvas).data.push({
        type:"img",
        BeforePosition:[100,100],
        AfterPosition:[300,300],
        pen:null,
        file
    })
    ShapeMap.get("img")?.draw(canvas.value as Canvas,file)
}
</script>
<script lang="ts">
import { defineComponent } from 'vue';
export default defineComponent({
    name:"operation"
})
</script>   

<style lang="less" scoped>
button {
    position: absolute;
}

input {
    opacity: 0;
    width: 60px;
    height: 20px;
}
</style>