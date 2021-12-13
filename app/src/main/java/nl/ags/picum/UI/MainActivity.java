package nl.ags.picum.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import nl.ags.picum.R;
import nl.ags.picum.UI.fragments.RouteDetailsFragment;
import nl.ags.picum.UI.fragments.SettingsFragment;
import nl.ags.picum.dataStorage.roomData.Route;
import nl.ags.picum.UI.Util.RouteAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setContentView(R.layout.activity_main);

        //TODO change code for implementation
        List<Route> routes = new ArrayList<>();
        routes.add(new Route("History", "Dit is een kilomter om te lopen", 0, false));
        routes.add(new Route("Geen history", "Dit is geen historische kilometer", 0, false));
        RecyclerView recyclerView = findViewById(R.id.main_routes_recyclerview);
        recyclerView.setAdapter(new RouteAdapter(routes, this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }

    public void onClickLanguageFAB(View view){

    }

    public void toEnglish(View view) {
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.activity_main);
    }

    public void toDutch(View view) {
        Locale locale = new Locale("nl");
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.activity_main);
    }

    //TODO change to nonstatic
    public void showDetailsFragment(Route selectedRoute){
        FragmentManager fragmentManager = getSupportFragmentManager();
        new RouteDetailsFragment(selectedRoute).show(fragmentManager, "Dialog-popup");
    }

}