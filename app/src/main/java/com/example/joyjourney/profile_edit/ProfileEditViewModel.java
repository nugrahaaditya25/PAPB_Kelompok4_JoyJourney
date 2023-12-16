package com.example.joyjourney.profile_edit;

import androidx.lifecycle.ViewModel;

import com.example.joyjourney.model.User;
import com.example.joyjourney.repository.FirestoreRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Map;

public class ProfileEditViewModel extends ViewModel {
    private FirestoreRepository firestoreRepository = new FirestoreRepository();


    void updateProfile(String userId, Map<String, Object> updates, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener){
        firestoreRepository.updateUser(userId, updates, onSuccessListener, onFailureListener);
    }
}
