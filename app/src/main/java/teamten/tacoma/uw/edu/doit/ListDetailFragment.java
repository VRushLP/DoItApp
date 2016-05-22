package teamten.tacoma.uw.edu.doit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import teamten.tacoma.uw.edu.doit.model.DoItList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListDetailFragment extends Fragment {

    private TextView mListTitleTextView;

    protected static String LIST_ITEM_SELECTED = "ListItemSelected";

    public ListDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_detail, container, false);
        mListTitleTextView = (TextView) view.findViewById(R.id.list_item_title);
        return view;
    }

    public void updateView(DoItList list) {
        if (list != null) {
            mListTitleTextView.setText(list.getTitle());
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            updateView((DoItList) args.getSerializable(LIST_ITEM_SELECTED));
        }
    }
}
