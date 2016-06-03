package teamten.tacoma.uw.edu.doit.authenticate;

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

import teamten.tacoma.uw.edu.doit.R;

/**
 * LogInFragment allows user to access their account.
 */
public class LogInFragment extends Fragment {

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

        getActivity().setTitle(getString(R.string.log_in_fragment_title));
        return v;
    }

    /**
     * A Listener to allow Activities to interact with this fragment.
     */
    public interface LoginInteractionListener {
        void login(String email, String pwd);
    }
}
