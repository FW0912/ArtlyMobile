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
import com.mobprog.artlymobile.databinding.FragmentProfileBinding;
import com.mobprog.artlymobile.viewmodel.ProfileViewModel;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel mViewModel;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        ProfileViewModel  profileViewModel = new ViewModelProvider
                .AndroidViewModelFactory(this.getActivity().getApplication()).create(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView username = binding.tvProfileUsername;
        final TextView balance = binding.tvProfileBalance;

        profileViewModel.getUsername().observe(getViewLifecycleOwner(), username::setText);
        profileViewModel.getBalance().observe(getViewLifecycleOwner(), balance::setText);

        return root;
    }

}