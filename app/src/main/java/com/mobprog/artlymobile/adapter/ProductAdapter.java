package com.mobprog.artlymobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobprog.artlymobile.R;
import com.mobprog.artlymobile.model.Product;
import com.mobprog.artlymobile.view.ProductDetailsActivity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> products;
    private Context context;

    public ProductAdapter(Context context, List<Product> products) {
        this.products = products;
        this.context = context;
    }

    public void filterItems(List<Product> filteredProducts) {
        this.products = filteredProducts;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull List<Object> payloads) {
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
    public void onViewRecycled(@NonNull ProductViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull ProductViewHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ProductViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ProductViewHolder holder) {
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
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product p = products.get(position);

        holder.tvProductName.setText(p.getProductName());
        holder.tvProductPrice.setText("IDR " + NumberFormat.getNumberInstance(new Locale("id", "ID")).format(p.getPrice()));
        holder.tvProductDescription.setText(p.getProductDescription());

        if (p.getProductImage() != null) {
            try {
                URL url = new URL(p.getProductImage());
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

        holder.itemView.setOnClickListener((v) -> {
            Intent intent = new Intent(context, ProductDetailsActivity.class);

            intent.putExtra("productId", p.getId());
            intent.putExtra("productImage", p.getProductImage());
            intent.putExtra("productName", p.getProductName());
            intent.putExtra("productPrice", p.getPrice());
            intent.putExtra("productDescription", p.getProductDescription());
            intent.putExtra("productCategory", p.getProductCategory());
            intent.putExtra("productType", p.getProductType());
            intent.putExtra("productStock", p.getStock());

            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName;
        TextView tvProductPrice;
        TextView tvProductDescription;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.iv_item_product_image);
            tvProductName = itemView.findViewById(R.id.tv_item_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_item_product_price);
            tvProductDescription = itemView.findViewById(R.id.tv_item_product_description);
        }
    }
}