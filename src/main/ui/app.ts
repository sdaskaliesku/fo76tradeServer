import './css/main.scss';
import Vue from 'vue';
import Main from './vue/Main.vue';
import VueRouter from 'vue-router'
import { BootstrapVue, BIcon, BIconSearch, BIconX } from 'bootstrap-vue'

Vue.use(VueRouter);
Vue.use(BootstrapVue);
Vue.component("BIcon", BIcon);
Vue.component("BIconSearch", BIconSearch);
Vue.component("BIconX", BIconX);


const router = new VueRouter({
  routes: []
});

new Vue({
  el: "#app",
  template: '<Main/>',
  router,
  components: {
    Main
  }
});