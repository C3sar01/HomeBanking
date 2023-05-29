new Vue({
  el: '#app',
  data: {
    clientInfo: { },
    clientAccounts: [],
    clientAccountsTo: [],
    translatedProducts: [],
    products: [],
    totalPuntos: 0,
    numeroTarjeta: '',
    puntos: '',
    resultado: false,
    errorToats: null,
    errorMsg: null,
    accountFromNumber: "VIN",
    accountToNumber: "VIN",
    trasnferType: "own",
    amount: 0,
  },
  mounted() {
    this.getDataPoints();

    this.getData();
    this.formatDate();

    this.okmodal = new bootstrap.Modal(document.getElementById('okModal'));
    this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
  },
  methods: {
     getDataPoints: function(){
              axios.get("/api/pointsTransaction/current")
              .then((response) => {
                  this.clientInfo = response.data;
                  console.log(response.data)
              })
              .catch((error)=>{
                  this.errorMsg = "Error getting data";
                  this.errorToats.show();
              })
          },
          changedFrom: function(){
                      if(this.trasnferType == "own"){
                          this.clientAccountsTo = this.clientAccounts.filter(account => account.number != this.accountFromNumber);
                          this.accountToNumber = "VIN";
                      }
                  },
    formatDate(dateString) {
      const date = new Date(dateString);
      return date.toLocaleString('default', { month: 'long' });
    },
    fetchPointsTransaction: function() {
      axios.get('/api/pointsTransaction')
        .then(response => {
          this.accountInfo = response.data;
          this.calculateTotalPoints();
        })
        .catch(error => {
          console.error(error);
        });
    },

    canjearPuntos: function() {
        this.resultado = true;
    },
    formatDate: function(date){
                return new Date(date).toLocaleDateString('en-gb');
            },
            signOut: function(){
                axios.post('/api/logout')
                .then(response => window.location.href="/web/html/index.html")
                .catch(() =>{
                    this.errorMsg = "Sign out failed"
                    this.errorToats.show();
                })
            },
            getData: function(){
                          axios.get("/api/clients/current/accounts")
                          .then((response) => {
                              //get client ifo
                              this.clientAccounts = response.data;
                                 console.log(response.data)
                          })
                          .catch((error) => {
                              this.errorMsg = "Error getting data";
                              this.errorToats.show();
                          })
                      },
            getProducts: function(){
                         axios.get("/api/Products")
                               .then((response) => {
                                this.products = response.data;
                                this.okmodal.show();
                                console.log(response.data)
                                this.modal.hide();

                         })
                         .catch((error) => {
                                this.errorMsg = "Error getting data";
                                this.errorToats.show();
                         })
            },
              getTranslatedProducts: function() {
                    axios.get('/products')
                      .then((response) => {
                        this.translatedProducts = response.data;
                        this.okmodal.show();
                        console.log(response.data)
                        this.modal.hide();
                      })
                      .catch(error => {
                        this.errorMsg = "Error getting data";
                        this.errorToats.show();
                      });
                  },
            finish: function(){
                        window.location.reload();
                    },
},
});

