import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import { createPinia } from "pinia";

import '@/assets/global.css'
import 'element-plus/dist/index.css'

const app = createApp(App).use(router);

app.use(createPinia())
app.use(ElementPlus)

app.mount('#app')