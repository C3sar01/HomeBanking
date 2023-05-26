new Vue({
  el: '#app',
  data: {
    clientInfo: { },
    clientAccounts: [],
    clientAccountsTo: [],
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
    //this.getDataPoints();
    this.getData();
    this.calculateTotalPoints();
    this.formatDate();
    this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
  },
  methods: {
     getData: function(){
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
    calculateTotalPoints: function() {
      this.totalPuntos = this.accountInfo.reduce((total) => {
        return total + points;
      }, 0);
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
  },
//  methods:{
//          getData: function(){
//              axios.get("/api/clients/current/accounts")
//              .then((response) => {
//                  //get client ifo
//                  this.clientAccounts = response.data;
//              })
//              .catch((error) => {
//                  this.errorMsg = "Error getting data";
//                  this.errorToats.show();
//              })
//          },
//          formatDate: function(date){
//              return new Date(date).toLocaleDateString('en-gb');
//          },
//          checkTransfer: function(){
//              if(this.accountFromNumber == "VIN"){
//                  this.errorMsg = "You must select an origin account";
//                  this.errorToats.show();
//              }
//              else if(this.accountToNumber == "VIN"){
//                  this.errorMsg = "You must select a destination account";
//                  this.errorToats.show();
//              }else if(this.amount == 0){
//                  this.errorMsg = "You must indicate an amount";
//                  this.errorToats.show();
//              }
//              else if(this.description.length <= 0){
//                  this.errorMsg = "You must indicate a description";
//                  this.errorToats.show();
//              }else{
//                  this.modal.show();
//              }
//          },
//          transfer: function(){
//              let config = {
//                  headers: {
//                      'content-type': 'application/x-www-form-urlencoded'
//                  }
//              }
//              axios.post(`/api/transactions?fromAccountNumber=${this.accountFromNumber}&toAccountNumber=${this.accountToNumber}&amount=${this.amount}&description=${this.description}&`,config)
//              .then(response => {
//                  this.modal.hide();
//                  this.okmodal.show();
//              })
//              .catch((error) =>{
//                  this.errorMsg = error.response.data;
//                  this.errorToats.show();
//              })
//          },
//          changedType: function(){
//              this.accountFromNumber = "VIN";
//              this.accountToNumber = "VIN";
//          },
//          changedFrom: function(){
//              if(this.trasnferType == "own"){
//                  this.clientAccountsTo = this.clientAccounts.filter(account => account.number != this.accountFromNumber);
//                  this.accountToNumber = "VIN";
//              }
//          },
//          finish: function(){
//              window.location.reload();
//          },
//          signOut: function(){
//              axios.post('/api/logout')
//              .then(response => window.location.href="/web/html/index.html")
//              .catch(() =>{
//                  this.errorMsg = "Sign out failed"
//                  this.errorToats.show();
//              })
//          },
//
//          pointsTransaction: function() {
//                let config = {
//                    headers: {
//                      'content-type': 'application/x-www-form-urlencoded'
//                    }
//                  };
//                  axios.post(`/api/pointsTransaction?amount=${this.amount}&accountFromNumber=${this.accountFromNumber}`, config)
//                  .then(function (response) {
//                    console.log(response.data);
//                  })
//                  .catch(function (error) {
//                    console.error(error);
//                  });
//              },
//          },

});

