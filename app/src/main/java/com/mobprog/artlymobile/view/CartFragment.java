package com.mobprog.artlymobile.view;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobprog.artlymobile.R;
import com.mobprog.artlymobile.controller.CartItemController;
import com.mobprog.artlymobile.databinding.FragmentCartBinding;
import com.mobprog.artlymobile.databinding.FragmentHomeBinding;
import com.mobprog.artlymobile.viewmodel.CartViewModel;

public class CartFragment extends Fragment {

    private FragmentCartBinding binding;
    private CartItemController cartItemController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        CartViewModel cartViewModel = new ViewModelProvider
                .AndroidViewModelFactory(this.getActivity().getApplication()).create(CartViewModel.class);

        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        cartItemController = new CartItemController(getContext());

        final TextView tvUsername = binding.tvCartUsername;

        cartViewModel.getUsername().observe(getViewLifecycleOwner(), tvUsername::setText);

        return root;
    }

}