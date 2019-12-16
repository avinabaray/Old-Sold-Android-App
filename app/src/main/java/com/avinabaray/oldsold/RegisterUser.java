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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterUser extends AppCompatActivity {

    public static final String EMAIL = "email";
    public static final String USER_ROLE = "userRole";
    private FirebaseAuth mAuth;

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Switch switchUserRole;
    private String stringEmail;
    private String stringPassword;
    private String userRole = "Customer";

    private AlertDialog.Builder alertBuilderRegisterUser;
    private CollectionReference mDocRef = FirebaseFirestore.getInstance().collection("users");
    CommonMethods commonMethods = new CommonMethods();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        editTextEmail = findViewById(R.id.editTextPhone);
        editTextPassword = findViewById(R.id.editTextPassword);

        mAuth = FirebaseAuth.getInstance();
        alertBuilderRegisterUser = new AlertDialog.Builder(RegisterUser.this);
        switchUserRole = findViewById(R.id.switchUserRole);

        switchUserRole.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isSeller) {
                if(isSeller)
                    userRole = "Seller";
                else
                    userRole = "Customer";
            }
        });
    }

    public void register(View view) {
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
                    Toast.makeText(RegisterUser.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                    // Data to be pushed to Firestore
                    Map<String, Object> userData = new HashMap<String, Object>();
                    userData.put(EMAIL, stringEmail);
                    userData.put(USER_ROLE, userRole);

                    mDocRef.add(userData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                Log.i("STATUS", "User Data saved successfully.");
                            } else {
                                Log.i("STATUS", task.getException().getMessage());
                            }
                        }
                    });

                    Intent intentFromRegisterUser = new Intent(RegisterUser.this, MainActivity.class);
                    startActivity(intentFromRegisterUser);
                } else {
                    Log.i("ERROR", task.getException().getMessage());
                    String taskErrorMsg = task.getException().getMessage();
                    if (taskErrorMsg == null) {
                        taskErrorMsg = "Authentication Failed";
                    }
                    commonMethods.createAlert(alertBuilderRegisterUser, taskErrorMsg);
                    editTextEmail.setText("");
                    editTextPassword.setText("");
                }
            }
        });



    }
}
