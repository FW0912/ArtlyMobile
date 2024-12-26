package com.mobprog.artlymobile.view;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mobprog.artlymobile.R;
import com.mobprog.artlymobile.controller.UserController;
import com.mobprog.artlymobile.databinding.FragmentProfileBinding;
import com.mobprog.artlymobile.viewmodel.ProfileViewModel;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private UserController userController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        ProfileViewModel  profileViewModel = new ViewModelProvider
                .AndroidViewModelFactory(this.getActivity().getApplication()).create(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        userController = new UserController(getContext());

        final TextView username = binding.tvProfileUsername;
        final TextView balance = binding.tvProfileBalance;

        profileViewModel.getUsername().observe(getViewLifecycleOwner(), username::setText);
        profileViewModel.getBalance().observe(getViewLifecycleOwner(), balance::setText);

        return root;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        Button logout_btn = view.findViewById(R.id.btn_logout);
        logout_btn.setOnClickListener(e -> {
            userController.logout();
        });

        Button topup_btn = view.findViewById(R.id.topup_btn_profile);
        topup_btn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TopupActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshProfileData();
    }

    private void refreshProfileData() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("LoggedInUser", Context.MODE_PRIVATE);
        int balance = sharedPreferences.getInt("balance", 0);

        TextView balanceTextView = getView().findViewById(R.id.tv_profile_balance);
        balanceTextView.setText(String.format("Rp. %,d", balance));
    }


}