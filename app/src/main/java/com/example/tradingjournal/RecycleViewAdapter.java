package com.example.tradingjournal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tradingjournal.model.TradeJournal;

import java.math.BigDecimal;
import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {

    List<TradeJournal> tradeJournalList;
    Context context;

    public RecycleViewAdapter(List<TradeJournal> tradeJournalList, Context context) {
        this.tradeJournalList = tradeJournalList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_line_trade_journal, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_tickerSymbol.setText(tradeJournalList.get(position).getTickerSymbol());
        holder.tv_atr.setText(String.valueOf(tradeJournalList.get(position).getAtr()));
        holder.tv_buyerProximal.setText(String.valueOf(tradeJournalList.get(position).getBuyerProximal()));
        holder.tv_buyerDistal.setText(String.valueOf(tradeJournalList.get(position).getBuyerDistal()));
        holder.tv_sellerProximal.setText(String.valueOf(tradeJournalList.get(position).getSellerProximal()));
        holder.tv_amountWillingToLose.setText(String.valueOf(tradeJournalList.get(position).getAmountWillingToLose()));
        holder.tv_stopLoss.setText(String.valueOf(tradeJournalList.get(position).getStopLoss()));
        holder.tv_reward.setText(String.valueOf(tradeJournalList.get(position).getReward()));
        holder.tv_risk.setText(String.valueOf(tradeJournalList.get(position).getRisk()));
        holder.tv_profit.setText(String.valueOf(tradeJournalList.get(position).getProfit()));
        holder.tv_loss.setText(String.valueOf(tradeJournalList.get(position).getLoss()));
        String rrString = "RR: " + String.valueOf(tradeJournalList.get(position).getRrRatio());
        holder.tv_rrRatio.setText(rrString);
        holder.tv_quantity.setText(String.valueOf(tradeJournalList.get(position).getQuantity()));
        holder.tv_totalCost.setText(String.valueOf(tradeJournalList.get(position).getTotalCost()));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditTradeActivity.class);
                intent.putExtra("id", tradeJournalList.get(holder.getAdapterPosition()).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tradeJournalList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_tickerSymbol;
        TextView tv_atr ;
        TextView tv_buyerProximal;
        TextView tv_buyerDistal;
        TextView tv_sellerProximal;
        TextView tv_amountWillingToLose;
        TextView tv_stopLoss;
        TextView tv_reward;
        TextView tv_risk;
        TextView tv_profit;
        TextView tv_loss;
        TextView tv_rrRatio;
        TextView tv_quantity;
        TextView tv_totalCost;
        TableLayout parentLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tickerSymbol = itemView.findViewById(R.id.tv_tickerSymbol);
            tv_atr = itemView.findViewById(R.id.tv_atr);
            tv_buyerProximal = itemView.findViewById(R.id.tv_buyerProximal);
            tv_buyerDistal = itemView.findViewById(R.id.tv_buyerDistal);
            tv_sellerProximal = itemView.findViewById(R.id.tv_sellerProximal);
            tv_amountWillingToLose = itemView.findViewById(R.id.tv_amountWillingToLose);
            tv_stopLoss = itemView.findViewById(R.id.tv_stopLoss);
            tv_reward = itemView.findViewById(R.id.tv_reward);
            tv_risk = itemView.findViewById(R.id.tv_risk);
            tv_profit = itemView.findViewById(R.id.tv_Profit);
            tv_loss = itemView.findViewById(R.id.tv_Loss);
            tv_rrRatio = itemView.findViewById(R.id.tv_rrRatio);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            tv_totalCost = itemView.findViewById(R.id.tv_totalCost);
            parentLayout = itemView.findViewById(R.id.oneLineTradeJournalLayout);
        }
    }
}
