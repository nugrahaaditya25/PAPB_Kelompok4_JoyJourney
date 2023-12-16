package com.example.joyjourney.user.wahanadetail.checkout;

import androidx.appcompat.app.AppCompatActivity;
import static com.example.joyjourney.utils.Utils.isNotEmpty;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

import com.example.joyjourney.R;
import com.example.joyjourney.databinding.ActivityCheckoutBinding;
import com.example.joyjourney.model.Pesanan;
import com.example.joyjourney.model.User;
import com.example.joyjourney.model.Wahana;
import com.example.joyjourney.utils.Utils;
import com.google.android.material.tabs.TabLayout;

public class CheckoutActivity extends AppCompatActivity {
    ActivityCheckoutBinding binding;
    CheckoutViewModel viewModel;
    CheckoutViewPageAdapter adapter;
    Wahana wahana;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent i = getIntent();
        wahana = (Wahana) i.getSerializableExtra("wahana");
        viewModel = new ViewModelProvider(this).get(CheckoutViewModel.class);
        user = (User) i.getSerializableExtra("user");
        if(wahana==null || user == null){
            finish();
        }
        viewModel.setWahana(wahana);
        viewModel.setUser(user);
        viewModel.fetchFirebaseUser();

        TabLayout tabLayout = binding.tabLayout;
        ViewPager2 viewPager = binding.viewPager;
        viewPager.setUserInputEnabled(false);
        binding.backButton.setOnClickListener(v -> {
            finish();
        });
        adapter = new CheckoutViewPageAdapter(this);
        viewPager.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Pesanan pesanan = viewModel.getPesanan().getValue();
                boolean giveAccess = false;
                switch (tab.getPosition()){
                    case 0:
                        giveAccess=true;
                        break;
                    case 1:
                        if(isNotEmpty(pesanan.getName()) && isNotEmpty(pesanan.getGender()) && isNotEmpty(pesanan.getPhoneNumber()) && isNotEmpty(pesanan.getEmail())){
                            giveAccess = true;
                        }
                        break;
                    case 2:
                        if(isNotEmpty(pesanan.getDate()) && isNotEmpty(pesanan.getReservationHour()) && (pesanan.getVisitors()>0) && isNotEmpty(pesanan.getPaymentMethod())){
                            giveAccess = true;
                        }
                        break;
                    default:giveAccess=false;
                }
                if(giveAccess){
                    viewPager.setCurrentItem(tab.getPosition());
                }else{
                    tabLayout.getTabAt(viewPager.getCurrentItem()).select();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewModel.getCurrentTab().observe(this, tabPosition->{
            viewPager.setCurrentItem(tabPosition);
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

    }
}