package edu.rutgers.dripndashproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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





    }
}
