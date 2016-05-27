package teamten.tacoma.uw.edu.doit;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;

import teamten.tacoma.uw.edu.doit.model.DoItList;
import teamten.tacoma.uw.edu.doit.model.DoItTask;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DoItTaskFragment extends Fragment {

    private final static String TASK_MANAGER_URL =
            "http://cssgate.insttech.washington.edu/~_450atm10/android/taskManager.php?";

    private static final String TAG = "DoItTaskFragment";
    private TextView mListTitleTextView;
    private OnListFragmentInteractionListener mListener;
    private DoItList mDoItList = null;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DoItTaskFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doittask_list, container, false);
//        mListTitleTextView = (TextView) view.findViewById(R.id.list_item_title);
        Log.i(TAG, "OnCreateView called");
        Bundle args = getArguments();
        if(args != null){
            Log.i(TAG, "args was not null");
            mDoItList= (DoItList) args.get("DoItTaskList");
            Log.i(TAG, "" + (mDoItList != null));
        } else{
            Log.i(TAG, "args was null");
        }

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            if(mDoItList != null){
                recyclerView.setAdapter(new MyDoItTaskRecyclerViewAdapter(mDoItList.getTasks(), mListener));
            }
        }
        return view;
    }

       @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(DoItTask item);
    }

    //retrieve all tasks with associated passed listID
    //build get allURL

    //http://cssgate.insttech.washington.edu/~_450atm10/android/taskManager.php?cmd=getAll&id=61
    private String buildGetAllTasksURL(View v){
        Log.i(TAG, "buildGetAllTasksURL Called");
        StringBuilder sb = new StringBuilder(TASK_MANAGER_URL);
        try {
            Bundle data = getArguments();

            //append arguments to the url
        }
        catch(Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();    }

    private String buildAddTaskURL(View v) {
        Log.i(TAG, "buildAddTaskURL called");
        StringBuilder sb = new StringBuilder(TASK_MANAGER_URL);
        try {
            Bundle data = getArguments();

            //append arguments to the url


        }
        catch(Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }
}
