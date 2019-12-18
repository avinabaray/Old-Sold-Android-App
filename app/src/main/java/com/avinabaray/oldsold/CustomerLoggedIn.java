package com.avinabaray.oldsold;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CustomerLoggedIn extends BaseActivity {

    public static final String TAG = "CustomerLoggedIn";
    private CollectionReference mDocRef = FirebaseFirestore.getInstance().collection("items");
    RecyclerView recyclerViewItems;

    private ArrayList<String> itemPhotos = new ArrayList<>();
    private ArrayList<String> itemCatgs = new ArrayList<>();
    private ArrayList<String> itemTitles = new ArrayList<>();
    private ArrayList<String> itemDescs = new ArrayList<>();

    @Override
    public void onBackPressed() {
        // Uncomment the line below to enable BACK button
//        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_logged_in);

//        recyclerViewItems.setHasFixedSize(true);
        initDataArrays();
    }

    public void initDataArrays() {
        mDocRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    itemPhotos.add(documentSnapshot.getString("i_image"));
                    itemCatgs.add(documentSnapshot.getString("i_category"));
                    itemTitles.add(documentSnapshot.getString("i_title"));
                    itemDescs.add(documentSnapshot.getString("i_desc"));
                }
                initRecyclerView();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CustomerLoggedIn.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview");
        recyclerViewItems = findViewById(R.id.recyclerViewItems);
        RecyclerViewAdapterItems adapter = new RecyclerViewAdapterItems(this, itemPhotos, itemCatgs, itemTitles, itemDescs);
        recyclerViewItems.setAdapter(adapter);
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));
    }
}
