package nl.ags.picum.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import nl.ags.picum.R;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
    }

    public void onStartRouteButtonClick(View view){
        ((Button)view).setVisibility(View.INVISIBLE);
        //TODO add function to start route
    }
}