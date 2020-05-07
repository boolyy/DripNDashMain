package edu.rutgers.dripndashproject;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;





public class CustomerHomeActivity extends AppCompatActivity {
    //initialize fragments
    private CustomerHomeFragment customerHomeFragment;
    private CustomerProfileFragment customerProfileFragment;
    private CustomerPastJobsFragment customerPastJobsFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dasher_home);
        if(savedInstanceState == null){ //initialize fragment objects
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
                        case R.id.nav_pastJobs: //if past jobs button is selected
                            getSupportFragmentManager().beginTransaction().show(customerPastJobsFragment)
                                    .hide(customerHomeFragment)
                                    .hide(customerProfileFragment)
                                    .commit();
                            break;
                        case R.id.nav_profile: //if profiles button is selected
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
