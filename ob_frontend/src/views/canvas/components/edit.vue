<template>
    <div class="edit">
        <div style="text-align: center;">
            <svg t="1668279275914" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
                p-id="2889" width="30" height="30" xmlns:xlink="http://www.w3.org/1999/xlink" style="cursor: pointer; "
                @click="closeEdit">
                <path
                    d="M512 960c-247.039484 0-448-200.960516-448-448S264.960516 64 512 64 960 264.960516 960 512 759.039484 960 512 960zM512 128.287273c-211.584464 0-383.712727 172.128262-383.712727 383.712727 0 211.551781 172.128262 383.712727 383.712727 383.712727 211.551781 0 383.712727-172.159226 383.712727-383.712727C895.712727 300.415536 723.551781 128.287273 512 128.287273z"
                    p-id="2890" fill="#319ecc"></path>
                <path
                    d="M557.05545 513.376159l138.367639-136.864185c12.576374-12.416396 12.672705-32.671738 0.25631-45.248112s-32.704421-12.672705-45.248112-0.25631l-138.560301 137.024163-136.447897-136.864185c-12.512727-12.512727-32.735385-12.576374-45.248112-0.063647-12.512727 12.480043-12.54369 32.735385-0.063647 45.248112l136.255235 136.671523-137.376804 135.904314c-12.576374 12.447359-12.672705 32.671738-0.25631 45.248112 6.271845 6.335493 14.496116 9.504099 22.751351 9.504099 8.12794 0 16.25588-3.103239 22.496761-9.247789l137.567746-136.064292 138.687596 139.136568c6.240882 6.271845 14.432469 9.407768 22.65674 9.407768 8.191587 0 16.352211-3.135923 22.591372-9.34412 12.512727-12.480043 12.54369-32.704421 0.063647-45.248112L557.05545 513.376159z"
                    p-id="2891" fill="#319ecc"></path>
            </svg>
        </div>
        <span style="text-align:  center; "> 选中操作栏</span>
        <div>
            <span>width</span>
            <el-slider v-model="width" :min="1" :max="100" />
        </div>
        <div>
            <span>
                color
            </span>
            <input type="color" v-model="color">
        </div>
        <div @click="deleteShape">
            <span>
                delete
            </span>
            <br>
            <svg t="1668278443711" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
                p-id="1368" width="50" height="50" xmlns:xlink="http://www.w3.org/1999/xlink">
                <path
                    d="M358.925672 596.814688v30.450522c0 17.248849 13.985526 31.233352 31.233352 31.233352 17.248849 0 31.233352-13.985526 31.233352-31.233352v-30.450522c0-17.248849-13.985526-31.233352-31.233352-31.233352-17.248849 0-31.233352 13.985526-31.233352 31.233352zM602.506317 596.814688v30.450522c0 17.248849 13.985526 31.233352 31.233352 31.233352s31.233352-13.985526 31.233351-31.233352v-30.450522c0-17.248849-13.984503-31.233352-31.233351-31.233352s-31.233352 13.985526-31.233352 31.233352zM437.047937 699.686636c-14.650675 9.104355-19.155269 28.360931-10.04989 43.01263 11.015891 17.73185 41.238216 47.740304 84.651982 47.740304 43.195801 0 73.79368-29.780257 85.059258-47.379077 9.216919-14.391778 5.03262-33.338293-9.237385-42.742477-14.270005-9.393951-33.576723-5.409197-43.159985 8.739035-0.12689 0.188288-13.049201 18.915815-32.661888 18.915815-19.028379 0-30.93864-17.274432-31.772634-18.530028-9.175987-14.412244-28.259624-18.788925-42.829458-9.756202zM907.576407 160.082952H699.352015v-26.882254c0-40.145325-32.692586-72.807213-72.878844-72.807213h-229.046626c-40.186258 0-72.878844 32.661887-72.878844 72.807213v26.882254H116.323309c-17.248849 0-31.233352 13.984503-31.233352 31.233352s13.984503 31.233352 31.233352 31.233351h791.253098c17.248849 0 31.233352-13.984503 31.233352-31.233351s-13.985526-31.233352-31.233352-31.233352z m-270.692119 0H387.014404v-26.882254c0-5.607718 4.768607-10.340509 10.411117-10.340509h229.046627c5.64251 0 10.411117 4.732791 10.411117 10.340509v26.882254z"
                    p-id="1369" fill="#3690b6"></path>
                <path
                    d="M824.286446 259.279185c-17.248849 0-31.233352 13.984503-31.233352 31.233352v530.07261c0 40.089044-32.692586 72.705905-72.878844 72.705906H303.725466c-40.186258 0-72.878844-32.616862-72.878844-72.705906v-530.07261c0-17.248849-13.984503-31.233352-31.233352-31.233352s-31.233352 13.984503-31.233352 31.233352v530.07261c0 74.535577 60.71378 135.172609 135.345548 135.172609h416.448784c74.632791 0 135.345548-60.637032 135.345548-135.172609v-530.07261c0-17.248849-13.984503-31.233352-31.233352-31.233352z"
                    p-id="1370" fill="#3690b6"></path>
                <path
                    d="M355.781052 259.279185c-17.248849 0-31.233352 13.984503-31.233351 31.233352v167.494758c0 17.248849 13.985526 31.233352 31.233351 31.233352 17.248849 0 31.233352-13.985526 31.233352-31.233352v-167.494758c0-17.248849-13.984503-31.233352-31.233352-31.233352zM699.352015 458.007295v-167.494758c0-17.248849-13.984503-31.233352-31.233351-31.233352s-31.233352 13.984503-31.233352 31.233352v167.494758c0 17.248849 13.985526 31.233352 31.233352 31.233352s31.233352-13.984503 31.233351-31.233352zM511.949858 489.240647c17.248849 0 31.233352-13.985526 31.233352-31.233352v-167.494758c0-17.248849-13.985526-31.233352-31.233352-31.233352s-31.233352 13.984503-31.233352 31.233352v167.494758c-0.001023 17.248849 13.984503 31.233352 31.233352 31.233352z"
                    p-id="1371" fill="#3690b6"></path>
            </svg>
        </div>
    </div>
