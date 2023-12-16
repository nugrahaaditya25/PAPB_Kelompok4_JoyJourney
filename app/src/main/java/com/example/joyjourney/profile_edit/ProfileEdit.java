package com.example.joyjourney.profile_edit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.joyjourney.R;
import com.example.joyjourney.databinding.ActivityProfileEditBinding;
import com.example.joyjourney.model.User;
import com.example.joyjourney.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class ProfileEdit extends AppCompatActivity {
    User user;
    ActivityProfileEditBinding binding;
    ProfileEditViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        vm = new ViewModelProvider(ProfileEdit.this).get(ProfileEditViewModel.class);
        Intent intent = getIntent();
        if(intent!=null){
            user = (User) intent.getSerializableExtra("user");
        }else{
            finish();
        }

        binding.nameEditText.getEditText().setText(user.getName());
        binding.phoneEditText.getEditText().setText(user.getPhoneNumber());

        binding.editButton.setOnClickListener(v->{
            String name = binding.nameEditText.getEditText().getText().toString().trim();
            String numberPhone = binding.phoneEditText.getEditText().getText().toString().trim();

            if(Utils.isNotEmpty(name) && Utils.isNotEmpty(numberPhone)){
                Map<String, Object> updates = new HashMap<>();
                updates.put("name", name);
                updates.put("phoneNumber", numberPhone);
                vm.updateProfile(user.getUid(), updates, unused -> {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("isSuccess", true);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();

                }, e -> {
                    Toast.makeText(this, "Failed to Update User", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}