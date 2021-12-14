package nl.ags.picum.UI.dialog;

import android.Manifest;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import nl.ags.picum.R;

public class PermissionDeniedDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.GPS_PERMISSION_EXPLAINER)
               .setTitle(R.string.GPS_PERMISSION_DENIED)
                .setNeutralButton(R.string.Settings, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                        intent.setData(uri);
                        getContext().startActivity(intent);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }


    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            super.onCancel(dialog);
        } else {

            CharSequence text = "To use the app we need the permission for GPS";
            Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
            this.dismiss();
            this.show(getActivity().getSupportFragmentManager(), "gps");
        }
    }
}