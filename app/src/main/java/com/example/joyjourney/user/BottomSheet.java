package com.example.joyjourney.user;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joyjourney.databinding.FragmentBottomSheetBinding;
import com.example.joyjourney.model.User;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.HashMap;
import java.util.Map;

public class BottomSheet extends BottomSheetDialogFragment {
    int startPrice;
    int endPrice;
    Map<String, Boolean> facilities;
    UserSharedViewModel vm;
    FragmentBottomSheetBinding binding;
    String name;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getArguments().getString("search");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vm = new ViewModelProvider(requireActivity()).get(UserSharedViewModel.class);
        facilities = vm.getFacilities().getValue();
        startPrice = vm.getStartPrice().getValue();
        endPrice = vm.getEndPrice().getValue();

        vm.getStartPrice().observe(getViewLifecycleOwner(), integer -> {
            startPrice = integer;
        });

        vm.getEndPrice().observe(getViewLifecycleOwner(), integer -> {
            endPrice = integer;
        });
        vm.getFacilities().observe(getViewLifecycleOwner(), map->{
            facilities = map;
        });

        binding.endPrice.setText(endPrice==-1?0+"":endPrice+"");
        binding.startPrice.setText(startPrice+"");
        ChipGroup chipGroup = binding.chipGroup;

        for(int i = 0; i<chipGroup.getChildCount(); i++){
            Chip chip = (Chip) chipGroup.getChildAt(i);
            String key = chip.getText().toString();
            chip.setChecked(facilities.get(key));
        }

        binding.filterButton.setOnClickListener(v->{
            int startPrice = Integer.parseInt(binding.startPrice.getText().toString());
            int endPrice = Integer.parseInt(binding.endPrice.getText().toString());
            for(int i = 0; i<chipGroup.getChildCount(); i++){
                Chip chip = (Chip) chipGroup.getChildAt(i);
                String key = chip.getText().toString();
                if(chip.isChecked()){
                    facilities.replace(key, true);
                }else{
                    facilities.replace(key, false);
                }
            }
            vm.fetchQueriedWahana(name, startPrice, endPrice, facilities);
            dismiss();
        });
    }
}