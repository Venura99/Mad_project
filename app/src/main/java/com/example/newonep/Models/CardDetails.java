package com.example.newonep.Models;

public class CardDetails {

    String owner;
    String cardNumber;
    String expireDate;
    String Csv;
    String bankName;

    public CardDetails() {
    }

    public CardDetails(String owner, String cardNumber, String expireDate, String csv, String bankName) {
        this.owner = owner;
        this.cardNumber = cardNumber;
        this.expireDate = expireDate;
        Csv = csv;
        this.bankName = bankName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getCsv() {
        return Csv;
    }

    public void setCsv(String csv) {
        Csv = csv;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
