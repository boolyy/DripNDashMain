package edu.rutgers.dripndashproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;


public class DasherJobAcceptDialog extends AppCompatDialogFragment {
    DasherJobAcceptDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Job Assigned")
                .setMessage("Would you like to accept this job?")
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() { //listener for accept button
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onAcceptClicked();
                    }
                })
                .setNegativeButton("Decline", new DialogInterface.OnClickListener() { //listener for decline button
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }

    public interface DasherJobAcceptDialogListener{
        void onAcceptClicked();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

}
