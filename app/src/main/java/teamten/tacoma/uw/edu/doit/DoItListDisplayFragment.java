package teamten.tacoma.uw.edu.doit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import teamten.tacoma.uw.edu.doit.Data.StationDB;
import teamten.tacoma.uw.edu.doit.model.DoItList;
import teamten.tacoma.uw.edu.doit.model.DoItTask;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DoItListDisplayFragment extends Fragment {

    private final static String TASK_MANAGER_URL =
            "http://cssgate.insttech.washington.edu/~_450atm10/android/taskManager.php";

    private static final String TAG = "DoItListDisplayFragment";
    private OnListFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private DoItList mDoItList = null;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DoItListDisplayFragment() {
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

        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.i(TAG, TASK_MANAGER_URL);
            DownloadListsTask task = new DownloadListsTask();
            task.execute(getGetAllURL());
        }

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            if(mDoItList != null){
                mRecyclerView.setAdapter(new MyDoItTaskRecyclerViewAdapter(mDoItList.getTasks(), mListener));
            }
        }

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.new_list_button);
        fab.show();
        return view;
    }

    public int getCurrentListID(){ return mDoItList.getId(); };

    private String getGetAllURL() {
        Log.i(TAG, "buildGetAllTasksURL Called");
        StringBuilder sb = new StringBuilder(TASK_MANAGER_URL);
        //append arguments to the url
        sb.append("?cmd=getAll&id=");
        sb.append(mDoItList.getId());
        Log.i(TAG, sb.toString());
        //example URL: http://cssgate.insttech.washington.edu/~_450atm10/android/taskManager.php?cmd=getAll&id=61
        return sb.toString();
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

    private class DownloadListsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            URL urlObject=null;
            for (String url : urls) {
                try {
                    urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to get tasks, Reason: "
                            + e.getClass();
                }
                finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
        // Something wrong with the network or the URL.
            Log.i(TAG, result);
            if (result.startsWith("Unable to")) {
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
            } else  {
                result = DoItTask.parseTaskListJSON(result, mDoItList.mList);
            }

            // Everything is good, show the tasks.
            if (!mDoItList.mList.isEmpty()) {
                mRecyclerView.setAdapter(new MyDoItTaskRecyclerViewAdapter(mDoItList.mList, mListener));

            }
        }
    }
}
