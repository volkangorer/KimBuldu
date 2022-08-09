package com.volkangorer.kimbuldu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.volkangorer.kimbuldu.databinding.ActivityDetailBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;
    Intent intent;
    FirebaseFirestore firebaseFirestore;
    String id;
    DateFormat dateFormat;
    FirebaseAuth auth;
    FirebaseUser user;
    String ques1;
    String ques2;
    String ques3;
    String verify;
    int advertNo;
    String name;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        intent = getIntent();
        id = intent.getStringExtra("id");
        auth = FirebaseAuth.getInstance();
        user =  auth.getCurrentUser();

        firebaseFirestore = FirebaseFirestore.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        getData();
        getProfil();

    }

    public void getProfil(){
        firebaseFirestore.collection("Users").document(user.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String,Object> map = documentSnapshot.getData();
                name = (String) map.get("name");
                phone = (String) map.get("phoneNumber");
                binding.nameLabel.setText("Hoşgeldin "+name);
            }
        });
    }

    public void getData(){
        firebaseFirestore.collection("Adverts").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> data = documentSnapshot.getData();
                advertNo = documentSnapshot.getLong("id").intValue();
                String category = (String) data.get("category");
                String product = (String) data.get("product");
                String dowloadurl = (String) data.get("dowloadurl");
                String strDate = (String) data.get("date");
                verify = (String) data.get("verify");
                String email =  (String) data.get("usermail");
                String explanation = (String) data.get("explanation");
                int advertNo = documentSnapshot.getLong("id").intValue();
                ques1 = (String) data.get("q1");
                ques2 = (String) data.get("q2");
                ques3 = (String) data.get("q3");
                        /*Timestamp timestamp = (Timestamp) data.get("date");
                        Date date = timestamp.toDate();
                        String strDate = dateFormat.format(date);*/

                binding.category3.setText(category);
                binding.product3.setText(product);
                binding.date3.setText(strDate);
                binding.verify3.setText(verify);
                binding.explanation.setText("Açıklama :"+explanation);
                binding.advertNo.setText(String.valueOf(advertNo));
                binding.ques1.setText(ques1);
                binding.ques2.setText(ques2);
                binding.ques3.setText(ques3);
                Picasso.get().load(dowloadurl).into(binding.imageView3);

                System.out.println(ques1);

                if(verify.equals("etkin değil")){
                    //do not nothing
                    block();
                }

                if (email.equals(user.getEmail())){
                    block();
                    //binding.apply.setVisibility(View.GONE);
                }
            }
        });
    }


    public void block(){

        binding.answerLayout.setVisibility(View.GONE);
        binding.additonal.setVisibility(View.GONE);
    }

    public void applyOnClicked(View view){

        if (verify.equals("etkin")){
            String answer1 = binding.answer1.getText().toString();
            String answer2 = binding.answer2.getText().toString();
            String answer3 = binding.answer3.getText().toString();
            String detail = binding.additonal.getText().toString();

            if (answer1.equals("")||answer2.equals("")||answer3.equals("")){
                Toast.makeText(DetailActivity.this,"cevap alanlarını doldurunuz",Toast.LENGTH_LONG).show();

            }else {
                Map<String,Object>  map = new HashMap<>();
                map.put("id",id);
                map.put("username",user.getEmail());
                map.put("a1",answer1);
                map.put("a2",answer2);
                map.put("a3",answer3);
                map.put("detail",detail);
                map.put("status","beklemede");
                map.put("advertNo",advertNo);
                map.put("phone",phone);
                map.put("name",name);
                firebaseFirestore.collection("Applies").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(DetailActivity.this,"Başarıyla başvuruldu",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(DetailActivity.this,ProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }else{
            Map<String,Object>  map = new HashMap<>();
            map.put("id",id);
            map.put("username",user.getEmail());
            map.put("status","beklemede");
            map.put("phone",phone);
            map.put("name",name);
            map.put("advertNo",advertNo);
            firebaseFirestore.collection("Applies").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(DetailActivity.this,"Başarıyla başvuruldu",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(DetailActivity.this,ProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

    }

    public void homeOnClicked(View view){
        Intent intent = new Intent(DetailActivity.this,HomeActivity.class);
        startActivity(intent);
    }

    public void addOnClicked(View view){
        Intent intent = new Intent(DetailActivity.this,AddAdvertActivity.class);
        startActivity(intent);
    }

    public void logOutOnClicked(View view){
        auth.signOut();

        Intent intentToMain = new Intent(DetailActivity.this,MainActivity.class);
        startActivity(intentToMain);
        finish();
    }

    public void profileOnClicked(View view){
        Intent intent = new Intent(DetailActivity.this,ProfileActivity.class);
        startActivity(intent);
    }
}