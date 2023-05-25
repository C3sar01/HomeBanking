new Vue({
  el: '#app',
  data: {
    accountInfo: {
      transactions: []
    }
    totalPuntos: 0
  },
  mounted() {
    this.obtenerPuntos();
  },
  methods: {
    obtenerPuntos() {
      axios.get('/api/pointsTransaction/current')
        .then(response => {
          this.accountInfo.transactions = response.data;
          this.totalPuntos = this.accountInfo.transactions.reduce((total, transaction) => total + transaction.points, 0);
          })
        .catch(error => {
          console.error(error);
        });
    }
  }
});