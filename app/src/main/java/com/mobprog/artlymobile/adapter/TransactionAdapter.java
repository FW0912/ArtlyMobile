package com.mobprog.artlymobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobprog.artlymobile.R;
import com.mobprog.artlymobile.model.Transaction;
import com.mobprog.artlymobile.view.TransactionDetailsActivity;
import com.mobprog.artlymobile.viewmodel.TransactionsViewModel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private List<Transaction> transactions;
    private Context context;
    private TransactionsViewModel transactionsViewModel;
    private final String DATE_FORMAT = "dd-MMMM-yyyy";
    private final String TIME_FORMAT = "hh:mm a";
    private SimpleDateFormat dateFormatter, timeFormatter;

    public TransactionAdapter(List<Transaction> transactions, TransactionsViewModel transactionsViewModel, Context context) {
        this.transactions = transactions;
        this.transactionsViewModel = transactionsViewModel;
        this.context = context;
        dateFormatter = new SimpleDateFormat(DATE_FORMAT);
        timeFormatter = new SimpleDateFormat(TIME_FORMAT);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.TransactionViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onViewRecycled(@NonNull TransactionAdapter.TransactionViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull TransactionAdapter.TransactionViewHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull TransactionAdapter.TransactionViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull TransactionAdapter.TransactionViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }

    @Override
    public void unregisterAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        transactionsViewModel.setTransactionsCount(transactions.size());
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);

        holder.tvTransactionDate.setText(dateFormatter.format(transaction.getOrderDate()));
        holder.tvTransactionTime.setText(timeFormatter.format(transaction.getOrderDate()));
        holder.tvPaymentMethod.setText(transaction.getPaymentMethod());
        holder.tvTransactionStatus.setText(transaction.getTransactionStatus());
        if(transaction.getProductCount() == 1) {
            holder.tvProductCount.setText(transaction.getProductCount() + " Product");
        }
        else {
            holder.tvProductCount.setText(transaction.getProductCount() + " Products");
        }
        if(transaction.getQuantityCount() == 1) {
            holder.tvQuantityCount.setText(transaction.getQuantityCount() + " Item");
        }
        else {
            holder.tvQuantityCount.setText(transaction.getQuantityCount() + " Items");
        }
        holder.tvTotalPrice.setText("IDR " +
                NumberFormat.getNumberInstance(new Locale("id", "ID")).format(transaction.getTotalPrice()));

        holder.itemView.setOnClickListener((v) -> {
            Intent intent = new Intent(context, TransactionDetailsActivity.class);
            intent.putExtra("transactionId", transaction.getId());
            intent.putExtra("transactionDate",
                    dateFormatter.format(transaction.getOrderDate()) + ", " +
                            timeFormatter.format(transaction.getOrderDate()));
            intent.putExtra("paymentMethod", transaction.getPaymentMethod());
            intent.putExtra("productCount", transaction.getProductCount());
            intent.putExtra("quantityCount", transaction.getQuantityCount());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView tvTransactionDate, tvTransactionTime, tvPaymentMethod, tvTransactionStatus,
                tvProductCount, tvQuantityCount, tvTotalPrice;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTransactionDate = itemView.findViewById(R.id.tv_item_transaction_date);
            tvTransactionTime = itemView.findViewById(R.id.tv_item_transaction_time);
            tvPaymentMethod = itemView.findViewById(R.id.tv_item_transaction_payment_method);
            tvTransactionStatus = itemView.findViewById(R.id.tv_item_transaction_status);
            tvProductCount = itemView.findViewById(R.id.tv_item_transaction_product_count);
            tvQuantityCount = itemView.findViewById(R.id.tv_item_transaction_quantity_count);
            tvTotalPrice = itemView.findViewById(R.id.tv_item_transaction_total_price);
        }
    }
}
