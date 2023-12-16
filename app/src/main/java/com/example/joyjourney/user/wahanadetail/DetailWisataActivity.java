package com.example.joyjourney.user.wahanadetail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.joyjourney.R;
import com.example.joyjourney.databinding.ActivityDetailWisataBinding;
import com.example.joyjourney.model.User;
import com.example.joyjourney.model.Wahana;
import com.example.joyjourney.user.FacilitiesAdapter;
import com.example.joyjourney.user.wahanadetail.checkout.CheckoutActivity;
import com.squareup.picasso.Picasso;

public class DetailWisataActivity extends AppCompatActivity {
    private Wahana wahana;
    private User user;
    ActivityDetailWisataBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityDetailWisataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent i = getIntent();
        wahana = (Wahana) i.getSerializableExtra("wahana");
        user = (User) i.getSerializableExtra("user");
        if(wahana==null || user==null){
            Toast.makeText(this, "There is no wahana || user provided", Toast.LENGTH_SHORT).show();
            finish();
        }
        FacilitiesAdapter adapter = new FacilitiesAdapter(this, wahana.getFacilities());
        binding.facilitiesGrid.setAdapter(adapter);
        binding.titleTv.setText(wahana.getName());
        binding.addressTv.setText(wahana.getAddress());
        binding.openTv.setText(wahana.getOpenDate()+":00 WIB");
        binding.wisataDesc.setText(wahana.getDescription());
        binding.priceTv.setText(formatAmount(wahana.getPrice()));
        Picasso.get().load(wahana.getImageUrl()).into(binding.imageIv);
        binding.backButton.setOnClickListener(v->{
            finish();
        });
        binding.mapIllusration.setOnClickListener(v -> {
            openMap();
        });
        binding.showMap.setOnClickListener(v->{
           openMap();
        });

        binding.bookButton.setOnClickListener(v->{
            Intent intent = new Intent(this, CheckoutActivity.class);
            intent.putExtra("wahana", wahana);
            intent.putExtra("user", user);
            startActivity(intent);
        });
    }

    public static String formatAmount(int amount) {
        if (amount >= 1000) {
            int thousands = amount / 1000;
            return String.format("Rp %dK", thousands);
        } else {
            return String.format("Rp %d", amount);
        }
    }

    private void openMap(){
        Uri gmapsUri = Uri.parse(wahana.getMapsUrl());
        Intent intent = new Intent(Intent.ACTION_VIEW, gmapsUri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }
}