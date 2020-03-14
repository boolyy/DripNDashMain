package edu.rutgers.dripndashproject;
// modification

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DasherRegisterActivity extends AppCompatActivity {
    Button randomButton;
    FirebaseAuth firebaseAuthorizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dasher_register);
        randomButton = findViewById(R.id.button);

        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuthorizer = FirebaseAuth.getInstance();
                firebaseAuthorizer.createUserWithEmailAndPassword("liamTest@gmail.com","password")
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(DasherRegisterActivity.this, "Account Created", Toast.LENGTH_LONG).show();
                            }
                        });

            }
        });



    }
}
