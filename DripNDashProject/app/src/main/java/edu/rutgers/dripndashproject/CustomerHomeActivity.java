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
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomerHomeActivity extends AppCompatActivity {
    private CustomerHomeFragment customerHomeFragment;
    private CustomerProfileFragment customerProfileFragment;
    private CustomerPastJobsFragment customerPastJobsFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dasher_home);
        if(savedInstanceState == null){ //initialize fragments
            customerHomeFragment = new CustomerHomeFragment();
            customerProfileFragment = new CustomerProfileFragment();
            customerPastJobsFragment = new CustomerPastJobsFragment();
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation); //initializes bottom navigation
        bottomNav.setOnNavigationItemSelectedListener(navListener); //set up listener
        bottomNav.setSelectedItemId(R.id.nav_home); //set home button as initial selected button, this will cause home page to be the first page shown
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, customerProfileFragment)
                .add(R.id.fragment_container, customerPastJobsFragment)
                .add(R.id.fragment_container, customerHomeFragment).commit(); //loads up all the different fragments so that they can be accessed easily later
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = //initialize listener
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) { //method that runs when item on nav is selected

                    switch(item.getItemId()){ //checks which button has been pressed
                        case R.id.nav_home: //if home button is selected
                            getSupportFragmentManager().beginTransaction().show(customerHomeFragment) //shows home fragment
                                    .hide(customerPastJobsFragment) //hides past jobs fragment
                                    .hide(customerProfileFragment) //hides profile fragment
                                    .commit();
                            break;
                        case R.id.nav_pastJobs:
                            //selectedFragment = dasherPastJobsFragment;
                            getSupportFragmentManager().beginTransaction().show(customerPastJobsFragment)
                                    .hide(customerHomeFragment)
                                    .hide(customerProfileFragment)
                                    .commit();
                            break;
                        case R.id.nav_profile:
                            //selectedFragment = dasherProfileFragment;
                            getSupportFragmentManager().beginTransaction().show(customerProfileFragment)
                                    .hide(customerHomeFragment)
                                    .hide(customerPastJobsFragment)
                                    .commit();
                            break;
                    }
                    return true;
                }
            };



}
