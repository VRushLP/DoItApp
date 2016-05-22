package teamten.tacoma.uw.edu.doit;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListAddFragment extends Fragment {

    private final static String LIST_ADD_URL = "http://cssgate.insttech.washington.edu/~_450atm10/android/addList.php?";

//    private TextView mTaskContentTextView;
    private ListAddListener mListener;
    private EditText mListTitleEditText;


    public ListAddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ListAddListener) {
            mListener = (ListAddListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ListAddListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_list, container, false);

        mListTitleEditText = (EditText) v.findViewById(R.id.list_title_ET);

//        FloatingActionButton floatingActionButton = (FloatingActionButton)
//                getActivity().findViewById(R.id.fab);
//        floatingActionButton.hide();

        Button addListButton = (Button) v.findViewById(R.id.create_list_title);
        addListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = buildListURL(v);
                mListener.addList(url);
            }
        });

        return v;
    }


    private String buildListURL(View v) {

        StringBuilder sb = new StringBuilder(LIST_ADD_URL);

        try {

            Bundle data = getArguments();
            String userEmail = data.getString("EMAIL");
            sb.append("email=");
            sb.append(userEmail);

            String listTitle = mListTitleEditText.getText().toString();
            sb.append("&title=");
            sb.append(URLEncoder.encode(listTitle, "UTF-8"));

            String userID = data.getString("USERID");
            sb.append("&userID=");
            sb.append(userID);

            Log.i("ListAddFragment", sb.toString());

        }
        catch(Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }

    public interface ListAddListener{
        public void addList(String url);
    }
}