<template>
    <div class="pen">
        <div>
            <el-icon>
                <MagicStick />
            </el-icon>
            <input type="color" v-model="penColor">
            <span>画笔颜色</span>
        </div>
        <div>
            <el-icon>
                <StarFilled />
            </el-icon>
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
input:hover{
    cursor: pointer;
}
.el-icon {
    font-size: 30px;
    display: block;
}
</style>