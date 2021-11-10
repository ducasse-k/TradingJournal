package com.example.tradingjournal.helpers;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tradingjournal.MainActivity;
import com.example.tradingjournal.TradeListActivity;
import com.example.tradingjournal.dao.DatabaseHelper;
import com.example.tradingjournal.model.TradeJournal;

import java.math.BigDecimal;

public class MainActivityHelper {

    public static void setTextViewForCalculations(TextView tvRisk, TextView tvReward, TextView tvRiskRewardRatio,
                                                  TextView tvQuantity, TextView tvCost, TextView tvPotentialProfit,
                                                  TextView tvPotentialLoss, TextView tvStopLoss, Button saveButton, TradeJournal tradeJournal, Context application) {
        int quantity = tradeJournal.getQuantity();

        tvRisk.setText("-$" + tradeJournal.getRisk().toString());
        tvReward.setText("+$" + tradeJournal.getReward().toString());
        tvRiskRewardRatio.setText(tradeJournal.getRrRatio().toString());
        tvQuantity.setText("Quantity: " + String.valueOf(quantity));
        tvCost.setText("Cost: $" + tradeJournal.getTotalCost().toString());

        if (quantity == 0) {
            tvPotentialProfit.setText("----");
            tvPotentialLoss.setText("----");
            Toast.makeText(application.getApplicationContext(), "Increase your risk tolerance!",
                    Toast.LENGTH_LONG).show();
            saveButton.setVisibility(View.GONE);
        } else if (quantity >= 1) {
            tvPotentialProfit.setText("Profit: +$" + tradeJournal.getProfit().toString());
            tvPotentialLoss.setText("Loss: -$" + tradeJournal.getLoss().toString());
            tvReward.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            tvRisk.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            setRiskRatioColor(tvRiskRewardRatio, tradeJournal.getRrRatio());
            saveButton.setVisibility(View.VISIBLE);
        } else {
            //Negative
            tvPotentialProfit.setText("");
            tvPotentialLoss.setText("");
            tvReward.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            tvRisk.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            saveButton.setVisibility(View.GONE);
        }
        tvStopLoss.setText("Stop Loss: $" + tradeJournal.getStopLoss());
    }

    private static void setRiskRatioColor(TextView tvRiskRewardRatio, BigDecimal rrRatio) {
        if (rrRatio.intValue() >= 3) {
            tvRiskRewardRatio.setTextColor(Color.parseColor("#578018"));
        } else {
            tvRiskRewardRatio.setTextColor(Color.parseColor("#000000"));
        }
    }

    public static void showTickerSymbolDialog(MainActivity mainActivity, int dialog_ticker_symbol, int submitTickerId,
                                              int etTickerSymbolId, TradeJournal tradeJournal, DatabaseHelper databaseHelper,
                                              LinearLayout llStopLoss, LinearLayout llResults, EditText[] editTexts) {
        final Dialog dialog = new Dialog(mainActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(dialog_ticker_symbol);

        Context context = mainActivity.getApplicationContext();

        Button submitTickerButton = dialog.findViewById(submitTickerId);
        EditText etTickerSymbol = dialog.findViewById(etTickerSymbolId);
        etTickerSymbol.requestFocus();

        submitTickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tickerSymbol = etTickerSymbol.getText().toString();
                if (!tickerSymbol.isEmpty()) {
                    tradeJournal.setTickerSymbol(tickerSymbol);
                    dialog.dismiss();
                    boolean success = databaseHelper.addTrade(tradeJournal);
                    if (success) {
                        Toast.makeText(context, "Trade Saved", Toast.LENGTH_LONG).show();
                        llStopLoss.setVisibility(View.GONE);
                        llResults.setVisibility(View.GONE);
                        for (int i = 0; i < editTexts.length; i++) {
                            editTexts[i].getText().clear();
                        }
                    }
                } else {
                    Toast.makeText(context, "Please Enter a Ticker Symbol", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }
}
