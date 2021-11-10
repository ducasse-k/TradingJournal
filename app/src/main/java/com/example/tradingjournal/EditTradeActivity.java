package com.example.tradingjournal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tradingjournal.TradeJournalService.TradeJournalService;
import com.example.tradingjournal.dao.DatabaseHelper;
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

    Button editButton;
    Button deleteButton;

    TradeJournal tradeJournal;
    int tradeId;
    DatabaseHelper databaseHelper = databaseHelper = new DatabaseHelper(this);;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trade);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        etAtr = findViewById(R.id.etAtr);
        etBuyerProximal = findViewById(R.id.etBuyerProximal);
        etBuyerDistal = findViewById(R.id.etBuyerDistal);
        etSellerProximal = findViewById(R.id.etSellerProximal);
        etWillingToLose = findViewById(R.id.etWillingToLose);
        tvTickerSymbol = findViewById(R.id.tv_tickerSymbol);
        etNotes = findViewById(R.id.etNotes);

        editButton = findViewById(R.id.btnEdit);
        deleteButton = findViewById(R.id.btnDelete);

        Intent intent = getIntent();
        tradeId = intent.getIntExtra("id", -1);

        if ( tradeId >= 0) {
            tradeJournal = databaseHelper.getTradeJournalById(tradeId);
            tvTickerSymbol.setText(tradeJournal.getTickerSymbol());
            etAtr.setText(tradeJournal.getAtr().toString());
            etBuyerProximal.setText(tradeJournal.getBuyerProximal().toString());
            etBuyerDistal.setText(tradeJournal.getBuyerDistal().toString());
            etSellerProximal.setText(tradeJournal.getBuyerDistal().toString());
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

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tradeJournal.getAtr().toString().equals(etAtr) ||
                        !tradeJournal.getBuyerProximal().toString().equals(etBuyerProximal) ||
                        !tradeJournal.getBuyerDistal().toString().equals(etBuyerDistal) ||
                        !tradeJournal.getSellerProximal().toString().equals(etSellerProximal) ||
                        !tradeJournal.getAmountWillingToLose().toString().equals(etWillingToLose)
                ) {

                    tradeJournal.setAtr(new BigDecimal(etAtr.getText().toString()));
                    tradeJournal.setBuyerProximal(new BigDecimal(etBuyerProximal.getText().toString()));
                    tradeJournal.setBuyerDistal(new BigDecimal(etBuyerDistal.getText().toString()));
                    tradeJournal.setSellerProximal(new BigDecimal(etSellerProximal.getText().toString()));
                    tradeJournal.setAmountWillingToLose(new BigDecimal(etWillingToLose.getText().toString()));
                    tradeJournal.setNotes(etNotes.getText().toString());

                    TradeJournalService.performTradeCalculations(tradeJournal);
                    databaseHelper.updateTradeJournal(tradeJournal);
                    Intent intent = new Intent(EditTradeActivity.this, TradeListActivity.class);
                    intent.putExtra("sorted", true);
                    startActivity(intent);
                }

            }
        });


    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    databaseHelper.deleteTradeJournal(tradeId);
                    Toast.makeText(getApplicationContext(), "Deleted!", Toast.LENGTH_SHORT);
                    Intent intent = new Intent(EditTradeActivity.this, MainActivity.class);
                    startActivity(intent);

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };
}