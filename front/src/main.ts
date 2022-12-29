import { createApp } from "vue";
import { createPinia } from "pinia";

import App from "./App.vue";
import router from "./router";

import "normalize.css";

import ElemntPlus from 'element-plus';
import 'element-plus/dist/index.css'

import "bootstrap/dist/css/bootstrap.min.css";

const app = createApp(App);

app.use(createPinia());
app.use(router);
app.use(ElemntPlus);

app.mount("#app");
