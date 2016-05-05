package teamten.tacoma.uw.edu.doit.authenticate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.OutputStreamWriter;

import teamten.tacoma.uw.edu.doit.StationActivity;
import teamten.tacoma.uw.edu.doit.R;

/**
 * AuthenticationActivity determines if a user is already logged in or
 * needs to log-in. Also, this activity houses the login and registration fragments.
 */
public class AuthenticationActivity extends AppCompatActivity implements LogInFragment.LoginInteractionListener, RegistrationFragment.RegistrationInteractionListener{

    /* holds the applications preferences */
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS)
                , Context.MODE_PRIVATE);
<<<<<<< HEAD

        if (mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {  // if not logged in, start login
            Intent i = new Intent(this, DoItStationActivity.class);
=======
        if (!mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {  // if not logged in, start login
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.login_fragment_container, new LogInFragment())
                    .commit();
        } else {
            //TODO They are logged in already,
            //so check db against the name and pw of the logged in user to return the correct lists.
            Intent i = new Intent(this, StationActivity.class);
>>>>>>> database
            startActivity(i);
            finish();
        } else { //They are logged in already,
            //TODO check db against the name and pw of the logged in user to return the correct lists.
            setContentView(R.layout.activity_authentication);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.authentication_activity_container, new LogInFragment() )
                    .commit();
        }
    }

    /**
     *
     * @param email
     * @param pwd
     */
    @Override
    public void login(String email, String pwd) {
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
                Toast.makeText(this, email + "logged in successfully!", Toast.LENGTH_LONG)
                        .show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else {
            Toast.makeText(this, "No network connection available. Cannot authenticate user",
                    Toast.LENGTH_SHORT) .show();
            return;
        }

        mSharedPreferences
                .edit()
                .putBoolean(getString(R.string.LOGGEDIN), true)
                .commit();

        // sets login credentials within sharedPreferences
        // putting the key of the sharedPref into a string resource
        // will allow universal access to the key to then obtain value
        mSharedPreferences.edit().putString("@string/userEmail", email).commit();


        Intent i = new Intent(this, StationActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void register(String url) {
//        RegisterUserTask task = new RegisterUserTask();
//        task.execute(new String[]{url.toString()});
//
//        // Takes you back to the previous fragment by popping the current fragment out.
//        getSupportFragmentManager().popBackStackImmediate();
    }

<<<<<<< HEAD

//    @Override
//    public void register(String email, String pwd) {
//        ConnectivityManager connMgr = (ConnectivityManager)
//                getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//        if (networkInfo != null && networkInfo.isConnected()) {
//            //Check if the login and password are valid
//            //new LoginTask().execute(url);
//            try {
//                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
//                        openFileOutput(getString(R.string.LOGIN_FILE)
//                                , Context.MODE_PRIVATE));
//                outputStreamWriter.write("email = " + email + ";");
//                outputStreamWriter.write("password = " + pwd);
//                outputStreamWriter.close();
//                Toast.makeText(this, "Stored in File Successfully!", Toast.LENGTH_LONG)
//                        .show();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//        else {
//            Toast.makeText(this, "No network connection available. Cannot register user",
//                    Toast.LENGTH_SHORT) .show();
//            return;
//        }
//
//        Intent i = new Intent(this, LogInFragment.class);
//        startActivity(i);
//        finish();
//
//        mSharedPreferences
//                .edit()
//                .putBoolean(getString(R.string.LOGGEDIN), true)
//                .commit();
//
//        // sets login credentials within sharedPreferences
//        // putting the key of the sharedPref into a string resource
//        // will allow universal access to the key to then obtain value
//        mSharedPreferences.edit().putString("@string/userEmail", email);
//        mSharedPreferences.edit().putString("@string/userPassword", pwd);
//
//
//        Intent j = new Intent(this, DoItStationActivity.class);
//        startActivity(j);
//        finish();
//    }

//    private final static String USER_ADD_URL =
//            "http://cssgate.insttech.washington.edu/~_450atm10/android/addUser.php";
//
//    class RegisterUserTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... urls) {
//            String response = "";
//            HttpURLConnection urlConnection = null;
//            for (String url : urls) {
//                try {
//                    URL urlObject = new URL(url);
//                    urlConnection = (HttpURLConnection) urlObject.openConnection();
//
//                    InputStream content = urlConnection.getInputStream();
//
//                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
//                    String s = "";
//                    while ((s = buffer.readLine()) != null) {
//                        response += s;
//                    }
//
//                } catch (Exception e) {
//                    response = "Unable to add course, Reason: "
//                            + e.getMessage();
//                    Log.wtf("wtf", e.getMessage());
//                } finally {
//                    if (urlConnection != null)
//                        urlConnection.disconnect();
//                }
//            }
//            return response;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            if(s.equals("")){
//                s="Data saved successfully.";
//            }
//            Toast.makeText(AuthenticationActivity.this, s, Toast.LENGTH_LONG).show();
//        }
//    }
=======
>>>>>>> database
}
