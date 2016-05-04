package teamten.tacoma.uw.edu.doit.authenticate;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import teamten.tacoma.uw.edu.doit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogInFragment extends Fragment {

    private final static String USER_LOGIN_URL =
            "http://cssgate.insttech.washington.edu/~_450atm10/android/login.php?";

    public LogInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

                //TODO check against webservices to authenticate user

                ((AuthenticationActivity) getActivity()).login(userEmail, pwd);
            }
        });

        getActivity().setTitle("Log In");

        return v;
    }


    public interface LoginInteractionListener {
        public void login(String email, String pwd);
    }
}
