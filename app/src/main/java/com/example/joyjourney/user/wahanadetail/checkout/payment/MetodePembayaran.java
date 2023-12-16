package com.example.joyjourney.user.wahanadetail.checkout.payment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;

import com.example.joyjourney.R;
import com.example.joyjourney.RadioGridGroup;
import com.example.joyjourney.databinding.ActivityMetodePembayaranBinding;

public class MetodePembayaran extends AppCompatActivity {
    ActivityMetodePembayaranBinding binding;
    String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        result = "";
        binding = ActivityMetodePembayaranBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        RadioGridGroup bank = binding.bankRadioGroup;
        RadioGridGroup ewallet = binding.ewalletRadioGroup;
        RadioGridGroup merchant = binding.merchantRadioGroup;
        RadioGridGroup.OnCheckedChangeListener commonListener = (group, checkedId) -> {
            if (checkedId != -1) {
                if (group == bank) {
                    ewallet.clearCheck();
                    merchant.clearCheck();
                    bank.check(checkedId);
                    result = ((RadioButton)bank.findViewById(checkedId)).getText().toString();
                } else if (group == ewallet) {
                    bank.clearCheck();
                    merchant.clearCheck();
                    ewallet.check(checkedId);
                    result = ((RadioButton)ewallet.findViewById(checkedId)).getTag().toString();
                } else if (group == merchant) {
                    bank.clearCheck();
                    ewallet.clearCheck();
                    merchant.check(checkedId);
                    result = ((RadioButton)merchant.findViewById(checkedId)).getTag().toString();
                }
            }
        };

        // Set the common listener to all three RadioGridGroups
        bank.setOnCheckedChangeListener(commonListener);
        ewallet.setOnCheckedChangeListener(commonListener);
        merchant.setOnCheckedChangeListener(commonListener);

        binding.confirmPayment.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("result", result);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
    }
}