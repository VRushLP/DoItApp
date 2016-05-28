package teamten.tacoma.uw.edu.doit;

import android.app.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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


public class StationActivity extends AppCompatActivity implements StationFragment.OnDoItStationFragmentInteractionListener,
        DoItListDisplayFragment.OnListFragmentInteractionListener,
                        ListAddFragment.ListAddListener, StationFragment.DeleteListClickListener, DoItListDisplayFragment.UpdateListTitleListener,
        TaskAddFragment.TaskAddListener{


    private android.support.v4.app.FragmentManager m;

    private String userEmailSharePref;
    private String userIdSharePref;
    private static String mUserID;
    private static final String TAG = "StationActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_it_station);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        m = getSupportFragmentManager();


        Bundle bundle = getIntent().getExtras();
        if (bundle!= null) {// to avoid the NullPointerException
            mUserID = bundle.getString("userID");
        }

        // to obtain user's userEmailSharePref to send to station.php (DoItStationFragment)
        SharedPreferences mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS)
                , Context.MODE_PRIVATE);
        userEmailSharePref = mSharedPreferences.getString("@string/userEmail", null);
        System.out.println("StationActivity onCreate email from shared pref: " + userEmailSharePref);


        userIdSharePref = mSharedPreferences.getString("@string/userID", null);
        System.out.println("StationActivity onCreate userID from shared pref: " + userIdSharePref);

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

                    Fragment f = getSupportFragmentManager().findFragmentById(R.id.station_container);
                    Log.i(TAG, "Current Fragment: " + f.getClass().getCanonicalName());
                    if(f instanceof StationFragment){
                        ListAddFragment listAddFragment = new ListAddFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.station_container, listAddFragment)
                                .addToBackStack(null)
                                .commit();
                    } else if (f instanceof DoItListDisplayFragment){

                        TaskAddFragment taskAddFragment = new TaskAddFragment();
                        Bundle args = new Bundle();
                        //getCurrentListID
                        args.putInt("ListID", ((DoItListDisplayFragment) f).getCurrentListID());
                        taskAddFragment.setArguments(args);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.station_container, taskAddFragment)
                                .addToBackStack(null)
                                .commit();
                    }
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
            Bundle userBundleData = new Bundle();
            userBundleData.putString("EMAIL", userEmailSharePref);
            userBundleData.putString("USERID", userIdSharePref);
            userBundleData.putString("TASK_ACTION", "add");
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
    public void onDoItListItemInteraction(DoItList item) {
        DoItListDisplayFragment doItListDisplayFragment = new DoItListDisplayFragment();
        Bundle args = new Bundle();
        args.putSerializable(DoItListDisplayFragment.LIST_ITEM_SELECTED, item);
        args.putSerializable("DoItTaskList", item);

        System.out.println("onListFragmentInteraction method StationActivity = " + item.getTitle());

        doItListDisplayFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.station_container, doItListDisplayFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void itemClickedToBeDeleted(DoItList item) {
        String listURL = "http://cssgate.insttech.washington.edu/~_450atm10/android/station.php?cmd=";

        listURL += "delete";
        listURL += "&listID=" + item.getListID();
        Log.i("Delete", new String(listURL));

        AddList_AsyncTask task = new AddList_AsyncTask("delete");
        task.execute(new String[]{listURL});
    }


    @Override
    public void updateListTitle(int theListID, String theNewTitle) {
        // change name to UpdateOrAddList_AsynTask
        AddList_AsyncTask task = new AddList_AsyncTask("update");
        String updateURL = "http://cssgate.insttech.washington.edu/~_450atm10/android/station.php?cmd=update";
        updateURL += "&listID=" + theListID;
        updateURL += "&title=" + theNewTitle;
        task.execute(new String[]{updateURL.toString()});
    }

    public void onDoItTaskInteraction(DoItTask item) {
        item.checkOff();
    }

    @Override
    public void onBackPressed(){
        if(getSupportFragmentManager().getBackStackEntryCount() < 1){
            AlertDialog quit = new AlertDialog.Builder(this)
                    .setTitle("Close")
                    .setMessage("Are you sure you want to close Do It?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which){
                            finish();
                        }
                    })
                    .setNegativeButton("No", null).create();
              quit.show();
        } else{
            super.onBackPressed();
        }
    }

    @Override
    public void addList(String url, String taskAction) {
        AddList_AsyncTask task = new AddList_AsyncTask(taskAction);
        task.execute(new String[]{url.toString()});

        // Takes you back to the previous fragment by popping the current fragment out.
        getSupportFragmentManager().popBackStackImmediate();
    }

    @Override
    public void addTask(String url) {
        //Execute a task to add new... task
        new AddDoItTaskAsyncTask().execute(url);
        getSupportFragmentManager().popBackStackImmediate(); //go back
    }

    private class AddList_AsyncTask extends AsyncTask<String, Void, String> {


        private String TASK_ACTION;

        public AddList_AsyncTask(String taskAction) {
            TASK_ACTION = taskAction;
        }

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
                    response = "Unable to " + TASK_ACTION + " list, Reason: "
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
                    Toast.makeText(getApplicationContext(), "List successfully "+TASK_ACTION+"ed!"
                            , Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to "+TASK_ACTION+": "
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

    /**
     * Adds a DoItTask to a specified DoItList in the background of the App.
     */
    private class AddDoItTaskAsyncTask extends AsyncTask<String, Void, String> {

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
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, result);
            result = result.substring(result.lastIndexOf('>') + 1); //Account for errors
            Log.i(TAG, result); //Check changed result
            //The error reporting on the php complains about undefined variables.
            // Something wrong with the network or the URL.
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.startsWith("success")) {
                    Toast.makeText(getApplicationContext(), "Task successfully added!"
                            , Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to add task because: "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data " +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}