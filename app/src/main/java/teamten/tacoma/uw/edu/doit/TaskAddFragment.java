package teamten.tacoma.uw.edu.doit;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A subclass of Fragment that gathers information about
 * new tasks being created before sending the information out to the server.
 */
public class TaskAddFragment extends Fragment {

    private final static String TAG = "TaskAddFragment";
    private final static String TASK_ADD_URL = "http://cssgate.insttech.washington.edu/~_450atm10/android/taskManager.php?";

    private TaskAddListener mListener;
    private EditText mTaskTitleEditText;
    private int mListToAddTo;

    public TaskAddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);     // finds if options on Menu exist
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_task_add, container, false);
        mTaskTitleEditText = (EditText) v.findViewById(R.id.task_title_ET);
        Button addTaskButton = (Button) v.findViewById(R.id.add_task_create_button);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInputTask = mTaskTitleEditText.getText().toString();
                if (userInputTask.length() > 0) {
                    String url = buildTaskURL(v);
                    mListener.addTask(url);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Task description must be at least 1 character long.", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        Button cancelButton = (Button) v.findViewById(R.id.add_task_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.task_add_floating_button);
        fab.hide();

        mListToAddTo = getArguments().getInt("ListID");
        return v;
    }

    // on viewing of fragment, decide whether to show/hide MenuItems
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem addListItem = menu.findItem(R.id.action_add_list);
        MenuItem itemShare = menu.findItem(R.id.action_share);
        itemShare.setVisible(false);
        addListItem.setVisible(false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TaskAddListener) {
            mListener = (TaskAddListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement TaskAddListener");
        }
    }

    /**
     *
     * @param v Usually passed as "this" from the calling activity, which is probably StationActivity.
     * @return A string representing the task and a command to add to the server.
     * Note that only the text parameter is encoded.
     */
    private String buildTaskURL(View v) {
        StringBuilder sb = new StringBuilder(TASK_ADD_URL);
        try {
            //ex cmd=add&textInput=test&list=61
            sb.append("cmd=add&textInput=");
            sb.append(Uri.encode(mTaskTitleEditText.getText().toString().trim()));
            sb.append("&list=");
            sb.append(mListToAddTo);
            Log.i(TAG, sb.toString());
        }
        catch(Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
        return sb.toString();
    }

    public interface TaskAddListener{
        void addTask(String url);
    }
}
