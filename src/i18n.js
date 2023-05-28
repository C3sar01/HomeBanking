 import Vue from 'vue'
 import VueI18n from 'vue-i18n'

 Vue.use(VueI18n)

// Ready translated locale messages
const messages = {
  en: {
    message: {
      hello: 'hello world'
    }
  },
  es: {
    message: {
      hello: 'Hola'
    }
  }
}

// Create VueI18n instance with options
const i18n = new VueI18n({
  locale: 'en', // set locale
  fallbackLocale: 'es',
  messages, // set locale messages
})

export default i18n;

// Create a Vue instance with `i18n` option
new Vue({ i18n }).$mount('#app')

// Now the app has started!