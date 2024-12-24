package com.mobprog.artlymobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobprog.artlymobile.R;
import com.mobprog.artlymobile.controller.CartItemController;
import com.mobprog.artlymobile.model.CartItem;
import com.mobprog.artlymobile.model.Product;
import com.mobprog.artlymobile.utils.RangeInputFilter;
import com.mobprog.artlymobile.view.ProductDetailsActivity;
import com.mobprog.artlymobile.viewmodel.CartViewModel;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {
    private List<CartItem> cartItems;
    private CartViewModel cartViewModel;
    private Context context;

    public CartItemAdapter(Context context, CartViewModel cartViewModel, List<CartItem> cartItems) {
        this.cartItems = cartItems;
        this.cartViewModel = cartViewModel;
        this.context = context;
    }

    private void updateTotalItems() {
        cartViewModel.setTotalItems(cartItems.size());
    }

    private void updateTotalPrice() {
        int totalPrice = 0;

        for(CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            totalPrice += (product.getPrice() * cartItem.getQty());
        }

        cartViewModel.setTotalPrice(totalPrice);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemAdapter.CartItemViewHolder holder, int position, @NonNull List<Object> payloads) {
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
    public void onViewRecycled(@NonNull CartItemAdapter.CartItemViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull CartItemAdapter.CartItemViewHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull CartItemAdapter.CartItemViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull CartItemAdapter.CartItemViewHolder holder) {
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

        updateTotalItems();
        updateTotalPrice();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public CartItemAdapter.CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_item, parent, false);
        return new CartItemAdapter.CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemAdapter.CartItemViewHolder holder, int position) {
        CartItemController cartController = new CartItemController(holder.itemView.getContext());
        CartItem cartItem = cartItems.get(position);
        Product p = cartItem.getProduct();

        RangeInputFilter rangeInputFilter = new RangeInputFilter(1, p.getStock());
        InputFilter[] filters = new InputFilter[] {rangeInputFilter};
        holder.etQty.setFilters(filters);

        holder.tvProductName.setText(p.getProductName());
        holder.tvProductPrice.setText("IDR " + NumberFormat.getNumberInstance(new Locale("id", "ID")).format(p.getPrice()));

        if(p.getProductType().equals("Digital")) {
            ((ViewGroup) holder.tvProductStock.getParent()).removeAllViews();
            ((ViewGroup) holder.llQty.getParent()).removeView(holder.llQty);
        }
        else {
            holder.tvProductStock.setText(String.valueOf(p.getStock()));
            holder.etQty.setText(String.valueOf(cartItem.getQty()));
        }

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

        holder.btnDetails.setOnClickListener((v) -> {
            Intent intent = new Intent(context, ProductDetailsActivity.class);

            intent.putExtra("productId", p.getId());
            intent.putExtra("productImage", p.getProductImage());
            intent.putExtra("productName", p.getProductName());
            intent.putExtra("productPrice", p.getPrice());
            intent.putExtra("productDescription", p.getProductDescription());
            intent.putExtra("productCategory", p.getProductCategory());
            intent.putExtra("productType", p.getProductType());
            intent.putExtra("productStock", p.getStock());
            intent.putExtra("fromCart", true);

            context.startActivity(intent);
        });

        holder.btnRemove.setOnClickListener((v) -> {
            cartController.removeFromCart(p.getId());
            cartItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItems.size());
            updateTotalItems();
            updateTotalPrice();
        });

        holder.etQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String qtyString = holder.etQty.getText().toString();

                if(qtyString.isEmpty()) {
                    return;
                }

                int qty = Integer.parseInt(qtyString);

                if(qty >= 1 && qty <= p.getStock()) {
                    cartController.changeCartItemQty(p.getId(), qty);
                    cartItem.setQty(qty);
                    updateTotalPrice();
                }
            }
        });

        holder.btnDecrementQty.setOnClickListener((v) -> {
            String qtyString = holder.etQty.getText().toString();

            if(qtyString.isEmpty()) {
                holder.tvProductStock.setText("1");
                holder.etQty.setText("1");
                return;
            }

            int qty = Integer.parseInt(qtyString);

            if(qty > 1) {
                qty--;
                holder.tvProductStock.setText(String.valueOf(qty));
                holder.etQty.setText(String.valueOf(qty));
            }
        });

        holder.btnIncrementQty.setOnClickListener((v) -> {
            String qtyString = holder.etQty.getText().toString();

            if(qtyString.isEmpty()) {
                holder.tvProductStock.setText("1");
                holder.etQty.setText("1");
                return;
            }

            int qty = Integer.parseInt(qtyString);

            if(qty < p.getStock()) {
                qty++;
                holder.tvProductStock.setText(String.valueOf(qty));
                holder.etQty.setText(String.valueOf(qty));
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartItemViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName, tvProductPrice, tvProductStock;
        EditText etQty;
        Button btnDecrementQty, btnIncrementQty, btnDetails, btnRemove;
        LinearLayout llQty;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.iv_item_cart_item_image);

            tvProductName = itemView.findViewById(R.id.tv_item_cart_item_name);
            tvProductPrice = itemView.findViewById(R.id.tv_item_cart_item_price);
            tvProductStock = itemView.findViewById(R.id.tv_item_cart_item_stock);

            etQty = itemView.findViewById(R.id.et_cart_item_quantity);

            btnDecrementQty = itemView.findViewById(R.id.btn_cart_item_decrement_quantity);
            btnIncrementQty = itemView.findViewById(R.id.btn_cart_item_increment_quantity);
            btnDetails = itemView.findViewById(R.id.btn_cart_item_details);
            btnRemove = itemView.findViewById(R.id.btn_cart_item_remove);

            llQty = itemView.findViewById(R.id.ll_cart_item_quantity);
        }
    }
}
