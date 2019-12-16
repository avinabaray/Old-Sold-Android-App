package com.avinabaray.oldsold;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText editTextEmail;
    private EditText editTextPassword;
    private String stringEmail;
    private String stringPassword;

    private AlertDialog.Builder alertBuilderMainActivity;
    CommonMethods commonMethods = new CommonMethods();

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        alertBuilderMainActivity = new AlertDialog.Builder(MainActivity.this);
    }

    public void login(View view){
        editTextEmail = findViewById(R.id.editTextPhone);
        editTextPassword = findViewById(R.id.editTextPassword);
        stringEmail = editTextEmail.getText().toString();
        stringPassword = editTextPassword.getText().toString();

        if(stringEmail.isEmpty() || stringPassword.isEmpty()) {
            commonMethods.createAlert(alertBuilderMainActivity, "Please fill all the fields");
            return;
        }

        mAuth.signInWithEmailAndPassword(stringEmail, stringPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // Sign in success
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                } else {
                    // Sign in fails
//                    Toast.makeText(MainActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    String taskErrorMsg = task.getException().getMessage();
                    if (taskErrorMsg == null) {
                        taskErrorMsg = "Authentication Failed";
                    }
                    commonMethods.createAlert(alertBuilderMainActivity, taskErrorMsg);
                    updateUI(null);
                }
            }
        });
    }

    public void register(View view) {
        Intent intentToRegisterUser = new Intent(this, RegisterUser.class);
        startActivity(intentToRegisterUser);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Log.wtf("CURRENT_USER", user.getDisplayName());
            Intent intentToCustomerLoggedIn = new Intent(this, CustomerLoggedIn.class);
            startActivity(intentToCustomerLoggedIn);
        } else {
            editTextEmail.setText("");
            editTextPassword.setText("");
        }
    }
}
