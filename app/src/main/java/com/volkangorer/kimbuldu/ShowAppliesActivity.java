package com.volkangorer.kimbuldu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.volkangorer.kimbuldu.databinding.ActivityShowAppliesBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShowAppliesActivity extends AppCompatActivity {
    ActivityShowAppliesBinding binding;
    Intent intent;
    String id;
    FirebaseFirestore firebaseFirestore;
    Adapter5 adapter5;
    ArrayList<Post5> postArrayList2;
    String q1;
    String q2;
    String q3;
    String a1;
    String a2;
    String a3;
    String detail;
    String verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowAppliesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        q1 = "";
        q2 = "";
        q3 = "";

        a1 = "";
        a2 = "";
        a3 = "";

        detail ="";

        intent = getIntent();
        id = intent.getStringExtra("id");
        firebaseFirestore = FirebaseFirestore.getInstance();

        postArrayList2 = new ArrayList<>();


        getData();

        binding.recyclerView5.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        adapter5 = new Adapter5(postArrayList2);
        binding.recyclerView5.setAdapter(adapter5);

    }

    public void getData(){
        firebaseFirestore.collection("Adverts").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                postArrayList2.clear();
                Map<String,Object> map = documentSnapshot.getData();
                verify = (String) map.get("verify");

                if (verify.equals("etkin")){
                    q1 = (String) map.get("q1");
                    q2 = (String) map.get("q2");
                    q3 = (String) map.get("q3");

                    firebaseFirestore.collection("Applies").whereEqualTo("id",id).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable  FirebaseFirestoreException error) {
                            for (DocumentSnapshot snapshot : value.getDocuments()) {
                                Map<String,Object> data =  snapshot.getData();
                                a1 = (String) data.get("a1");
                                a2 = (String) data.get("a2");
                                a3 = (String) data.get("a3");
                                detail = (String) data.get("detail");
                                String mail = (String) data.get("username");
                                String phone = (String) data.get("phone");
                                String name = (String) data.get("name");
                                String appliesId = snapshot.getId();

                                Post5 post5 = new Post5(id,name,mail,phone,q1,q2,q3,a1,a2,a3,detail,verify,appliesId);
                                postArrayList2.add(post5);
                            }

                            adapter5.notifyDataSetChanged();



                        }
                    });
                }else {

                    firebaseFirestore.collection("Applies").whereEqualTo("id",id).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable  FirebaseFirestoreException error) {
                            for (DocumentSnapshot snapshot : value.getDocuments()) {
                                Map<String,Object> data =  snapshot.getData();

                                String mail = (String) data.get("username");
                                String phone = (String) data.get("phone");
                                String name = (String) data.get("name");
                                String appliesId = (String) snapshot.getId();

                                Post5 post5 = new Post5(id,name,mail,phone,q1,q2,q3,a1,a2,a3,detail,verify,appliesId);
                                postArrayList2.add(post5);

                            }

                            adapter5.notifyDataSetChanged();

                        }
                    });
                }



            }
        });
    }
}