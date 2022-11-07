<template>
    <div style="display: grid;   grid-template-columns: repeat(2, 50px);">
        <div>
            <el-icon @click="cleanCanvas">
                <DeleteFilled />
            </el-icon>
            <span>
                清空
            </span>
        </div>
        <div>
            <el-icon>
                <Refresh />
            </el-icon>
            <span>
                回退
            </span>
        </div>
        <div>
            <el-icon @click="exportAsPng">
                <Picture />
            </el-icon>
            <span>
                导出
            </span>
        </div>

        <div>
            <div>
                <el-icon class="upload">
                    <FolderOpened />
                </el-icon>
                <input type="file" @change="importPng" accept="image/*" >
            </div>
            <span>
                上传
            </span>
        </div>
        <img :src="ImageSrc" id="img">
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
.upload {
    position: absolute;
    width: 30px;
}

.el-icon {
    font-size: 30px;
}

input {
    opacity: 0;
    width: 40px;
    height: 35px;
}
</style>