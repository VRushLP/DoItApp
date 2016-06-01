package teamten.tacoma.uw.edu.doit.authenticate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import teamten.tacoma.uw.edu.doit.StationActivity;
import teamten.tacoma.uw.edu.doit.R;
import teamten.tacoma.uw.edu.doit.StationFragment;

/**
 * AuthenticationActivity determines if a user is already logged in or
 * needs to log-in. Also, this activity houses the login and registration fragments.
 */
public class AuthenticationActivity extends AppCompatActivity implements LogInFragment.LoginInteractionListener {

    /* holds the applications preferences */
    private SharedPreferences mSharedPreferences;
    private String mUserID;
    private Intent i;
    private static String USER_LOGIN_URL =
            "http://cssgate.insttech.washington.edu/~_450atm10/android/login.php?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mUserID = "";
        super.onCreate(savedInstanceState);
        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS)
                , Context.MODE_PRIVATE);

        if (mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {  // if not logged in, start login
            Intent i = new Intent(this, StationActivity.class);
            startActivity(i);
            finish();
        } else { //They are logged in already,
            //TODO check db against the name and pw of the logged in user to return the correct lists.
            setContentView(R.layout.activity_authentication);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.authentication_activity_container, new LogInFragment())
                    .commit();
        }
    }

    /**
     *
     * @param email
     * @param pwd
     */
    @Override
    public void login (String email, String pwd){
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //Check if the login and password are valid
            //new LoginTask().execute(url);
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                        openFileOutput(getString(R.string.LOGIN_FILE)
                                , Context.MODE_PRIVATE));
                outputStreamWriter.write("email = " + email + ";");
                outputStreamWriter.write("password = " + pwd);
                outputStreamWriter.close();
//                Toast.makeText(this, email + " logged in successfully!", Toast.LENGTH_LONG)
//                        .show();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(this, "No network connection available. Cannot authenticate user",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        mSharedPreferences
                .edit()
                .putBoolean(getString(R.string.LOGGEDIN), true)
                .apply();

        // sets login credentials within sharedPreferences
        // putting the key of the sharedPref into a string resource
        // will allow universal access to the key to then obtain value
        mSharedPreferences.edit().putString("@string/userEmail", email).commit();
        String buildURL = USER_LOGIN_URL;

        buildURL += "email=" + email + "&pwd=" + pwd;
        Log.i("AuthenticationActivity", buildURL);
        new  VerifyLoginAndRetrieveUserIdTask().execute(buildURL);

        mSharedPreferences.edit().putString("@string/userID", mUserID).commit();
        String mUserIDSP = mSharedPreferences.getString("@string/userID", null);
//        System.out.println("AuthenticationActivity mUserID= " + mUserIDSP);
        i = new Intent(this, StationActivity.class);
        Intent i = new Intent(this, StationActivity.class);
        i.putExtra("userID", mUserIDSP);
        startActivity(i);
        finish();
    }

    /**
     * Web service to retrieve userID stored in database
     * Creates a POST of user's account credentials saved to the database.
     */
    private class VerifyLoginAndRetrieveUserIdTask extends AsyncTask<String, Void, String> {

        private static final String TAG = "RetrieveUserIdTask";

        /**
         * Checks connection for service.
         *
         * @param urls php file path.
         * @return if accessible or not.
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            //for (String url : urls) {
            try {

                URL urlObject = new URL(urls[0]);
                urlConnection = (HttpURLConnection) urlObject.openConnection();

                InputStream content = urlConnection.getInputStream();

                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }

            } catch (Exception e) {
                response = "Unable to retrieve userID, Reason: "
                        + e.getMessage();
                Log.wtf("wtf", e.getMessage());

            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            //}
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
//                JSONObject jsonObject = new JSONObject(result).getJSONObject("categories");
//                JSONObject[] keys = jsonObject.getJSONArray();
//                for (String value : keys){
//                    String status = (String) jsonObject.get("result");
//                    String userID = (String) jsonObject.get("userid");
//                }

                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                String userID = (String) jsonObject.get("userid");
                mSharedPreferences.edit().putString("@string/userID", userID).commit();
//                System.out.println("AuthenticationActivity: onPostExecute userID: " + userID);

                Bundle args = new Bundle();
                args.putString("USERIDAUTH", userID);
                StationFragment fragment = new StationFragment();
                fragment.setArguments(args);

                i.putExtra("userID", userID);
                startActivity(i);
                finish();

                if (status.equals("success")) {
//                    Toast.makeText(getApplicationContext(), "Database user data successfully retrieved! userID = " + userID
//                            , Toast.LENGTH_LONG)
//                            .show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to retrieve: "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the php file/other" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
