package edu.rutgers.dripndashproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

import com.google.firebase.Timestamp;

public class CustomerRequestDialog extends AppCompatDialogFragment {
    private EditText numLoadsEstimatedField, specialDetailsField; //initialize text fields
    private CustomerRequestDialogListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_customerrequest, null);

        builder.setView(view)
                .setTitle("Set Job Details")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { //NEED TO ADD FORM VALIDATION
                        String numLoadsEstimate = numLoadsEstimatedField.getText().toString();
                        String customerInstructions = specialDetailsField.getText().toString();
                        listener.createJob(numLoadsEstimate, customerInstructions);

                    }
                });
        numLoadsEstimatedField = view.findViewById(R.id.numLoadsEstimatedEditText); //initialize
        specialDetailsField = view.findViewById(R.id.specialDetailsField);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (CustomerRequestDialogListener) getTargetFragment();
    }

    public interface CustomerRequestDialogListener{
        void createJob(String numLoadsEstimate, String customerInstructions);
    }
}