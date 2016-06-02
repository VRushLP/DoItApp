package teamten.tacoma.uw.edu.doit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.Fragment;
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

public class StationActivity extends AppCompatActivity implements
        StationFragment.OnDoItStationFragmentInteractionListener,
        StationFragment.DeleteListClickListener,
        StationFragment.UpdateListTitleListener,
        DoItListDisplayFragment.OnTaskDisplayInteractionListener,
        DoItListDisplayFragment.EditTaskTitleListener,
        DoItListDisplayFragment.DeleteTaskListener,
        DoItListDisplayFragment.EditTaskDependencyListener,
        ListAddFragment.ListAddListener,
        TaskAddFragment.TaskAddListener {

    private static final String TAG = "StationActivity";
    private String userEmailSharePref;
    private String userIdSharePref;
    private int taskViewMode; //verbose by default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_it_station);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        setTitle("Station");
        Bundle bundle = getIntent().getExtras();

        // to obtain user's userEmailSharePref to send to station.php (DoItStationFragment)
        SharedPreferences mSharedPreferences = getSharedPreferences(getString(R.string.PREFS_FILE)
                , Context.MODE_PRIVATE);
        userEmailSharePref = mSharedPreferences.getString("@string/userEmail", null);
        Log.i(TAG, "StationActivity onCreate email from shared pref: " + userEmailSharePref);

        userIdSharePref = mSharedPreferences.getString("@string/userID", null);
        Log.i(TAG, "StationActivity onCreate userID from shared pref: " + userIdSharePref);

        taskViewMode = mSharedPreferences.getInt("@string/taskView", 0);
        Log.i(TAG, "Read from shared pref:" + taskViewMode);

        Bundle args = new Bundle();
        args.putString("EMAIL", userEmailSharePref);
        args.putString("USERID", userIdSharePref);

        StationFragment fragment = new StationFragment();
        fragment.setArguments(args);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.task_add_floating_button);

        fab.hide();
        if(fab != null){
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment f = getSupportFragmentManager().findFragmentById(R.id.station_container);
                    Log.i(TAG, "Current Fragment: " + f.getClass().getCanonicalName());
                    if (f instanceof DoItListDisplayFragment){
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

        if (savedInstanceState == null
                || getSupportFragmentManager().findFragmentById(R.id.list) == null) {
            StationFragment stationFragment = new StationFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.station_container, stationFragment)
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
            getSharedPreferences(getString(R.string.PREFS_FILE), Context.MODE_PRIVATE)
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

        } else if (id == R.id.action_settings){
            AlertDialog.Builder settingsAlertDialog = new AlertDialog.Builder(this);
            settingsAlertDialog.setTitle(R.string.settings)
                    .setItems(R.array.view_types, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            getSharedPreferences(getString(R.string.PREFS_FILE), 0).edit()
                                    .putInt(getString(R.string.VIEW_MODE), which).apply();
                            taskViewMode = which;

                            Log.i(TAG, "Selected"+ which);
                            Fragment f = getSupportFragmentManager().findFragmentById(R.id.station_container);
                            Log.i(TAG, "Current Fragment: " + f.getClass().getCanonicalName());
                            if (f instanceof DoItListDisplayFragment){
                                if(taskViewMode == DoItListDisplayFragment.VERBOSE_VEW){
                                    ((DoItListDisplayFragment) f).setVerboseRecyclerView();
                                } else{
                                    ((DoItListDisplayFragment) f).setCompactRecyclerView();
                                }
                            }
                        }
                    });
            settingsAlertDialog.show();

        }  else if (id == R.id.action_share) {
//            Toast.makeText(this, "Share button clicked",
//                    Toast.LENGTH_SHORT)
//                    .show();
            // create the send intent
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            // set the type
            shareIntent.setType("text/plain");
            // build message to be shared
            String shareMessage = "Don't let your dreams be dreams. Just Do It!";
            // add message
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            // start the chooser for sharing
            startActivity(Intent.createChooser(shareIntent, "Share the Motivation"));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDoItListItemInteraction(DoItList item) {
        DoItListDisplayFragment doItListDisplayFragment = new DoItListDisplayFragment();
        Bundle args = new Bundle();
        args.putSerializable(DoItListDisplayFragment.LIST_ITEM_SELECTED, item);
        args.putSerializable("DoItTaskList", item);

        Log.i(TAG, "onListFragmentInteraction method StationActivity = " + item.getTitle());

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
        Log.i(TAG, listURL);
        StationAsyncTask task = new StationAsyncTask("delete");
        task.execute(listURL);
    }

    @Override
    public void updateListTitle(int theListID, String theNewTitle) {
        StationAsyncTask task = new StationAsyncTask("update");
        String updateURL = "http://cssgate.insttech.washington.edu/~_450atm10/android/station.php?cmd=update";
        updateURL += "&listID=" + theListID;
        updateURL += "&title=" + theNewTitle;
        task.execute(updateURL);
    }

    public void onDoItTaskInteraction(DoItTask item) {
        item.checkOff();
        String markURL = "http://cssgate.insttech.washington.edu/~_450atm10/android/taskManager.php?cmd=mark";
        markURL += "&id=" + item.mTaskID;
        markURL += "&as=" + item.mCheckedOff;
        Log.i(TAG, markURL);
        new StationAsyncTask("mark task").execute(markURL);
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.station_container);

        if (f instanceof DoItListDisplayFragment){
            ((DoItListDisplayFragment) f).refreshView();
        }
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
        StationAsyncTask task = new StationAsyncTask(taskAction);

        task.execute(url);
        // Takes you back to the previous fragment by popping the current fragment out.
        getSupportFragmentManager().popBackStackImmediate();
    }

    @Override
    public void addTask(String url) {
        //Execute a task to add new... task
        new StationAsyncTask("AddTask").execute(url);
        getSupportFragmentManager().popBackStackImmediate(); //go back
    }

    @Override
    public void deleteTask(DoItTask item) {
        String url =
                "http://cssgate.insttech.washington.edu/~_450atm10/android/taskManager.php?cmd=delete";
        url += "&id=" + item.mTaskID;
        Log.i(TAG, url);
        new StationAsyncTask("delete task").execute(url);
    }

    @Override
    public void editTaskTitle(int id, String newTitle) {
        String url =
                "http://cssgate.insttech.washington.edu/~_450atm10/android/taskManager.php?cmd=edit";
        url += "&id=" + id;
        url += "&newtext=" + Uri.encode(newTitle);
        Log.i(TAG, url);
        new StationAsyncTask("edit task").execute(url);
    }

    @Override
    public void editTaskDependency(int id, int dependency) {
        String url =
                "http://cssgate.insttech.washington.edu/~_450atm10/android/taskManager.php?cmd=depend";
        url += "&id=" + id;
        url += "&dependsOn=" + dependency;
        Log.i(TAG, url);
        new StationAsyncTask("edit task").execute(url);
    }

    private class StationAsyncTask extends AsyncTask<String, Void, String> {
        private String mTaskAction;
        public StationAsyncTask(String taskAction) {
            mTaskAction = taskAction;
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
                    response = "Unable to " + mTaskAction + " list, Reason: "
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
                    if (mTaskAction.equals("delete")) {
                        Toast.makeText(getApplicationContext(), "List successfully " + mTaskAction + "d!"
                                , Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        Toast.makeText(getApplicationContext(), "List successfully " + mTaskAction + "ed!"
                                , Toast.LENGTH_SHORT)
                                .show();
                    }

                } else {
//                    Toast.makeText(getApplicationContext(), "Failed to "+ mTaskAction +": "
//                                    + jsonObject.get("error")
//                            , Toast.LENGTH_LONG)
//                            .show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}