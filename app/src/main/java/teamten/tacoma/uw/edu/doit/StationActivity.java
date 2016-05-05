package teamten.tacoma.uw.edu.doit;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import teamten.tacoma.uw.edu.doit.authenticate.AuthenticationActivity;
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
        DoItStationFragment fragment = new DoItStationFragment();
        Bundle args = new Bundle();
        args.putString("EMAIL", email);
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.station_activity, fragment)
                .commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.new_list_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with new List creation action!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE)
                            .edit().putBoolean(getString(R.string.LOGGEDIN), false)
                            .apply();
                    Intent i = new Intent(v.getContext(), AuthenticationActivity.class);
                    startActivity(i);
                    finish();
                }
            });
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }


    @Override
    public void onListFragmentInteraction(DoItList item) {
//        CourseDetailFragment courseDetailFragment = new CourseDetailFragment();
//        Bundle args = new Bundle();
//        args.putSerializable(CourseDetailFragment.COURSE_ITEM_SELECTED, item);
//        courseDetailFragment.setArguments(args);
//
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.course_fragment_container, courseDetailFragment)
//                .addToBackStack(null)
//                .commit();
    }
}
