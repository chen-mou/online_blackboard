import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import { createPinia } from "pinia";
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

import '@/assets/global.css'
import 'element-plus/dist/index.css'

const app = createApp(App).use(router);

app.use(createPinia())
app.use(ElementPlus)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.mount('#app')