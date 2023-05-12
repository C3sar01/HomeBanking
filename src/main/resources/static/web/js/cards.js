var app = new Vue({
    el:"#app",
    data:{
        clientInfo: {},
        creditCards: [],
        debitCards: [],
        errorToats: null,
        errorMsg: null,
    },
    methods:{
        getData: function(){
            axios.get("/api/clients/current")
                .then((response) => {
                    //get client ifo
                    this.clientInfo = response.data;
                    this.creditCards = this.clientInfo.cards.filter(card => card.type == "CREDITO");
                    this.debitCards = this.clientInfo.cards.filter(card => card.type == "DEBITO");
                })
                .catch((error) => {
                    this.errorMsg = "Error getting data";
                    this.errorToats.show();
                })
        },
        formatDate: function(date){
            return new Date(date).toLocaleDateString('en-gb');
        },
        signOut: function(){
            axios.post('/api/logout')
                .then(response => window.location.href="/web/index.html")
                .catch(() =>{
                    this.errorMsg = "Sign out failed"
                    this.errorToats.show();
                })
        },
    },
    mounted: function(){
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.getData();
    }
})