package nl.ags.picum.UI.Util;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.ags.picum.R;
import nl.ags.picum.UI.MapActivity;
import nl.ags.picum.UI.fragments.SightDetailsPopupFragment;
import nl.ags.picum.dataStorage.roomData.Sight;
import nl.ags.picum.dataStorage.roomData.Waypoint;

public class SightAdapter extends RecyclerView.Adapter<SightAdapter.SightViewHolder> {
    private final Map<Sight, Waypoint> sights;
    private final MapActivity context;
    public SightAdapter(Map<Sight, Waypoint> sights, MapActivity context){
        this.sights = sights;
        this.context = context;
    }

    @NonNull
    @Override
    public SightAdapter.SightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.sight_list_item, parent, false);
        return new SightViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull SightViewHolder holder, int position) {
        Sight sight = new ArrayList<>(this.sights.keySet()).get(position);
        holder.title.setText(sight.getSightName());
        holder.visited.setVisibility(sights.get(sight).isVisited() ? View.VISIBLE : View.INVISIBLE);
        String url = "@" + sight.getPhotoURL().substring(0, sight.getPhotoURL().lastIndexOf("."));
        holder.image.setImageDrawable(context.getDrawable(context.getResources().getIdentifier(url, null, context.getPackageName())));
        String description = context.getString(context.getResources().getIdentifier("@string/" + sight.getSightDescription(), null, context.getPackageName()));
        System.out.println(sight.getSightName());
        if (description.length() >160){
        holder.description.setText(description.substring(0, 160) + "...");}
        else    {
            holder.description.setText(description.substring(0, description.length() - 1) + "...");
        }
        holder.layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                playNotification();
                new SightDetailsPopupFragment(sight, context).show(context.getSupportFragmentManager(), null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sights.size();
    }

    static class SightViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView title;
        final TextView description;
        final ImageView image;
        final ConstraintLayout layout;
        final ImageView visited;
        public SightViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.sight_list_item_name);
            description = itemView.findViewById(R.id.sight_list_item_description);
            image = itemView.findViewById(R.id.sight_list_item_image);
            layout = itemView.findViewById(R.id.sight_list_item_layout);
            visited = itemView.findViewById(R.id.sight_list_item_visited);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public void playNotification() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(this.context, notification);
        r.play();

        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
        v.vibrate(VibrationEffect.createOneShot(500, 255));

    }
}
