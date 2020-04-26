package edu.rutgers.dripndashproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CustomerProfileFragment extends Fragment {
    @Nullable
    Button editButton, saveButton; //initialize buttons
    EditText firstNameField, emailField, dormRoomField; // initialize user info
    Spinner dormField; //initialize spinners
    RatingBar ratingBar; // initialize rating bar
    FirebaseFirestore db; //initialize fire store to add in user's information to database
    @Override
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

        class EditProfile extends Customer{

            void edit(final Customer customer){

                // Static Name
                String firstName = customer.firstName.toString();
                String lastName = customer.lastName.toString();
                String fullName = firstName + ' ' + lastName;
                firstNameField.setText(fullName);
                // Static Dorm Number
                Integer currentDorm = Integer.parseInt(customer.dormRoom);
                dormRoomField.setText(currentDorm);
                // Static Rating (Having trouble getting rating)
                DocumentReference ref = db.collection("customer").document(customer.uid);
                String rating = ref.get("RATING").toString();
                int stars = Integer.parseInt(rating);
                ratingBar.setRating(stars);
                // Static Email
                String currentEmail = customer.email;
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
                                customer.email = email;
                                customer.dormRoom = dormNumber;
                                customer.dorm = dorm;
                                // Update the database
                                // Update email
                                db.collection("customer").document(customer.uid)
                                        .update("EMAIL", customer.email);
                                // Update dorm
                                db.collection("customer").document(customer.uid)
                                        .update("DORM_ROOM", customer.dormRoom);
                                // Update dorm
                                db.collection("customer").document(customer.uid)
                                        .update("DORM", customer.dorm);
                            }

                        });

                    };
                });
            }

        }
        return (ViewGroup) inflater.inflate(R.layout.fragment_profile, container, false);
    }
}
