package edu.rutgers.dripndashproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterActivity extends AppCompatActivity {
    Button customerRegisterButton; //initialize buttons
    Button dasherRegisterButton; //initialize buttons

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        customerRegisterButton = findViewById(R.id.button7);
        dasherRegisterButton = findViewById(R.id.button8);

        customerRegisterButton.setOnClickListener(new View.OnClickListener() { //sets up listener for customer register button
            @Override
            public void onClick(View v) { //runs when customer register button is clicked
                startActivity(new Intent(RegisterActivity.this, CustomerRegisterActivity.class));
            }
        });

        dasherRegisterButton.setOnClickListener(new View.OnClickListener() {//sets up listener for dasher reg button
            @Override
            public void onClick(View v) { //runs when dash reg button is clicked
                startActivity((new Intent(RegisterActivity.this, DasherRegisterActivity.class)));
            }
        });






    }
}
