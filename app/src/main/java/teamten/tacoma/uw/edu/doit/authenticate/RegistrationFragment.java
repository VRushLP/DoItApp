package teamten.tacoma.uw.edu.doit.authenticate;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import teamten.tacoma.uw.edu.doit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {

    EditText mEmailText;
    EditText mPwdText;

    private final static String USER_ADD_URL =
            "http://cssgate.insttech.washington.edu/~_450atm10/android/addUser.php";

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
        final EditText pwdConfirm = (EditText) v.findViewById(R.id.register_edit_text_password_confirm);
        Button finishRegisterButton = (Button) v.findViewById(R.id.finish_button);

        finishRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailText.getText().toString();
                String pwd = mPwdText.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(v.getContext(), "Enter an email"
                            , Toast.LENGTH_SHORT)
                            .show();
                    mEmailText.requestFocus();
                    return;
                }
                if (!email.contains("@")) {
                    Toast.makeText(v.getContext(), "Enter a valid email address"
                            , Toast.LENGTH_SHORT)
                            .show();
                    mEmailText.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(v.getContext(), "Enter password"
                            , Toast.LENGTH_SHORT)
                            .show();
                    mPwdText.requestFocus();
                    return;
                }
                if (pwd.length() < 6) {
                    Toast.makeText(v.getContext(), "Enter password of at least 6 characters"
                            , Toast.LENGTH_SHORT)
                            .show();
                    mPwdText.requestFocus();
                    return;
                }

                if (!pwd.equals(pwdConfirm)) {
                    Toast.makeText(v.getContext(), "Your password fields must match",
                            Toast.LENGTH_SHORT)
                            .show();
                    pwdConfirm.requestFocus();
                    return;
                }

                ((AuthenticationActivity) getActivity()).register(email, pwd);
            }
        });

        getActivity().setTitle("Register New Account");
        return v;
    }


    public interface RegistrationInteractionListener {
        public void register(String email, String pwd);
    }

    private String buildUserURL(View v) {

        StringBuilder sb = new StringBuilder(USER_ADD_URL);

        try {

            String email = mEmailText.getText().toString();
            sb.append("id=");
            sb.append(email);

            String pwd = mPwdText.getText().toString();
            sb.append("&shortDesc=");
            sb.append(URLEncoder.encode(pwd, "UTF-8"));

            Log.i("RegistrationFragment", sb.toString());

        }
        catch(Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }

    public interface UserAddListener{
        public void addUser(String url);
    }



    private class AddUserTask extends AsyncTask<String, Void, String> {

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
                    response = "Unable to add course, Reason: "
                            + e.getMessage();
                    Log.wtf("wtf", e.getMessage());
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }
    }



}
