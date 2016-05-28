package teamten.tacoma.uw.edu.doit;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
 * Displays a List of DoItTasks associated with a specific DoItList.
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DoItListDisplayFragment extends Fragment {

    private final static String TASK_MANAGER_URL =
            "http://cssgate.insttech.washington.edu/~_450atm10/android/taskManager.php";
    private static final String TAG = "DoItListDisplayFragment";

    private OnListFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private TextView mListTitleTextView;
    private UpdateListTitleListener mListTitleListener;
    private DoItList mListItem;
    private DoItList mDoItList = null;

    protected static String LIST_ITEM_SELECTED = "ListItemSelected";

    public DoItListDisplayFragment() {
        //required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
//        Bundle args = getArguments();
//        if (args != null) {
//            // Set article based on argument passed in
//            mListItem = (DoItList) args.getSerializable(LIST_ITEM_SELECTED);
//            Log.i(TAG, "mListItem = " + mListItem.getTitle());
//            updateView(mListItem);
//
//            Log.i(TAG, "args was not null");
//            mDoItList = (DoItList) args.getSerializable("DoItTaskList");
//            Log.i(TAG, "" + (mDoItList != null));
//        } else{
//            Log.e(TAG, "args was null");
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doittask_list, container, false);
//        mListTitleTextView = (TextView) view.findViewById(R.id.list_item_title);
        // put textView in fragment_doittask_list
        mListTitleTextView = (TextView) view.findViewById(R.id.list_item_title);
        Log.i(TAG, "OnCreateView called");
        Bundle args = getArguments();
        if(args != null){
            Log.i(TAG, "args was not null");
            mDoItList= (DoItList) args.get("DoItTaskList");
            Log.i(TAG, "" + (mDoItList != null));

            mListItem = (DoItList) args.getSerializable(LIST_ITEM_SELECTED);
            //Log.i(TAG, "mListItem = " + mListItem.getTitle());
//            mListTitleTextView.setText(mDoItList.getTitle());

//            updateView(mDoItList);
        } else{
            Log.e(TAG, "args was null");
        }

        //check connection
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
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



//        // long press on textview to update list's title
//        mListTitleTextView.setOnLongClickListener(new View.OnLongClickListener() {
//
//            @Override
//            public boolean onLongClick(View v) {
//                // get update_list_title_prompt.xml_title_prompt.xml view
//                LayoutInflater li = LayoutInflater.from(getContext());
//                View promptsView = li.inflate(R.layout.update_list_title_prompt, null);
//
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//                        getContext());
//
//                // set update_list_title_prompt.xml_title_prompt.xml to alertdialog builder
//                alertDialogBuilder.setView(promptsView);
//
//                final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
//
//                // set dialog message
//                alertDialogBuilder
//                        .setCancelable(false)
//                        .setPositiveButton("OK",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(
//                                            DialogInterface dialog,
//                                            int id) {
//                                        // get user input and set it to
//                                        // result
//                                        // edit text
//                                        String newTitle =  userInput.getText().toString();
//                                        if (newTitle != "") {
//                                            mListTitleTextView.setText(newTitle);
//                                            // query database
//
//                                            mListTitleListener.updateListTitle(mDoItList.getListID(), newTitle);
//                                        }
//                                    }
//                                })
//                        .setNegativeButton("Cancel",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(
//                                            DialogInterface dialog,
//                                            int id) {
//                                        dialog.cancel();
//                                    }
//                                });
//
//                // create alert dialog
//                AlertDialog alertDialog = alertDialogBuilder.create();
//
//                // show it
//                alertDialog.show();
//                return false;
//            }
//        });


        //ensure fab is visible.
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.new_list_button);
        fab.show();
        return view;
    }

    public int getCurrentListID(){ return mDoItList.getId(); };

    /**
     * Builds a String for the URL of getting all the tasks for the list associated with this Fragment.
     * @return
     */
    private String getGetAllURL() {
        Log.i(TAG, "buildGetAllTasksURL Called");

        StringBuilder sb = new StringBuilder(TASK_MANAGER_URL);
        //append arguments to the url
        sb.append("?cmd=getAll&id=");
        sb.append(mListItem.getId());

//        sb.append(mDoItList.getId());
        Log.i(TAG, sb.toString());
        //example URL: http://cssgate.insttech.washington.edu/~_450atm10/android/taskManager.php?cmd=getAll&id=61
        return sb.toString();
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

        if (context instanceof UpdateListTitleListener) {
            mListTitleListener = (UpdateListTitleListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement UpdateListTitleListener");
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnListFragmentInteractionListener {
        void onDoItTaskInteraction(DoItTask item);
    }

    public interface UpdateListTitleListener {
        void updateListTitle(int theListID, String newTitle);
    }

    public void updateView(DoItList list) {
        if (list != null) {
            mListTitleTextView.setText(list.getTitle());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     */
//    public interface OnListFragmentInteractionListener {
//        void onListFragmentInteraction(DoItTask item);
//    }

    /**
     * A task that gets all the tasks associated with a given list in the background of the application.
     */
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
