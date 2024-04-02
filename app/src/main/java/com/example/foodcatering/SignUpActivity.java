package com.example.foodcatering;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
                String email = signupEmail.getText().toString();
                String password = signupPassword.getText().toString();
                String confirmPassword = signupconfirmPassword.getText().toString();

                HelperClass helperClass = new HelperClass(name, email, password);
                reference.child(email).setValue(helperClass);

                Toast.makeText(SignUpActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
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