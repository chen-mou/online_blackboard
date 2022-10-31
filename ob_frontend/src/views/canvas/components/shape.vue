<template>
    <div class="shape" @click="HandleSelect">
        <!-- 遍历icon及形状 -->
        <div v-for="item in shapeList" :key="item.name">
            <Img :src="item.icon" :name="item.name"></Img>
        </div>
    </div>
</template>

<script lang="ts" >
import  {defineComponent} from 'vue'
export default defineComponent({
    name:"shape"
})
</script>
<script lang="ts" setup>
import { ref,reactive } from 'vue';
type Shape ={
    name:string
    icon:string
    cb:Function
}
/**
 * 
 */
const shapeList = reactive<Array<Shape>>([
    {
        icon:"/canvsShapeImg/rect.png",
        name:"矩形",
        cb:()=>{
            console.log("改变绘制样式")
        }
    },
])
const shapeMap =new Map<string,Function>()
    for(let shape  of shapeList){
        shapeMap.set(shape.name,shape.cb)
    }
/**
 * 处理选中图形的事件
 */
const HandleSelect=(e:MouseEvent)=>{
    /**
     * 事件代理触发的回调
     */
    const cb =shapeMap.get((e.target as any).name as string) 
    cb && cb()
}

</script>

<style lang="less" scoped>
.shape {
    width: 40vh;
    border-left: 1px solid rgb(211, 201, 201);
    border-right: 1px solid rgb(211, 201, 201);
}
</style>