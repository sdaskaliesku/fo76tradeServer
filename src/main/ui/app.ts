import './css/main.scss';
import {createApp} from 'vue';
// import VueRouter from 'vue-router';
import Main from './vue/Main.vue';
import PrimeVue from 'primevue/config';

// @ts-ignore
// import JsonViewer from 'vue-json-viewer/ssr'
import JsonViewer from 'vue3-json-viewer'


const app = createApp(Main);
app.use(JsonViewer);
app.use(PrimeVue, {ripple: true});

// app.component("BIcon", BIcon);
// app.component("BIconSearch", BIconSearch);
// app.component("BIconCash", BIconCash);
// app.component("BIconX", BIconX);
// app.component("BFormGroup", BFormGroup);
// app.component("BFormCheckboxGroup", BFormCheckboxGroup);
// app.component("BFormFile", BFormFile);
// app.component("BSpinner", BSpinner);
// app.component("BModal", BModal);
// app.component("BToast", BToast);
// app.component("BNavbar", BNavbar);
// app.component("BNavbarNav", BNavbarNav);
// app.component("BCollapse", BCollapse);
// app.component("BNavbarBrand", BNavbarBrand);
// app.component("BNavbarToggle", BNavbarToggle);
// app.component("BNavItem", BNavItem);
// app.component("BNavForm", BNavForm);
// app.component("BButtonGroup", BButtonGroup);
// app.component("BButton", BButton);
// app.component("BDropdown", BDropdown);
// app.component("BFormCheckbox", BFormCheckbox);
// app.component("BDropdownDivider", BDropdownDivider);
// app.component("BDropdownItem", BDropdownItem);
// app.component("BInputGroup", BInputGroup);
// app.component("BInputGroupPrepend", BInputGroupPrepend);
// app.component("BInputGroupAppend", BInputGroupAppend);
// app.component("BFormInput", BFormInput);
// app.component("BBadge", BBadge);
// app.component("BListGroup", BListGroup);
// app.component("BListGroupItem", BListGroupItem);
// app.component("BIconPersonFill", BIconPersonFill);
// app.use(ModalPlugin);
// app.use(ToastPlugin);

app.mount('#app');