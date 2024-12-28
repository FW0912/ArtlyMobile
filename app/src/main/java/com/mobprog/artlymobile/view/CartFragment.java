package com.mobprog.artlymobile.view;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mobprog.artlymobile.R;
import com.mobprog.artlymobile.controller.CartItemController;
import com.mobprog.artlymobile.databinding.FragmentCartBinding;
import com.mobprog.artlymobile.utils.SuccessToast;
import com.mobprog.artlymobile.viewmodel.CartViewModel;

public class CartFragment extends Fragment {
    private CartViewModel cartViewModel;
    private FragmentCartBinding binding;
    private CartItemController cartItemController;
    private RecyclerView rvCartItems;
    private Button btnPurchase;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        cartViewModel = new ViewModelProvider
                .AndroidViewModelFactory(this.getActivity().getApplication()).create(CartViewModel.class);

        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        cartItemController = new CartItemController(getContext());

        final TextView tvUsername = binding.tvCartUsername;
        final TextView tvTotalItems = binding.tvCartTotalItems;
        final TextView tvTotalPrice = binding.tvCartTotalPrice;
        final TextView tvCurrentBalance = binding.tvCartCurrentBalance;
        final TextView tvBalanceAfter = binding.tvCartBalanceAfter;
        final TextView tvNotEnoughBalance = binding.tvCartNotEnoughBalance;
        rvCartItems = binding.rvCartItems;
        btnPurchase = binding.btnCartPurchase;

        cartViewModel.getUsername().observe(getViewLifecycleOwner(), tvUsername::setText);
        cartViewModel.getTotalItems().observe(getViewLifecycleOwner(), totalItems -> {
            if(totalItems == 0) {
                ((ViewGroup) rvCartItems.getParent()).removeView(rvCartItems);
                ((ViewGroup) btnPurchase.getParent()).removeView(btnPurchase);
            }
        });
        cartViewModel.getTotalItemsString().observe(getViewLifecycleOwner(), tvTotalItems::setText);
        cartViewModel.getTotalPrice().observe(getViewLifecycleOwner(), tvTotalPrice::setText);
        cartViewModel.getCurrentBalance().observe(getViewLifecycleOwner(), tvCurrentBalance::setText);
        cartViewModel.getBalanceAfter().observe(getViewLifecycleOwner(), balanceAfter -> {
            if(balanceAfter < 0) {
                tvNotEnoughBalance.setVisibility(View.VISIBLE);
                btnPurchase.setVisibility(View.GONE);
            }
            else {
                tvNotEnoughBalance.setVisibility(View.GONE);
                btnPurchase.setVisibility(View.VISIBLE);
            }
        });
        cartViewModel.getBalanceAfterString().observe(getViewLifecycleOwner(), tvBalanceAfter::setText);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Cart", Context.MODE_PRIVATE);

        if(sharedPreferences.contains("CartItems")) {
            rvCartItems.setLayoutManager(new LinearLayoutManager(getContext()));
            cartItemController.bindCart(rvCartItems, cartViewModel);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Are you sure?")
                    .setPositiveButton("Confirm", (dialog, which) -> cartItemController.buyProducts(rvCartItems, cartViewModel))
                    .setNegativeButton("Cancel", null);

            btnPurchase.setOnClickListener((v) -> {
                AlertDialog dialog = builder.create();
                dialog.show();
            });
        }
        else {
            ((ViewGroup) rvCartItems.getParent()).removeView(rvCartItems);
            ((ViewGroup) btnPurchase.getParent()).removeView(btnPurchase);
        }
    }
}