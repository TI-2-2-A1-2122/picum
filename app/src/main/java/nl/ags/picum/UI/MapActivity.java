package nl.ags.picum.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;

import java.util.List;

import nl.ags.picum.R;
import nl.ags.picum.UI.viewmodels.MapViewModel;
import nl.ags.picum.dataStorage.dataUtil.Point;

public class MapActivity extends AppCompatActivity {

    private MapViewModel mapViewModel;
    private MapView mMap;
    private IMapController mMapController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        this.mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        this.mMap = findViewById(R.id.MainMap);
        mMapController = mMap.getController();
    }

    public void onStartRouteButtonClick(View view){
        ((Button)view).setVisibility(View.INVISIBLE);
        //TODO add function to start route
    }

    public void initializeMap(){
        mMap.setTileSource(TileSourceFactory.MAPNIK);
        LiveData<List<Point>> points = mapViewModel.getCalculatedRoute();

    }
}