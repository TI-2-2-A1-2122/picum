package nl.ags.picum.UI.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nl.ags.picum.R;
import nl.ags.picum.UI.MapActivity;
import nl.ags.picum.UI.Util.SightAdapter;
import nl.ags.picum.dataStorage.roomData.Sight;

public class SightsListFragment extends DialogFragment {


    public final List<Sight> sightList;
    private final MapActivity mapActivity;

    public SightsListFragment(List<Sight> sightList, MapActivity activity){
        this.sightList = sightList;
        this.mapActivity = activity;
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
        RecyclerView recyclerView = view.findViewById(R.id.sight_recyclerview);
        recyclerView.setAdapter(new SightAdapter(sightList, mapActivity));
        recyclerView.setLayoutManager(new LinearLayoutManager(mapActivity, RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sights_list, container, false);
    }
}