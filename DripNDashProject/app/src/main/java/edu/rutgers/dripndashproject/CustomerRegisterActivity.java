package edu.rutgers.dripndashproject;
// modification

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CustomerRegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button registerButton; //initialize buttons
    EditText firstNameField, lastNameField, emailField, dormRoomField, passwordField, confirmPasswordField, ageField; //initialize edit text
    Spinner campusField, dormField, genderField; //initialize spinners
    FirebaseAuth firebaseAuthorizer; //initialize Fire base authorization for sign up authentication
    FirebaseFirestore db; //initialize fire store to add in user's information to database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_register);

        //assigning variables to their buttons, spinners, and edit text fields
        registerButton = findViewById(R.id.button);
        firstNameField = findViewById(R.id.editText3);
        lastNameField = findViewById(R.id.editText4);
        emailField = findViewById(R.id.editText5);
        campusField = findViewById(R.id.spinner);
        dormField = findViewById(R.id.spinner2);
        dormRoomField = findViewById(R.id.editText8);
        genderField = findViewById(R.id.spinner4);
        passwordField = findViewById(R.id.editText6);
        confirmPasswordField = findViewById(R.id.editText7);
        ageField = findViewById(R.id.editText9);

        //adding values to campusField spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.campuses, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campusField.setAdapter(adapter);
        campusField.setOnItemSelectedListener(this);


        //initializing adapter2 for dormField spinner, the actual values for the spinner are defined below
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.plsChooseCampus, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dormField.setAdapter(adapter2);

        //initializing adapter 3 for gender spinner
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderField.setAdapter(adapter3);

        registerButton.setOnClickListener(new View.OnClickListener() { //sets up listener, checks when button is pressed
            @Override
            public void onClick(View v) { //method that runs when button is pressed
                //assigns values based on what is typed in the edit text fields and what is selected in the spinners
                final String firstName = firstNameField.getText().toString();
                final String lastName = lastNameField.getText().toString();
                final String email = emailField.getText().toString();
                String campus = campusField.getSelectedItem().toString();
                final String dorm = dormField.getSelectedItem().toString();
                final String dormRoom = dormRoomField.getText().toString();
                final String gender = genderField.getSelectedItem().toString();
                String password =  passwordField.getText().toString();
                String confirmPassword = confirmPasswordField.getText().toString();
                final String age = ageField.getText().toString(); //will convert this to int later

                if(firstName.isEmpty()){ //checks if first name is empty
                    firstNameField.setError("First name is missing!");
                    firstNameField.requestFocus();
                } else if(lastName.isEmpty()){ //check if last name is empty
                    lastNameField.setError("Last name is missing!");
                    lastNameField.requestFocus();
                } else if(email.isEmpty()){
                    emailField.setError("Email is missing");
                    emailField.requestFocus();
                } else if(campus.equals("Choose your campus")){ //checking if user chose a campus
                    Toast.makeText(CustomerRegisterActivity.this, "Please select the campus you live on", Toast.LENGTH_LONG).show();
                    campusField.requestFocus();
                } else if(dorm.equals("Choose your dorm")){ //checking if user chose a dorm
                    Toast.makeText(CustomerRegisterActivity.this, "Please select the dorm you live in", Toast.LENGTH_LONG).show();
                    dormField.requestFocus();
                } else if(dormRoom.isEmpty()){
                    dormRoomField.setError("Room number is missing!");
                    dormRoomField.requestFocus();
                } else if(age.isEmpty()){
                    ageField.setError("Age is missing");
                    ageField.requestFocus();
                } else if(gender.equals("Choose your gender")){
                    Toast.makeText(CustomerRegisterActivity.this, "Please choose a gender!", Toast.LENGTH_LONG).show();
                    genderField.requestFocus();
                } else if(password.isEmpty()) {
                    passwordField.setError("Password is missing!");
                    passwordField.requestFocus();
                } else if(confirmPassword.isEmpty()){
                    confirmPasswordField.setError("Please confirm your password");
                    confirmPasswordField.requestFocus();
                } else if(!password.equals(confirmPassword)){ //checks if password matches confirm password
                    confirmPasswordField.setError("Does not match your password");
                    confirmPasswordField.requestFocus();
                } else{ //all fields have been filled in
                    firebaseAuthorizer = FirebaseAuth.getInstance();
                    firebaseAuthorizer.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CustomerRegisterActivity.this, new OnCompleteListener<AuthResult>() { //create user with email and password, and initializes listener
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) { //runs when creation of account is completed
                            if(!task.isSuccessful()){ //if task is unsuccessful
                                Toast.makeText(CustomerRegisterActivity.this, "Profile creation Failed", Toast.LENGTH_LONG).show();
                            } else{ //if task is successful, add all fields to firebase database
                                FirebaseUser user = firebaseAuthorizer.getCurrentUser(); //creates user
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName("customer").build(); //initializes profile changes to be made for user. Display name will be set to Dasher since user is registering as dasher
                                user.updateProfile(profileUpdates); //updates profile based on previous command
                                String uid = firebaseAuthorizer.getCurrentUser().getUid(); //gets uid of user, this will be the name of their document

                                Map<String, Object> userProfile = new HashMap<>(); //creates map that contains all fields
                                userProfile.put("FIRST_NAME", firstName); //all fields that will be added to document
                                userProfile.put("LAST_NAME", lastName);
                                userProfile.put("EMAIL", email);
                                userProfile.put("DORM", dorm);
                                userProfile.put("DORM_ROOM", dormRoom);
                                userProfile.put("GENDER", gender);
                                userProfile.put("AGE", Integer.parseInt(age));
                                userProfile.put("NUM_JOBS_COMPLETED", 0);
                                userProfile.put("RATING", 5);
                                userProfile.put("REGISTER_TIMESTAMP", Timestamp.now());
                                userProfile.put("COMPLETED_JOBS", new ArrayList<>(Collections.<String>emptyList()));

                                db = FirebaseFirestore.getInstance(); //initialize database
                                db.collection("customers").document(uid).set(userProfile) //Since this is customer Register, the uid document is placed in customers collection
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(CustomerRegisterActivity.this, "All fields have been created", Toast.LENGTH_LONG).show();
                                            }
                                        }) .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });

                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { //runs when item is selected from campus spinner, dorm spinner will have values dependant on selected item of campus spinner
        //the arrays in R.array.(ARRAY NAME HERE) are defined in strings.xml under main -> res -> values

        if(campusField.getSelectedItem().toString().equals("Choose your campus")){ //user has not chosen a campus so there are no options for dorms to show
            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.plsChooseCampus, android.R.layout.simple_spinner_item);
            dormField.setAdapter(adapter2);
        }else if(campusField.getSelectedItem().toString().equals("Busch")){ //if campus is busch, then dorm spinner will show dorms from busch
            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.buschDorms, android.R.layout.simple_spinner_item);
            dormField.setAdapter(adapter2);
        } else if(campusField.getSelectedItem().toString().equals("College Ave")){
            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.collegeAveDorms, android.R.layout.simple_spinner_item);
            dormField.setAdapter(adapter2);
        } else if(campusField.getSelectedItem().toString().equals("Douglass")){
            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.douglassDorms, android.R.layout.simple_spinner_item);
            dormField.setAdapter(adapter2);
        } else if(campusField.getSelectedItem().toString().equals("Livingston")){
            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.liviDorms, android.R.layout.simple_spinner_item);
            dormField.setAdapter(adapter2);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
