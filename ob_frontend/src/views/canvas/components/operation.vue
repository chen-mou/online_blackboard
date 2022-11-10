<template>
    <div style="display: grid;   grid-template-columns: repeat(3, 50px);">
        <div>
            <el-icon @click="cleanCanvas">
                <DeleteFilled />
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
                <input type="file" @change="importPng" accept="image/*">
            </div>
            <span>
                导入
            </span>
        </div>
        <div>
            <svg t="1667916145025" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
                p-id="3130" xmlns:xlink="http://www.w3.org/1999/xlink" width="40" height="35">
                <path
                    d="M898.56 432.64c10.24-10.24 10.24-25.6 0-35.84l-238.08-230.4c-10.24-10.24-25.6-10.24-35.84 0L125.44 680.96c-10.24 10.24-10.24 25.6 0 35.84l99.84 97.28 53.76 51.2h576c15.36 0 25.6-10.24 25.6-25.6s-10.24-25.6-25.6-25.6H529.92l368.64-381.44zM179.2 698.88l248.32-258.56 202.24 194.56-171.52 179.2h-161.28L179.2 698.88z"
                    fill="#515151" p-id="3131"></path>
            </svg>
            <span>删除</span>
        </div>
    </div>
</template>

<script lang="ts" setup>
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
    // canvas.value?.drawData()
    canvas.value.layers.drawData()
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
//   console.log('原数据', dataLen, '转换后', bytesToInt32(int32ToBytes(dataLen)))

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
    // 拿到二进制流并把 json 的部分截取出来
    let arr = new Uint8Array(e1.target?.result as unknown as Array<number>)
    arr = arr.slice(-bytesToInt32(String.fromCharCode(...arr.slice(-4))) - 4, -4)
    let bytes = ''
    for (let i of arr) {
      bytes += String.fromCharCode(i)
    }
    try {
      // 转成对象
      let data = JSON.parse(bytes)
      canvas.value?.data.push(...data)
      console.log(data)
      canvas.value?.drawData()
      console.log('ok')
    } catch (e) {
      console.log('图片格式出错！')
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
const turnBack = () => {
  canvas.value!.layers.data = canvas.value!.layers.data.slice(0, canvas.value!.data.length - 1)
  canvas.value?.layers.drawData()
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
    height: 38px;
}
</style>