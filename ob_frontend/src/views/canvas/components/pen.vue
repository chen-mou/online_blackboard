<template>
    <div>
        <div>
            <div class="grid" >
                <div style="height: 20px ;width: 20px;border: 1px black solid;"  v-for="item in PenList" :key="item.icon" >
                    <div :style="{backgroundColor:item.color }"
                        style="height: 18px;width:18px;margin: 1px;" >
                    </div>
                </div>
                <el-color-picker v-model="penColor  " />
            </div>

            <hr>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent, inject, onMounted, reactive, ref, watch } from 'vue';
export default defineComponent({
    name:"pen"
}) 
</script>

<script lang="ts" setup>
const PenList=reactive<Array<any>>([
    {
        icon:'a',
        color:"#000000"
    }
])
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
input {
    border: solid 2px gray;
}
.grid{
    height: 15vh;
    width: 200px;
    border: 1px black solid;
}
</style>