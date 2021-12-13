package nl.ags.picum.UI.fragments;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Locale;

import nl.ags.picum.R;

public class SettingsFragment extends DialogFragment {
    private Button englishButton;
    private Button dutchButtom;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        englishButton = getActivity().findViewById(R.id.englishButtonIMG);
        dutchButtom = getActivity().findViewById(R.id.dutchButtonIMG);


        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);





    }


}