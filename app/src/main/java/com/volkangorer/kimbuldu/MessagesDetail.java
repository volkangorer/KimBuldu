package com.volkangorer.kimbuldu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.volkangorer.kimbuldu.databinding.ActivityMessagesDetailBinding;

import java.util.Map;

public class MessagesDetail extends AppCompatActivity {
    ActivityMessagesDetailBinding binding;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser user;
    FirebaseAuth auth;
    Intent intent;
    String mId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_detail);
        binding = ActivityMessagesDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        intent = getIntent();

        mId = intent.getStringExtra("mId");

        getData();
    }

    public void getData(){
        firebaseFirestore.collection("Messages").document(mId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String,Object> map = documentSnapshot.getData();
                String sender =  (String) map.get("sender");
                String content = (String) map.get("content");
                String date = (String) map.get("date");

                binding.sender.setText("Gönderen: "+sender);
                binding.content4.setText(content);
                binding.date4.setText("Tarih:  " +date);
            }
        });
    }

    public void homeOnClicked(View view){
        Intent intent = new Intent(MessagesDetail.this,HomeActivity.class);
        startActivity(intent);
    }

    public void addOnClicked(View view){
        Intent intent = new Intent(MessagesDetail.this,AddAdvertActivity.class);
        startActivity(intent);
    }

    public void logOutOnClicked(View view){
        auth.signOut();

        Intent intentToMain = new Intent(MessagesDetail.this,MainActivity.class);
        startActivity(intentToMain);
        finish();
    }

    public void profileOnClicked(View view){
        Intent intent = new Intent(MessagesDetail.this,ProfileActivity.class);
        startActivity(intent);
    }
}