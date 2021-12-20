package nl.ags.picum.UI.Util;

import android.app.Activity;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nl.ags.picum.R;
import nl.ags.picum.UI.MapActivity;
import nl.ags.picum.UI.fragments.SightDetailsPopupFragment;
import nl.ags.picum.dataStorage.roomData.Sight;
import nl.ags.picum.mapManagement.MapManager;

public class SightAdapter extends RecyclerView.Adapter<SightAdapter.SightViewHolder> {
    private List<Sight> sights;
    private MapActivity context;
    public SightAdapter(List<Sight> sights, MapActivity context){
        this.sights = sights;
        this.context = context;
    }

    @NonNull
    @Override
    public SightAdapter.SightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.sight_list_item, parent, false);
        SightAdapter.SightViewHolder viewHolder = new SightAdapter.SightViewHolder(viewItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SightViewHolder holder, int position) {
        Sight sight = sights.get(position);
        holder.title.setText(sight.getSightName());
        String url = "@" + sight.getPhotoURL().substring(0, sight.getPhotoURL().lastIndexOf("."));
        holder.image.setImageDrawable(context.getDrawable(context.getResources().getIdentifier(url, null, context.getPackageName())));
        holder.description.setText(sight.getSightDescription().substring(0, sight.getSightDescription().indexOf(".", 100) + 1));
        holder.layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                playNotification();
                new SightDetailsPopupFragment(sight, context).show(context.getSupportFragmentManager(), null);
            }
        });
        //TODO add visual indication that sight has been visited
    }

    @Override
    public int getItemCount() {
        return sights.size();
    }

    class SightViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        TextView description;
        ImageView image;
        ConstraintLayout layout;
        public SightViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.sight_list_item_name);
            description = itemView.findViewById(R.id.sight_list_item_description);
            image = itemView.findViewById(R.id.sight_list_item_image);
            layout = itemView.findViewById(R.id.sight_list_item_layout);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, 255));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }

    }
}
