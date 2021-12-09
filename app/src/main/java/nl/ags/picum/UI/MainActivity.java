package nl.ags.picum.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import nl.ags.picum.R;
import nl.ags.picum.dataStorage.roomData.Route;
import nl.ags.picum.Route;
import nl.ags.picum.UI.Util.RouteAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO change code for implementation
        List<Route> routes = new ArrayList<>();
        routes.add(new Route());
        routes.add(new Route());
        routes.add(new Route());
        RecyclerView recyclerView = findViewById(R.id.main_routes_recyclerview);
        recyclerView.setAdapter(new RouteAdapter(routes));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

    }

    public void onClickLanguageFAB(View view){

    }

    public void showDetailsFragment(){

    }
}