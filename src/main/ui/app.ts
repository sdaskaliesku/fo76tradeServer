import './css/main.scss';
import Vue from 'vue';
import VueRouter from 'vue-router';
import Main from './vue/Main.vue';
import {
  BBadge,
  BButton,
  BButtonGroup,
  BCollapse,
  BDropdown,
  BDropdownDivider,
  BDropdownItem,
  BFormCheckbox,
  BFormCheckboxGroup,
  BFormFile,
  BFormGroup,
  BFormInput,
  BIcon,
  BIconCash,
  BIconPersonFill,
  BIconSearch,
  BIconX,
  BInputGroup,
  BInputGroupAppend,
  BInputGroupPrepend,
  BListGroup,
  BListGroupItem,
  BModal,
  BNavbar,
  BNavbarBrand,
  BNavbarNav,
  BNavbarToggle,
  BNavForm,
  BNavItem,
  BSpinner,
  BToast,
  ModalPlugin,
  ToastPlugin
} from 'bootstrap-vue'

Vue.component("BIcon", BIcon);
Vue.component("BIconSearch", BIconSearch);
Vue.component("BIconCash", BIconCash);
Vue.component("BIconX", BIconX);
Vue.component("BFormGroup", BFormGroup);
Vue.component("BFormCheckboxGroup", BFormCheckboxGroup);
Vue.component("BFormFile", BFormFile);
Vue.component("BSpinner", BSpinner);
Vue.component("BModal", BModal);
Vue.component("BToast", BToast);
Vue.component("BNavbar", BNavbar);
Vue.component("BNavbarNav", BNavbarNav);
Vue.component("BCollapse", BCollapse);
Vue.component("BNavbarBrand", BNavbarBrand);
Vue.component("BNavbarToggle", BNavbarToggle);
Vue.component("BNavItem", BNavItem);
Vue.component("BNavForm", BNavForm);
Vue.component("BButtonGroup", BButtonGroup);
Vue.component("BButton", BButton);
Vue.component("BDropdown", BDropdown);
Vue.component("BFormCheckbox", BFormCheckbox);
Vue.component("BDropdownDivider", BDropdownDivider);
Vue.component("BDropdownItem", BDropdownItem);
Vue.component("BInputGroup", BInputGroup);
Vue.component("BInputGroupPrepend", BInputGroupPrepend);
Vue.component("BInputGroupAppend", BInputGroupAppend);
Vue.component("BFormInput", BFormInput);
Vue.component("BBadge", BBadge);
Vue.component("BListGroup", BListGroup);
Vue.component("BListGroupItem", BListGroupItem);
Vue.component("BIconPersonFill", BIconPersonFill);
Vue.use(ModalPlugin);
Vue.use(ToastPlugin);
Vue.use(VueRouter);

const router = new VueRouter();

new Vue({
  el: "#app",
  template: '<Main/>',
  router,
  components: {
    Main
  }
});