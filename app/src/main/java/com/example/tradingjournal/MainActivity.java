package com.example.tradingjournal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tradingjournal.TradeJournalService.TradeJournalService;
import com.example.tradingjournal.model.TradeJournal;

import java.math.BigDecimal;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button calculateButton;
    Button clearButton;

    EditText etAtr;
    EditText etBuyerProximal;
    EditText etBuyerDistal;
    EditText etSellerProximal;
    EditText etWillingToLose;

    TextView tvRisk;
    TextView tvReward;
    TextView tvRiskRewardRatio;
    TextView tvQuantity;
    TextView tvCost;
    TextView tvPotentialProfit;
    TextView tvPotentialLoss;
    TextView tvStopLoss;

    LinearLayout llResults;
    LinearLayout llStopLoss;
    LinearLayout llErrorMessage;
    LinearLayout llMainLayout;

    TradeJournal tradeJournal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        llResults = (LinearLayout) findViewById(R.id.llResults);
        llStopLoss = (LinearLayout) findViewById(R.id.llStopLoss);
        llErrorMessage = (LinearLayout) findViewById(R.id.llErrorMessage);

        llMainLayout = findViewById(R.id.llMainLayout);

        etAtr = findViewById(R.id.etAtr);
        etBuyerProximal = findViewById(R.id.etBuyerProximal);
        etBuyerDistal = findViewById(R.id.etBuyerDistal);
        etSellerProximal = findViewById(R.id.etSellerProximal);
        etWillingToLose = findViewById(R.id.etWillingToLose);

        EditText[] editTexts = new EditText[]{
                etAtr, etBuyerProximal, etBuyerDistal, etSellerProximal, etWillingToLose
        };

        tvRisk = (TextView) findViewById(R.id.tvRisk);
        tvReward = (TextView) findViewById(R.id.tvReward);
        tvRiskRewardRatio = (TextView) findViewById(R.id.tvRiskRewardRatio);
        tvQuantity = (TextView) findViewById(R.id.tvQuantity);
        tvCost = (TextView) findViewById(R.id.tvCost);
        tvPotentialProfit = (TextView) findViewById(R.id.tvPotentialProfit);
        tvPotentialLoss = (TextView) findViewById(R.id.tvPotentialLoss);
        tvStopLoss = (TextView) findViewById(R.id.tvStopLoss);

        calculateButton = findViewById(R.id.btnCalculate);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( !isEmpty(etAtr) && !isEmpty(etBuyerProximal) && !isEmpty(etBuyerDistal) && !isEmpty(etSellerProximal) && !isEmpty(etWillingToLose)) {
                    tradeJournal = TradeJournalService.newTradeJournal(etAtr, etBuyerProximal, etBuyerDistal, etSellerProximal, etWillingToLose);
                    llStopLoss.setVisibility(View.VISIBLE);
                    llResults.setVisibility(View.VISIBLE);
                } else if (!isEmpty(etAtr) && isEmpty(etBuyerProximal) && !isEmpty(etBuyerDistal) && isEmpty(etSellerProximal) && isEmpty(etWillingToLose)) {
                    tradeJournal = TradeJournalService.newTradeJournal(etAtr, etBuyerDistal);
                    tvStopLoss.setText("Stop Loss: $" + TradeJournalService.performTradeCalculationsStopLoss(tradeJournal).toString());
                    llStopLoss.setVisibility(View.VISIBLE);
                    hideErrorMessage(llErrorMessage);
                } else {
                    llErrorMessage.setVisibility(View.VISIBLE);
                }
                hideKeyboard(llMainLayout);
            }
        });

        clearButton = findViewById(R.id.btnClear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < editTexts.length; i++) {
                    editTexts[i].getText().clear();
                }
                llStopLoss.setVisibility(View.GONE);
                llResults.setVisibility(View.GONE);
                hideErrorMessage(llErrorMessage);
                etAtr.requestFocus();
            }
        });

    }

    private void hideKeyboard(LinearLayout llMainLayout) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(llMainLayout.getWindowToken(), 0);
    }

    private void hideErrorMessage(LinearLayout llErrorMessage) {
        llErrorMessage.setVisibility(View.GONE);
    }

    private void setTextViewForCalculations(TextView tvRisk, TextView tvReward, TextView tvRiskRewardRatio,
                                            TextView tvQuantity, TextView tvCost, TextView tvPotentialProfit,
                                            TextView tvPotentialLoss, TextView tvStopLoss, TradeJournal tradeJournal) {
        int quantity = tradeJournal.getQuantity();

        tvRisk.setText("-$" + tradeJournal.getRisk().toString());
        tvReward.setText("+$" + tradeJournal.getReward().toString());
        tvRiskRewardRatio.setText(tradeJournal.getRrRatio().toString());
        tvQuantity.setText("Quantity: " + String.valueOf(quantity));
        tvCost.setText("Cost: $" + tradeJournal.getTotalCost().toString());

        if (quantity == 0) {
            tvPotentialProfit.setText("----");
            tvPotentialLoss.setText("----");
            Toast.makeText(getApplicationContext(), "Increase your risk tolerance!",
                    Toast.LENGTH_LONG).show();
        } else if (quantity >= 1) {
            tvPotentialProfit.setText("Profit: +$" + tradeJournal.getProfit().toString());
            tvPotentialLoss.setText("Loss: -$" + tradeJournal.getLoss().toString());
            tvReward.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            tvRisk.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        } else {
            tvPotentialProfit.setText("");
            tvPotentialLoss.setText("");
            tvReward.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            tvRisk.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        }
        tvStopLoss.setText("Stop Loss: $" + tradeJournal.getStopLoss());
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }
}