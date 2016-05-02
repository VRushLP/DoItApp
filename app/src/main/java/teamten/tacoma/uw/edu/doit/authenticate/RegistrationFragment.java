package teamten.tacoma.uw.edu.doit.authenticate;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URLEncoder;

import teamten.tacoma.uw.edu.doit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {

    private final static String USER_ADD_URL =
            "http://http://cssgate.insttech.washington.edu/~_450atm10/android/addUser.php?";

    private UserAddListener mListener;
    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;

    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UserAddListener) {
            mListener = (UserAddListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement UserAddListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_registration, container, false);

        mFirstNameEditText = (EditText) v.findViewById(R.id.firstName_edittext_input);
        mLastNameEditText = (EditText) v.findViewById(R.id.lastName_edittext_input);
        mEmailEditText = (EditText) v.findViewById(R.id.email_edittext_input);
        mPasswordEditText = (EditText) v.findViewById(R.id.password_edittext_input);

        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.fab);
        floatingActionButton.hide();

        Button addUserButton = (Button) v.findViewById(R.id.register_button);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = buildCourseURL(v);
                mListener.addUser(url);
            }
        });


        return v;
    }

    private String buildCourseURL(View v) {

        StringBuilder sb = new StringBuilder(USER_ADD_URL);

        try {

            String firstName = mFirstNameEditText.getText().toString();
            sb.append("firstName=");
            sb.append(firstName);

            String courseShortDesc = mLastNameEditText.getText().toString();
            sb.append("&lastName=");
            sb.append(URLEncoder.encode(courseShortDesc, "UTF-8"));


            String courseLongDesc = mEmailEditText.getText().toString();
            sb.append("&email=");
            sb.append(URLEncoder.encode(courseLongDesc, "UTF-8"));

            String coursePrereqs = mPasswordEditText.getText().toString();
            sb.append("&password=");
            sb.append(URLEncoder.encode(coursePrereqs, "UTF-8"));

            Log.i("RegistrationFragment", sb.toString());

        }
        catch(Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }

    public interface UserAddListener {
        public void addUser(String url);
    }

}
