package edu.rutgers.dripndashproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;

import java.util.List;

public class DasherHomeActivity extends AppCompatActivity {
    private DasherHomeFragment dasherHomeFragment;
    private DasherProfileFragment dasherProfileFragment;
    private DasherPastJobsFragment dasherPastJobsFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dasher_home);

        if(savedInstanceState == null){ //initialize fragments
            dasherHomeFragment = new DasherHomeFragment();
            dasherProfileFragment = new DasherProfileFragment();
            dasherPastJobsFragment = new DasherPastJobsFragment();
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation); //initializes bottom navigation
        bottomNav.setOnNavigationItemSelectedListener(navListener); //set up listener
        bottomNav.setSelectedItemId(R.id.nav_home); //set home button as initial selected button, this will cause home page to be the first page shown
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, dasherProfileFragment)
                .add(R.id.fragment_container, dasherPastJobsFragment)
                .add(R.id.fragment_container, dasherHomeFragment).commit(); //loads up all the different fragments so that they can be accessed easily later
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = //initialize listener
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) { //method that runs when item on nav is selected

                    switch(item.getItemId()){ //checks which button has been pressed
                        case R.id.nav_home: //if home button is selected
                            getSupportFragmentManager().beginTransaction().show(dasherHomeFragment) //shows home fragment
                            .hide(dasherPastJobsFragment) //hides past jobs fragment
                            .hide(dasherProfileFragment) //hides profile fragment
                            .commit();
                            break;
                        case R.id.nav_pastJobs:
                            //selectedFragment = dasherPastJobsFragment;
                            getSupportFragmentManager().beginTransaction().show(dasherPastJobsFragment)
                                    .hide(dasherHomeFragment)
                                    .hide(dasherProfileFragment)
                                    .commit();
                            break;
                        case R.id.nav_profile:
                            //selectedFragment = dasherProfileFragment;
                            getSupportFragmentManager().beginTransaction().show(dasherProfileFragment)
                                    .hide(dasherHomeFragment)
                                    .hide(dasherPastJobsFragment)
                                    .commit();
                            break;
                    }
                    return true;
                }
            };





}
