package com.mindhub.homebanking.models;

public class Indicadores {
    private Divisa uf;
    private Divisa dolar;
    private Divisa utm;
    private Divisa euro;

    public Divisa getUf() {
        return uf;
    }

    public void setUf(Divisa uf) {
        this.uf = uf;
    }

    public Divisa getDolar() {
        return dolar;
    }

    public void setDolar(Divisa dolar) {
        this.dolar = dolar;
    }

    public Divisa getUtm() {
        return utm;
    }

    public void setUtm(Divisa utm) {
        this.utm = utm;
    }

    public Divisa getEuro() {
        return euro;
    }

    public void setEuro(Divisa euro) {
        this.euro = euro;
    }
}
