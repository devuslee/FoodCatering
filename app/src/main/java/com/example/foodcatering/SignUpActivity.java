package com.example.foodcatering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    EditText signupUsername, signupEmail, signupPassword, signupconfirmPassword;

    TextInputLayout passwordTextInputLayout, passwordTextInputLayout2;
    TextInputEditText passwordEditText, signupPasswordEditText;
    TextView loginRedirectText;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;
    private FirebaseAuth fAuth;



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
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);
        passwordTextInputLayout2 = findViewById(R.id.passwordTextInputLayout2);
        passwordEditText = findViewById(R.id.signuppassword);
        signupPasswordEditText = findViewById(R.id.signupconfirmpassword);

        fAuth = FirebaseAuth.getInstance();



        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && signupPassword.getError() != null) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            passwordTextInputLayout.setPasswordVisibilityToggleEnabled(true);
                        }
                    }, 2000);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            signupPassword.setError(null);
                        }
                    }, 2000); // Delay in milliseconds (adjust as needed)
                } else {

                    passwordTextInputLayout.setPasswordVisibilityToggleEnabled(false);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            signupPassword.setError(null);
                        }
                    }, 2000); // Delay in milliseconds (adjust as needed)
                }

                if (hasFocus && signupPassword.getError() == null) {
                    passwordTextInputLayout.setPasswordVisibilityToggleEnabled(true);

                } else {
                    passwordTextInputLayout.setPasswordVisibilityToggleEnabled(false);
                }
            }
        });

        signupPasswordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && signupconfirmPassword.getError() != null) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            passwordTextInputLayout2.setPasswordVisibilityToggleEnabled(true);
                        }
                    }, 2000);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            signupconfirmPassword.setError(null);
                        }
                    }, 2000);

                } else {

                    passwordTextInputLayout2.setPasswordVisibilityToggleEnabled(false);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            signupconfirmPassword.setError(null);
                        }
                    }, 2000); // Delay in milliseconds (adjust as needed)
                }

                if (hasFocus && signupconfirmPassword.getError() == null) {
                    passwordTextInputLayout2.setPasswordVisibilityToggleEnabled(true);
                } else {
                    passwordTextInputLayout2.setPasswordVisibilityToggleEnabled(false);
                }
            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                String name = signupUsername.getText().toString();
                String email = signupEmail.getText().toString().replace(".", ",");
                String firebaseemail = signupEmail.getText().toString();
                String password = signupPassword.getText().toString();
                String confirmPassword = signupconfirmPassword.getText().toString();

                signupUsername.clearFocus();
                signupEmail.clearFocus();
                signupPassword.clearFocus();
                signupconfirmPassword.clearFocus();

                if (!(name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty())) {
                    if (email.contains("@")) {
                        if (password.length() >= 6) {
                            if (password.equals(confirmPassword)) {
                                checkUser();
                                fAuth.createUserWithEmailAndPassword(firebaseemail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // New addition: Send Verification Email
                                            FirebaseUser user = fAuth.getCurrentUser();
                                            if (user != null) {
                                                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(SignUpActivity.this, "Verification email sent! Please check your inbox.", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(SignUpActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }
                                });
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

    public void checkUser() {
        String name = signupUsername.getText().toString();
        String email = signupEmail.getText().toString().replace(".", ",");
        String password = signupPassword.getText().toString();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkEmailDatabase = reference.orderByChild("email").equalTo(email);
        checkEmailDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    signupEmail.setError("Such user already exist");
                } else {
                    HelperClass helperClass = new HelperClass(name, email, password);
                    reference.child(email).setValue(helperClass);

                    Toast.makeText(SignUpActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}