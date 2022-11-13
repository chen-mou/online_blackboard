<template>
  <div class="operations">
    <div @click="cleanCanvas">
      <svg t="1668312520242" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
        p-id="9592" width="35" height="40">
        <path
          d="M607.897867 768.043004c-17.717453 0-31.994625-14.277171-31.994625-31.994625L575.903242 383.935495c0-17.717453 14.277171-31.994625 31.994625-31.994625s31.994625 14.277171 31.994625 31.994625l0 351.94087C639.892491 753.593818 625.61532 768.043004 607.897867 768.043004z"
          p-id="9593" fill="#319ecc"></path>
        <path
          d="M415.930119 768.043004c-17.717453 0-31.994625-14.277171-31.994625-31.994625L383.935495 383.935495c0-17.717453 14.277171-31.994625 31.994625-31.994625 17.717453 0 31.994625 14.277171 31.994625 31.994625l0 351.94087C447.924744 753.593818 433.647573 768.043004 415.930119 768.043004z"
          p-id="9594" fill="#319ecc"></path>
        <path
          d="M928.016126 223.962372l-159.973123 0L768.043004 159.973123c0-52.980346-42.659499-95.983874-95.295817-95.983874L351.94087 63.989249c-52.980346 0-95.983874 43.003528-95.983874 95.983874l0 63.989249-159.973123 0c-17.717453 0-31.994625 14.277171-31.994625 31.994625s14.277171 31.994625 31.994625 31.994625l832.032253 0c17.717453 0 31.994625-14.277171 31.994625-31.994625S945.73358 223.962372 928.016126 223.962372zM319.946246 159.973123c0-17.545439 14.449185-31.994625 31.994625-31.994625l320.806316 0c17.545439 0 31.306568 14.105157 31.306568 31.994625l0 63.989249L319.946246 223.962372 319.946246 159.973123 319.946246 159.973123z"
          p-id="9595" fill="#319ecc"></path>
        <path
          d="M736.048379 960.010751 288.123635 960.010751c-52.980346 0-95.983874-43.003528-95.983874-95.983874L192.139761 383.591466c0-17.717453 14.277171-31.994625 31.994625-31.994625s31.994625 14.277171 31.994625 31.994625l0 480.435411c0 17.717453 14.449185 31.994625 31.994625 31.994625l448.096758 0c17.717453 0 31.994625-14.277171 31.994625-31.994625L768.215018 384.795565c0-17.717453 14.277171-31.994625 31.994625-31.994625s31.994625 14.277171 31.994625 31.994625l0 479.231312C832.032253 916.835209 789.028725 960.010751 736.048379 960.010751z"
          p-id="9596" fill="#319ecc"></path>
      </svg>
      <span>清空</span>
    </div>
    <div @click="undo">
      <svg t="1668312585157" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
        p-id="10567" width="35" height="40">
        <path
          d="M396.544 135.968L73.28 459.2a44.8 44.8 0 0 0 0 63.36l323.264 323.264a35.2 35.2 0 0 0 24.896 10.304l3.84-0.192a35.2 35.2 0 0 0 31.36-35.008l-0.032-189.024 10.656 0.416c146.08 7.2 278.304 80.256 359.68 196.032l47.36 67.456 10.56-81.792c2.272-17.856 3.424-35.936 3.424-54.144l-0.096-9.792c-5.216-227.968-191.84-412.096-424.672-422.144l-6.88-0.224V160.832a35.2 35.2 0 0 0-60.096-24.864z m-3.904 94.368v163.04l33.408-1.44c5.76-0.256 11.584-0.384 17.408-0.384l9.696 0.096c189.888 4.608 345.024 143.552 368.16 321.92l1.152 10.304-2.496-2.56c-97.664-96.768-232.192-153.536-376.512-153.536-6.688 0-13.344 0.096-19.968 0.32l-30.848 1.152v182.176L132.096 490.88l260.544-260.544z"
          fill="#319ecc" p-id="10568"></path>
      </svg>
      <span>撤销</span>
    </div>
    <div @click="redo">
      <svg t="1668312644583" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
        p-id="11598" width="35" height="40">
        <path
          d="M512 371.2V261.973333a25.6 25.6 0 0 1 40.106667-20.906666L819.2 426.666667a25.6 25.6 0 0 1 0 42.666666l-267.093333 186.453334a25.6 25.6 0 0 1-40.106667-20.906667V567.466667a25.173333 25.173333 0 0 0-26.88-25.6 341.333333 341.333333 0 0 0-301.653333 263.68 336.64 336.64 0 0 1 304.64-409.173334 25.173333 25.173333 0 0 0 23.893333-25.173333z"
          fill="#319ecc" p-id="11599"></path>
      </svg>
      <span>恢复</span>
    </div>
    <div @click="exportAsPng">
      <svg t="1668312767720" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
        p-id="12603" width="35" height="40">
        <path
          d="M452.923077 315.076923v315.076923h118.153846V315.076923h65.831385L512 190.168615 387.091692 315.076923z"
          fill="#319ecc" p-id="12604"></path>
        <path
          d="M157.538462 866.461538h708.923076V512h78.769231v433.230769H78.769231V512h78.769231v354.461538z m492.307692-472.615384v315.076923H374.153846V393.846154h-177.230769L512 78.769231l315.076923 315.076923h-177.230769z m-196.923077-78.769231v315.076923h118.153846V315.076923h65.831385L512 190.168615 387.091692 315.076923H452.923077z m-78.769231 433.230769h275.692308v78.769231H374.153846v-78.769231z"
          fill="#319ecc" p-id="12605"></path>
      </svg>
      <span>导出</span>
    </div>
    <div class="load-img">
      <svg t="1668312794054" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
        p-id="13602" width="35" height="40">
        <path d="M328 576h152V128h64v448h152L512 768 328 576z m568-64h-64v320H192V512h-64v384h768V512z" p-id="13603"
          fill="#319ecc"></path>
      </svg>
      <span>导入</span>
      <input type="file" @change="importPng" accept="image/*">
    </div>
    <div>
      <svg t="1668312834499" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
        p-id="14924" width="35" height="40">
        <path
          d="M554.3 850.7L981 424c12.5-12.5 12.5-32.8 0-45.3L636.7 34.6c-12.5-12.5-32.8-12.5-45.3 0L90.5 535.5c-50 50-50 131 0 181l272.1 270.3c24 23.8 56.4 37.2 90.2 37.2H864c17.7 0 32-14.3 32-32s-14.3-32-32-32H599.5c-57 0-85.6-68.9-45.2-109.3z m71.1-736.9l276.4 276.4c6.2 6.2 6.2 16.4 0 22.6L607.9 706.6l-299-299 293.8-293.9c6.3-6.2 16.4-6.2 22.7 0.1zM135.8 580.7l127.9-127.9 299 299-127.9 127.9c-25 25-65.5 25-90.5 0L135.8 671.3c-25-25-25-65.6 0-90.6z"
          p-id="14925" fill="#319ecc"></path>
      </svg>
      <span>橡皮</span>
    </div>
  </div>
