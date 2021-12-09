package nl.ags.picum.UI.Util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nl.ags.picum.R;
import nl.ags.picum.Route;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder>{

    private List<Route> routes;
    public RouteAdapter(List<Route> routes){
        this.routes = routes;
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
        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.route_listItem_nameView);
            description = itemView.findViewById(R.id.route_listItem_details);
            image = itemView.findViewById(R.id.route_listItem_imageView);
            progressBar = itemView.findViewById(R.id.route_listItem_progressBar);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
