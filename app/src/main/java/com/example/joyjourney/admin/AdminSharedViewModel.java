package com.example.joyjourney.admin;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.joyjourney.model.Pesanan;
import com.example.joyjourney.model.User;
import com.example.joyjourney.model.Wahana;
import com.example.joyjourney.repository.AuthRepository;
import com.example.joyjourney.repository.FirestoreRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminSharedViewModel extends ViewModel {
    private FirestoreRepository firestoreRepository = new FirestoreRepository();
    private MutableLiveData<List<Wahana>> listAllWahana= new MutableLiveData<>();
    private MutableLiveData<User> user = new MutableLiveData<>();
    private AuthRepository authRepository = new AuthRepository();
    private MutableLiveData<List<Wahana>> listQueriedWahana = new MutableLiveData<>();
    private MutableLiveData<String>errMessage = new MutableLiveData<>();
    private MutableLiveData<List<Pesanan>> listPesanan = new MutableLiveData<>();
    public LiveData<User> getUser(){
        return user;
    }

    public LiveData<List<Pesanan>> getListPesanan(){return listPesanan;};

    public LiveData<List<Wahana>> getListWahana(){
        return listAllWahana;
    }

    public LiveData<List<Wahana>> getQueriedListWahana(){return  listQueriedWahana;}

    public LiveData<String> getErrorMessage(){
        return errMessage;
    }

    void getAllWahana(){
        firestoreRepository.getAllWahana(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
               if(task.isSuccessful()){
                   List<Wahana> result = new ArrayList<>();
                   QuerySnapshot querySnapshot = task.getResult();

                   for(DocumentSnapshot doc:querySnapshot.getDocuments()){
                       Wahana wahana = doc.toObject(Wahana.class);
                       result.add(wahana);
                   }

                   listAllWahana.postValue(result);
               }else{
                   errMessage.postValue(task.getException().getMessage());
               }
            }
        });
    }

    public void fetchUser (){
        FirebaseUser crnUser = FirebaseAuth.getInstance().getCurrentUser();
        firestoreRepository.getUser(crnUser.getUid(), task -> {
            User result = task.getResult().toObject(User.class);
            user.postValue(result);
        });
    }

    public void fetchPesanan(){
        firestoreRepository.getAllPesanan(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<Pesanan> result = new ArrayList<>();
                    QuerySnapshot querySnapshot = task.getResult();

                    for(DocumentSnapshot doc:querySnapshot.getDocuments()){
                        Pesanan pesanan = doc.toObject(Pesanan.class);
                        Log.d("fetchWahana", "wahananame:"+pesanan.getName());
                        result.add(pesanan);
                    }

                    listPesanan.postValue(result);
                }else{
                    errMessage.postValue(task.getException().getMessage());
                }
            }
        });
    }

    public void getPesananByName(String name){
        Log.d("fetchWahana", "name:"+name);
        firestoreRepository.getPesananByName(name, new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<Pesanan> result = new ArrayList<>();
                    QuerySnapshot querySnapshot = task.getResult();

                    for(DocumentSnapshot doc:querySnapshot.getDocuments()){
                        Pesanan pesanan = doc.toObject(Pesanan.class);
                        Log.d("fetchWahanaByName", "wahananame:"+pesanan.getName());
                        result.add(pesanan);
                    }
                    listPesanan.setValue(result);
                }else{
                    errMessage.postValue(task.getException().getMessage());
                }
            }
        });
    }

    void getWahanaByName(String name){
        firestoreRepository.getWahanaByName(name,new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<Wahana> result = new ArrayList<>();
                    QuerySnapshot querySnapshot = task.getResult();

                    for(DocumentSnapshot doc:querySnapshot.getDocuments()){
                        Wahana wahana = doc.toObject(Wahana.class);
                        result.add(wahana);
                    }

                    listQueriedWahana.postValue(result);
                }else{
                    errMessage.postValue(task.getException().getMessage());
                }
            }
        });
    }

    void logout(){
        authRepository.logout();
    }

    void deleteAccount(){
        FirebaseUser user = authRepository.getCurrentUser();
        firestoreRepository.deleteUser(user.getUid(), unused -> {
            authRepository.deleteAccount(task -> {
                authRepository.logout();
            }, e->{});
        },e -> {});
    }
}
