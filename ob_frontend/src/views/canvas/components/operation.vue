<template>
  <div style="display: grid;   grid-template-columns: repeat(2, 50px);">
    <div>
      <el-icon @click="cleanCanvas">
        <DeleteFilled/>
      </el-icon>
      <span>
                清空
            </span>
        </div>
        <div>
            <el-icon @click="turnBack">
                <Refresh />
            </el-icon>
            <span>
                回退
            </span>
    </div>
    <div>
      <el-icon @click="exportAsPng">
        <Picture/>
      </el-icon>
      <span>
                导出
            </span>
    </div>

    <div>
      <div>
        <el-icon class="upload">
          <FolderOpened/>
        </el-icon>
        <input type="file" @change="importPng" accept="image/*">
      </div>
      <span>
                上传
            </span>
        </div>
    </div>
</template> 

<script lang="ts" setup>
import ShapeMap from "@/utils/Canvas/ShapeMap";
import Canvas from "@/utils/Canvas/canvas";
import { inject, nextTick, onMounted, reactive, ref } from "vue"
import { bytesToInt32, int32ToBytes } from "@/utils/convert";
import { ElMessage } from "element-plus";

let canvas = ref<Canvas>()
onMounted(() => {

  const canvasInjetct = inject("canvas__") as any
  canvas = canvasInjetct
})

/**
 * 清空Canvas
 */
const cleanCanvas = () => {
  if (canvas.value) {
    canvas.value.data = []
    canvas.value?.drawData()
  }
}
/**
 * 导出图片
 */
const exportAsPng = () => {
  const el = document.createElement('a');
  // 设置 href 为图片经过 base64 编码后的字符串，默认为 png 格式
  // 拿到 base64 部分并转成二进制
  let bytes = atob((canvas.value as Canvas).canvas.toDataURL().split('base64,')[1])
  let data = JSON.stringify(canvas.value?.data)
  // 把数据长度（32位int）转成二进制（byte*4）
  let dataLen = int32ToBytes(data.length)
  // console.log('原数据', dataLen, '转换后', bytesToInt32(int32ToBytes(dataLen)))

  // 把数据和图片的二进制流合并，转回 base64
  el.href = 'data:image/png;base64,' + btoa(bytes + data + dataLen)
  el.download = '项目名称';

  // 创建一个点击事件并对 a 标签进行触发
  const event = new MouseEvent('click');
  el.dispatchEvent(event);
}


/**
 *
 * @param e
 */
const ImageSrc = ref("")
const importPng = (e: any) => {
  const file = e.target.files[0]
  let b = new FileReader()
  b.onload = (e1: ProgressEvent<FileReader>) => {
    // 拿到二进制流
    let bytes = String.fromCharCode.apply(null, new Uint8Array(e1.target?.result as unknown as Array<number>) as unknown as Array<number>)
    try {
      // 把 json 的部分截取出来转成对象
      let data = JSON.parse(bytes.slice(-bytesToInt32(bytes.slice(-4)) - 4, -4))
      canvas.value?.data.push(...data)
      canvas.value?.drawData()
    } catch (e) {
      ElMessage.error({
        message: '图片格式出错！'
      })
    }
  }
  b.readAsArrayBuffer(file)
}
/**
 * 实现回退功能
 */
const turnBack= ()=>{
    canvas.value!.data=canvas.value!.data.slice(0,canvas.value!.data.length-1)
    canvas.value?.drawData()
}
</script>
<script lang="ts">
import { defineComponent } from 'vue';

export default defineComponent({
  name: "operation"
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