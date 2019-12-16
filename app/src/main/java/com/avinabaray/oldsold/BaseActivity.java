package com.avinabaray.oldsold;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.itemHelp:
                Toast.makeText(this, "Help and Support will be added soon", Toast.LENGTH_SHORT).show();
                break;

            case R.id.itemLogout:
                MainActivity.CURRENT_USER_EMAIL = null;
                MainActivity.CURRENT_USER_ID = null;
                MainActivity.CURRENT_USER_ROLE = null;
                FirebaseAuth.getInstance().signOut();
                MainActivity.currentUser = null;

                Intent intentToMainActivity = new Intent(this, MainActivity.class);
                startActivity(intentToMainActivity);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
