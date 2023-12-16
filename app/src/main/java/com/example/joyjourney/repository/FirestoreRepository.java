package com.example.joyjourney.repository;

import android.util.Log;

import com.example.joyjourney.model.Pesanan;
import com.example.joyjourney.model.User;
import com.example.joyjourney.model.Wahana;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FirestoreRepository {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public void addUser(User user, OnSuccessListener<Void> onSuccessListener) {
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {

            DocumentReference userRef = db.collection("users").document(currentUser.getUid());

            userRef.set(user).addOnSuccessListener(onSuccessListener);
        }
    }

    public void deleteUser (String uid, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener){
        DocumentReference userRef = db.collection("users").document(uid);
        userRef.
                delete().
                addOnSuccessListener(onSuccessListener).
                addOnFailureListener(onFailureListener);
    }

    public void getUser(String userId, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        db.collection("users").document(userId).get()
                .addOnCompleteListener(onCompleteListener);
    }
    public void updateUser(String userId, Map<String, Object> updates, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.update(updates)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }


    public void addOrUpdateWahana(Wahana wahana, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        db.collection("wahana").document(wahana.getId()).set(wahana).addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
    }

    public void deleteWahana(Wahana wahana, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener){
        db.collection("wahana").document(wahana.getId()).delete().addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
    }


    public void getAllWahana(OnCompleteListener<QuerySnapshot> onCompleteListener) {
        db.collection("wahana").get()
                .addOnCompleteListener(onCompleteListener);
    }

    public void getWahanaByName (String name, OnCompleteListener<QuerySnapshot> onCompleteListener){
        db.collection("wahana")
                .orderBy("name")
                .startAt(name)
                .endAt(name + "\uf8ff")
                .get()
                .addOnCompleteListener(onCompleteListener);
    }

    public void addPesanan(Pesanan pesanan, OnSuccessListener<DocumentReference> onSuccessListener, OnFailureListener onFailureListener){
        db.collection("pesanan").add(pesanan).addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
    }

    public void getAllPesanan(OnCompleteListener<QuerySnapshot> onCompleteListener){
        db.collection("pesanan").get().addOnCompleteListener(onCompleteListener);
    }

    public void getAllUserPesanan(String uid,OnCompleteListener<QuerySnapshot> onCompleteListener){
        db.collection("pesanan").whereEqualTo("uid", uid).get().addOnCompleteListener(onCompleteListener);
    }

    public void getPesananByName (String name, OnCompleteListener<QuerySnapshot> onCompleteListener){
        db.collection("pesanan")
                .orderBy("wahanaName")
                .startAt(name)
                .endAt(name + "\uf8ff")
                .get()
                .addOnCompleteListener(onCompleteListener);
    }
    public void getWahanaByNameAndPrice(String name, int startPrice, int endPrice, Map<String, Boolean> facilities, WahanaResultCallback callback) {
        Log.d("filter user", "started");
        CollectionReference wahanaCollection = db.collection("wahana");
        Query query = wahanaCollection;

        // Apply facilities filter on the server
        List<String> facilitiestTarget = new LinkedList<>();
        for (Map.Entry<String, Boolean> entry : facilities.entrySet()) {
            String facilityName = entry.getKey();
            boolean hasFacility = entry.getValue();
            if (hasFacility) {
                facilitiestTarget.add(facilityName);
            }
        }

        if (facilitiestTarget.size() > 0) {
            Log.d("filterfirestore", "filtered facilities");
            for (String s : facilitiestTarget) {
                Log.d("filterfirestore", "value:" + s);
            }
            query = query.whereArrayContainsAny("facilities", facilitiestTarget);
        }

        // Apply price filter on the server
        if (startPrice > 0) {
            query = query.whereGreaterThanOrEqualTo("price", startPrice);
        }
        if (endPrice > 0) {
            query = query.whereLessThanOrEqualTo("price", endPrice);
        }

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Wahana> filteredDocuments = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        String documentName = document.getString("name").toLowerCase();
                        if (documentName != null && documentName.contains(name.toLowerCase())) {
                            filteredDocuments.add(document.toObject(Wahana.class));
                        }
                    }

                    // Call the callback with the result
                    callback.onWahanaResult(filteredDocuments);
                })
                .addOnFailureListener(e -> callback.onFailure(e));
    }

    public interface WahanaResultCallback {
        void onWahanaResult(List<Wahana> wahanaList);
        void onFailure(Exception e);
    }

}
