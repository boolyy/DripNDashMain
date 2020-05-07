package edu.rutgers.dripndashproject;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;




public class DasherProfileFragment extends Fragment {
    @Nullable

    Button editButton, saveButton; //initialize buttons
    EditText firstNameField, emailField, dormRoomField; // initialize user info
    Spinner dormField; //initialize spinners
    RatingBar ratingBar; // initialize rating bar
    FirebaseFirestore db; //initialize fire store to add in user's information to database

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Get the resources
        firstNameField = getView().findViewById(R.id.editText3);
        emailField = getView().findViewById(R.id.editText5);
        dormRoomField = getView().findViewById(R.id.editText8);
        dormField = getView().findViewById(R.id.spinner2);
        editButton = getView().findViewById(R.id.button6);
        saveButton = getView().findViewById(R.id.button9);
        ratingBar = getView().findViewById(R.id.ratingBar);

        // Firebase
        db = FirebaseFirestore.getInstance();

        class EditProfile extends Dasher{

            void edit(final Dasher dasher){

                // Static Name
                String firstName = dasher.firstName.toString();
                String lastName = dasher.lastName.toString();
                String fullName = firstName + ' ' + lastName;
                firstNameField.setText(fullName);
                // Static Dorm Number
                Integer currentDorm = Integer.parseInt(dasher.dormRoom);
                dormRoomField.setText(currentDorm);
                // Static Rating (Having trouble getting rating)
                Double rating = dasher.rating;
                ratingBar.setRating(rating.intValue());
                // Static Email
                String currentEmail = dasher.email;
                emailField.setText(currentEmail);

                // Edit button listener
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Allow user to change values
                        final String email = emailField.getText().toString();
                        final String dormNumber = dormRoomField.getText().toString();
                        final String dorm = dormField.getSelectedItem().toString();


                        saveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Update the dasher instance
                                dasher.email = email;
                                dasher.dormRoom = dormNumber;
                                dasher.dorm = dorm;

                                // Update the database
                                // Update email
                                db.collection("dasher").document(dasher.uid)
                                        .update("EMAIL", dasher.email);
                                // Update dorm
                                db.collection("dasher").document(dasher.uid)
                                        .update("DORM_ROOM", dasher.dormRoom);
                                // Update dorm
                                db.collection("dasher").document(dasher.uid)
                                        .update("DORM", dasher.dorm);
                            }
                        });
                    }
                });

            }

        }


        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


}
