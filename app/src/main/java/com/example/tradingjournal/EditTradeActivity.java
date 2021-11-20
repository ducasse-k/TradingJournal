package com.example.tradingjournal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tradingjournal.TradeJournalService.TradeJournalService;
import com.example.tradingjournal.dao.DatabaseHelper;
import com.example.tradingjournal.helpers.MainActivityHelper;
import com.example.tradingjournal.model.TradeJournal;

import java.math.BigDecimal;

public class EditTradeActivity extends AppCompatActivity {

    TextView tvTickerSymbol;

    EditText etAtr;
    EditText etBuyerProximal;
    EditText etBuyerDistal;
    EditText etSellerProximal;
    EditText etWillingToLose;
    EditText etNotes;

    Button saveButton;
    Button deleteButton;
    Button calculateButton;

    TextView tvRisk;
    TextView tvReward;
    TextView tvRiskRewardRatio;
    TextView tvQuantity;
    TextView tvCost;
    TextView tvPotentialProfit;
    TextView tvPotentialLoss;
    TextView tvStopLoss;

    LinearLayout llResults;
    TableLayout tlEdit;

    TradeJournal tradeJournal;
    int tradeId;
    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trade);

        etAtr = findViewById(R.id.etAtr);
        etBuyerProximal = findViewById(R.id.etBuyerProximal);
        etBuyerDistal = findViewById(R.id.etBuyerDistal);
        etSellerProximal = findViewById(R.id.etSellerProximal);
        etWillingToLose = findViewById(R.id.etWillingToLose);
        tvTickerSymbol = findViewById(R.id.tv_tickerSymbol);
        etNotes = findViewById(R.id.etNotes);

        saveButton = findViewById(R.id.btnSave);
        deleteButton = findViewById(R.id.btnDelete);
        calculateButton = findViewById(R.id.btnCalculate);

        tvRisk = findViewById(R.id.tvRisk);
        tvReward = findViewById(R.id.tvReward);
        tvRiskRewardRatio = findViewById(R.id.tvRiskRewardRatio);
        tvQuantity = findViewById(R.id.tvQuantity);
        tvCost = findViewById(R.id.tvCost);
        tvPotentialProfit = findViewById(R.id.tvPotentialProfit);
        tvPotentialLoss = findViewById(R.id.tvPotentialLoss);
        tvStopLoss = findViewById(R.id.tvStopLoss);
        llResults = findViewById(R.id.llResults);
        tlEdit = findViewById(R.id.tlEdit);


        Intent intent = getIntent();
        tradeId = intent.getIntExtra("id", -1);

        if (tradeId >= 0) {
            tradeJournal = databaseHelper.getTradeJournalById(tradeId);
            tvTickerSymbol.setText(tradeJournal.getTickerSymbol());
            etAtr.setText(tradeJournal.getAtr().toString());
            etBuyerProximal.setText(tradeJournal.getBuyerProximal().toString());
            etBuyerDistal.setText(tradeJournal.getBuyerDistal().toString());
            etSellerProximal.setText(tradeJournal.getSellerProximal().toString());
            etWillingToLose.setText(tradeJournal.getAmountWillingToLose().toString());
            etNotes.setText(tradeJournal.getNotes());
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditTradeActivity.this);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tradeJournal.getAtr().toString().equals(etAtr.getText().toString()) ||
                        !tradeJournal.getBuyerProximal().toString().equals(etBuyerProximal.getText().toString()) ||
                        !tradeJournal.getBuyerDistal().toString().equals(etBuyerDistal.getText().toString()) ||
                        !tradeJournal.getSellerProximal().toString().equals(etSellerProximal.getText().toString()) ||
                        !tradeJournal.getAmountWillingToLose().toString().equals(etWillingToLose.getText().toString())
                ) {
                    hideKeyboard();
                    tradeJournal.setAtr(new BigDecimal(etAtr.getText().toString()));
                    tradeJournal.setBuyerProximal(new BigDecimal(etBuyerProximal.getText().toString()));
                    tradeJournal.setBuyerDistal(new BigDecimal(etBuyerDistal.getText().toString()));
                    tradeJournal.setSellerProximal(new BigDecimal(etSellerProximal.getText().toString()));
                    tradeJournal.setAmountWillingToLose(new BigDecimal(etWillingToLose.getText().toString()));
                    tradeJournal.setNotes(etNotes.getText().toString());
                    TradeJournalService.performTradeCalculations(tradeJournal);

                    MainActivityHelper.setTextViewForCalculations(tvRisk, tvReward, tvRiskRewardRatio, tvQuantity, tvCost, tvPotentialProfit, tvPotentialLoss, tvStopLoss, saveButton, tradeJournal, getApplicationContext());
                    llResults.setVisibility(View.VISIBLE);
                } else if (!tradeJournal.getNotes().equals(etNotes.getText().toString()) &&
                        (tradeJournal.getAtr().toString().equals(etAtr.getText().toString()) ||
                        tradeJournal.getBuyerProximal().toString().equals(etBuyerProximal.getText().toString()) ||
                        tradeJournal.getBuyerDistal().toString().equals(etBuyerDistal.getText().toString()) ||
                        tradeJournal.getSellerProximal().toString().equals(etSellerProximal.getText().toString()) ||
                        tradeJournal.getAmountWillingToLose().toString().equals(etWillingToLose.getText().toString()))) {
                    //The notes are only updated
                    tradeJournal.setNotes(etNotes.getText().toString());
                    transitionToTradeList();
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transitionToTradeList();
            }
        });

    }

    private void transitionToTradeList() {
        databaseHelper.updateTradeJournal(tradeJournal);
        hideKeyboard();
        Intent intent = new Intent(EditTradeActivity.this, TradeListActivity.class);
        intent.putExtra("sorted", true);
        startActivity(intent);
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    databaseHelper.deleteTradeJournal(tradeId);
                    Intent intent = new Intent(EditTradeActivity.this, TradeListActivity.class);
                    startActivity(intent);

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tlEdit.getWindowToken(), 0);
    }
}