package com.example.joyjourney.admin;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.joyjourney.R;
import com.example.joyjourney.admin.crud.CrudActivity;
import com.example.joyjourney.databinding.FragmentHomeBinding;
import com.example.joyjourney.model.Wahana;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private AdminSharedViewModel viewModel;
    private List<Wahana> wahanaList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AdminSharedViewModel.class);
        wahanaList = new ArrayList<>();
        WahanaAdapter adapter = new WahanaAdapter(wahanaList, requireContext(), new WahanaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Wahana wahana) {
                Intent i = new Intent(requireActivity(), CrudActivity.class);
                i.putExtra("wahana", wahana);
                startActivityForResult(i, 1);
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2);
        binding.recycler.setLayoutManager(layoutManager);
        binding.recycler.setAdapter(adapter);

        viewModel.getAllWahana();

        viewModel.getListWahana().observe(getViewLifecycleOwner(), wahanas->{
            wahanaList.clear();
            int length = Math.min(wahanas.size(), 4);
            wahanaList.addAll(wahanas.subList(0, length));
            adapter.notifyDataSetChanged();
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        binding.showAllButton.setOnClickListener(v->{
            navigateToFragmentB();

        });
    }

    private void navigateToFragmentB() {
        // Use Navigation.findNavController() to get the NavController
        NavController navController = Navigation.findNavController(requireView());

        // Use the generated action to navigate from fragmentA to fragmentB
        navController.navigate(R.id.action_homeFragment_to_detailWahanaFragment);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==1 && resultCode == RESULT_OK && data != null) {
            boolean resultData =data.getBooleanExtra("refresh", false);
            if (resultData) {
                viewModel.getAllWahana();
            }
        }
    }
}