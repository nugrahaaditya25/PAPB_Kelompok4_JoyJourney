package com.example.joyjourney.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.joyjourney.MainActivity;
import com.example.joyjourney.R;
import com.example.joyjourney.databinding.ActivityRegisterBinding;
import com.example.joyjourney.model.Pesanan;
import com.example.joyjourney.model.User;
import com.example.joyjourney.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding activityRegisterBinding;
    RegisterViewModel registerViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRegisterBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(activityRegisterBinding.getRoot());

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        activityRegisterBinding.dateEditText.setOnClickListener(view->{
            showDatePickerDialog(view,RegisterActivity.this);
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.gender_array,
                R.layout.dropdown_item
        );

        activityRegisterBinding.genderSpinner.setAdapter(adapter);

        activityRegisterBinding.genderSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item from the adapter
                String selectedGender = (String) parent.getItemAtPosition(position);

                // Show a Toast with the selected gender
                Toast.makeText(RegisterActivity.this, "Selected Gender: " + selectedGender, Toast.LENGTH_SHORT).show();
            }
        });

        activityRegisterBinding.nextButton.setOnClickListener(view->{
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String name = activityRegisterBinding.nameEditText.getEditText().getText().toString().trim();
            String telp = activityRegisterBinding.phoneEditText.getEditText().getText().toString().trim();
            User.Gender gender = activityRegisterBinding.genderSpinner.getText().toString().equals("Laki-Laki")? User.Gender.MALE: User.Gender.FEMALE;
            String date = activityRegisterBinding.dateEditText.getText().toString();
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(telp)  || TextUtils.isEmpty(date)) {
                Toast.makeText(RegisterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }else{
                User user = new User(currentUser.getUid(), name, date, gender, telp, false, new ArrayList<Pesanan>());
                registerViewModel.addUser(user);
            }

        });

        registerViewModel.getIsSuccess().observe(this, isSuccess->{
            if(isSuccess){
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public static void showDatePickerDialog(View view, Context context) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (DatePicker view1, int year, int month, int dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    ((TextInputEditText) view).setText(selectedDate);
                },
                // Set initial date if needed
                1960, 0, 1
        );
        datePickerDialog.show();
    }

}