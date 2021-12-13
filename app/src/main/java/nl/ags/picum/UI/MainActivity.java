package nl.ags.picum.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import nl.ags.picum.R;
import nl.ags.picum.UI.fragments.RouteDetailsFragment;
import nl.ags.picum.dataStorage.managing.AppDatabaseManager;
import nl.ags.picum.dataStorage.roomData.Route;
import nl.ags.picum.UI.Util.RouteAdapter;

public class MainActivity extends AppCompatActivity {
    private List<Route> routes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.main_routes_recyclerview);
        recyclerView.setAdapter(new RouteAdapter(routes, this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        new Thread(() -> {
            AppDatabaseManager manager = AppDatabaseManager.getInstance(getApplicationContext());
            this.routes.clear();

            List<Route> tempList = manager.getRoutes();
            this.routes.addAll(tempList);

            recyclerView.getAdapter().notifyDataSetChanged();
        }).start();

        /*
        //TODO change code for implementation
        List<Route> routes = new ArrayList<>();
        routes.add(new Route("History", "Dit is een kilomter om te lopen", 0, false));
        routes.add(new Route("Geen history", "Dit is geen historische kilometer", 0, false));

         */
    }

    public void onClickLanguageFAB(View view){

    }

    //TODO change to nonstatic
    public void showDetailsFragment(Route selectedRoute){
        FragmentManager fragmentManager = getSupportFragmentManager();
        new RouteDetailsFragment(selectedRoute).show(fragmentManager, "Dialog-popup");
    }

}