package com.volkangorer.kimbuldu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.volkangorer.kimbuldu.databinding.ActivityApplyiesAndAdvertsBinding;


import java.util.ArrayList;
import java.util.Map;

public class ApplyiesAndAdverts extends AppCompatActivity {
    FirebaseAuth auth;
    ActivityApplyiesAndAdvertsBinding binding;

    ArrayList<Post2> postArrayList2;
    Adapter adapter;
    Adapter2 adapter2;
    Adapter3 adapter3;
    FirebaseFirestore firebaseFirestore;
    Intent intent;
    FirebaseUser user;
    int value;
    ArrayList list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityApplyiesAndAdvertsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();


        postArrayList2 = new ArrayList<>();
        list = new ArrayList();

        intent = getIntent();

        value = intent.getIntExtra("value",0);

        getProfil();


        if (value==0){
            getData();
        }else if (value==1){
            getData2();
        }

        /*

        binding.recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        adapter2 = new Adapter2(postArrayList2);
        binding.recyclerView2.setAdapter(adapter2);

         */

        binding.recyclerView3.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        adapter3 = new Adapter3(postArrayList2);
        binding.recyclerView3.setAdapter(adapter3);


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

    public void getData(){
        firebaseFirestore.collection("Adverts").whereEqualTo("usermail",user.getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                postArrayList2.clear();
                if(error != null){
                    Toast.makeText(ApplyiesAndAdverts.this,error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }

                if(value != null){

                    for(DocumentSnapshot snapshot : value.getDocuments()){
                        Map<String, Object> data = snapshot.getData();

                        String id = snapshot.getId();
                        String status = (String) data.get("status");
                        int advertNo = snapshot.getLong("id").intValue();
                        String mail = (String) data.get("usermail");


                        Post2 post2 = new Post2(advertNo,id,status,mail);
                        postArrayList2.add(post2);

                    }
                    adapter3.notifyDataSetChanged();

                }
            }
        });
    }

    public void getData2(){



        firebaseFirestore.collection("Applies").whereEqualTo("username",user.getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable  QuerySnapshot value, @Nullable  FirebaseFirestoreException error) {
                postArrayList2.clear();

                if(error != null){
                    Toast.makeText(ApplyiesAndAdverts.this,error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }

                if(value != null){
                    for(DocumentSnapshot snapshot : value.getDocuments()){
                        Map<String, Object> data = snapshot.getData();

                        String id = (String) data.get("id");
                        String status = (String) data.get("status");
                        int advertNo = snapshot.getLong("advertNo").intValue();
                        String mail = (String) data.get("username");


                        Post2 post2 = new Post2(advertNo,id,status,mail);
                        postArrayList2.add(post2);

                    }
                    adapter3.notifyDataSetChanged();



                }


            }



        });

    }

    public void homeOnClicked(View view){
        Intent intent = new Intent(ApplyiesAndAdverts.this,HomeActivity.class);
        startActivity(intent);
    }

    public void addOnClicked(View view){
        Intent intent = new Intent(ApplyiesAndAdverts.this,AddAdvertActivity.class);
        startActivity(intent);
    }

    public void logOutOnClicked(View view){
        auth.signOut();

        Intent intentToMain = new Intent(ApplyiesAndAdverts.this,MainActivity.class);
        startActivity(intentToMain);
        finish();
    }

    public void profileOnClicked(View view){
        Intent intent = new Intent(ApplyiesAndAdverts.this,ProfileActivity.class);
        startActivity(intent);
    }



}