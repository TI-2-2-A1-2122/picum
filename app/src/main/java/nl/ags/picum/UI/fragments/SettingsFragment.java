package nl.ags.picum.UI.fragments;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Locale;

import nl.ags.picum.R;

public class SettingsFragment extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);

//
//
//        Locale locale = new Locale("en");
//        Locale.setDefault(locale);
//        Configuration configuration = new Configuration();
//        configuration.locale = locale;
//        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
//        System.out.println("hey schemzy");
//        setContentView(R.layout.activity_main);
    }
}