package com.example.avaliacao_2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RecyclerAdapterRepository extends RecyclerView.Adapter<RecyclerAdapterRepository.MyViewHolder> {
    Context context;
    private ArrayList<Photo> photoArrayList;

    public RecyclerAdapterRepository(Context context, ArrayList<Photo> photoArrayList){
        this.context = context;
        this.photoArrayList = photoArrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView categories;
        ImageView imageView;
        View view;
        Button addBtn;
        public MyViewHolder(final View itemView){
            super(itemView);
            view = itemView;
            addBtn = view.findViewById(R.id.addButton);
            addBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.minus_thick, 0,0,0);
            addBtn.setPadding(60,5,0,0);
            addBtn.setTextColor(Color.BLACK);
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
    public RecyclerAdapterRepository.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.photo_item, parent, false);
        return new RecyclerAdapterRepository.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterRepository.MyViewHolder holder, int position) {
        Photo photo = photoArrayList.get(position);
        holder.setImageView(photo.getPreviewURL());
        holder.setCategories(photo.getCategories());
        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogView(v.getRootView().getContext(), photo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return photoArrayList.size();
    }

    public void showDialogView(Context context, Photo photo){
        AlertDialog.Builder confirmAdd = new AlertDialog.Builder(context);
        confirmAdd.setTitle("Atenção!");
        confirmAdd.setMessage("Deseja remover o item?").setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference myRef = mFirebaseDatabase.getReference();
                FirebaseUser user = mAuth.getCurrentUser();
                String userID = user.getUid();
                myRef.child("photos").child(userID).child(String.valueOf(photo.getId())).removeValue();
                Toast.makeText(context, "Imagem salva no banco de dados!", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Não", null);
        confirmAdd.show();
    }
}
