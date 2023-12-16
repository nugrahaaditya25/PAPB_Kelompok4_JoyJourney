package com.example.joyjourney.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.example.joyjourney.R;
import com.example.joyjourney.admin.WahanaAdapter;
import com.example.joyjourney.databinding.FragmentUserFilterBinding;
import com.example.joyjourney.model.User;
import com.example.joyjourney.model.Wahana;
import com.example.joyjourney.user.wahanadetail.DetailWisataActivity;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserFilterFragment extends Fragment {

    FragmentUserFilterBinding binding;
    UserSharedViewModel vm;
    List<Wahana> wahanaList;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserFilterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vm = new ViewModelProvider(getActivity()).get(UserSharedViewModel.class);
        wahanaList = new LinkedList<>();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        vm.getUser().observe(getViewLifecycleOwner(),result->{
            if(result!=null){
                user = result;
            }
        });
        WahanaAdapter adapter = new WahanaAdapter(wahanaList, requireContext(), R.layout.katalog_item_horizontal_layout,  new WahanaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Wahana wahana) {
                Intent i = new Intent(requireActivity(), DetailWisataActivity.class);
                i.putExtra("wahana", wahana);
                i.putExtra("user", user);
                startActivity(i);
            }
        });

        binding.wahanaRecycler.setLayoutManager(layoutManager);
        binding.wahanaRecycler.setAdapter(adapter);

        vm.getQueriedWahanaList().observe(getViewLifecycleOwner(), wahanas->{
            Log.d("filter user", "data changed");
            wahanaList.clear();
            wahanaList.addAll(wahanas);
            adapter.notifyDataSetChanged();
        });
        vm.fetchQueriedWahana("");

        binding.filterButton.setOnClickListener(v->{
            showBottomSheet();
        });

        binding.editTextText.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE){
                String query = v.getText().toString();
                vm.fetchQueriedWahana(query);
                return true;
            }
            return  false;
        });
    }

    private void showBottomSheet(){
        String text = binding.editTextText.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString("search", text);
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_userFilterFragment_to_bottomSheet, bundle);
    }

}