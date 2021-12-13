package nl.ags.picum.UI.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import nl.ags.picum.R;
import nl.ags.picum.UI.MapActivity;
import nl.ags.picum.dataStorage.roomData.Route;

public class RouteDetailsFragment extends DialogFragment {

    private Route selectedRoute;

    public RouteDetailsFragment(Route route) {
        this.selectedRoute = route;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_route_details, container);
        setTextAndButtons(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Dialog dialog = getDialog();
        if(dialog != null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    private void setTextAndButtons(View view){
        ((TextView)view.findViewById(R.id.route_details_fragment_details_description)).setText(selectedRoute.getDescription());
        ((TextView)view.findViewById(R.id.route_details_fragment_details_name)).setText(selectedRoute.getRouteName());
        ((Button)view.findViewById(R.id.route_details_fragment_details_backButton)).setOnClickListener(v -> dismiss());

        ((Button)view.findViewById(R.id.route_details_fragment_details_showButton)).setOnClickListener(v -> openSelectedRoute());

        //TODO image
        //TODO amount of sights
    }

    private void openSelectedRoute(){

        Intent intent = new Intent(getContext(), MapActivity.class);

        intent.putExtra("SelectedRoute",selectedRoute);

        startActivity(intent);
        dismiss();
    }


}