package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDateTime;

public class TransactionDTO {

    private TransactionType type;

    private int amount;

    private LocalDateTime date;

    private String description;


    public TransactionDTO(){}

    public TransactionDTO(Transaction transaction) {
        this.amount = transaction.getAmount();
        this.type = transaction.getType();
        this.date = transaction.getDate();
        this.description = transaction.getDescription();
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
