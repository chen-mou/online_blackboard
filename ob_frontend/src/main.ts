import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import { createPinia } from "pinia";

const app = createApp(App).use(router);

app.use(createPinia())
app.use(ElementPlus)

app.mount('#app')