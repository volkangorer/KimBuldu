package com.volkangorer.kimbuldu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;
import com.volkangorer.kimbuldu.databinding.RecyclerRowBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class Adapter extends RecyclerView.Adapter<Adapter.PostHolder>{

    @NonNull
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;
    FirebaseUser user;
    private ArrayList<Post> postArrayList;

    public Adapter(ArrayList<Post> postArrayList) {
        this.postArrayList = postArrayList;
    }

    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerRowBinding recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new PostHolder(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull  Adapter.PostHolder holder, int position) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        Picasso.get().load(postArrayList.get(position).downloadurl).into(holder.recyclerRowBinding.imageView);
        holder.recyclerRowBinding.category.setText(postArrayList.get(position).category);
        holder.recyclerRowBinding.product.setText(postArrayList.get(position).product);
        holder.recyclerRowBinding.date.setText(postArrayList.get(position).date);

        holder.recyclerRowBinding.advertNo.setText(String.valueOf(postArrayList.get(position).advertNo));
        holder.recyclerRowBinding.explanation.setText("Açıklama : "+postArrayList.get(position).explanation);

        if (postArrayList.get(position).verify.equals("etkin değil")){
            holder.recyclerRowBinding.incele.setVisibility(View.GONE);
            holder.recyclerRowBinding.verified.setVisibility(View.GONE);
        }else {
            holder.recyclerRowBinding.quickApply.setVisibility(View.GONE);
        }

        if (postArrayList.get(position).usermail.equals(user.getEmail())){
            holder.recyclerRowBinding.quickApply.setEnabled(false);
            holder.recyclerRowBinding.incele.setEnabled(false);
            holder.recyclerRowBinding.incele.setColorFilter(R.color.black);
        }

        holder.recyclerRowBinding.quickApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(holder.itemView.getContext())
                        .setMessage(postArrayList.get(position).advertNo+"'nolu ilana başvurmak istiyor musunuz ?").setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        firebaseFirestore.collection("Users").document(user.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                Map<String,Object> data = documentSnapshot.getData();
                                String phone = (String) data.get("phoneNumber");
                                String name = (String) data.get("name");

                                Map<String,Object>  map = new HashMap<>();
                                map.put("id",postArrayList.get(position).id);
                                map.put("username",user.getEmail());
                                map.put("status","beklemede");
                                map.put("phone",phone);
                                map.put("name",name);
                                map.put("advertNo",postArrayList.get(position).advertNo);
                                firebaseFirestore.collection("Applies").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(holder.itemView.getContext(),"Başarıyla başvuruldu",Toast.LENGTH_LONG).show();

                                    }
                                });
                            }
                        });

                    }
                }).setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
            }
        });

        if (postArrayList.get(position).userId==0){
            holder.recyclerRowBinding.incele.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(holder.itemView.getContext(),DetailActivity.class);
                    intent.putExtra("id",postArrayList.get(position).id);
                    holder.itemView.getContext().startActivity(intent);
                }
            });
            //holder.recyclerRowBinding.updateContent.setVisibility(View.GONE);
        }else if (postArrayList.get(position).userId==1){

            holder.recyclerRowBinding.incele.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        RecyclerRowBinding recyclerRowBinding;
        public PostHolder(RecyclerRowBinding recyclerRowBinding) {
            super(recyclerRowBinding.getRoot());
            this.recyclerRowBinding = recyclerRowBinding;
        }
    }
}
