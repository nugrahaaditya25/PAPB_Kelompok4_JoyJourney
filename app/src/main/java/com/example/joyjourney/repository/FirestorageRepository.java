package com.example.joyjourney.repository;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.internal.StorageReferenceUri;

import java.util.Date;

public class FirestorageRepository {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    public void uploadImage(Uri fileUri, OnFailureListener onFailureListener, OnSuccessListener<Uri> onSuccessListener){
        StorageReference imageRef = storageRef.child("images/"+System.currentTimeMillis());
        UploadTask uploadTask = imageRef.putFile(fileUri);
        uploadTask.addOnFailureListener(onFailureListener).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(onSuccessListener);
            }
        });
    }
}
