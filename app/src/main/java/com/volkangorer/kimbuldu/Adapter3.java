package com.volkangorer.kimbuldu;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter3 extends RecyclerView.Adapter<Adapter3.PostHolder> {
    private ArrayList<Post2> postArrayList2;

    public Adapter3(ArrayList<Post2> postArrayList2) {
        this.postArrayList2 = postArrayList2;
    }
    @NonNull

    @Override
    public Adapter3.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row3, parent, false);
        return new Adapter3.PostHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(@NonNull  Adapter3.PostHolder holder, int position) {
        holder.advertNo.setText("İlan Numarası : "+postArrayList2.get(position).advertNo);
        holder.advertNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(),Detail2Activity.class);
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
        private TextView advertNo;

        public PostHolder(@NonNull  View itemView) {
            super(itemView);
            this.advertNo = itemView.findViewById(R.id.advertNo);

        }
    }
}
