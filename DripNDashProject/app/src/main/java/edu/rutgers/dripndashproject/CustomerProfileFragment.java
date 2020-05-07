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

        return (ViewGroup) inflater.inflate(R.layout.fragment_profile, container, false);
    }
}
