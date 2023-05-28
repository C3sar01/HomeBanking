var app = new Vue({
  el:"#app",
  data:{
      accountInfo: {},
      errorToats: null,
      errorMsg: null,
      chatGPTData: null,
      loading: false, // Nueva propiedad para controlar el estado de carga


  },
  methods:{
      getData: function(){
          const urlParams = new URLSearchParams(window.location.search);
          const id = urlParams.get('id');
          axios.get(`/api/accounts/${id}`)
          .then((response) => {
              //get client ifo
              this.accountInfo = response.data;
              this.accountInfo.transactions.sort((a,b) => parseInt(b.id - a.id))
          })
          .catch((error) => {
              // handle error
              this.errorMsg = "Error getting data";
              this.errorToats.show();
          })
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

      openModal() {
        this.loading = true;
        this.getChatGPTData(); // Llamar a la función para obtener los datos del backend
      },

      getChatGPTData() {
        axios.post('/chat', { accountNumber: this.accountInfo.number })
          .then(response => {
            console.log(this.accountInfo.number);
            this.chatGPTData = response.data.choices[0].message.content;
            $('#chatModal').modal('show');
            this.loading = false;
          })
          .catch(error => {
            console.error(error);
          });
      },


  },
  mounted: function(){
      this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
      this.getData();
  }
})