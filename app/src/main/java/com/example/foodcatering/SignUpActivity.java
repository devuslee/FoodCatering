package com.example.foodcatering;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText signupUsername, signupEmail, signupPassword, signupconfirmPassword;

    TextView loginRedirectText;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signupUsername = findViewById(R.id.signupusername);
        signupEmail = findViewById(R.id.signupemail);
        signupPassword = findViewById(R.id.signuppassword);
        signupconfirmPassword = findViewById(R.id.signupconfirmpassword);
        signupButton = findViewById(R.id.signupbutton);
        loginRedirectText = findViewById(R.id.signinlink);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                String name = signupUsername.getText().toString();
                String email = signupEmail.getText().toString().replace(".", ",");
                String password = signupPassword.getText().toString();
                String confirmPassword = signupconfirmPassword.getText().toString();

                if (!(name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty())) {
                    if (email.contains("@")) {
                        if (password.length() >= 6) {
                            if (password.equals(confirmPassword)) {
                                signupconfirmPassword.setError("Password does not match");
                                HelperClass helperClass = new HelperClass(name, email, password);
                                reference.child(email).setValue(helperClass);

                                Toast.makeText(SignUpActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                signupconfirmPassword.setError("Password does not match");
                            }
                    } else {
                        signupPassword.setError("Password must be at least 6 characters");
                    }
                } else {
                    signupEmail.setError("Invalid email address");
                }
            } else {
                    signupUsername.setError("Field cannot be empty");
                    signupEmail.setError("Field cannot be empty");
                    signupPassword.setError("Field cannot be empty");
                    signupconfirmPassword.setError("Field cannot be empty");
                }
            }
        });


        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}