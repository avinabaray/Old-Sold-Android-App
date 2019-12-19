package com.avinabaray.oldsold;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SellerItemsActivity extends BaseActivity {

    public static final String TAG = "CustomerLoggedIn";
    private CollectionReference mDocRef = FirebaseFirestore.getInstance().collection("items");
    RecyclerView recyclerViewItems;
    TextView textViewNoItem;
    RecyclerViewAdapterItems adapter;

    private ArrayList<String> itemPhotos = new ArrayList<>();
    private ArrayList<String> itemCatgs = new ArrayList<>();
    private ArrayList<String> itemTitles = new ArrayList<>();
    private ArrayList<String> itemDescs = new ArrayList<>();
    private ArrayList<String> seller_id = new ArrayList<>();
    private ArrayList<String> item_id = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_items);
        recyclerViewItems = findViewById(R.id.recyclerViewItems);
        textViewNoItem = findViewById(R.id.textViewNoItem);

        initDataArrays();
    }

    public void initDataArrays() {
        mDocRef.whereEqualTo("seller_id", MainActivity.CURRENT_USER_ID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
                if (itemTitles.size() == 0) {
                    recyclerViewItems.setVisibility(View.GONE);
                    textViewNoItem.setVisibility(View.VISIBLE);
                } else {
                    initRecyclerView();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SellerItemsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview");

        adapter = new RecyclerViewAdapterItems(this, itemPhotos, itemCatgs, itemTitles, itemDescs, seller_id, item_id);
        recyclerViewItems.setAdapter(adapter);
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));
    }

    public void uploadAnItem(View view) {
        Intent intent = new Intent(this, SellerLoggedIn.class);
        startActivity(intent);
    }
}
