package com.example.joyjourney.admin.crud;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.joyjourney.model.Wahana;
import com.example.joyjourney.repository.FirestorageRepository;
import com.example.joyjourney.repository.FirestoreRepository;
import com.example.joyjourney.utils.NotifyDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class CrudViewModel extends ViewModel {
    private final FirestoreRepository repository = new FirestoreRepository();
    private final FirestorageRepository storageRepository = new FirestorageRepository();

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isSuccess = new MutableLiveData<>();
    private final MutableLiveData<NotifyDialog.DialogType> requestType = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<String> getError() {
        return error;
    }
    public LiveData<NotifyDialog.DialogType> getRequestType(){
        return requestType;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<Boolean> getIsSuccess() {
        return isSuccess;
    }

    public void addWahana(Wahana wahana, Uri imageUri) {
        loading.setValue(true);
        storageRepository.uploadImage(imageUri, this::handleUploadFailure, uri -> {
            String imageUrl = uri.toString();
            wahana.setImageUrl(imageUrl);
            Log.d("imageUploaded", imageUrl);
            repository.addOrUpdateWahana(wahana, unused -> {
                requestType.setValue(NotifyDialog.DialogType.Upload);
                isSuccess.postValue(true);
            }, this::handleFailure);
        });
        loading.setValue(false);
    }

    public void updateWahana(Wahana wahana, Uri imageUri) {
        loading.setValue(true);
        if (imageUri != null) {
            storageRepository.uploadImage(imageUri, this::handleUploadFailure, uri -> {
                String imageUrl = uri.toString();
                Log.d("imageUploaded", imageUrl);
                wahana.setImageUrl(imageUrl);
                repository.addOrUpdateWahana(wahana, unused -> {

                        requestType.setValue(NotifyDialog.DialogType.Update);
                        isSuccess.postValue(true);

                }, this::handleFailure);
            });
        }else{
            repository.addOrUpdateWahana(wahana, unused -> {
                requestType.setValue(NotifyDialog.DialogType.Delete);
                isSuccess.postValue(true);
            }, this::handleFailure);
        }
        loading.setValue(false);
    }

    public void deleteWahana(Wahana wahana){
        loading.setValue(true);
        repository.deleteWahana(wahana, unused ->{
            requestType.setValue(NotifyDialog.DialogType.Delete);
            isSuccess.postValue(true);
            },this::handleFailure);
        loading.setValue(false);
    }


    private void handleUploadFailure(@NonNull Exception e) {
        error.postValue(e.getMessage());
        loading.postValue(false);
    }

    private void handleFailure(@NonNull Exception e) {
        error.postValue(e.getMessage());
        isSuccess.postValue(false);
        loading.postValue(false);
    }
}
