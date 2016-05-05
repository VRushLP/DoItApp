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
public class AuthenticationActivity extends AppCompatActivity implements LogInFragment.LoginInteractionListener {

    /* holds the applications preferences */
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                Toast.makeText(this, email + "logged in successfully!", Toast.LENGTH_LONG)
                        .show();
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
                .commit();

        // sets login credentials within sharedPreferences
        // putting the key of the sharedPref into a string resource
        // will allow universal access to the key to then obtain value
        mSharedPreferences.edit().putString("@string/userEmail", email).commit();

        Intent i = new Intent(this, StationActivity.class);
        startActivity(i);
        finish();
    }
}
