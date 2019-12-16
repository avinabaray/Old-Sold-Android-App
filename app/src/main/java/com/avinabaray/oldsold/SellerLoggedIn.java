package com.avinabaray.oldsold;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class SellerLoggedIn extends BaseActivity {

    @Override
    public void onBackPressed() {
        // Uncomment the line below to enable BACK button
//        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_logged_in);

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


    }
}
