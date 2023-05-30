var app = new Vue({
  el: "#app",
  data: {
    accountInfo: {},
    errorMsg: null,
    chatGPTData: null,
    loading: false,
  },
  methods: {
    getData: function () {
      const urlParams = new URLSearchParams(window.location.search);
      const id = urlParams.get('id');
      axios
        .get(`/api/accounts/${id}`)
        .then((response) => {
          //get client ifo
          this.accountInfo = response.data;
          this.accountInfo.transactions.sort((a, b) => parseInt(b.id - a.id));
        })
        .catch((error) => {
          // handle error
          this.errorMsg = "Error getting data";
          this.errorToats.show();
        });
    },
    downloadPDF: function () {
      const urlParams = new URLSearchParams(window.location.search);
      var accountNumber = this.accountInfo.number;
      var url = `/api/pdf?accountNumber=${accountNumber};`;

      axios
        .get(url, { responseType: 'blob' })
        .then(response => {
          const url = window.URL.createObjectURL(new Blob([response.data]));
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', 'transactions.pdf');
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);
        })
        .catch(error => {
          console.error(error);
        });
    },
    sendPdf: function () {
      const urlParams = new URLSearchParams(window.location.search);
      var accountNumber = this.accountInfo.number;
      var url = `/send-email?accountNumber=${accountNumber}`;

      axios
        .get(url)
        .then(response => {
          console.log(response.data);
          this.showToast(response.data);
        })
        .catch(error => {
          console.error(error);
        });
    },
    formatDate: function (date) {
      return new Date(date).toLocaleDateString('en-gb');
    },
    signOut: function () {
      axios
        .post('/api/logout')
        .then(response => (window.location.href = "/web/html/index.html"))
        .catch(() => {
          this.errorMsg = "Sign out failed";
          this.errorToats.show();
        });
    },
    openModal() {
      this.loading = true;
      this.getChatGPTData();
    },
    getChatGPTData() {
      axios
        .post('/chat', { accountNumber: this.accountInfo.number })
        .then(response => {
          console.log(this.accountInfo.number);
          this.chatGPTData = response.data.choices[0].message.content;
          $('#chatModal').modal('show');
          this.loading = false;
          this.showChatText(this.chatGPTData);
        })
        .catch(error => {
          console.error(error);
        });
    },
    showChatText(text) {
      const chatTextElement = document.getElementById('chatText');
      chatTextElement.innerHTML = '';

      let index = 0;
      const interval = setInterval(() => {
        if (index < text.length) {
          chatTextElement.innerHTML += text.charAt(index);
          index++;
        } else {
          clearInterval(interval);
        }
      }, 8);
    },
    showToast: function (message) {
      var toastElement = document.getElementById('success-toast');
      var toast = new bootstrap.Toast(toastElement);
      var toastBody = toastElement.querySelector('.toast-body');
      toastBody.textContent = message;
      toast.show();
    },
  },
  mounted: function () {
    this.getData();
  },
});
