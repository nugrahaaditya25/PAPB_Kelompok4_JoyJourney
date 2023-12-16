package com.example.joyjourney.user;

import android.content.Intent;
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
import com.example.joyjourney.admin.WahanaAdapter;
import com.example.joyjourney.databinding.FragmentUserHomeBinding;
import com.example.joyjourney.model.User;
import com.example.joyjourney.model.Wahana;
import com.example.joyjourney.user.wahanadetail.DetailWisataActivity;

import java.util.ArrayList;
import java.util.List;

public class UserHomeFragment extends Fragment {

    private FragmentUserHomeBinding binding;
    private UserSharedViewModel vm;
    private List<Wahana> wahanaList;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserHomeBinding.inflate(inflater, container, false);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vm = new ViewModelProvider(requireActivity()).get(UserSharedViewModel.class);
        vm.fetchUser();
        vm.getUser().observe(getViewLifecycleOwner(),result->{
            if(result!=null){
                user = result;
            }
        });
        wahanaList = new ArrayList<>();
        WahanaAdapter adapter = new WahanaAdapter(wahanaList, requireContext(), R.layout.katalog_item_horizontal_layout,  new WahanaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Wahana wahana) {
                Intent i = new Intent(requireActivity(), DetailWisataActivity.class);
                i.putExtra("wahana", wahana);
                i.putExtra("user", user);
                startActivity(i);
            }
        });
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.recycler1.setAdapter(adapter);
        binding.recycler1.setLayoutManager(layoutManager1);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.recycler2.setAdapter(adapter);
        binding.recycler2.setLayoutManager(layoutManager2);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.recycler3.setAdapter(adapter);
        binding.recycler3.setLayoutManager(layoutManager3);

        binding.notificationButton.setOnClickListener(v->{
            Intent intent = new Intent(requireActivity(), NotificationActivity.class);
            startActivity(intent);
        });

        vm.fetchWahana();
        vm.getWahanaList().observe(getViewLifecycleOwner(), wahanas->{
            wahanaList.clear();
            wahanaList.addAll(wahanas);
            int endIndex = Math.min(wahanaList.size(), 4);
            wahanaList = wahanaList.subList(0, endIndex);
            adapter.notifyDataSetChanged();
        });




    }
}