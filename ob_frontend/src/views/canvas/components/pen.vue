<template>
    <div class="pen">
        <div>
            <svg t="1668312985270" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
                p-id="16890" width="35" height="40">
                <path d="M822.4 432.7m-69.8 0a69.8 69.8 0 1 0 139.6 0 69.8 69.8 0 1 0-139.6 0Z" fill="#f4ea2a"
                    p-id="16891"></path>
                <path d="M647.8 231.8m-69.8 0a69.8 69.8 0 1 0 139.6 0 69.8 69.8 0 1 0-139.6 0Z" fill="#f4ea2a"
                    p-id="16892"></path>
                <path d="M372.3 262.7m-69.8 0a69.8 69.8 0 1 0 139.6 0 69.8 69.8 0 1 0-139.6 0Z" fill="#f4ea2a"
                    p-id="16893"></path>
                <path d="M221 498.4m-69.8 0a69.8 69.8 0 1 0 139.6 0 69.8 69.8 0 1 0-139.6 0Z" fill="#f4ea2a"
                    p-id="16894"></path>
                <path
                    d="M502.4 1020.7h-1.2C155.8 1020.7 0 722.4 0 510.5 0 299.9 158.8 3.3 512 3.3c357.4 0 512 302.1 512 456.2 0 163.9-126.4 256.3-201 285.1-58.1 22.4-141.8 22.2-208.1 19.8l-10.4-0.3c1.3 0.5-13.1 8.3-13.1 34.9 0 6.2 0.8 8.3 0.8 8.4 1 1.4 2.5 2.7 3.9 4.1 16.7 17.6 33 39.2 33 92.6 0 32.3-11.1 60.2-32 80.8-34.9 34-84.7 35.8-94.7 35.8zM512 89.6c-321.6 0-425.7 283.6-425.7 420.9 0 138.3 101.6 423.9 415.7 423.9 18.8 0 40.7-5.4 40.7-30.4 0-23.4-3.3-26.9-9.3-33.2-13.7-14.5-28.4-33.1-28.4-71.9 0-79.1 57.4-121.3 98.9-121.3 2.8 0 7.7 0.2 14.3 0.4 30.9 1.3 124.8 4.8 173.7-14 50.4-19.4 145.7-87.1 145.7-204.5 0.1-94.8-104.7-369.9-425.6-369.9z"
                    fill="#f4ea2a" p-id="16895"></path>
            </svg>
            <input type="color" v-model="penColor">
            <span>画笔颜色</span>
        </div>
        <div>
            <svg t="1668313035175" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
                p-id="17908" width="35" height="40">
                <path
                    d="M738.272 285.728m-67.882251-67.882251a96 96 0 1 0 135.764502 135.764502 96 96 0 1 0-135.764502-135.764502Z"
                    fill="#f4ea2a" p-id="17909"></path>
                <path d="M832 512m0-96a96 96 0 1 0 0 192 96 96 0 1 0 0-192Z" fill="#f4ea2a" p-id="17910"></path>
                <path d="M192 512m0-96a96 96 0 1 0 0 192 96 96 0 1 0 0-192Z" fill="#f4ea2a" p-id="17911"></path>
                <path
                    d="M285.728 285.728m67.882251-67.882251a96 96 0 1 0-135.764502 135.764502 96 96 0 1 0 135.764502-135.764502Z"
                    fill="#f4ea2a" p-id="17912"></path>
                <path d="M512 192m-96 0a96 96 0 1 0 192 0 96 96 0 1 0-192 0Z" fill="#f4ea2a" p-id="17913"></path>
                <path d="M368 656v128h64v-128h-64z m224 128v128h64v-128h-64z" fill="#f4ea2a" p-id="17914"></path>
                <path
                    d="M576 832v-64h96v64h256v32h-256v64h-96v-64H96v-32h480z m32-32v96h32v-96h-32z m-160-96h480v32H448v64H352v-64H96v-32h256v-64h96v64z m-32-32H384v96h32v-96z"
                    fill="#f4ea2a" p-id="17915"></path>
                <path d="M352 640h96v160H352v-160z m32 32v96h32v-96H384zM576 768h96v160h-96v-160z m32 32v96h32v-96h-32z"
                    fill="#f4ea2a" p-id="17916"></path>
            </svg>
            <input type="color" v-model="penBorderColor">
            <span>填充颜色</span>
        </div>
        <div style="padding-left: 20px;padding-right: 20px">
            <el-slider v-model="penSize" height="60px" :min="1" :max="100" />
            <span>画笔大小</span>
        </div>
    </div>
</template>

<script lang="ts">
import { Pen } from '@/utils/Canvas/type/CanvasType';
import { defineComponent, inject, onMounted, reactive, ref, watch } from 'vue';
export default defineComponent({
    name:"pen"
}) 
</script>

<script lang="ts" setup>
let canvas =ref()
const penColor =ref<string>("a42d2d")
const penSize =ref<number>(2)
const penBorderColor= ref<string>('#9f9')
onMounted( ()=>{
const canvasInjetct =inject("canvas__") as any
    canvas=canvasInjetct
})

watch(penColor,(newValue)=>{
    canvas.value.context.strokeStyle=newValue
    console.log(canvas.value.context.strokeStyle)
    canvas.value.pen.strokeStyle=newValue
})
watch(penSize,(newValue)=>{
    canvas.value.context.lineWidth =newValue
    console.log(canvas.value.context.lineWidth );
    (canvas.value.pen as Pen).linewidth=newValue
})
watch(penBorderColor,(newValue)=>{
    canvas.value.context.fillStyle =newValue
    console.log(canvas.value.context.lineWidth );
    (canvas.value.pen as Pen).fillStyle=newValue
})
</script>   

<style lang="less" scoped>
input {
    border: solid 2px gray;
}

::v-deep .el-color-picker {
    position: absolute;
    left: 20px;
}

.pen {
    display: flex;
}

.pen>* {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

.pen>*:hover {
    cursor: pointer;
    background-color: #dcefff;
}

input {
    position: absolute;
    opacity: 0;
    height: 60px;
    width: 60px;
}

input:hover {
    cursor: pointer;
}

.el-icon {
    font-size: 30px;
    display: block;
}
</style>