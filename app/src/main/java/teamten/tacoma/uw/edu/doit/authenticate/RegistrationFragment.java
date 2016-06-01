package teamten.tacoma.uw.edu.doit.authenticate;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import teamten.tacoma.uw.edu.doit.R;

/**
 * RegistrationFragment allows users to register a new account.
 */
public class RegistrationFragment extends Fragment {

    /* text edit view for email */
    private EditText mEmailText;
    /* text edit view for password */
    private EditText mPwdText;
    /* text edit view for confirmed password */
    private EditText mPwdConfirmText;
    /* URL  */
    private String url;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_registration, container, false);
        mEmailText = (EditText) v.findViewById(R.id.register_edit_text_user_email);
        mPwdText = (EditText) v.findViewById(R.id.register_edit_text_password);
        mPwdConfirmText = (EditText) v.findViewById(R.id.register_edit_text_password_confirm);
        Button finishRegisterButton = (Button) v.findViewById(R.id.finish_button);

        url = "";
        finishRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmailText.getText().toString();
                final String pwd = mPwdText.getText().toString();
                final String pwdConfirm = mPwdConfirmText.getText().toString();

                boolean emailOkay = true;
                boolean pwdOkay = true;

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(v.getContext(), "Enter an email"
                            , Toast.LENGTH_SHORT)
                            .show();
                    emailOkay = false;
                    mEmailText.requestFocus();
                    return;
                } else  if (!email.contains("@")) {
                    Toast.makeText(v.getContext(), "Enter a valid email address"
                            , Toast.LENGTH_SHORT)
                            .show();
                    mEmailText.requestFocus();
                    emailOkay = false;
                    return;
                }

                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(v.getContext(), "Enter password"
                            , Toast.LENGTH_SHORT)
                            .show();
                    pwdOkay = false;
                    mPwdText.requestFocus();
                    return;
                }else if (pwd.length() < 6) {
                    Toast.makeText(v.getContext(), "Enter password of at least 6 characters"
                            , Toast.LENGTH_SHORT)
                            .show();
                    pwdOkay = false;
                    mPwdText.requestFocus();
                    return;
                } else  if (!pwd.equals(pwdConfirm)) {
                    Toast.makeText(v.getContext(), "Your password fields must match",
                            Toast.LENGTH_SHORT)
                            .show();
                    pwdOkay = false;
                    mPwdConfirmText.requestFocus();
                    return;
                }
                url = buildNewUserURL(v);

                //Register if they hit the button and everything succeeded.
                if(emailOkay && pwdOkay){
                    RegisterUserTask task = new RegisterUserTask();
                    task.execute(new String[]{url});

                    //Go back to LogIn Fragment
                    Fragment fragment = new LogInFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.authentication_activity_container, fragment)
                            .commit();
                }
            }
        });

        getActivity().setTitle("Register New Account");
        return v;
    }

    private final static String USER_ADD_URL =
            "http://cssgate.insttech.washington.edu/~_450atm10/android/addUser.php?";

    /**
     * Given URL, build a url string for an http connection.
     *
     * @param v the view
     * @return string of composed url
     */
    private String buildNewUserURL(View v) {
        StringBuilder sb = new StringBuilder(USER_ADD_URL);

        try {
            String email = mEmailText.getText().toString();
            sb.append("email=");
            sb.append(email);

            String pwd = mPwdText.getText().toString();
            sb.append("&pwd=");
            sb.append(URLEncoder.encode(pwd, "UTF-8"));

            Log.i("RegistrationFragment", sb.toString());
        }
        catch(Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }

    private class RegisterUserTask extends AsyncTask<String, Void, String> {
        /* For easy Log tracking */
        private static final String TAG = "RegisterUserTask";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

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
                    response = "Unable to add user, Reason: "
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
         * Checks to see if there was a problem with the URL(Network) which is when an
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
                    Toast.makeText(getContext(), "Account successfully registered!"
                            , Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(getContext(), "Failed to register account: "
                            + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {
                Toast.makeText(getContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}