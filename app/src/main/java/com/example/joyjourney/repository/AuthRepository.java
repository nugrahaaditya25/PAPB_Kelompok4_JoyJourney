package com.example.joyjourney.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthRepository {
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public FirebaseUser getCurrentUser(){
        return auth.getCurrentUser();
    }
    public void loginUser(String email, String password, AuthCallback callback) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        callback.onSuccess(user);
                    } else {
                        String errorMessage = task.getException().getMessage();
                        callback.onError(errorMessage);
                    }
                });
    }

    public void deleteAccount(OnCompleteListener<Void> onCompleteListener, OnFailureListener onFailureListener){
        FirebaseUser user = auth.getCurrentUser();
        user.delete().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
    public void registerUser(String email, String password, AuthCallback callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        callback.onSuccess(user);
                    } else {
                        String errorMessage = task.getException().getMessage();
                        callback.onError(errorMessage);
                    }
                });
    }

    public void logout(){
        auth.signOut();
    }

    // Callback interface to handle success and error events
    public interface AuthCallback {
        void onSuccess(FirebaseUser user);

        void onError(String errorMessage);
    }
}
