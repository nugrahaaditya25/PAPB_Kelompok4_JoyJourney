package com.example.joyjourney.user;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserSharedViewModel extends ViewModel {
    private AuthRepository authRepository = new AuthRepository();
    private FirestoreRepository firestoreRepository = new FirestoreRepository();
    private MutableLiveData<List<Wahana>> wahanaList = new MutableLiveData<>();
    private MutableLiveData<List<Pesanan>> listPesanan = new MutableLiveData<>();
    private MutableLiveData<List<Wahana>> listQueriedWahana = new MutableLiveData<>(new LinkedList<>());
    private MutableLiveData<Integer> startPrice = new MutableLiveData<>(0);
    private MutableLiveData<String> queryString = new MutableLiveData<>("");
    private MutableLiveData<Integer> endPrice = new MutableLiveData<>(-1);

    private MutableLiveData<Map<String, Boolean>> facilities = new MutableLiveData<>(new HashMap<String, Boolean>(){
        {
            put("Wifi", false);
            put("Kantin", false);
            put("Kolam Renang", false);
            put("Musholla", false);
            put("Parkir Gratis", false);
        }
    });
    private MutableLiveData<String> error = new MutableLiveData<>();
    public FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    public LiveData<String> getError(){
        return error;
    }
    private MutableLiveData<User> user = new MutableLiveData<>();

    public LiveData<List<Wahana>> getWahanaList(){
        return wahanaList;
    }
    public LiveData<List<Wahana>> getQueriedWahanaList(){
        return listQueriedWahana;
    }
    public LiveData<Integer> getStartPrice(){
        return startPrice;
    }
    public LiveData<Integer> getEndPrice(){
        return endPrice;
    }
    public LiveData<Map<String, Boolean>> getFacilities(){
        return facilities;
    }
    public LiveData<String> getQueryString(){return queryString;}
    public MutableLiveData<User> getUser(){
        return user;
    }
    public LiveData<List<Pesanan>> getListPesanan(){return listPesanan;};

    public void fetchUser(){
        firestoreRepository.getUser(firebaseUser.getUid(), task -> {
            User result = task.getResult().toObject(User.class);
            if(result!=null){
                user.setValue(result);
            }
        });
    }

    public void fetchWahana(){
        firestoreRepository.getAllWahana(task -> {
           if(task.isSuccessful()){
               List<Wahana> result = new ArrayList<>();
               QuerySnapshot querySnapshot = task.getResult();

               for(DocumentSnapshot doc:querySnapshot.getDocuments()){
                   Wahana wahana = doc.toObject(Wahana.class);
                   result.add(wahana);
               }

               wahanaList.postValue(result);
           }else{
               error.postValue(task.getException().getMessage());
           }
        });
    }

    public void fetchPesanan(){
        FirebaseUser user = authRepository.getCurrentUser();
        firestoreRepository.getAllUserPesanan(user.getUid(),new OnCompleteListener<QuerySnapshot>() {
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
                }
            }
        });
    }

    public void fetchQueriedWahana(String name, int startPrice, int endPrice, Map<String, Boolean> facilities){
        Log.d("filter user", name+" "+startPrice+" "+endPrice);
        this.startPrice.setValue(startPrice);
        this.endPrice.setValue(endPrice);
        this.facilities.setValue(facilities);
        firestoreRepository.getWahanaByNameAndPrice(name, startPrice, endPrice, facilities, new FirestoreRepository.WahanaResultCallback() {
            @Override
            public void onWahanaResult(List<Wahana> wahanaList) {
                listQueriedWahana.postValue(wahanaList);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    public void fetchQueriedWahana(String name){
        Log.d("filter user", "fetched from search bar");
        int startPrice = getStartPrice().getValue();
        int endPrice = getEndPrice().getValue();
        firestoreRepository.getWahanaByNameAndPrice(name, startPrice, endPrice, getFacilities().getValue(), new FirestoreRepository.WahanaResultCallback() {
            @Override
            public void onWahanaResult(List<Wahana> wahanaList) {
                listQueriedWahana.postValue(wahanaList);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    public void logout(){
        authRepository.logout();
    }
    void deleteAccount(OnCompleteListener<Void> onSuccessListener, OnFailureListener onFailureListener){
        FirebaseUser user = authRepository.getCurrentUser();
        firestoreRepository.deleteUser(user.getUid(), unused -> {
            authRepository.deleteAccount(onSuccessListener, onFailureListener);
        },onFailureListener);
    }
}
