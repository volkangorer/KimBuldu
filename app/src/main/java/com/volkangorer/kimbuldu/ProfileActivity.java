package com.volkangorer.kimbuldu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.volkangorer.kimbuldu.databinding.ActivityProfileBinding;

import java.util.ArrayList;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    FirebaseAuth auth;
    ActivityProfileBinding binding;
    FirebaseFirestore firebaseFirestore;

    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);



        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();

        getProfileData();




    }

    public void sendOnClicked(View view){
        Intent intent = new Intent(ProfileActivity.this,ShowSendingActivity.class);
        intent.putExtra("username","null");
        startActivity(intent);
    }

    public void getOnClicked(View view){
        Intent intent = new Intent(ProfileActivity.this,ShowGettingActivity.class);
        startActivity(intent);
    }

    public void myAdvertsOnClicked(View view){
        Intent intent = new Intent(ProfileActivity.this,ApplyiesAndAdverts.class);
        intent.putExtra("value",0);
        startActivity(intent);
    }

    public void myAppliesOnClicked(View view){
        Intent intent = new Intent(ProfileActivity.this,ApplyiesAndAdverts.class);
        intent.putExtra("value",1);
        startActivity(intent);
    }

    public void getProfileData(){
        firebaseFirestore.collection("Users").document(user.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String,Object> map = documentSnapshot.getData();
                String name = (String) map.get("name");
                String email = (String) map.get("user");
                String phoneNumber = (String) map.get("phoneNumber");

                binding.name1.setText(name);
                binding.phone2.setText(phoneNumber);
                binding.email1.setText(email);
                binding.nameLabel.setText("Hoşgeldin "+name);
            }
        });
    }

    public void homeOnClicked(View view){
        Intent intent = new Intent(ProfileActivity.this,HomeActivity.class);
        startActivity(intent);
    }

    public void addOnClicked(View view){
        Intent intent = new Intent(ProfileActivity.this,AddAdvertActivity.class);
        startActivity(intent);
    }

    public void logOutOnClicked(View view){
        auth.signOut();

        Intent intentToMain = new Intent(ProfileActivity.this,MainActivity.class);
        startActivity(intentToMain);
        finish();
    }

    public void profileOnClicked(View view){
        Intent intent = new Intent(ProfileActivity.this,ProfileActivity.class);
        startActivity(intent);
    }


}