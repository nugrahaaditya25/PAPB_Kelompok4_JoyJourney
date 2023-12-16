package com.example.joyjourney.user.wahanadetail.checkout.fragments;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.joyjourney.R;
import com.example.joyjourney.databinding.FragmentBayarBinding;
import com.example.joyjourney.databinding.FragmentKonfirmasifragmentBinding;
import com.example.joyjourney.model.Pesanan;
import com.example.joyjourney.user.wahanadetail.checkout.CheckoutViewModel;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Konfirmasifragment extends Fragment {
    FragmentKonfirmasifragmentBinding binding;
    CheckoutViewModel vm;
    Date dateNow;
    Pesanan pesanan;
    ClipboardManager clipboard;
    String randomString;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentKonfirmasifragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        randomString = generateRandomPaymentCode();
        dateNow = Calendar.getInstance().getTime();
        clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);

        vm = new ViewModelProvider(requireActivity()).get(CheckoutViewModel.class);
        pesanan = vm.getPesanan().getValue();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateNow);
        calendar.add(Calendar.MINUTE,10);
        Date tenggat = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
        String strWaktuJatuhTempo = sdf.format(tenggat);

        binding.copyButton.setOnClickListener(v->{
            ClipData clip = ClipData.newPlainText("Copied Text", randomString);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getContext(), "Text copied to clipboard", Toast.LENGTH_SHORT).show();
        });
        vm.getPesanan().observe(getViewLifecycleOwner(), pesanan1 -> {
            try {
                setPaymentImage(binding.paymentIV, pesanan1);
                binding.paymentCodeTV.setText(randomString);
                binding.priceTV.setText(pesanan1.getTotalPrice()+"");
                binding.tenggat.setText(strWaktuJatuhTempo);
            }catch (Exception e){
                Log.e("konfirmasi", e.getLocalizedMessage() + e.toString());
                Log.e("konfirmasi", pesanan1.getTotalPrice() + e.toString());
            }

        });

        binding.okButton.setOnClickListener(v->{
            vm.addPesanan();
        });

        vm.getIsSuccess().observe( getViewLifecycleOwner(), aBoolean -> {
            if(aBoolean){
                getActivity().finish();
            }
        });

        binding.cancelButton.setOnClickListener(v->{
            getActivity().finish();
        });

    }

    void setPaymentImage(ImageView iv, Pesanan pesanan){
        switch (pesanan.getPaymentMethod()){
            case "BCA":
                iv.setImageResource(R.drawable.bca);
            break;
            case "BRI":
                iv.setImageResource(R.drawable.bri);
                break;
            case "BNI":
                iv.setImageResource(R.drawable.bni);
                break;
                case "CIMB":
                iv.setImageResource(R.drawable.cimb);
                break;
            case"DANA":
                iv.setImageResource(R.drawable.dana);
                break;
            case"Shopee Pay":
                iv.setImageResource(R.drawable.shopee);
                break;
            case"Gopay":
                iv.setImageResource(R.drawable.gopay);
                break;
            case"OVO":
                iv.setImageResource(R.drawable.ovo);
                break;
            case"iSaku":
                iv.setImageResource(R.drawable.isaku);
                break;
            case"LinkAja":
                iv.setImageResource(R.drawable.linkaja);
                break;
            case"Alfamart":
                iv.setImageResource(R.drawable.alfamart);
                break;
            case"Circle K":
                iv.setImageResource(R.drawable.circlek);
                break;
            case"Indomaret":
                iv.setImageResource(R.drawable.indomaret);
                break;
        }
    }

    private String generateRandomPaymentCode() {
        int length = 10;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder randomCode = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            randomCode.append(characters.charAt(random.nextInt(characters.length())));
        }

        return randomCode.toString();
    }

}