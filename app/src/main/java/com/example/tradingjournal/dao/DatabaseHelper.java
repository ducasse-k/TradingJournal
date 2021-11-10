package com.example.tradingjournal.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.tradingjournal.model.TradeJournal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TRADE_JOURNAL_TABLE = "TRADE_JOURNAL_TABLE";
    public static final String TICKER_SYMBOL = "ticker_symbol";
    public static final String ATR = "atr";
    public static final String BUYER_PROXIMAL = "buyer_proximal";
    public static final String BUYER_DISTAL = "buyer_distal";
    public static final String SELLER_PROXIMAL = "seller_proximal";
    public static final String AMOUNT_WILLING_TO_LOSE = "amount_willing_to_lose";
    public static final String STOP_LOSS = "stop_loss";
    public static final String REWARD = "reward";
    public static final String RISK = "risk";
    public static final String PROFIT = "profit";
    public static final String LOSS = "loss";
    public static final String RRRATIO = "rr_Ratio";
    public static final String QUANTITY = "quantity";
    public static final String TOTAL_COST = "total_cost";
    public static final String NOTES = "notes";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "TradeJournal.db", null, 1);
    }

    //This is called the first time a database is created.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + TRADE_JOURNAL_TABLE +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TICKER_SYMBOL + " TEXT, " +
                ATR + " TEXT, " +
                BUYER_PROXIMAL + " TEXT, " +
                BUYER_DISTAL + " TEXT, " +
                SELLER_PROXIMAL + " TEXT, " +
                AMOUNT_WILLING_TO_LOSE + " TEXT, " +
                STOP_LOSS + " TEXT, " +
                REWARD + " TEXT, " +
                RISK + " TEXT, " +
                PROFIT + " TEXT, " +
                LOSS + " TEXT, " +
                RRRATIO + " TEXT, " +
                QUANTITY + " INTEGER, " +
                TOTAL_COST + " TEXT, " +
                NOTES + " TEXT)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public List<TradeJournal> getAllTradeJournals() {
        List<TradeJournal> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + TRADE_JOURNAL_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                extracted(returnList, cursor);
            } while (cursor.moveToNext());
        } else {
            //No results
        }

        closeConnections(db, cursor);
        return returnList;
    }

    private void extracted(List<TradeJournal> returnList, Cursor cursor) {
        int id = cursor.getInt(0);
        String tickerSymbol = cursor.getString(1);
        BigDecimal atr = new BigDecimal(cursor.getString(2));
        BigDecimal buyerProximal = new BigDecimal(cursor.getString(3));
        BigDecimal buyerDistal = new BigDecimal(cursor.getString(4));
        BigDecimal sellerProximal = new BigDecimal(cursor.getString(5));
        BigDecimal amountWillingToLose = new BigDecimal(cursor.getString(6));
        BigDecimal stopLoss = new BigDecimal(cursor.getString(7));
        BigDecimal reward = new BigDecimal(cursor.getString(8));
        BigDecimal risk = new BigDecimal(cursor.getString(9));
        BigDecimal profit = new BigDecimal(cursor.getString(10));
        BigDecimal loss = new BigDecimal(cursor.getString(11));
        BigDecimal rrRatio = new BigDecimal(cursor.getString(12));
        int quantity = cursor.getInt(13);
        BigDecimal totalCost = new BigDecimal(cursor.getString(14));
        String notes = cursor.getString(15);
        if (notes.equals(null)) notes = "";

        TradeJournal tradeJournal = new TradeJournal(id, tickerSymbol, atr, buyerProximal, buyerDistal, sellerProximal, amountWillingToLose, stopLoss, reward, risk, profit, loss, rrRatio, quantity, totalCost, notes);
        returnList.add(tradeJournal);
    }

    public TradeJournal getTradeJournalById(int id) {
        String queryString =  "SELECT * FROM " + TRADE_JOURNAL_TABLE + " WHERE ID=" + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        List<TradeJournal> returnList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                extracted(returnList, cursor);
            } while (cursor.moveToNext());
        } else {
            //No results
        }

        closeConnections(db, cursor);
        return returnList.get(0);
    }


    public boolean addTrade(TradeJournal tradeJournal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TICKER_SYMBOL, tradeJournal.getTickerSymbol());
        cv.put(ATR, tradeJournal.getAtr().toString());
        cv.put(BUYER_PROXIMAL, tradeJournal.getBuyerProximal().toString());
        cv.put(BUYER_DISTAL, tradeJournal.getBuyerDistal().toString());
        cv.put(SELLER_PROXIMAL, tradeJournal.getSellerProximal().toString());
        cv.put(AMOUNT_WILLING_TO_LOSE, tradeJournal.getAmountWillingToLose().toString());
        cv.put(STOP_LOSS, tradeJournal.getStopLoss().toString());
        cv.put(REWARD, tradeJournal.getReward().toString());
        cv.put(RISK, tradeJournal.getRisk().toString());
        cv.put(PROFIT, tradeJournal.getProfit().toString());
        cv.put(LOSS, tradeJournal.getLoss().toString());
        cv.put(RRRATIO, tradeJournal.getRrRatio().toString());
        cv.put(QUANTITY, tradeJournal.getQuantity());
        cv.put(TOTAL_COST, tradeJournal.getTotalCost().toString());
        if (tradeJournal.getNotes() == null) {
            tradeJournal.setNotes("");
        }
        cv.put(NOTES, tradeJournal.getNotes());

        long insert = db.insert(TRADE_JOURNAL_TABLE, null, cv);
        db.close();
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteTradeJournal(int id) {
        String queryString =  "DELETE FROM " + TRADE_JOURNAL_TABLE + " WHERE ID = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            closeConnections(db, cursor);
            return true;
        }else {
            closeConnections(db, cursor);
            return false;
        }
    }

    private void closeConnections(SQLiteDatabase db, Cursor cursor) {
        cursor.close();
        db.close();
    }

    public boolean updateTradeJournal(TradeJournal tradeJournal) {

        String queryString =  "UPDATE " + TRADE_JOURNAL_TABLE + " SET " +
                ATR + " = " + tradeJournal.getAtr().toString() + ", " +
                BUYER_PROXIMAL + " = " + tradeJournal.getBuyerProximal().toString() + ", " +
                BUYER_DISTAL + " = " + tradeJournal.getBuyerDistal().toString() + ", " +
                SELLER_PROXIMAL + " = " + tradeJournal.getSellerProximal().toString() + ", " +
                AMOUNT_WILLING_TO_LOSE + " = " + tradeJournal.getAmountWillingToLose().toString() + ", " +
                STOP_LOSS + " = " + tradeJournal.getStopLoss().toString() + ", " +
                REWARD + " = " + tradeJournal.getReward().toString() + ", " +
                RISK + " = " + tradeJournal.getRisk().toString() + ", " +
                PROFIT + " = " + tradeJournal.getProfit().toString() + ", " +
                LOSS + " = " + tradeJournal.getLoss().toString() + ", " +
                RRRATIO + " = " + tradeJournal.getRrRatio().toString() + ", " +
                QUANTITY + " = " + tradeJournal.getQuantity() + ", " +
                TOTAL_COST + " = " + tradeJournal.getTotalCost().toString() + ", " +
                NOTES + " =\"" + tradeJournal.getNotes().replaceAll("'","\\'").replaceAll("\\s+$", "") + "\"" +
                " WHERE ID= " + tradeJournal.getId() ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            closeConnections(db, cursor);
            return true;
        }else {
            closeConnections(db, cursor);
            return false;
        }
    }
}