</template>

<script lang="ts">
import Canvas from '@/utils/Canvas/canvas';
import CanvasConfig from '@/utils/Canvas/config';
import ShapeMap from '@/utils/Canvas/ShapeMap';
import { defineComponent, inject, onMounted, ref, watch  } from 'vue';
export default defineComponent({
    name:"edit"
})
</script>
<script lang="ts" setup>
let canvas =ref()
const width = ref<number>(1)
const Memberindex=ref<number>(1)
const color = ref<string>(CanvasConfig.PenStrokeStyle)


onMounted( ()=>{
const canvasInjetct =inject("canvas__") as any
    canvas=canvasInjetct
})

/**
 * 初始化
 */

watch(canvas.value,()=>{

width.value=(canvas.value as Canvas).layers.data[Memberindex.value].pen!.linewidth
color.value=(canvas.value as Canvas).layers.data[Memberindex.value].pen!.strokeStyle as string
})
/**
 * 宽度
 */
watch(width,(newValue:number)=>{
    /**
     * 拿到选中并修改
     */
     for(let i= 0 ;i<(canvas.value as Canvas).layers.data.length;i++ ){
        if((canvas.value as Canvas).layers.data[i].id===canvas!.value?.layers.Blacklist[0])
        {
            Memberindex.value=i
            break
        }
    }
   /**
    * 
             * 修改pen后绘入第一图层
             */
            canvas.value!.DrawClass.BeforePosition = canvas.value!.layers.data[Memberindex.value].BeforePosition
            canvas.value!.DrawClass.AfterPosition = canvas.value!.layers.data[Memberindex.value].AfterPosition
            canvas.value!.layers.data[Memberindex.value].pen!.linewidth=newValue
            ShapeMap.get(canvas.value!.layers.data[Memberindex.value].type)?.draw(canvas.value as Canvas)
})
/**
 * 颜色
 */
    watch(color,(newValue:string)=>{
        for(let i= 0 ;i<(canvas.value as Canvas).layers.data.length;i++ ){
        if((canvas.value as Canvas).layers.data[i].id===canvas!.value?.layers.Blacklist[0])
        {
            Memberindex.value=i
            break
        }
    }
    console.log("改变")
    /**
     * 拿到选中并修改
     */

            canvas.value!.DrawClass.BeforePosition = canvas.value!.layers.data[Memberindex.value].BeforePosition
            canvas.value!.DrawClass.AfterPosition = canvas.value!.layers.data[Memberindex.value].AfterPosition
            canvas.value!.layers.data[Memberindex.value].pen!.strokeStyle   =newValue
            ShapeMap.get(canvas.value!.layers.data[Memberindex.value].type)?.draw(canvas.value as Canvas)

})
/**
 * 删除
 */
const deleteShape=()=>{
    for(let i= 0 ;i<(canvas.value as Canvas).layers.data.length;i++ ){
        if((canvas.value as Canvas).layers.data[i].id===canvas!.value?.layers.Blacklist[0])
        {
            console.log("pkoo")
            /**
             * 修改pen后绘入第一图层      [i].id
             * splice(useCanvasStore().canvas.layers.data.findIndex(e => e.id === data.data), 1)
             */
            ;(canvas.value as Canvas).layers.data.splice(i,1)
            canvas.value?.layers.drawData()
            canvas.value?.drawData()
            //@ts-ignore
            canvas.value.layers.Blacklist=[]
            //@ts-ignore
            canvas.value.state=false
            break
        }
    }
}
/**
 * 关闭编辑页面
 */
const closeEdit= ()=>{
    canvas.value!.state=false
}
</script>

<style lang="less" scoped>
.edit {
    position: absolute;
    height: 300px;
    width: 200px;
    border-radius: 12px;
    left: 50px;
    top: 150px;
    border: 1px solid #eef1ff;
    background-color: #eef1ff;
    box-shadow: 10px 10px 20px 10px #c3cae7;
    z-index: 100;
    padding: 10px;
    display: flex;
    flex-direction: column;
    justify-content: space-evenly;


    input {
        display: block;
    }
}
</style>