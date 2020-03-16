package edu.rutgers.dripndashproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    EditText emailField, passwordField; //initialize edit texts
    Button signInButton, registerButton; //initializes buttons
    FirebaseAuth fireBaseAuthorizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailField = findViewById(R.id.editText);
        passwordField = findViewById(R.id.editText2);
        signInButton = findViewById(R.id.button2);
        registerButton = findViewById(R.id.button3);

        registerButton.setOnClickListener(new View.OnClickListener() { //listener for register button
            @Override
            public void onClick(View v) { //method that runs when button is clicked
                //starts choose Dasher or Register activity
                //Toast.makeText(SignInActivity.this, "Start RegistrationActivity", Toast.LENGTH_SHORT).show(); //just to see if button works
                startActivity(new Intent(SignInActivity.this, RegisterActivity.class));
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() { //initializes signInButton click listener
            @Override
            public void onClick(View v) { //runs when sign in button gets clicked
                String email = emailField.getText().toString(); //convert email to string
                String password = passwordField.getText().toString(); //convert password to string
                if(email.isEmpty()){ //checks if email is empty
                    emailField.setError("Please enter your email");
                    emailField.requestFocus();
                } else if(password.isEmpty()){ //checks if password is empty
                    passwordField.setError("Please enter your password");
                    passwordField.requestFocus();
                }else{ //all fields have been completed
                    fireBaseAuthorizer = FirebaseAuth.getInstance();
                    fireBaseAuthorizer.signInWithEmailAndPassword(email,password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) { //runs if sign in is successful
                                    FirebaseUser user = fireBaseAuthorizer.getCurrentUser(); //get user
                                    if(user.getDisplayName().equals("dasher")){ //user is a dasher
                                        Toast.makeText(SignInActivity.this, "User is a Dasher", Toast.LENGTH_LONG).show(); //just to make sure system is correctly identifying dashers
                                        //start dasher home page activity
                                    } else{
                                        Toast.makeText(SignInActivity.this, "User is a Customer", Toast.LENGTH_SHORT).show(); //just to make sure system is correctly identifying customers
                                        //start customer page activity
                                    }

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignInActivity.this, "Username or Password is wrong", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

            }
        });
    }
}
