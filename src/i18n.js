import Vue from 'vue';
import VueI18n from 'vue-i18n';
import messages  from './messages_es';

 Vue.use(VueI18n)

// Create VueI18n instance with options
const i18n = new VueI18n({
  locale: 'es', // set locale
  //fallbackLocale: 'en',
  messages: {
  es: messages,
  }, // set locale messages
})

export default i18n;

// Create a Vue instance with `i18n` option
new Vue({ i18n
el: '#app', // Elemento HTML donde se montará la aplicación
  components: { App }, // Componente raíz de la aplicación
  template: '<App/>',
}).$mount('#app')

// Now the app has started!