</template>

<script lang="ts" setup>
import Canvas from "@/utils/Canvas/canvas";
import { inject, nextTick, onMounted, reactive, ref } from "vue"
import { bytesToInt32, int32ToBytes } from "@/utils/convert";
import { ElMessage } from "element-plus";
import { useCanvasStore } from "@/store/canvas";

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
    canvas.value.layers.data = []
    // canvas.value?.drawData()
    canvas.value.layers.drawData()
    canvas.value.context.clearRect(0,0,1600,1600)
  }
}
/**
 * 导出图片
 */
const exportAsPng = () => {
  const el = document.createElement('a');
  // 设置 href 为图片经过 base64 编码后的字符串，默认为 png 格式
  // 拿到 base64 部分并转成二进制
  let bytes = atob((canvas.value as Canvas).layers.canvas.toDataURL().split('base64,')[1])
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
      canvas.value?.layers.data.push(...data)
      console.log(data)
      canvas.value?.layers.drawData()
      console.log('ok')
    } catch (e) {
      console.log('图片格式出错！')
      ElMessage.error({
        message: '图片格式出错！',
        grouping: true,
      })
    }
  }
  b.readAsArrayBuffer(file)
}
/**
 * 实现回退功能
 */
const canvasStore = useCanvasStore()
const undo = () => {
  canvasStore.undo()
}

const redo = () => {
  canvasStore.redo()
}

/**
 * 橡皮
 */
const changeDrawClass=()=>{
    canvas.value!.pen.strokeStyle="white"
}
</script>

<style lang="less" scoped>
.el-icon {
  font-size: 30px;
  display: block;
}

input {
  opacity: 0;
  width: 40px;
  height: 38px;
}

.operations {
  display: flex;
}

.operations>* {
  flex: fit-content;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.operations>*:hover {
  cursor: pointer;
  background-color: #dcefff;
}

.load-img {
  position: relative;
}

.load-img>input[type='file'] {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
}
</style>