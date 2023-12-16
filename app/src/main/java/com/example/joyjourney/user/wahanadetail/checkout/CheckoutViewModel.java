package com.example.joyjourney.user.wahanadetail.checkout;

import static com.example.joyjourney.utils.Utils.isNotEmpty;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.joyjourney.model.Pesanan;
import com.example.joyjourney.model.User;
import com.example.joyjourney.model.Wahana;
import com.example.joyjourney.repository.AuthRepository;
import com.example.joyjourney.repository.FirestoreRepository;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CheckoutViewModel extends ViewModel {
    private FirestoreRepository repository = new FirestoreRepository();
    private MutableLiveData<Pesanan> pesanan = new MutableLiveData<>(new Pesanan());
    private MutableLiveData<Integer> visitors = new MutableLiveData<>(1);
    private MutableLiveData<Boolean> isSuccess = new MutableLiveData<>(false);
    private MutableLiveData<Integer> currentTab = new MutableLiveData<>(0);
    public LiveData<Integer> getCurrentTab() {
        return currentTab;
    }
    public LiveData<Boolean> getIsSuccess(){
        return isSuccess;
    }

    public LiveData<Integer> getVisitors(){
        return visitors;
    }
    public void setCurrentTab(int tabPosition) {
        boolean giveAccess = false;
        Pesanan tempPesanan = pesanan.getValue();
        switch (tabPosition){
            case 0:
                giveAccess=true;
                break;
            case 1:
                if(isNotEmpty(tempPesanan.getName()) && isNotEmpty(tempPesanan.getGender()) && isNotEmpty(tempPesanan.getPhoneNumber()) && isNotEmpty(tempPesanan.getEmail())){
                    giveAccess = true;
                }
                break;
            case 2:
                if(isNotEmpty(tempPesanan.getDate()) && isNotEmpty(tempPesanan.getReservationHour()) && (tempPesanan.getVisitors()>0) && isNotEmpty(tempPesanan.getPaymentMethod())){
                    giveAccess = true;
                }
                break;
            default:giveAccess=false;
        }
        if(giveAccess){
            currentTab.setValue(tabPosition);
        }
    }

    public void increase(){
        int temp = visitors.getValue()+1;
        visitors.postValue(temp);
    }

    public void decrease(){
        int temp = visitors.getValue()-1;
        if(temp>=1){
            visitors.postValue(temp);
        }
    }
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private AuthRepository authRepository = new AuthRepository();
    private FirebaseUser firebaseUser;
    private Wahana wahana;
    private User user;

    public void setWahana(Wahana wahana) {
        this.wahana = wahana;
    }

    public void setPesananChanged(){
        Pesanan pesanan = getPesanan().getValue();
        this.pesanan.postValue(pesanan);
    }


    void fetchFirebaseUser(){
        firebaseUser = auth.getCurrentUser();
    }

    public void addPesanan(){
        Pesanan target = pesanan.getValue();
        target.setIsUsed(false);
        target.setUid(firebaseUser.getUid());
        repository.addPesanan(pesanan.getValue(), documentReference -> {
            String pesananID = documentReference.getId();
            if(pesananID!=null){
                isSuccess.postValue(true);
            }else{
                isSuccess.postValue(false);
            }
        }, e -> {
            isSuccess.postValue(false);
        });
    }

    public FirebaseUser getCurrentUser(){
        return firebaseUser;
    }

    public void setUser (User user){
        this.user = user;
    }

    public User getUser(){
        return user;
    }
    public Wahana getWahana(){
        return wahana;
    }

    public LiveData<Pesanan> getPesanan(){
        return pesanan;
    }
}
