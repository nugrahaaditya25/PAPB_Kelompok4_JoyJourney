package com.example.joyjourney.boarding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import com.example.joyjourney.R;
import com.example.joyjourney.databinding.ActivityBoardingBinding;
import com.example.joyjourney.login.LoginActivity;
import com.example.joyjourney.signup.SignupActivity;

public class BoardingActivity extends AppCompatActivity {
    ActivityBoardingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBoardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.masukButton.setOnClickListener(v -> {
            Intent intent = new Intent(BoardingActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        binding.textButton.setOnClickListener(v -> {
            Intent intent = new Intent(BoardingActivity.this, SignupActivity.class);
            startActivity(intent);
        });

    }
}