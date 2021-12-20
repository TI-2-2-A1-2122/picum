package nl.ags.picum.UI.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import nl.ags.picum.R;
import nl.ags.picum.UI.MapActivity;
import nl.ags.picum.UI.viewmodels.MapViewModel;
import nl.ags.picum.dataStorage.managing.AppDatabaseManager;
import nl.ags.picum.dataStorage.roomData.Route;
import nl.ags.picum.dataStorage.roomData.Sight;
import nl.ags.picum.dataStorage.roomData.Waypoint;

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

        AppDatabaseManager manager = new AppDatabaseManager(getContext());

        ((TextView)view.findViewById(R.id.route_details_fragment_details_name)).setText(selectedRoute.getRouteName());
        ((TextView)view.findViewById(R.id.route_details_fragment_details_descriptionText)).setText(getContext().getResources().getString(getContext().getResources().getIdentifier("@string/"+selectedRoute.getDescription(), null, getContext().getPackageName())));
        ((Button)view.findViewById(R.id.route_details_fragment_details_backButton)).setOnClickListener(v -> dismiss());

        ((Button)view.findViewById(R.id.route_details_fragment_details_showButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new  Thread(() -> {
                    AppDatabaseManager.getInstance(getContext()).setCurrentRoute(selectedRoute);
                }).start();

                openSelectedRoute();
            }
        });

        //TODO image

        //TODO amount of sights
        TextView text = view.findViewById(R.id.route_details_fragment_details_amountOfSightsNumber);
        ProgressBar p = view.findViewById(R.id.progressBar2);

        new Thread(() -> {
            List<Sight> Sights = AppDatabaseManager.getInstance(getContext()).getSightsPerRoute(selectedRoute);

            double amountOfVisitedSights = 0;
            List<Waypoint> waypoints = AppDatabaseManager.getInstance(getContext()).getWaypointsPerRoute(selectedRoute);

            for (Waypoint w : waypoints) {
                if (w.isVisited())
                    amountOfVisitedSights++;
            }

            double divide = amountOfVisitedSights / waypoints.size();
            int progress = (int)(divide * 100);

            Activity activity = getActivity();
            if(activity != null) {
                activity.runOnUiThread(() -> {
                    text.setText(Sights.size() + "");
                    p.setProgress(progress);
                });
            }

        }).start();

    }

    private void openSelectedRoute(){
        Intent intent = new Intent(getContext(), MapActivity.class);
        intent.putExtra("SelectedRoute",selectedRoute);

        startActivity(intent);

        dismiss();
    }


}