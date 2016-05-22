package teamten.tacoma.uw.edu.doit.authenticate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import teamten.tacoma.uw.edu.doit.R;
import teamten.tacoma.uw.edu.doit.StationActivity;

/**
 * LogInFragment allows user to access their account.
 */
public class LogInFragment extends Fragment {

    private static String USER_LOGIN_URL =
            "http://cssgate.insttech.washington.edu/~_450atm10/android/login.php?";

    public LogInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_log_in, container, false);

        final EditText emailText = (EditText) v.findViewById(R.id.signin_edit_text_user_email);
        final EditText pwdText = (EditText) v.findViewById(R.id.signin_edit_text_password);
        Button signInButton = (Button) v.findViewById(R.id.signin_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = emailText.getText().toString();
                String pwd = pwdText.getText().toString();
                if (TextUtils.isEmpty(userEmail))  {
                    Toast.makeText(v.getContext(), "Enter email"
                            , Toast.LENGTH_SHORT)
                            .show();
                    emailText.requestFocus();
                    return;
                }
                if (!userEmail.contains("@")) {
                    Toast.makeText(v.getContext(), "Enter a valid email address"
                            , Toast.LENGTH_SHORT)
                            .show();
                    emailText.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(pwd))  {
                    Toast.makeText(v.getContext(), "Enter password"
                            , Toast.LENGTH_SHORT)
                            .show();
                    pwdText.requestFocus();
                    return;
                }
                if (pwd.length() < 6) {
                    Toast.makeText(v.getContext(), "Enter password of at least 6 characters"
                            , Toast.LENGTH_SHORT)
                            .show();
                    pwdText.requestFocus();
                    return;
                }

                ((AuthenticationActivity) getActivity()).login(userEmail, pwd);
//                USER_LOGIN_URL += "email=" + userEmail + "&pwd=" + pwd;
//                Log.i("LoginFragment", USER_LOGIN_URL.toString());
//                new  VerifyLoginAndRetrieveUserIdTask().execute(USER_LOGIN_URL);
            }
        });

        Button regButton = (Button) v.findViewById(R.id.registration_button);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LoginFragment", "Register button clicked");
                RegistrationFragment fragment = new RegistrationFragment();
                getFragmentManager()
                        .beginTransaction() //launch fragment
                        .replace(R.id.authentication_activity_container, fragment, "fragment")
                        .addToBackStack(null) //be able to go back
                        .commit();
            }
        });

        getActivity().setTitle("Log In");

        return v;
    }

    public interface LoginInteractionListener {
        public void login(String email, String pwd);
    }
}
