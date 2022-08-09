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
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.volkangorer.kimbuldu.databinding.ActivityDetail2Binding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class Detail2Activity extends AppCompatActivity {
    ActivityDetail2Binding binding;

    Intent intent;
    FirebaseFirestore firebaseFirestore;
    String id;

    FirebaseAuth auth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetail2Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        intent = getIntent();
        id = intent.getStringExtra("id");
        auth = FirebaseAuth.getInstance();
        user =  auth.getCurrentUser();

        firebaseFirestore = FirebaseFirestore.getInstance();
        getProfileData();
        getData();
    }

    public void seeAppliesOnClicked(View view){
        Intent intent = new Intent(Detail2Activity.this,ShowAppliesActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }

    public void deleteOnClicked(View view){
        firebaseFirestore.collection("Adverts").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                firebaseFirestore.collection("Applies").whereEqualTo("id",id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                            String advertId = snapshot.getId();
                            Map<String,Object> mapp = new HashMap<>();
                            mapp.put("status","yayından kaldırıldı");

                            firebaseFirestore.collection("Applies").document(advertId).update(mapp);
                        }

                        Intent intent = new Intent(Detail2Activity.this,ApplyiesAndAdverts.class);
                        startActivity(intent);
                        finish();
                    }
                });

            }
        });


    }

    public void getData(){
        firebaseFirestore.collection("Adverts").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> data = documentSnapshot.getData();
                int advertNo = documentSnapshot.getLong("id").intValue();
                String category = (String) data.get("category");
                String product = (String) data.get("product");
                String dowloadurl = (String) data.get("dowloadurl");
                String strDate = (String) data.get("date");
                String verify = (String) data.get("verify");
                String status = (String) data.get("status");
                String email =  (String) data.get("usermail");
                String explanation = (String) data.get("explanation");


                binding.category3.setText(category);
                binding.product3.setText(product);
                binding.date3.setText(strDate);
                binding.verify3.setText(verify);
                binding.advertNo1.setText(String.valueOf(advertNo));
                binding.status.setText("Durumu: "+status);

                binding.explanation.setText("Açıklama :"+explanation);

                Picasso.get().load(dowloadurl).into(binding.imageView3);

                if (verify.equals("etkin")){
                    String q1 = (String) data.get("q1");
                    String q2 = (String) data.get("q2");
                    String q3 = (String) data.get("q3");


                    binding.q1.setText("1.Soru: "+q1);
                    binding.q2.setText("2.Soru: "+q2);
                    binding.q3.setText("3.Soru: "+q3);

                }else {
                    binding.linearLayout5.setVisibility(View.GONE);
                }

            }
        });
    }

    public void getProfileData(){
        firebaseFirestore.collection("Users").document(user.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String,Object> map = documentSnapshot.getData();
                String name = (String) map.get("name");

                binding.nameLabel.setText("Hoşgeldin "+name);
            }
        });
    }

    public void homeOnClicked(View view){
        Intent intent = new Intent(Detail2Activity.this,HomeActivity.class);
        startActivity(intent);
    }

    public void addOnClicked(View view){
        Intent intent = new Intent(Detail2Activity.this,AddAdvertActivity.class);
        startActivity(intent);
    }

    public void logOutOnClicked(View view){
        auth.signOut();

        Intent intentToMain = new Intent(Detail2Activity.this,MainActivity.class);
        startActivity(intentToMain);
        finish();
    }

    public void profileOnClicked(View view){
        Intent intent = new Intent(Detail2Activity.this,ProfileActivity.class);
        startActivity(intent);
    }
}