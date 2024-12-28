package com.mobprog.artlymobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobprog.artlymobile.R;
import com.mobprog.artlymobile.model.TransactionDetails;
import com.mobprog.artlymobile.view.ProductDetailsActivity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransactionDetailsAdapter extends RecyclerView.Adapter<TransactionDetailsAdapter.TransactionDetailsViewHolder> {
    private List<TransactionDetails> transactionDetails;
    private Context context;

    public TransactionDetailsAdapter(List<TransactionDetails> transactionDetails, Context context) {
        this.transactionDetails = transactionDetails;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionDetailsViewHolder holder, int position, @NonNull List<Object> payloads) {
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
    public void onViewRecycled(@NonNull TransactionDetailsViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull TransactionDetailsViewHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull TransactionDetailsViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull TransactionDetailsViewHolder holder) {
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
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public TransactionDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction_details, parent, false);
        return new TransactionDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionDetailsViewHolder holder, int position) {
        TransactionDetails details = transactionDetails.get(position);

        holder.tvProductName.setText(details.getProductName());
        holder.tvProductPrice.setText("IDR " + NumberFormat.getNumberInstance(new Locale("id", "ID")).format(details.getProductPrice()));
        if(details.getQty() == 1 || details.getProductType().equals("Digital")) {
            holder.tvQuantity.setText("1 Item");
            holder.tvTotalPrice.setText("IDR " + NumberFormat.getNumberInstance(new Locale("id", "ID")).format(details.getProductPrice()));
        }
        else {
            holder.tvQuantity.setText(details.getQty() + " Items");
            holder.tvTotalPrice.setText("IDR " + NumberFormat.getNumberInstance(new Locale("id", "ID")).format(details.getProductPrice() * details.getQty()));
        }

        if (details.getProductImage() != null) {
            try {
                URL url = new URL(details.getProductImage());
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());

                executor.execute(() -> {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                        handler.post(() -> {
                            holder.ivProductImage.setImageBitmap(bitmap);
                        });
                    } catch (IOException e) {
                        holder.ivProductImage.setImageDrawable(context.getDrawable(R.drawable.ic_image_not_found));
                    }
                });
            } catch (MalformedURLException e) {
                holder.ivProductImage.setImageDrawable(context.getDrawable(R.drawable.ic_image_not_found));
            }
        }

        holder.btnProductDetails.setOnClickListener((v) -> {
            Intent intent = new Intent(context, ProductDetailsActivity.class);
            intent.putExtra("productId", details.getProductId());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return transactionDetails.size();
    }

    public static class TransactionDetailsViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName, tvProductPrice, tvQuantity, tvTotalPrice;
        Button btnProductDetails;

        public TransactionDetailsViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProductImage = itemView.findViewById(R.id.iv_transaction_details_product_image);

            tvProductName = itemView.findViewById(R.id.tv_transaction_details_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_transaction_details_product_price);
            tvQuantity = itemView.findViewById(R.id.tv_transaction_details_quantity);
            tvTotalPrice = itemView.findViewById(R.id.tv_transaction_details_total_price);

            btnProductDetails = itemView.findViewById(R.id.btn_transaction_details_product_details);
        }
    }
}
