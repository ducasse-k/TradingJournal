package com.example.tradingjournal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
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
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button calculateButton;
    Button clearButton;
    Button saveButton;
    NavigationView navigationView;

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

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    int llDialogTickerSymbol;
    TradeJournal tradeJournal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setNavigationViewListener();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setUpNavigationMenu();

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

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmpty(etAtr) && !isEmpty(etBuyerProximal) && !isEmpty(etBuyerDistal) && !isEmpty(etSellerProximal) && !isEmpty(etWillingToLose)) {
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

    private void setUpNavigationMenu() {
        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // override the onOptionsItemSelected()
    // function to implement
    // the item click listener callback
    // to open and close the navigation
    // drawer when the icon is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemTradingJournals:
                Intent i = new Intent(MainActivity.this, TradeListActivity.class);
                MainActivity.this.startActivity(i);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setNavigationViewListener() {
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
}