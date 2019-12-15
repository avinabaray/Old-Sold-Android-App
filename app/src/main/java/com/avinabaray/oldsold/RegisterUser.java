package com.avinabaray.oldsold;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterUser extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Switch switchUserMode;
    private String stringEmail;
    private String stringPassword;
    private String userMode = "Customer";

    private AlertDialog.Builder alertBuilderRegisterUser;
    CommonMethods commonMethods = new CommonMethods();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();
        alertBuilderRegisterUser = new AlertDialog.Builder(RegisterUser.this);
        switchUserMode = findViewById(R.id.switchUserMode);

        switchUserMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isSeller) {
                if(isSeller)
                    userMode = "Seller";
                else
                    userMode = "Customer";
            }
        });
    }

    public void register(View view) {
        editTextEmail = findViewById(R.id.editTextPhone);
        editTextPassword = findViewById(R.id.editTextPassword);
        stringEmail = editTextEmail.getText().toString();
        stringPassword = editTextPassword.getText().toString();

        if(stringEmail.isEmpty() || stringPassword.isEmpty()) {
            commonMethods.createAlert(alertBuilderRegisterUser, "Please fill all the fields");
            return;
        }

        mAuth.createUserWithEmailAndPassword(stringEmail,stringPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    Intent intentToCustomerLoggedIn = new Intent(RegisterUser.this, CustomerLoggedIn.class);
                } else {
                    Log.i("ERROR", task.getException().getMessage());
                    Toast.makeText(RegisterUser.this, "Registration failed", Toast.LENGTH_SHORT).show();
                    editTextEmail.setText("");
                    editTextPassword.setText("");
                }
            }
        });

    }
}
