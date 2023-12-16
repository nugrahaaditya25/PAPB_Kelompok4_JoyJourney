package com.example.joyjourney.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joyjourney.R;
import com.example.joyjourney.admin.PesananAdapter;
import com.example.joyjourney.databinding.FragmentUserRiwayatBinding;
import com.example.joyjourney.model.Pesanan;
import com.example.joyjourney.utils.PesananDialog;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UserRiwayatFragment extends Fragment {
    FragmentUserRiwayatBinding binding;
    UserSharedViewModel vm;
    List<Pesanan> pesananList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserRiwayatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vm = new ViewModelProvider(requireActivity()).get(UserSharedViewModel.class);
        pesananList = new ArrayList<>();
        vm.fetchPesanan();

        PesananAdapter adapter = new PesananAdapter(PesananAdapter.type.User, pesananList, getContext(), pesanan -> {
            PesananDialog pesananDialog = new PesananDialog(requireActivity(), pesanan);
            pesananDialog.showDialog();
        });

        binding.chipAktif.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.chipInactive.setChecked(false);
                filterIsUsed(false, adapter);
            }
        });

        binding.chipInactive.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.chipAktif.setChecked(false);
                filterIsUsed(true, adapter);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.historyRecycler.setLayoutManager(layoutManager);
        binding.historyRecycler.setAdapter(adapter);

        vm.getListPesanan().observe(getViewLifecycleOwner(), pesanans -> {
            pesananList.clear();
            pesananList.addAll(pesanans);
            adapter.notifyDataSetChanged();
        });
    }

    private void filterIsUsed(boolean isUsed, PesananAdapter adapter){
        pesananList.clear();
        List<Pesanan> list = new ArrayList<>();
        vm.getListPesanan().getValue().forEach(pesanan -> {
            if(pesanan.getIsUsed()==isUsed){
                list.add(pesanan);
            }
        });
        pesananList.addAll(list);
        adapter.notifyDataSetChanged();
    }
}