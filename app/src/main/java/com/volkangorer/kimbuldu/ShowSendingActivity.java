package com.volkangorer.kimbuldu;

import androidx.annotation.FontRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.volkangorer.kimbuldu.databinding.ActivityShowSendingBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ShowSendingActivity extends AppCompatActivity {
    ActivityShowSendingBinding binding;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser user;
    FirebaseAuth auth;
    String usermail;
    Intent intent;
    String getterMail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowSendingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        intent = getIntent();

        getterMail = intent.getStringExtra("username");

        if (getterMail.equals("null")){

        }else {
            binding.advertNo2.setText(getterMail);
        }

        getProfil();
    }

    public void getProfil(){
        firebaseFirestore.collection("Users").document(user.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String,Object> map = documentSnapshot.getData();
                String name = (String) map.get("name");
                binding.nameLabel.setText("Hoşgeldin "+name);
            }
        });
    }

    public void sendOnClicked(View view){
        if (getterMail.equals("null")){
            int advertNo = Integer.parseInt(binding.advertNo2.getText().toString());

            firebaseFirestore.collection("Adverts").whereEqualTo("id",advertNo).limit(1).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable  QuerySnapshot value, @Nullable  FirebaseFirestoreException error) {
                    for (DocumentSnapshot documentSnapshot:value.getDocuments()){
                        Map<String,Object> data = documentSnapshot.getData();
                        usermail = (String) data.get("usermail").toString();

                    }

                    String content = binding.content.getText().toString();
                    String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(Calendar.getInstance().getTime());

                    Map<String,Object> map = new HashMap<>();
                    map.put("date",timeStamp);
                    map.put("sender",user.getEmail());
                    map.put("advertNo",advertNo);
                    map.put("content",content);
                    map.put("receiver",usermail);

                    firebaseFirestore.collection("Messages").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(ShowSendingActivity.this,"Mesaj Başarıyla Gönderildi",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }else {

            String content = binding.content.getText().toString();
            String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(Calendar.getInstance().getTime());

            Map<String,Object> map = new HashMap<>();
            map.put("date",timeStamp);
            map.put("sender",user.getEmail());
            map.put("content",content);
            map.put("receiver",getterMail);

            firebaseFirestore.collection("Messages").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(ShowSendingActivity.this,"Mesaj Başarıyla Gönderildi",Toast.LENGTH_LONG).show();
                }
            });
        }





    }

    public void homeOnClicked(View view){
        Intent intent = new Intent(ShowSendingActivity.this,HomeActivity.class);
        startActivity(intent);
    }

    public void addOnClicked(View view){
        Intent intent = new Intent(ShowSendingActivity.this,AddAdvertActivity.class);
        startActivity(intent);
    }

    public void logOutOnClicked(View view){
        auth.signOut();

        Intent intentToMain = new Intent(ShowSendingActivity.this,MainActivity.class);
        startActivity(intentToMain);
        finish();
    }

    public void profileOnClicked(View view){
        Intent intent = new Intent(ShowSendingActivity.this,ProfileActivity.class);
        startActivity(intent);
    }
}