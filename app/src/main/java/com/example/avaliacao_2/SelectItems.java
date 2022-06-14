package com.example.avaliacao_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class SelectItems extends AppCompatActivity {
    SearchView searchViewInput;
    ProgressBar progressBarSelectItems;

    private RequestQueue requestQueue;
    ArrayList<Photo> photosAPI;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_items);

        searchViewInput = findViewById(R.id.searchViewInput);
        progressBarSelectItems = findViewById(R.id.progressBarSelectItems);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        requestQueue = VolleySingleton.getmInstance(this).getRequestQueue();
        progressBarSelectItems.setVisibility(View.GONE);
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseDatabase.setPersistenceEnabled(true);
        searchViewInput.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressBarSelectItems.setVisibility(View.VISIBLE);
                try {
                    query = java.net.URLEncoder.encode(query, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String url = URLs.BASE_URL + URLs.API_KEY + URLs.IMAGE_TYPE + URLs.LANGUAGE + "&q=" + query;


                getData(url, query);
                progressBarSelectItems.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                progressBarSelectItems.setVisibility(View.VISIBLE);
                try {
                    newText = java.net.URLEncoder.encode(newText, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String url = URLs.BASE_URL + URLs.API_KEY + URLs.IMAGE_TYPE + URLs.LANGUAGE + "&q=" + newText;

                getData(url , newText);
                progressBarSelectItems.setVisibility(View.GONE);
                return false;
            }
        });



//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Metodo para avisar no log que algo foi alterado no banco de dados desse usuario
//                Log.d("Atenção", "onDataChange: Informação adicionado no BD: \n" +
//                        dataSnapshot.getValue());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Falhou
//                Log.w("Atenção", "Falha ao ler valor", error.toException());
//            }
//        });
    }

    private void getData(String url, String text) {
        photosAPI = new ArrayList<>();
        if(text.isEmpty()) {
            RecyclerAdapter adapter = new RecyclerAdapter(this, photosAPI);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            return;
        }
        requestQueue = Volley.newRequestQueue(this);
        Log.d("Estado", url);
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray responseArray = response.getJSONArray("hits");
                    for (int i = 0; i < responseArray.length(); i++) {
                        if(i >=20){
                            return;
                        }
                        try {
                            JSONObject responseObj = responseArray.getJSONObject(i);
                            Photo newObj = new Photo(
                                    Long.valueOf(responseObj.getString("id")),
                                    responseObj.getString("previewURL"),
                                    responseObj.getString("tags"));
                            photosAPI.add(newObj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    RecyclerAdapter adapter = new RecyclerAdapter(SelectItems.this, photosAPI);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SelectItems.this, "Não foi encontrado nenhuma imagem!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
}