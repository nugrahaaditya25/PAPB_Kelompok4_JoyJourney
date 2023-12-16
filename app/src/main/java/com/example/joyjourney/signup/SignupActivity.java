package com.example.joyjourney.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.joyjourney.MainActivity;
import com.example.joyjourney.R;
import com.example.joyjourney.databinding.ActivityLoginBinding;
import com.example.joyjourney.databinding.ActivitySignupBinding;
import com.example.joyjourney.login.LoginActivity;
import com.example.joyjourney.register.RegisterActivity;
import com.example.joyjourney.utils.Utils;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;

    private SignupViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(SignupViewModel.class);

        binding.masukButton.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        binding.daftarButton.setOnClickListener(v -> {
            String email = binding.emailEditText.getText().toString();
            String password = binding.passwordEditText.getText().toString();
            String passwordConfirrmation = binding.passwordConfirmation.getEditText().getText().toString();

            if(!password.equals(passwordConfirrmation)){
                Toast.makeText(this, "Password tidak sama", Toast.LENGTH_SHORT).show();
                return;
            }
            if(Utils.isValidEmail(email)){
                viewModel.registerUser(email, password);
            }else{
                binding.emailEditText.setError("Email tidak valid");
            }
        });

        viewModel.getNavigateToNextScreen().observe(this, navigate->{
            if(navigate){
                Intent intent = new Intent(SignupActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        viewModel.getErrorLiveData().observe(this, error->{
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        });
    }
}