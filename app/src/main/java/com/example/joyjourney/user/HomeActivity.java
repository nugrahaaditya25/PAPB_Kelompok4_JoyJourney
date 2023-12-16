package com.example.joyjourney.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;

import com.example.joyjourney.R;
import com.example.joyjourney.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    UserSharedViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        vm = new ViewModelProvider(this).get(UserSharedViewModel.class);
        vm.fetchUser();
        vm.fetchWahana();
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.homeFragmentContainer);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.userBottomNavigation, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {

        });
    }
}