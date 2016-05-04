package teamten.tacoma.uw.edu.doit.authenticate;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import teamten.tacoma.uw.edu.doit.DoItStationActivity;
import teamten.tacoma.uw.edu.doit.R;

public class AuthenticationActivity extends AppCompatActivity implements LogInFragment.LoginInteractionListener, RegistrationFragment.RegistrationInteractionListener{

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.authentication_activity_container, new LogInFragment() )
                .commit();

        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS)
                , Context.MODE_PRIVATE);
        if (!mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {  // if not logged in, start login
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.login_fragment_container, new LogInFragment())
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

//    public void authen_register(View v){
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        RegistrationFragment fragment = new RegistrationFragment();
//        fragmentTransaction.add(R.id.registration_fragment_container, fragment);
//        fragmentTransaction.commit();
//    }
//
//    public void authen_login(View v){
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        RegistrationFragment fragment = new RegistrationFragment();
//        fragmentTransaction.add(R.id.login_fragment_container, fragment);
//        fragmentTransaction.commit();
//    }



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
            Toast.makeText(this, "No network connection available. Cannot register user",
                    Toast.LENGTH_SHORT) .show();
            return;
        }

        Intent i = new Intent(this, LogInFragment.class);
        startActivity(i);
        finish();

        mSharedPreferences
                .edit()
                .putBoolean(getString(R.string.LOGGEDIN), true)
                .commit();

        // sets login credentials within sharedPreferences
        // putting the key of the sharedPref into a string resource
        // will allow universal access to the key to then obtain value
        mSharedPreferences.edit().putString("@string/userEmail", email);
        mSharedPreferences.edit().putString("@string/userPassword", pwd);


        Intent j = new Intent(this, DoItStationActivity.class);
        startActivity(j);
        finish();
    }

    private final static String USER_ADD_URL =
            "http://cssgate.insttech.washington.edu/~_450atm10/android/addUser.php";

    class RegisterUserTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String email = params[0];
            String password = params[1];
            String data = "";
            int tmp;

            try {
                URL url = new URL(USER_ADD_URL);
                String urlParams = "email="+email+"&password="+password;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();
                InputStream is = httpURLConnection.getInputStream();
                while((tmp=is.read())!=-1){
                    data+= (char)tmp;
                }
                is.close();
                httpURLConnection.disconnect();

                return data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("")){
                s="Data saved successfully.";
            }
            Toast.makeText(AuthenticationActivity.this, s, Toast.LENGTH_LONG).show();
        }
    }

}
