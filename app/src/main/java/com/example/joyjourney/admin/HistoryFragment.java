package com.example.joyjourney.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;

import com.example.joyjourney.R;
import com.example.joyjourney.databinding.FragmentHistoryBinding;
import com.example.joyjourney.databinding.FragmentHomeBinding;
import com.example.joyjourney.databinding.FragmentProfileBinding;
import com.example.joyjourney.model.Pesanan;
import com.example.joyjourney.utils.PesananDialog;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HistoryFragment extends Fragment {
    private FragmentHistoryBinding binding;
    private AdminSharedViewModel viewModel;
    List<Pesanan> pesananList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pesananList = new LinkedList<>();
        viewModel = new ViewModelProvider(requireActivity()).get(AdminSharedViewModel.class);
        viewModel.fetchPesanan();
        PesananAdapter adapter = new PesananAdapter(PesananAdapter.type.Admin, pesananList,getContext(), pesanan -> {
            PesananDialog pesananDialog = new PesananDialog(requireActivity(), pesanan);
            pesananDialog.showDialog();
        } );
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.historyRecycler.setLayoutManager(layoutManager);
        binding.historyRecycler.setAdapter(adapter);

        viewModel.getListPesanan().observe( getViewLifecycleOwner(), pesanans -> {
            pesananList.clear();
            pesananList.addAll(pesanans);
            adapter.notifyDataSetChanged();
        });

        binding.historyEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                Log.d("search", v.getText().toString());
                viewModel.getPesananByName(v.getText().toString());
                return true;
            }
            return false;
        });
    }
}