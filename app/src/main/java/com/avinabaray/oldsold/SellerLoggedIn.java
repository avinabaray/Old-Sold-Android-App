package com.avinabaray.oldsold;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class SellerLoggedIn extends AppCompatActivity {

        @Override
        public void onBackPressed() {
            // Uncomment the line below to enable BACK button
//        super.onBackPressed();
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.mymenu, menu);

            return super.onCreateOptionsMenu(menu);
        }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Logout) {
            MainActivity.CURRENT_USER_EMAIL = null;
            MainActivity.CURRENT_USER_ID = null;
            MainActivity.CURRENT_USER_ROLE = null;
            FirebaseAuth.getInstance().signOut();
            MainActivity.currentUser = null;

            Intent intentToMainActivity = new Intent(this, MainActivity.class);
            startActivity(intentToMainActivity);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_logged_in);
    }
}
