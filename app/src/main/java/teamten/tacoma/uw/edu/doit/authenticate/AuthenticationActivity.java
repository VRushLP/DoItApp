package teamten.tacoma.uw.edu.doit.authenticate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.OutputStreamWriter;

import teamten.tacoma.uw.edu.doit.DoItStationActivity;
import teamten.tacoma.uw.edu.doit.R;

public class AuthenticationActivity extends AppCompatActivity implements LogInFragment.LoginInteractionListener, RegistrationFragment.RegistrationInteractionListener{

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.authentication_activity, new LogInFragment() )
                .commit();

        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS)
                , Context.MODE_PRIVATE);
        if (!mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.authentication_activity, new LogInFragment())
                    .commit();
        } else {
            //TODO They are logged in already,
            //so check db against the name and pw of the logged in user to return the correct lists.
            Intent i = new Intent(this, DoItStationActivity.class);
            startActivity(i);
            finish();
        }
    }

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
                Toast.makeText(this, "Stored in File Successfully!", Toast.LENGTH_LONG)
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
        mSharedPreferences.edit().putString("@string/userEmail", email);
        mSharedPreferences.edit().putString("@string/userPassword", pwd);


        Intent i = new Intent(this, DoItStationActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void register(String email, String pwd) {
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
                Toast.makeText(this, "Stored in File Successfully!", Toast.LENGTH_LONG)
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
        mSharedPreferences.edit().putString("@string/userEmail", email);
        mSharedPreferences.edit().putString("@string/userPassword", pwd);


        Intent i = new Intent(this, DoItStationActivity.class);
        startActivity(i);
        finish();
    }
}
