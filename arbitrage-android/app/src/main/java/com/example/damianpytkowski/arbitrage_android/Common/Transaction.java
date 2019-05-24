package com.example.damianpytkowski.arbitrage_android.Common;

import java.util.Date;

public class Transaction {
    private Double roi;
    private Double sumToSell;
    private Double sumToBuy;
    private Date time;

    public Transaction(Double roi, Double sumToSell, Double sumToBuy) {
        super();
        this.roi = roi;
        this.sumToSell = sumToSell;
        this.sumToBuy = sumToBuy;
        this.time = new Date();
    }

    public Double getRoi() {
        return roi;
    }

    public void setRoi(Double roi) {
        this.roi = roi;
    }

    public Double getSumToSell() {
        return sumToSell;
    }

    public void setSumToSell(Double sumToSell) {
        this.sumToSell = sumToSell;
    }

    public Double getSumToBuy() {
        return sumToBuy;
    }

    public void setSumToBuy(Double sumToBuy) {
        this.sumToBuy = sumToBuy;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

}

