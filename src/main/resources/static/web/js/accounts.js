var app = new Vue({
    el:"#app",
    data:{
        clientInfo: {},
        errorToats: null,
        errorMsg: null,
        cryptos:[],
        indicadores: [],
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
            axios.post('/api/clients/current/accounts')
            .then(response => window.location.reload())
            .catch((error) =>{
                this.errorMsg = error.response.data;
                this.errorToats.show();
            })
        },
        getCryptos: function () {
                axios.get('/api/cryptos')
                .then(response => {
                    this.cryptos = response.data;
                    console.log(this.cryptos);
                })
                .catch(error => {
                    console.error(error);
                });
        },
        fetchIndicadores() {
            axios.get('/api/indicadores')
                .then(response => {
                    // Filtrar las divisas requeridas
                    const divisasRequeridas = ['uf', 'dolar', 'utm', 'euro'];
                    this.indicadores = Object.values(response.data).filter(indicador => divisasRequeridas.includes(indicador.codigo));
                })
                .catch(error => {
                    console.error('Error fetching indicadores:', error);
                });
        }
    },
    mounted: function(){
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.getData();
        this.getCryptos();
        this.fetchIndicadores();
    }
})