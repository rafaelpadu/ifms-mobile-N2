package com.example.avaliacao_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PersonalRepository extends AppCompatActivity {
    ProgressBar progressBarRepository;
    ArrayList<Photo> photosAPI = new ArrayList<>();
    private RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String userID ;
    Button homeBtnRepository;
    SearchView searchViewInputRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_repository);

        progressBarRepository = findViewById(R.id.progressBarRepository);
        searchViewInputRepository = findViewById(R.id.searchViewInputRepository);
        MaterialShapeDrawable shapeDrawable = new MaterialShapeDrawable();
        shapeDrawable.setFillColor(ContextCompat.getColorStateList(this, android.R.color.transparent));
        shapeDrawable.setStroke(2.0f, ContextCompat.getColor(this,R.color.black));
        ViewCompat.setBackground(searchViewInputRepository, shapeDrawable);
        recyclerView = findViewById(R.id.recyclerViewRepository);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBarRepository.setVisibility(View.GONE);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("photos");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userID = user.getUid();

        homeBtnRepository = findViewById(R.id.homeBtnRepository);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child(userID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        Object fieldsObj = new Object();
                        photosAPI = new ArrayList<>();
                        if(task.isSuccessful()){
                            if(task.getResult().exists()){
                                Toast.makeText(PersonalRepository.this, "Dados recuperados", Toast.LENGTH_SHORT).show();
                                DataSnapshot dataSnapshot = task.getResult();
                                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                                    HashMap fldObj;
                                    fldObj = (HashMap) snapshot.getValue(fieldsObj.getClass());
                                    Photo newObj = new Photo(Long.valueOf(fldObj.get("id").toString()), fldObj.get("previewURL").toString(),fldObj.get("categories").toString());
                                    photosAPI.add(newObj);
                                }
                                RecyclerAdapterRepository adapter = new RecyclerAdapterRepository(PersonalRepository.this, photosAPI);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }else{
                                Toast.makeText(PersonalRepository.this, "Não há imagens no banco de dados", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

        homeBtnRepository.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photosAPI = new ArrayList<>();
                Intent i = new Intent(PersonalRepository.this, SelectItems.class);
                startActivity(i);
            }
        });

        searchViewInputRepository.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String palavra) {
                Query query;
                Object fieldsObj = new Object();
                if(palavra.equals("")){
                    query = databaseReference.child("photos").child(userID).orderByChild("categories");
                }else{
                    query = databaseReference.child("photos").child(userID).orderByChild("categories").;
                }
                photosAPI.clear();
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Toast.makeText(PersonalRepository.this, "Dados recuperados", Toast.LENGTH_SHORT).show();
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                            HashMap fldObj;
                            fldObj = (HashMap) snapshot.getValue(fieldsObj.getClass());
                            Photo newObj = new Photo(Long.valueOf(fldObj.get("id").toString()), fldObj.get("previewURL").toString(),fldObj.get("categories").toString());
                            photosAPI.add(newObj);
                        }
                        RecyclerAdapterRepository adapter = new RecyclerAdapterRepository(PersonalRepository.this, photosAPI);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}