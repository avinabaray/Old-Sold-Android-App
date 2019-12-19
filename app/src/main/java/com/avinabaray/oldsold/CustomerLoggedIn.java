package com.avinabaray.oldsold;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CustomerLoggedIn extends BaseActivity {

    public static final String TAG = "CustomerLoggedIn";
    private CollectionReference mDocRef = FirebaseFirestore.getInstance().collection("items");
    RecyclerView recyclerViewItems;
    TextView textViewNoItem;
    RecyclerViewAdapterItems adapter;
    CommonMethods commonMethods;

    private ArrayList<String> itemPhotos = new ArrayList<>();
    private ArrayList<String> itemCatgs = new ArrayList<>();
    private ArrayList<String> itemTitles = new ArrayList<>();
    private ArrayList<String> itemDescs = new ArrayList<>();
    private ArrayList<String> seller_id = new ArrayList<>();
    private ArrayList<String> item_id = new ArrayList<>();

    @Override
    public void onBackPressed() {
        // Uncomment the line below to enable BACK button
//        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_logged_in);
        recyclerViewItems = findViewById(R.id.recyclerViewItems);
        textViewNoItem = findViewById(R.id.textViewNoItem);
        commonMethods = new CommonMethods();

        commonMethods.loadingDialogStart(this);

        initDataArrays();
    }

    public void initDataArrays() {
        mDocRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    item_id.add(documentSnapshot.getId());
                    itemPhotos.add(documentSnapshot.getString("i_image"));
                    itemCatgs.add(documentSnapshot.getString("i_category"));
                    itemTitles.add(documentSnapshot.getString("i_title"));
                    itemDescs.add(documentSnapshot.getString("i_desc"));
                    seller_id.add(documentSnapshot.getString("seller_id"));
                }
                Log.wtf("SIZE", String.valueOf(itemTitles.size()));
                if(itemTitles.size() == 0) {
                    recyclerViewItems.setVisibility(View.GONE);
                    textViewNoItem.setVisibility(View.VISIBLE);
                } else {
                    initRecyclerView();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CustomerLoggedIn.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void refresh(View view) {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview");
        commonMethods.loadingDialogStop();

        adapter = new RecyclerViewAdapterItems(this, itemPhotos, itemCatgs, itemTitles, itemDescs, seller_id, item_id);
        recyclerViewItems.setAdapter(adapter);
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));
    }
}
