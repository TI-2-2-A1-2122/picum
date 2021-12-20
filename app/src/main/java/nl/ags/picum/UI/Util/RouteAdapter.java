package nl.ags.picum.UI.Util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nl.ags.picum.R;
import nl.ags.picum.UI.MainActivity;
import nl.ags.picum.dataStorage.roomData.Route;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder>{

    private List<Route> routes;
    private MainActivity mainActivity;
    public RouteAdapter(List<Route> routes, MainActivity mainActivity){
        this.routes = routes;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_list_item, parent, false);
        RouteViewHolder viewHolder = new RouteViewHolder(viewItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        Route selectedRoute = routes.get(position);
        holder.title.setText(selectedRoute.getRouteName());
        holder.description.setText(mainActivity.getApplicationContext().getResources().getString(mainActivity.getApplicationContext().getResources().getIdentifier("@string/" + selectedRoute.getDescription(), null, mainActivity.getPackageName())));
        //TODO Dit kan niet meer want INT
//        if(!selectedRoute.getPhotoURL().equals("")){
//            //TODO set image recourse
//        }
        //TODO set progressBar

        holder.layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mainActivity.showDetailsFragment(selectedRoute);
            }
        });
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    class RouteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        TextView description;
        ImageView image;
        ProgressBar progressBar;
        ConstraintLayout layout;
        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.route_listItem_nameView);
            description = itemView.findViewById(R.id.route_listItem_details);
            image = itemView.findViewById(R.id.route_listItem_imageView);
            progressBar = itemView.findViewById(R.id.route_listItem_progressBar);
            layout = itemView.findViewById(R.id.route_listItem_layout);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
