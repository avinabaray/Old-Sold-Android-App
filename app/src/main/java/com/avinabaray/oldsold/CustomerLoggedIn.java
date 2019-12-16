package com.avinabaray.oldsold;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CustomerLoggedIn extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        // Uncomment the line below to enable BACK button
//        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_logged_in);
    }
}
