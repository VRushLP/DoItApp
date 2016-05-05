package teamten.tacoma.uw.edu.doit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import teamten.tacoma.uw.edu.doit.model.DoItList;


public class StationActivity extends AppCompatActivity implements DoItStationFragment.OnDoItStationFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_it_station);

        // to obtain user's email to send to station.php (DoItStationFragment)
        SharedPreferences mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS)
                , Context.MODE_PRIVATE);
        String email = mSharedPreferences.getString("@string/userEmail", null);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.station_activity, new DoItStationFragment(email) )
                .commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




    }


    @Override
    public void onListFragmentInteraction(DoItList item) {

    }
}
