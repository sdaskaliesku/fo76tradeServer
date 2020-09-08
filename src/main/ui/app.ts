import './css/main.scss';
import Vue from 'vue';
import Main from './vue/Main.vue';
import VueRouter from 'vue-router'
import {BootstrapVue, IconsPlugin} from 'bootstrap-vue'
import '@fortawesome/fontawesome-free/js/all.js';

Vue.use(VueRouter);
Vue.use(BootstrapVue);
Vue.use(IconsPlugin);


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