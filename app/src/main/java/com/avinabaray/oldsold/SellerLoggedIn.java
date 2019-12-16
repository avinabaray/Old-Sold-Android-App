package com.avinabaray.oldsold;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerLoggedIn extends BaseActivity implements AdapterView.OnItemSelectedListener {

    public static final String I_CATEGORY = "i_category";
    public static final String I_TITLE = "i_title";
    public static final String I_DESC = "i_desc";
    public static final String SELLER_ID = "seller_id";
    private EditText editTextItemTitle;
    private EditText editTextItemDesc;
    private String itemTitle;
    private String itemDesc;
    private String itemCatg = "Mobile Phones";

    private AlertDialog.Builder alertBuilder;
    private CollectionReference mDocRef = FirebaseFirestore.getInstance().collection("items");
    CommonMethods commonMethods = new CommonMethods();

    @Override
    public void onBackPressed() {
        // Uncomment the line below to enable BACK button
//        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_logged_in);
        alertBuilder = new AlertDialog.Builder(SellerLoggedIn.this);

        // Edit Text Data
        editTextItemTitle = findViewById(R.id.editTextItemTitle);
        editTextItemDesc = findViewById(R.id.editTextItemDesc);


        // Setup Dropdown Menu
        List<String> itemCategories = new ArrayList<String>();
        itemCategories.add("Mobile Phones");
        itemCategories.add("Furniture");
        itemCategories.add("Home Appliances");
        itemCategories.add("Gadgets");

        Spinner spinnerItemCategory = findViewById(R.id.spinnerItemCategory);
        ArrayAdapter<String> itemCategoryAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                itemCategories
        );
        spinnerItemCategory.setAdapter(itemCategoryAdapter);
        spinnerItemCategory.setOnItemSelectedListener(this);


    }

    public void uploadItem(View view) {
        itemTitle = editTextItemTitle.getText().toString();
        itemDesc = editTextItemDesc.getText().toString();

        // Data to be pushed to Firestore
        Map<String, Object> itemDetails = new HashMap<String, Object>();
        itemDetails.put(SELLER_ID, MainActivity.CURRENT_USER_ID);
        itemDetails.put(I_CATEGORY, itemCatg);
        itemDetails.put(I_TITLE, itemTitle);
        itemDetails.put(I_DESC, itemDesc);

        mDocRef.add(itemDetails).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    commonMethods.createAlert(alertBuilder, "Item uploaded successfully!");
                } else {
                    String errorMsg = task.getException().getMessage();
                    commonMethods.createAlert(alertBuilder, errorMsg);
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        switch (pos) {
            case 0:
                itemCatg = "Mobile Phones";
                break;
            case 1:
                itemCatg = "Furniture";
                break;
            case 2:
                itemCatg = "Home Appliances";
                break;
            case 3:
                itemCatg = "Gadgets";
                break;
            default:
                itemCatg = "Unknown";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Do nothing
    }
}
