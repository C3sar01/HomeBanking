var app = new Vue({
  el: "#app",
  data: {
    accountInfo: {},
    errorToats: null,
    errorMsg: null,
  },
  methods: {
    getData: function() {
      const urlParams = new URLSearchParams(window.location.search);
      const id = urlParams.get('id');
      axios.get(`/api/accounts/${id}`)
        .then((response) => {
          // Obtener información del cliente
          this.accountInfo = response.data;
          this.accountInfo.transactions.sort((a, b) => parseInt(b.id - a.id));
        })
        .catch((error) => {
          // Manejar el error
          this.errorMsg = "Error al obtener los datos";
          this.errorToats.show();
        });
    },
    downloadPDF: function() {
      const urlParams = new URLSearchParams(window.location.search);
       var accountNumber = this.accountInfo.number;
       var url = `http://localhost:8080/api/pdf?accountNumber=${accountNumber}`;

      axios.get(url, { responseType: 'blob' })
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

      pDF: function (){
           {
              fetch('/send')
                  .then(response => response.text())
                  .then(result => {
                      alert(result); // Muestra el resultado del envío del correo
                  })
                  .catch(error => {
                      console.error(error);
                  });
          }
      },

    formatDate: function(date) {
      return new Date(date).toLocaleDateString('en-gb');
    },
    signOut: function() {
      axios.post('/api/logout')
        .then(response => window.location.href = "/web/html/index.html")
        .catch(() => {
          this.errorMsg = "Error al cerrar sesión";
          this.errorToats.show();
        });
    },
  },
  mounted: function() {
    this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
    this.getData();
  }
});


