package com.example.joyjourney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.joyjourney.admin.AdminActivity;
import com.example.joyjourney.boarding.BoardingActivity;
import com.example.joyjourney.databinding.ActivityMainBinding;
import com.example.joyjourney.login.LoginActivity;
import com.example.joyjourney.model.User;
import com.example.joyjourney.register.RegisterActivity;
import com.example.joyjourney.repository.FirestoreRepository;
import com.example.joyjourney.user.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirestoreRepository firestoreRepository;
    ActivityMainBinding activityMainBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        FirebaseUser currentUser = auth.getCurrentUser();

        if(currentUser==null){
            Intent intent = new Intent(getApplicationContext(), BoardingActivity.class);
            startActivity(intent);
            finish();
        }else{
            firestoreRepository = new FirestoreRepository();

            firestoreRepository.getUser(currentUser.getUid(), new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            try {
                                activityMainBinding.progressBar.setVisibility(View.INVISIBLE);
                                User user = document.toObject(User.class);
                                if(user.getAdmin()){
                                    Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }catch (Exception e){
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            });

        }
    }
}