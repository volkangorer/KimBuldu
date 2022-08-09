package com.volkangorer.kimbuldu;

import androidx.annotation.FontRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.volkangorer.kimbuldu.databinding.ActivityShowGettingBinding;

import java.util.ArrayList;
import java.util.Map;

public class ShowGettingActivity extends AppCompatActivity {
    ActivityShowGettingBinding binding;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser user;
    FirebaseAuth auth;
    ArrayList<Post3> postArrayList2;
    Adapter4 adapter4;
    Intent intent;
    int value;
    ArrayList list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowGettingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        postArrayList2 = new ArrayList<>();

        getData();

        getProfil();

        binding.recyclerView4.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        adapter4 = new Adapter4(postArrayList2);
        binding.recyclerView4.setAdapter(adapter4);


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
        firebaseFirestore.collection("Messages").whereEqualTo("receiver",user.getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable  QuerySnapshot value, @Nullable  FirebaseFirestoreException error) {
                for (DocumentSnapshot snapshot : value.getDocuments()){
                    Map<String,Object> map = snapshot.getData();
                    String sender = (String) map.get("sender");
                    String mId = snapshot.getId();

                    Post3 post3 = new Post3(sender,mId);
                    postArrayList2.add(post3);


                }
                System.out.println(postArrayList2.size());
                adapter4.notifyDataSetChanged();
            }
        });
    }

    public void homeOnClicked(View view){
        Intent intent = new Intent(ShowGettingActivity.this,HomeActivity.class);
        startActivity(intent);
    }

    public void addOnClicked(View view){
        Intent intent = new Intent(ShowGettingActivity.this,AddAdvertActivity.class);
        startActivity(intent);
    }

    public void logOutOnClicked(View view){
        auth.signOut();

        Intent intentToMain = new Intent(ShowGettingActivity.this,MainActivity.class);
        startActivity(intentToMain);
        finish();
    }

    public void profileOnClicked(View view){
        Intent intent = new Intent(ShowGettingActivity.this,ProfileActivity.class);
        startActivity(intent);
    }
}