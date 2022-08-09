package com.volkangorer.kimbuldu;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Adapter5 extends RecyclerView.Adapter<Adapter5.PostHolder> {
    private ArrayList<Post5> postArrayList2;
    FirebaseFirestore firebaseFirestore;

    public Adapter5(ArrayList<Post5> postArrayList2) {
        this.postArrayList2 = postArrayList2;
    }
    @NonNull

    @Override
    public Adapter5.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row5, parent, false);
        return new Adapter5.PostHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(@NonNull  Adapter5.PostHolder holder, int position) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        if (postArrayList2.get(position).verify.equals("etkin")){
            holder.email.setText("E-Mail: "+postArrayList2.get(position).email);
            holder.name.setText("İsim: "+postArrayList2.get(position).name);
            holder.phone.setText("Telefon: "+postArrayList2.get(position).phone);
            holder.q1.setText("1.Soru: "+postArrayList2.get(position).q1);
            holder.q2.setText("2.Soru: "+postArrayList2.get(position).q2);
            holder.q3.setText("3.Soru: "+postArrayList2.get(position).q3);
            holder.a1.setText("Cevap: "+postArrayList2.get(position).a1);
            holder.a2.setText("Cevap: "+postArrayList2.get(position).a2);
            holder.a3.setText("Cevap: "+postArrayList2.get(position).a3);
            holder.detail.setText("Detay: "+postArrayList2.get(position).detail);
        }else {
            holder.email.setText("E-Mail: "+postArrayList2.get(position).email);
            holder.name.setText("İsim: "+postArrayList2.get(position).name);
            holder.phone.setText("Telefon: "+postArrayList2.get(position).phone);
            holder.q1.setVisibility(View.GONE);
            holder.q2.setVisibility(View.GONE);
            holder.q3.setVisibility(View.GONE);
            holder.a1.setVisibility(View.GONE);
            holder.a2.setVisibility(View.GONE);
            holder.a3.setVisibility(View.GONE);
            holder.detail.setVisibility(View.GONE);

        }

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(),ShowSendingActivity.class);
                intent.putExtra("username",postArrayList2.get(position).email);
                holder.itemView.getContext().startActivity(intent);
            }
        });

        holder.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map = new HashMap<>();
                map.put("status","onaylandı");
                firebaseFirestore.collection("Adverts").document(postArrayList2.get(position).id).update(map);
                firebaseFirestore.collection("Applies").document(postArrayList2.get(position).appliesId).update(map);
                Toast.makeText(holder.itemView.getContext(),"Başvuru Onaylandı",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(holder.itemView.getContext(),ShowAppliesActivity.class);
                intent.putExtra("id",postArrayList2.get(position).id);
                holder.itemView.getContext().startActivity(intent);


            }
        });

        holder.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map = new HashMap<>();
                map.put("status","reddedildi");
                firebaseFirestore.collection("Applies").document(postArrayList2.get(position).appliesId).update(map);
                Toast.makeText(holder.itemView.getContext(),"Başvuru Reddedildi",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(holder.itemView.getContext(),ShowAppliesActivity.class);
                intent.putExtra("id",postArrayList2.get(position).id);
                holder.itemView.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return postArrayList2.size();
    }

    public class PostHolder  extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView phone;
        private TextView email;
        private TextView q1;
        private TextView q2;
        private TextView q3;
        private TextView a1;
        private TextView a2;
        private TextView a3;
        private TextView detail;
        private Button button;
        private Button button1;
        private  Button button2;

        public PostHolder(@NonNull  View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name2);
            this.phone = itemView.findViewById(R.id.phoneNumber2);
            this.email = itemView.findViewById(R.id.eMail2);
            this.q1 = itemView.findViewById(R.id.q4);
            this.q2 = itemView.findViewById(R.id.q5);
            this.q3 = itemView.findViewById(R.id.q6);
            this.a1 = itemView.findViewById(R.id.a4);
            this.a2 = itemView.findViewById(R.id.a5);
            this.a3 = itemView.findViewById(R.id.a6);
            this.detail = itemView.findViewById(R.id.detail);
            this.button = itemView.findViewById(R.id.sendMessage);
            this.button1 = itemView.findViewById(R.id.approved);
            this.button2 = itemView.findViewById(R.id.rejected);

        }
    }
}
