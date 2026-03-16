package com.example.paisa.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.paisa.activities.LoginActivity;
import com.example.paisa.databinding.FragmentProfileBinding;
import com.example.paisa.viewmodels.StockViewModel;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private StockViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(StockViewModel.class);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            binding.tvProfileEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        }

        viewModel.getUserData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.tvWalletBalance.setText(String.format(Locale.getDefault(), "₹ %.2f", user.getBalance()));
                
                binding.btnAddFunds.setOnClickListener(v -> {
                    // Simple logic to add 50k for testing
                    viewModel.updateBalance(user.getBalance() + 50000);
                });
            }
        });

        binding.btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
