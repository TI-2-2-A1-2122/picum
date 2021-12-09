package nl.ags.picum.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import nl.ags.picum.R;
import nl.ags.picum.Route;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO change code for implementation
        List<Route> routes = new ArrayList<>();
        RecyclerView recyclerView = new RecyclerView(this);

    }

    public void onClickLanguageFAB(View view){

    }
}