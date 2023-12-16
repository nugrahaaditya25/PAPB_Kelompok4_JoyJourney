package com.example.joyjourney.user.wahanadetail.checkout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.joyjourney.user.wahanadetail.checkout.fragments.BayarFragment;
import com.example.joyjourney.user.wahanadetail.checkout.fragments.IsiDataFragment;
import com.example.joyjourney.user.wahanadetail.checkout.fragments.Konfirmasifragment;

public class CheckoutViewPageAdapter extends FragmentStateAdapter {

    public CheckoutViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new IsiDataFragment();
            case 1:
                return new BayarFragment();
            case 2:
                return new Konfirmasifragment();
            default:
                return new IsiDataFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
