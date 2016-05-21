package teamten.tacoma.uw.edu.doit;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;

import teamten.tacoma.uw.edu.doit.authenticate.AuthenticationActivity;
import teamten.tacoma.uw.edu.doit.model.DoItListCollection;


public class DoItStationActivity extends AppCompatActivity implements DoItStationFragment.OnDoItStationFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_it_station);
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
    }

    @Override
    public void onListFragmentInteraction(DoItListCollection.DoItList item) {

    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("Close")
                .setMessage("Are you sure you want to close Do It?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which){
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .setOnKeyListener(new Dialog.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface arg0, int keyCode,
                                         KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            finish();
                        }
                        return true;
                    }
                })
                .show();
    }
}
