package com.volkangorer.kimbuldu;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter4 extends RecyclerView.Adapter<Adapter4.PostHolder> {
    private ArrayList<Post3> postArrayList2;

    public Adapter4(ArrayList<Post3> postArrayList2) {
        this.postArrayList2 = postArrayList2;
    }
    @NonNull

    @Override
    public Adapter4.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row3, parent, false);
        return new Adapter4.PostHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(@NonNull  Adapter4.PostHolder holder, int position) {
        holder.sender.setText("Gönderen: "+postArrayList2.get(position).sender);
        holder.sender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(),MessagesDetail.class);
                intent.putExtra("mId",postArrayList2.get(position).mId);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postArrayList2.size();
    }

    public class PostHolder  extends RecyclerView.ViewHolder{
        private TextView sender;

        public PostHolder(@NonNull  View itemView) {
            super(itemView);
            this.sender = itemView.findViewById(R.id.advertNo);

        }
    }
}
