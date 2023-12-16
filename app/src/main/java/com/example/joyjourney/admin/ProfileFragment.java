package com.example.joyjourney.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joyjourney.MainActivity;
import com.example.joyjourney.R;
import com.example.joyjourney.databinding.FragmentHomeBinding;
import com.example.joyjourney.databinding.FragmentProfileBinding;
import com.example.joyjourney.model.User;
import com.example.joyjourney.profile_edit.ProfileEdit;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private AdminSharedViewModel viewModel;
    FirebaseUser currentUser;
    final int REQUEST_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AdminSharedViewModel.class);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        viewModel.fetchUser();
        binding.userEmail.setText(currentUser.getEmail());
        binding.logoutButton.setOnClickListener(v->{
            viewModel.logout();
            Intent i = new Intent(requireActivity(), MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            requireActivity().finish();
        });
        binding.deleteButton.setOnClickListener(v->{
            viewModel.deleteAccount();
            Intent i = new Intent(requireActivity(), MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            requireActivity().finish();
        });

        viewModel.getUser().observe(getViewLifecycleOwner(), user1 -> {
            binding.userName.setText(user1.getName());
        });

        binding.deleteButton.setOnClickListener(v->{
            viewModel.deleteAccount();
        });

        binding.editButton.setOnClickListener(v->{
            Intent i = new Intent(requireActivity(), ProfileEdit.class);
            User user = viewModel.getUser().getValue();
            i.putExtra("user", user);
            startActivityForResult(i, REQUEST_CODE);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            if(data!=null){
                boolean isSuccess = data.getBooleanExtra("isSuccess", false);
                if(isSuccess){
                    viewModel.fetchUser();
                }
            }
        }
    }
}