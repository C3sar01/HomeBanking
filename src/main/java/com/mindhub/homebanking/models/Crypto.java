package com.mindhub.homebanking.models;
public class Crypto {
    private long market_cap_rank;
    private String name;
    private String symbol;
    private double current_price;
    private double price_change_percentage_24h;
    private String image;
    /*  ************************  */
    public Crypto(long market_cap_rank, String name, String symbol, double current_price, double price_change_percentage_24h, String image) {
        this.market_cap_rank = market_cap_rank;
        this.name = name;
        this.symbol = symbol;
        this.current_price = current_price;
        this.price_change_percentage_24h = price_change_percentage_24h;
        this.image=image;
    }

    public Crypto() {
    }
    /*  ************************  */
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public double getCurrent_price() {
        return current_price;
    }
    public void setCurrent_price(double current_price) {
        this.current_price = current_price;
    }
    public double getPrice_change_percentage_24h() {
        return price_change_percentage_24h;
    }
    public void setPrice_change_percentage_24h(double price_change_percentage_24h) {
        this.price_change_percentage_24h = price_change_percentage_24h;
    }
    public long getMarket_cap_rank() {return market_cap_rank;
    }
    public void setMarket_cap_rank(long market_cap_rank) {
        this.market_cap_rank = market_cap_rank;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
}
