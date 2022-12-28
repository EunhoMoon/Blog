import { createApp } from "vue";
import { createPinia } from "pinia";

import App from "./App.vue";
import router from "./router";

import ElemntPlus from 'element-plus';
import 'element-ui/lib/theme-chalk/index.css';

import "bootstrap/dist/css/bootstrap.min.css";

import "./assets/main.css";

const app = createApp(App);

app.use(createPinia());
app.use(router);
app.use(ElemntPlus);

app.mount("#app");
