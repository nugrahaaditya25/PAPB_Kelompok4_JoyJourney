package com.example.joyjourney.user.wahanadetail.checkout.fragments;

import android.os.Bundle;
import static com.example.joyjourney.utils.Utils.isNotEmpty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.joyjourney.R;
import com.example.joyjourney.databinding.FragmentIsiDataBinding;
import com.example.joyjourney.model.Pesanan;
import com.example.joyjourney.model.User;
import com.example.joyjourney.model.Wahana;
import com.example.joyjourney.user.wahanadetail.checkout.CheckoutViewModel;

public class IsiDataFragment extends Fragment {
    FragmentIsiDataBinding binding;
    CheckoutViewModel vm;
    Wahana wahana;
    User user;
    Pesanan pesanan;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentIsiDataBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vm = new ViewModelProvider(requireActivity()).get(CheckoutViewModel.class);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.gender_array,
                R.layout.dropdown_item
        );
        wahana = vm.getWahana();
        user = vm.getUser();
        pesanan = vm.getPesanan().getValue();
        pesanan.setWahanaName(wahana.getName());
        pesanan.setWahanaImage(wahana.getImageUrl());
        pesanan.setWahanaId(wahana.getId());
        pesanan.setIsRefundable(wahana.isRefundable());
        binding.genderSpinner.setAdapter(arrayAdapter);
        binding.nameEditText.setText(user.getName());
        binding.phoneEditText.setText(user.getPhoneNumber());
        binding.emailEditText.setText(vm.getCurrentUser().getEmail());
        binding.nextButton.setOnClickListener(v->{
            String name = binding.nameEditText.getText().toString();
            String phone = binding.phoneEditText.getText().toString();
            String gender = binding.genderSpinner.getText().toString();
            String email = binding.emailEditText.getText().toString();

            if(isNotEmpty(name) && isNotEmpty(phone) && isNotEmpty(gender) && isNotEmpty(email)){
                pesanan.setName(name);
                pesanan.setGender(gender);
                pesanan.setPhoneNumber(phone);
                pesanan.setEmail(email);
                vm.setCurrentTab(1);
            }
        });
    }
}