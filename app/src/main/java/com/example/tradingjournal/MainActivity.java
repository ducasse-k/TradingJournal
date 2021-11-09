package com.example.tradingjournal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.tradingjournal.TradeJournalService.TradeJournalService;
import com.example.tradingjournal.dao.DatabaseHelper;
import com.example.tradingjournal.helpers.MainActivityHelper;
import com.example.tradingjournal.model.TradeJournal;

public class MainActivity extends AppCompatActivity {

    Button calculateButton;
    Button clearButton;
    Button saveButton;
    Button btnTest;

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

    int llDialogTickerSymbol;

    TradeJournal tradeJournal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        llResults = findViewById(R.id.llResults);
        llStopLoss = findViewById(R.id.llStopLoss);
        llErrorMessage = findViewById(R.id.llErrorMessage);
        llMainLayout = findViewById(R.id.llMainLayout);
        llDialogTickerSymbol = R.layout.dialog_ticker_symbol;

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

        saveButton = findViewById(R.id.btnSave);
        calculateButton = findViewById(R.id.btnCalculate);
        clearButton = findViewById(R.id.btnClear);

        DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);

        btnTest = findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, TradeListActivity.class);
                startActivity(myIntent);
            }
        });

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( !isEmpty(etAtr) && !isEmpty(etBuyerProximal) && !isEmpty(etBuyerDistal) && !isEmpty(etSellerProximal) && !isEmpty(etWillingToLose)) {
                    tradeJournal = TradeJournalService.newTradeJournal(etAtr, etBuyerProximal, etBuyerDistal, etSellerProximal, etWillingToLose);
                    MainActivityHelper.setTextViewForCalculations(tvRisk, tvReward, tvRiskRewardRatio, tvQuantity, tvCost, tvPotentialProfit, tvPotentialLoss, tvStopLoss, saveButton, tradeJournal, getApplicationContext());
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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivityHelper.showTickerSymbolDialog(MainActivity.this, llDialogTickerSymbol, R.id.btnSubmitTicker, R.id.etTickerSymbol, tradeJournal, databaseHelper);
                hideKeyboard(llMainLayout);
                etWillingToLose.clearFocus();
            }
        });


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

    public void hideKeyboard(LinearLayout llMainLayout) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(llMainLayout.getWindowToken(), 0);
    }

    private void hideErrorMessage(LinearLayout llErrorMessage) {
        llErrorMessage.setVisibility(View.GONE);
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }
}