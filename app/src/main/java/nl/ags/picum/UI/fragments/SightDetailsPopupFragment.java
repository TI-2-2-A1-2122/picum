package nl.ags.picum.UI.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import nl.ags.picum.R;
import nl.ags.picum.dataStorage.roomData.Sight;

public class SightDetailsPopupFragment extends DialogFragment {

    private Sight sight;
    private Context context;
    public SightDetailsPopupFragment(Sight sight, Context context){
        this.sight = sight;
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        String photoUrl = "@" + sight.getPhotoURL().substring(0, sight.getPhotoURL().indexOf("."));
        Drawable drawable = context.getDrawable(context.getResources().getIdentifier(photoUrl, null, context.getPackageName()));
        ImageView image = (ImageView)getView().findViewById(R.id.sight_details_image);
        image.setImageDrawable(drawable);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LargeImageFragment(drawable).show(getParentFragmentManager(), "image");
            }
        });
        ((TextView)getView().findViewById(R.id.sight_details_title)).setText(sight.getSightName());
        ((TextView)getView().findViewById(R.id.sight_details_detailsText)).setText(sight.getSightDescription());
        ((TextView)getView().findViewById(R.id.sight_details_detailsText)).setMovementMethod(new ScrollingMovementMethod());
        if(sight.getWebsiteURL().equals("") || sight.getWebsiteURL() == null){
            ((TextView)getView().findViewById(R.id.sight_details_siteRef)).setText("");
        } else {
            ((TextView)getView().findViewById(R.id.sight_details_siteRef)).setText(sight.getWebsiteURL());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sight_details_popup, container, false);
    }
}