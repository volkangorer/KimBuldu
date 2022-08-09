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

public class Adapter2 extends RecyclerView.Adapter<Adapter2.PostHolder> {

    private ArrayList<Post> postArrayList2;

    public Adapter2(ArrayList<Post> postArrayList2) {
        this.postArrayList2 = postArrayList2;
    }
    @NonNull

    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row2, parent, false);
        return new PostHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(@NonNull  Adapter2.PostHolder holder, int position) {
        Button incele = holder.itemView.findViewById(R.id.incele);
        Button updateContent = holder.itemView.findViewById(R.id.updateContent);
        holder.category.setText(this.postArrayList2.get(position).category);
        holder.product.setText(this.postArrayList2.get(position).product);
        holder.date.setText(this.postArrayList2.get(position).date);
        holder.verify.setText(this.postArrayList2.get(position).verify);
        Picasso.get().load(postArrayList2.get(position).downloadurl).into(holder.imageView);
        if (postArrayList2.get(position).userId==0){
            incele.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(holder.itemView.getContext(),DetailActivity.class);
                    intent.putExtra("id",postArrayList2.get(position).id);
                    holder.itemView.getContext().startActivity(intent);
                }
            });
            updateContent.setVisibility(View.GONE);
        }else if (postArrayList2.get(position).userId==1){

            incele.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return postArrayList2.size();
    }

    public class PostHolder  extends RecyclerView.ViewHolder{
        private TextView category;
        private TextView product;
        private TextView date;
        private TextView verify;
        private ImageView imageView;
        public PostHolder(@NonNull  View itemView) {
            super(itemView);
            this.category = itemView.findViewById(R.id.category);
            this.product = itemView.findViewById(R.id.product);
            this.date= itemView.findViewById(R.id.date);
            this.verify = itemView.findViewById(R.id.verify);
            this.imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
