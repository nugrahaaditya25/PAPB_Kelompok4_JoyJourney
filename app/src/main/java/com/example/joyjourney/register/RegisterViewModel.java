package com.example.joyjourney.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.joyjourney.model.User;
import com.example.joyjourney.repository.FirestoreRepository;
import com.google.android.gms.tasks.OnSuccessListener;

public class RegisterViewModel extends ViewModel {
    private FirestoreRepository repository = new FirestoreRepository();

    private MutableLiveData<Boolean> isSuccess = new MutableLiveData<Boolean>();

    public LiveData<Boolean> getIsSuccess(){
        return isSuccess;
    }

    void addUser(User user){
        repository.addUser(user, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                isSuccess.postValue(true);
            }
        });
    }
}
