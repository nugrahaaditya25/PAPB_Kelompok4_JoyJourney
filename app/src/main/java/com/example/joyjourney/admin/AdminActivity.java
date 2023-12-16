package com.example.joyjourney.admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.joyjourney.R;
import com.example.joyjourney.databinding.ActivityAdminBinding;

public class AdminActivity extends AppCompatActivity {
    private AdminSharedViewModel sharedVM;
    private ActivityAdminBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedVM = new ViewModelProvider(this).get(AdminSharedViewModel.class);
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fragmentContainerView);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.detailWahanaFragment) {
                binding.bottomNavigationView.setVisibility(View.GONE);
            } else {
                binding.bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });
    }

}