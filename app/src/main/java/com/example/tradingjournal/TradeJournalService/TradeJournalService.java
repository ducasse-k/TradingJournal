package com.example.tradingjournal.TradeJournalService;

import android.widget.EditText;

import com.example.tradingjournal.model.TradeJournal;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TradeJournalService {

    public static TradeJournal newTradeJournal(EditText etAtr, EditText etBuyerProximal, EditText etBuyerDistal, EditText etSellerProximal, EditText etWillingToLose) {
        TradeJournal tradeJournal = new TradeJournal(convertToBigDecimal(etAtr),
                convertToBigDecimal(etBuyerProximal),
                convertToBigDecimal(etBuyerDistal),
                convertToBigDecimal(etSellerProximal),
                convertToBigDecimal(etWillingToLose));
        tradeJournal.setNotes("");

        return  performTradeCalculations(tradeJournal);
    }

    public static TradeJournal newTradeJournal(EditText etAtr, EditText etBuyerDistal) {
        return new TradeJournal(convertToBigDecimal(etAtr), convertToBigDecimal(etBuyerDistal));
    }

    private static BigDecimal convertToBigDecimal(EditText et) {
        return new BigDecimal(et.getText().toString());
    }

    public static TradeJournal performTradeCalculations(TradeJournal tradeJournal) {
        BigDecimal stopLoss = calculateStopLoss(tradeJournal.getBuyerDistal(), tradeJournal.getAtr());
        BigDecimal reward = tradeJournal.getSellerProximal().subtract(tradeJournal.getBuyerProximal());
        BigDecimal risk = tradeJournal.getBuyerProximal().subtract(stopLoss);
        BigDecimal rrRatio = reward.divide(risk, RoundingMode.HALF_UP);
        BigDecimal quantity = tradeJournal.getAmountWillingToLose().divide(risk, RoundingMode.HALF_UP).setScale(0, RoundingMode.DOWN);
        BigDecimal cost = quantity.multiply(tradeJournal.getBuyerProximal());
        BigDecimal profit = quantity.multiply(reward);
        BigDecimal loss = quantity.multiply(risk);

        tradeJournal.setStopLoss(stopLoss.setScale(2, RoundingMode.HALF_EVEN));
        tradeJournal.setReward(reward.setScale(2, RoundingMode.HALF_EVEN));
        tradeJournal.setRisk(risk.setScale(2, RoundingMode.HALF_EVEN));
        tradeJournal.setRrRatio(rrRatio.setScale(2, RoundingMode.HALF_EVEN));
        tradeJournal.setQuantity(quantity.intValue());
        tradeJournal.setTotalCost(cost.setScale(2, RoundingMode.HALF_EVEN));
        tradeJournal.setProfit(profit.setScale(2, RoundingMode.HALF_EVEN));
        tradeJournal.setLoss(loss.setScale(2, RoundingMode.HALF_EVEN));

        return tradeJournal;
    }

    public static BigDecimal performTradeCalculationsStopLoss(TradeJournal tradeJournal) {
        return calculateStopLoss(tradeJournal.getBuyerDistal(), tradeJournal.getAtr());
    }

    private static BigDecimal calculateStopLoss(BigDecimal buyerDistal,BigDecimal atr) {
        return buyerDistal.subtract(atr.multiply(new BigDecimal("0.20")));
    }
}
