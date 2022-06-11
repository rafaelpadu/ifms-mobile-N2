package com.example.avaliacao_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    Context context;
    private ArrayList<Photo> photoArrayList;

    public RecyclerAdapter(Context context, ArrayList<Photo> photoArrayList){
        this.context = context;
        this.photoArrayList = photoArrayList;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView categories;
        ImageView imageView;
        View view;
        public MyViewHolder(final View itemView){
            super(itemView);
            view = itemView;
        }
        public void setImageView(String url){
            imageView = view.findViewById(R.id.imageViewItem);
            Glide.with(context).load(url).into(imageView);
        }
        public void setCategories(String categorieString){
            categories = view.findViewById(R.id.categoriesString);
            categories.setText(categorieString);
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.photo_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        Photo photo = photoArrayList.get(position);
        holder.setImageView(photo.getPreviewURL());
        holder.setCategories(photo.getCategories());
    }
    @Override
    public int getItemCount() {
        return photoArrayList.size();
    }
}
