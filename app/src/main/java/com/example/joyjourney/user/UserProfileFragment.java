package com.example.joyjourney.user;

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
import com.example.joyjourney.databinding.FragmentProfileBinding;
import com.example.joyjourney.model.User;
import com.example.joyjourney.profile_edit.ProfileEdit;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    UserSharedViewModel vm;
    FirebaseUser currentUser;
    final int REQUEST_CODE = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vm = new ViewModelProvider(requireActivity()).get(UserSharedViewModel.class);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        binding.userEmail.setText(currentUser.getEmail());
        binding.logoutButton.setOnClickListener(v->{
            vm.logout();
            Intent intent = new Intent(requireActivity(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });
        binding.deleteButton.setOnClickListener(v->{
            vm.deleteAccount(task -> {
                vm.logout();
            }, e -> {});

        });
        binding.editButton.setOnClickListener(v->{
            Intent i = new Intent(requireActivity(), ProfileEdit.class);
            User user = vm.getUser().getValue();
            i.putExtra("user", user);
            startActivityForResult(i, REQUEST_CODE);
        });

        vm.getUser().observe(getViewLifecycleOwner(), user -> {
            binding.userName.setText(user.getName());
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            if(data!=null){
                boolean isSuccess = data.getBooleanExtra("isSuccess", false);
                if(isSuccess){
                    vm.fetchUser();
                }
            }
        }
    }
}