package com.avinabaray.oldsold;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

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
    }
}
