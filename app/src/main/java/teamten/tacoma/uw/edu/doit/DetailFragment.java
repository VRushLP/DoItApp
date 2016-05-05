package teamten.tacoma.uw.edu.doit;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Adds the newly created List to the web service.
 */
public class DetailFragment extends Fragment {

//    private TextView mTaskContentTextView;
    private TextView mListTitleTextView;

    private final static String COURSE_ADD_URL =
            "http://cssgate.insttech.washington.edu/~_450atm10/android/addList.php?";

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }
}
