package edu.rutgers.dripndashproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {
    Button customerRegisterButton; //initialize buttons
    Button dasherRegisterButton; //initialize buttons
    Button customerInfoButton;
    Button dasherInfoButton;
    TextView txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        customerRegisterButton = findViewById(R.id.button7);
        dasherRegisterButton = findViewById(R.id.button8);
        customerInfoButton = findViewById(R.id.button12);
        dasherInfoButton = findViewById(R.id.button13);
        txt = findViewById(R.id.textView9);

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

        customerInfoButton.setOnClickListener(new View.OnClickListener() { //sets up listener for customer register button
            @Override
            public void onClick(View v) { //runs when customer info button is clicked
                txt.setText("Creating a Customer account will allow you \n to make use of the services Drip n' Dash has to offer!");
            }
        });

        dasherInfoButton.setOnClickListener(new View.OnClickListener() { //sets up listener for customer register button
            @Override
            public void onClick(View v) { //runs when customer info button is clicked
                txt.setText("By creating a Dasher account you will be \n able to get paid by doing laundry!");
            }
        });



    }
}
