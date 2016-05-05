package teamten.tacoma.uw.edu.doit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import teamten.tacoma.uw.edu.doit.model.DoItList;
import teamten.tacoma.uw.edu.doit.model.DoItList.DoItList;

/**
 * A fragment representing a list of Items. (Verbose View)
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnDoItStationFragmentInteractionListener}
 * interface.
 */
public class DoItStationFragment extends Fragment {

<<<<<<< HEAD
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 2;
    private OnDoItStationFragmentInteractionListener mDoItStationListener;
=======
    private int mColumnCount = 1;

    private static final String COURSE_URL
            = "http://http://cssgate.insttech.washington.edu/~_450atm10/android/station.php?cmd=station&email=";


    private String mUserEmail;
    private RecyclerView mRecyclerView;

    private CourseDB mCourseDB;
    private List<Course> mCourseList;
    private OnDoItStationFragmentInteractionListener mListener;
>>>>>>> database

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DoItStationFragment(String theEmail) {
        mUserEmail = theEmail;
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static DoItStationFragment newInstance(int columnCount) {
        DoItStationFragment fragment = new DoItStationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doitlist_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

<<<<<<< HEAD
            recyclerView.setAdapter(new MyDoItListRecyclerViewAdapter(DoItListCollection.ITEMS, mDoItStationListener));
=======
            recyclerView.setAdapter(new MyDoItListRecyclerViewAdapter(DoItList.ITEMS, mListener));
>>>>>>> database
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDoItStationFragmentInteractionListener) {
            mDoItStationListener = (OnDoItStationFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDoItStationFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDoItStationListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnDoItStationFragmentInteractionListener {
        void onListFragmentInteraction(DoItList item);
    }
}