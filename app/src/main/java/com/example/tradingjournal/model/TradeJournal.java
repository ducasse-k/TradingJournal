package com.example.tradingjournal.model;

import java.math.BigDecimal;

public class TradeJournal {

    //Use in constructor
    private BigDecimal atr;
    private BigDecimal buyerProximal;
    private BigDecimal buyerDistal;
    private BigDecimal sellerProximal;
    private BigDecimal amountWillingToLose;

    //Will need to be calculated
    private BigDecimal stopLoss;
    private BigDecimal reward;
    private BigDecimal risk;
    private BigDecimal profit;
    private BigDecimal loss;
    private BigDecimal rrRatio;
    private int quantity;
    private BigDecimal totalCost;

    public TradeJournal(BigDecimal atr,  BigDecimal buyerDistal) {
        this.atr = atr;
        this.buyerDistal = buyerDistal;
    }

    public TradeJournal(BigDecimal atr, BigDecimal buyerProximal, BigDecimal buyerDistal, BigDecimal sellerProximal, BigDecimal amountWillingToLose) {
        this.atr = atr;
        this.buyerProximal = buyerProximal;
        this.buyerDistal = buyerDistal;
        this.sellerProximal = sellerProximal;
        this.amountWillingToLose = amountWillingToLose;
    }

    @Override
    public String toString() {
        return "TradeJournal{" +
                "atr=" + atr +
                ", buyerProximal=" + buyerProximal +
                ", buyerDistal=" + buyerDistal +
                ", sellerProximal=" + sellerProximal +
                ", amountWillingToLose=" + amountWillingToLose +
                ", stopLoss=" + stopLoss +
                ", reward=" + reward +
                ", risk=" + risk +
                ", profit=" + profit +
                ", loss=" + loss +
                ", rrRatio=" + rrRatio +
                ", quantity=" + quantity +
                ", totalCost=" + totalCost +
                '}';
    }

    public BigDecimal getAtr() {
        return atr;
    }

    public void setAtr(BigDecimal atr) {
        this.atr = atr;
    }

    public BigDecimal getBuyerProximal() {
        return buyerProximal;
    }

    public void setBuyerProximal(BigDecimal buyerProximal) {
        this.buyerProximal = buyerProximal;
    }

    public BigDecimal getBuyerDistal() {
        return buyerDistal;
    }

    public void setBuyerDistal(BigDecimal buyerDistal) {
        this.buyerDistal = buyerDistal;
    }

    public BigDecimal getSellerProximal() {
        return sellerProximal;
    }

    public void setSellerProximal(BigDecimal sellerProximal) {
        this.sellerProximal = sellerProximal;
    }

    public BigDecimal getAmountWillingToLose() {
        return amountWillingToLose;
    }

    public void setAmountWillingToLose(BigDecimal amountWillingToLose) {
        this.amountWillingToLose = amountWillingToLose;
    }

    public BigDecimal getStopLoss() {
        return stopLoss;
    }

    public void setStopLoss(BigDecimal stopLoss) {
        this.stopLoss = stopLoss;
    }

    public BigDecimal getReward() {
        return reward;
    }

    public void setReward(BigDecimal reward) {
        this.reward = reward;
    }

    public BigDecimal getRisk() {
        return risk;
    }

    public void setRisk(BigDecimal risk) {
        this.risk = risk;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getLoss() {
        return loss;
    }

    public void setLoss(BigDecimal loss) {
        this.loss = loss;
    }

    public BigDecimal getRrRatio() {
        return rrRatio;
    }

    public void setRrRatio(BigDecimal rrRatio) {
        this.rrRatio = rrRatio;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
}
