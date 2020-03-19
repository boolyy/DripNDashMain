package edu.rutgers.dripndashproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DasherHomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dasher_home);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation); //initializes bottom navigation
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DasherProfileFragment()).commit(); //makes profile fragment the first thing that is seen,
                                                                                                                                // please don't change I know it's weird for now
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = //initialize listener
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch(item.getItemId()){ //checks which button has been
                        case R.id.nav_home:
                            selectedFragment = new DasherHomeFragment();
                            break;
                        case R.id.nav_pastJobs:
                            selectedFragment = new DasherPastJobsFragment();
                            break;
                        case R.id.nav_profile:
                            selectedFragment = new DasherProfileFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };
}
