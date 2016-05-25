package teamten.tacoma.uw.edu.doit;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import teamten.tacoma.uw.edu.doit.authenticate.AuthenticationActivity;
import teamten.tacoma.uw.edu.doit.model.DoItList;
import teamten.tacoma.uw.edu.doit.model.DoItTask;


public class StationActivity extends AppCompatActivity
        implements StationFragment.OnDoItStationFragmentInteractionListener,
        DoItTaskFragment.OnListFragmentInteractionListener,
        ListAddFragment.ListAddListener {

    //private static final String TAG = "StationActivity";
    private String userEmailSharePref;
    private String userIdSharePref;
    private static String mUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_it_station);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle!= null) {// to avoid the NullPointerException
            mUserID = bundle.getString("userID");
        }

//        StationActivity.context = getApplicationContext();

        // to obtain user's userEmailSharePref to send to station.php (DoItStationFragment)
        SharedPreferences mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS)
                , Context.MODE_PRIVATE);
        userEmailSharePref = mSharedPreferences.getString("@string/userEmail", null);
        System.out.println("StationActivity onCreate email from shared pref: " + userEmailSharePref);

        /////
//        mSharedPreferences.edit().putString("@string/userID", mUserID).commit();
        ////

        //String userIdSharePref = getUserID();
        //mSharedPreferences.edit().putString("@string/userID", userIdSharePref).commit();  //add userID for first time
        userIdSharePref = mSharedPreferences.getString("@string/userID", null);
        System.out.println("StationActivity onCreate userID from shared pref: " + userIdSharePref);
        //setDefaults("@string/userID", userIdSharePref, StationActivity.context);

        Bundle args = new Bundle();
        args.putString("EMAIL", userEmailSharePref);
        args.putString("USERID", userIdSharePref);

        StationFragment fragment = new StationFragment();
        fragment.setArguments(args);

//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.station_container, fragment)
//                .commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.new_list_button);
        if(fab != null){
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ListAddFragment listAddFragment = new ListAddFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.station_container, listAddFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }

        if (savedInstanceState == null || getSupportFragmentManager().findFragmentById(R.id.list) == null) {
            StationFragment courseListFragment = new StationFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.station_container, courseListFragment)
                    .commit();
        }
    }

//    public static void setDefaults(String key, String value, Context context) {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString(key, value);
//        editor.commit();
//    }
//
//    public static String getDefaults(String key, Context context) {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//        return preferences.getString(key, null);
//    }
//
//    public static void  setUserID(String userID) {
//        mUserID = userID;
//    }
//
//    public String getUserID() { return mUserID; }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_station, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.action_logout){
            getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE)
                    .edit().putBoolean(getString(R.string.LOGGEDIN), false)
                    .apply();
            Intent i = new Intent(this, AuthenticationActivity.class);
            startActivity(i);
            finish();
            return true;
        } else if (id == R.id.action_add_list) {
//            Log.d(TAG, "Add a new list");
            Bundle userBundleData = new Bundle();
            userBundleData.putString("EMAIL", userEmailSharePref);
            userBundleData.putString("USERID", userIdSharePref);
            ListAddFragment listAddFragment = new ListAddFragment();
            listAddFragment.setArguments(userBundleData);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.station_container, listAddFragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onListFragmentInteraction(DoItList item) {
        DoItTaskFragment doItTaskFragment = new DoItTaskFragment();
        Bundle args = new Bundle();
        args.putSerializable("DoItTaskList", item);

        doItTaskFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.station_container, doItTaskFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed(){
        if(getSupportFragmentManager().getBackStackEntryCount() < 1){
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
        } else{
            super.onBackPressed();
        }
    }

    @Override
    public void addList(String url) {
        AddList_AsyncTask task = new AddList_AsyncTask();
        task.execute(new String[]{url.toString()});

        // Takes you back to the previous fragment by popping the current fragment out.
        getSupportFragmentManager().popBackStackImmediate();
    }

    @Override
    public void onListFragmentInteraction(DoItTask item) {
        item.checkOff();
    }

    private class AddList_AsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to add list, Reason: "
                            + e.getMessage();
                    Log.wtf("wtf", e.getMessage());
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {
                    Toast.makeText(getApplicationContext(), "List successfully added!"
                            , Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to add: "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
