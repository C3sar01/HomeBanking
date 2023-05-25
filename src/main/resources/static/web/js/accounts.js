var app = new Vue({
    el:"#app",
    data:{
        clientInfo: {},
        errorToats: null,
        errorMsg: null,
        cryptos:[],
    },
    methods:{
        getData: function(){
            axios.get("/api/clients/current")
            .then((response) => {
                //get client ifo
                this.clientInfo = response.data;
            })
            .catch((error)=>{
                // handle error
                this.errorMsg = "Error getting data";
                this.errorToats.show();
            })
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
        create: function(){
            axios.post('http://localhost:8080/api/clients/current/accounts')
            .then(response => window.location.reload())
            .catch((error) =>{
                this.errorMsg = error.response.data;
                this.errorToats.show();
            })
        },
        getCryptos: function () {
                axios.get('http://localhost:8080/api/cryptos')
                .then(response => {
                    this.cryptos = response.data;
                    console.log(this.cryptos);
                })
                .catch(error => {
                    console.error(error);
                });
        },
/*        rotateBanner: function() {
            setInterval(() => {
                // Mover el primer elemento al final del arreglo
                this.cryptos.push(this.cryptos.shift());
            },3000); // Llamar al método cada 3 segundos para la rotación continua
        }*/

    },
    mounted: function(){
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.getData();
        this.getCryptos();/*
        this.rotateBanner();*/
    }
})