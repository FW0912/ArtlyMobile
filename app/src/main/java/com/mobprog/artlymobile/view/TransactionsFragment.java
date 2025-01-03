package com.mobprog.artlymobile.view;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobprog.artlymobile.controller.TransactionController;
import com.mobprog.artlymobile.databinding.FragmentTransactionsBinding;
import com.mobprog.artlymobile.viewmodel.TransactionsViewModel;

public class TransactionsFragment extends Fragment {

    private TransactionsViewModel transactionViewModel;
    private FragmentTransactionsBinding binding;
    private TransactionController controller;
    private RecyclerView rvTransactions;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        transactionViewModel = new ViewModelProvider
                .AndroidViewModelFactory(this.getActivity().getApplication()).create(TransactionsViewModel.class);

        binding = FragmentTransactionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView tvUsername = binding.tvTransactionsUsername;
        final TextView tvTransactionsCount = binding.tvTransactionsCount;
        rvTransactions = binding.rvTransactions;

        transactionViewModel.getUsername().observe(getViewLifecycleOwner(), tvUsername::setText);
        transactionViewModel.getTransactionsCount().observe(getViewLifecycleOwner(), tvTransactionsCount::setText);

        controller = new TransactionController(getContext());

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvTransactions.setLayoutManager(new LinearLayoutManager(getContext()));
        controller.bindTransactions(rvTransactions, transactionViewModel);
    }
}