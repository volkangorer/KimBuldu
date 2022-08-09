package com.volkangorer.kimbuldu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.volkangorer.kimbuldu.databinding.ActivityHomeBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;
    FirebaseUser user;
    ArrayList<Post> postArrayList;
    Adapter adapter;
    DateFormat dateFormat;
    ActivityHomeBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);



        postArrayList = new ArrayList<>();

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        postArrayList.clear();
        getData();
        getProfil();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(postArrayList);
        binding.recyclerView.setAdapter(adapter);






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

    public void searchOnClicked(View view){
        String value = binding.searching.getText().toString();



        firebaseFirestore.collection("Adverts").whereEqualTo("product",value).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable  QuerySnapshot value, @Nullable  FirebaseFirestoreException error) {

                postArrayList.clear();
                if(error != null){
                    Toast.makeText(HomeActivity.this,error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }

                if(value != null){

                    for(DocumentSnapshot snapshot : value.getDocuments()){
                        Map<String, Object> data = snapshot.getData();

                        String id = snapshot.getId();
                        String category = (String) data.get("category");
                        String product = (String) data.get("product");
                        String dowloadurl = (String) data.get("dowloadurl");
                        String date = (String) data.get("date");
                        String verify = (String) data.get("verify");
                        String explanation = (String) data.get("explanation");
                        String status = (String) data.get("status");
                        String usermail = (String) data.get("usermail");
                        int advertNo = snapshot.getLong("id").intValue();



                        Post post = new Post(category,product,date,dowloadurl,id,verify,0,explanation,status,advertNo,usermail);
                        postArrayList.add(post);

                    }
                    adapter.notifyDataSetChanged();

                }
            }
        });

    }

    public void getData(){
        firebaseFirestore.collection("Adverts").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable  QuerySnapshot value, @Nullable  FirebaseFirestoreException error) {

                postArrayList.clear();
                if(error != null){
                    Toast.makeText(HomeActivity.this,error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }

                if(value != null){

                    for(DocumentSnapshot snapshot : value.getDocuments()){
                        Map<String, Object> data = snapshot.getData();

                        String id = snapshot.getId();
                        String category = (String) data.get("category");
                        String product = (String) data.get("product");
                        String dowloadurl = (String) data.get("dowloadurl");
                        String date = (String) data.get("date");
                        String verify = (String) data.get("verify");
                        String explanation = (String) data.get("explanation");
                        String status = (String) data.get("status");
                        String usermail = (String) data.get("usermail");
                        int advertNo = snapshot.getLong("id").intValue();


                        if (status.equals("onaylanmadı")){
                            Post post = new Post(category,product,date,dowloadurl,id,verify,0,explanation,status,advertNo,usermail);
                            postArrayList.add(post);
                        }else {

                        }


                    }
                    adapter.notifyDataSetChanged();

                }
            }
        });
    }

    public void homeOnClicked(View view){
        Intent intent = new Intent(HomeActivity.this,HomeActivity.class);
        startActivity(intent);
    }

    public void addOnClicked(View view){
        Intent intent = new Intent(HomeActivity.this,AddAdvertActivity.class);
        startActivity(intent);
    }

    public void logOutOnClicked(View view){
        auth.signOut();

        Intent intentToMain = new Intent(HomeActivity.this,MainActivity.class);
        startActivity(intentToMain);
        finish();
    }

    public void profileOnClicked(View view){
        Intent intent = new Intent(HomeActivity.this,ProfileActivity.class);
        startActivity(intent);
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.option_menu,menu);

        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.signout){
            auth.signOut();

            Intent intentToMain = new Intent(HomeActivity.this,MainActivity.class);
            startActivity(intentToMain);
            finish();
            
        }else if (item.getItemId() == R.id.addAdvert){
            Intent intent = new Intent(HomeActivity.this,AddAdvertActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.home){
            Intent intent = new Intent(HomeActivity.this,HomeActivity.class);
            startActivity(intent);
        }else if (item.getItemId() == R.id.profile){
            Intent intent = new Intent(HomeActivity.this,ProfileActivity.class);
            startActivity(intent);
        }



        return super.onOptionsItemSelected(item);
    }
}