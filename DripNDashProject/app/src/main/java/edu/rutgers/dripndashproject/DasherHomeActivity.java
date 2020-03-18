package edu.rutgers.dripndashproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DasherHomeActivity extends AppCompatActivity {
    Button settingsButton, profileButton, requestJobButton, pastJobsButton; //initialize buttons

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dasher_home);

        settingsButton = findViewById(R.id.button7); //assign buttons their layouts
        profileButton = findViewById(R.id.button5);
        requestJobButton = findViewById(R.id.button4);
        pastJobsButton = findViewById((R.id.button9));

        profileButton.setOnClickListener(new View.OnClickListener() { //initialize click listener for profile button
            @Override
            public void onClick(View v) { //method that runs when profile button is clicked
                //open Dasher profile activity
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() { //initialize click listener for settings button
            @Override
            public void onClick(View v) { //method that runs when settings button is clicked
                //open settings profile activity
            }
        });

        requestJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        pastJobsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //

            }
        });

    }
}
