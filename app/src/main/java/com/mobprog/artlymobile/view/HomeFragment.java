package com.mobprog.artlymobile.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobprog.artlymobile.R;
import com.mobprog.artlymobile.controller.ProductController;
import com.mobprog.artlymobile.databinding.FragmentHomeBinding;
import com.mobprog.artlymobile.viewmodel.HomeViewModel;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ProductController productController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel =
                new ViewModelProvider.AndroidViewModelFactory(this.getActivity().getApplication()).create(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        productController = new ProductController(getContext());

        final TextView tvUsername = binding.tvHomeUsername;
        final TextView tvWelcome = binding.tvHomeWelcome;
        final TextView tvBalance = binding.tvHomeBalance;

        homeViewModel.getUsername().observe(getViewLifecycleOwner(), tvUsername::setText);
        homeViewModel.getUsername().observe(getViewLifecycleOwner(), tvWelcome::setText);
        homeViewModel.getBalance().observe(getViewLifecycleOwner(), tvBalance::setText);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv_interested_products = view.findViewById(R.id.rv_home_interested_products);
        rv_interested_products.setLayoutManager(new LinearLayoutManager(getContext()));
        productController.bindInterestedProducts(rv_interested_products);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}