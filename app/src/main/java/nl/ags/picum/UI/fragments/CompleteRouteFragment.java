package nl.ags.picum.UI.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import nl.ags.picum.R;
import nl.ags.picum.UI.MainActivity;

public class CompleteRouteFragment extends DialogFragment {

    public static CompleteRouteFragment newInstance() {
        return new CompleteRouteFragment();
    }

    public CompleteRouteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_complete_route, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Dialog dialog = getDialog();
        if (dialog == null) return;

        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        dialog.getWindow().setDimAmount(0);

        Button closeButton = view.findViewById(R.id.CloseFragmentButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent closeMapWindow = new Intent(getContext(), MainActivity.class);
                startActivity(closeMapWindow);
            }
        });
    }
}