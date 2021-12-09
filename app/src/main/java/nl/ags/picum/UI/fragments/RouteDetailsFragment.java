package nl.ags.picum.UI.fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import nl.ags.picum.R;
import nl.ags.picum.dataStorage.roomData.Route;

public class RouteDetailsFragment extends DialogFragment {

    private Route selectedRoute;

    public RouteDetailsFragment(Route route){
        this.selectedRoute = route;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_route_details, container, false);
        setTextAndButtons(view);
        return view;
    }

    private void setTextAndButtons(View view){
        ((TextView)view.findViewById(R.id.route_details_fragment_details_description)).setText(selectedRoute.getDescription());
        ((TextView)view.findViewById(R.id.route_details_fragment_details_name)).setText(selectedRoute.getRouteName());
        //TODO image
        //TODO amount of sights
    }


}