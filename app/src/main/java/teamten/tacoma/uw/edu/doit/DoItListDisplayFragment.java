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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import teamten.tacoma.uw.edu.doit.model.DoItList;
import teamten.tacoma.uw.edu.doit.model.DoItTask;

/**
 * Displays a List of DoItTasks associated with a specific DoItList.
 * Activities containing this fragment MUST implement the {@link OnTaskDisplayInteractionListener}
 * interface.
 */
public class DoItListDisplayFragment extends Fragment {

    private final static String TASK_MANAGER_URL =
            "http://cssgate.insttech.washington.edu/~_450atm10/android/taskManager.php";
    private static final String TAG = "DoItListDisplayFragment";
    public static final int VERBOSE_VEW = 0;

    private OnTaskDisplayInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private DeleteTaskListener mDeleteListener;
    private EditTaskTitleListener mEditListener;
    private EditTaskDependencyListener mDependencyListener;
    private DoItList mDoItList = null;
    private ArrayList<DoItTask> mCompactTasks = null;
    private int mHowDisplay;

    protected static String LIST_ITEM_SELECTED = "ListItemSelected";

    public DoItListDisplayFragment() {
        //required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doittask_list, container, false);
//        mListTitleTextView = (TextView) view.findViewById(R.id.list_item_title);
        // put textView in fragment_doittask_list
//        mListTitleTextView = (TextView) view.findViewById(R.id.list_item_title);
        setHasOptionsMenu(true);     // finds if options on Menu exist

        Log.i(TAG, "OnCreateView called");
        Bundle args = getArguments();
        if(args != null){
            Log.i(TAG, "args was not null");
            mDoItList= (DoItList) args.get("DoItTaskList");
            mCompactTasks = getTopLevelTasks();
            Log.i(TAG, "" + (mDoItList != null));
            mHowDisplay = args.getInt("taskViewMode");
            Log.i(TAG, "Display mode: " + mHowDisplay);
            //Log.i(TAG, "mListItem = " + mListItem.getTitle());
//            mListTitleTextView.setText(mDoItList.getTitle());
//            updateView(mDoItList);
            getActivity().setTitle(mDoItList.getTitle());
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
                refreshView();
            }
        }

        //ensure fab is visible.
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.task_add_floating_button);
        fab.show();
        return view;
    }

    private ArrayList<DoItTask> getTopLevelTasks() {
        ArrayList<DoItTask> out = new ArrayList<>();
        for(DoItTask t : mDoItList.getTasks()){
            if(!out.contains(t)){
                if(t.mDependency == -1){
                    out.add(t);
                } else {
                    DoItTask temp = checkForTaskByID(t.mDependency);
                    if (temp.mCheckedOff == 1){
                        out.add(t);
                    }
                }
            }
        }
        return out;
    }

    private DoItTask checkForTaskByID(int check){
        for(DoItTask t : mDoItList.getTasks()){
            if(t.mTaskID == check){
                return t;
            }
        }
        return null;
    }

    // on viewing of fragment, decide whether to show/hide MenuItems
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem addListItem = menu.findItem(R.id.action_add_list);
        addListItem.setVisible(false);
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
        sb.append(mDoItList.getId());
        Log.i(TAG, sb.toString());
        //example URL: http://cssgate.insttech.washington.edu/~_450atm10/android/taskManager.php?cmd=getAll&id=61
        return sb.toString();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTaskDisplayInteractionListener) {
            mListener = (OnTaskDisplayInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTaskDisplayInteractionListener");
        }

        if (context instanceof DeleteTaskListener) {
            mDeleteListener = (DeleteTaskListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DeleteTaskListener");
        }

        if (context instanceof EditTaskTitleListener) {
            mEditListener = (EditTaskTitleListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DeleteTaskListener");
        }

        if (context instanceof EditTaskDependencyListener) {
            mDependencyListener = (EditTaskDependencyListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DeleteTaskListener");
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnTaskDisplayInteractionListener {
        void onDoItTaskInteraction(DoItTask item);
    }

    public interface DeleteTaskListener {
        void deleteTask(DoItTask item);
    }

    public interface EditTaskTitleListener {
        void editTaskTitle(int id, String newTitle);
    }

    public interface EditTaskDependencyListener {
        void editTaskDependency(int id, int dependency);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void refreshView(){
        mHowDisplay = getActivity().
                getSharedPreferences(getString(R.string.PREFS_FILE), Context.MODE_PRIVATE)
        .getInt(getString(R.string.VIEW_MODE), 0);
        Log.i(TAG, "" + mHowDisplay);

        if(mHowDisplay == VERBOSE_VEW){
            setVerboseRecyclerView();
        } else{
            setCompactRecyclerView();
        }
    }

    public void setVerboseRecyclerView(){
        mRecyclerView.setAdapter(
                new MyDoItTaskRecyclerViewAdapter(
                        mDoItList.getTasks(),
                        mListener, mDeleteListener, mEditListener, mDependencyListener));
    }

    public void setCompactRecyclerView(){
        mRecyclerView.setAdapter(
                new MyDoItTaskRecyclerViewAdapter(
                        getTopLevelTasks(),
                        mListener, mDeleteListener, mEditListener, mDependencyListener));
    }

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
                if(result!= null) {
                    Log.i(TAG, result);
                }
            }

            // Everything is good, show the tasks.
            if (!mDoItList.mList.isEmpty()) {
                refreshView();
            }
        }
    }
}
