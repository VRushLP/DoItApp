package teamten.tacoma.uw.edu.doit;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskAddFragment extends Fragment {

    private final static String LIST_ADD_URL = "http://cssgate.insttech.washington.edu/~_450atm10/android/addList.php?";
    private final static String TAG = "TaskAddFragment";
    private TaskAddListener mListener;
    private EditText mTaskTitleEditText;
    private int mListToAddTo;

    public TaskAddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_task_add, container, false);
        mTaskTitleEditText = (EditText) v.findViewById(R.id.list_title_ET);
        Button addListButton = (Button) v.findViewById(R.id.create_list_title);
        addListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = buildTaskURL(v);
                mListener.addTask(url);
            }
        });

        mListToAddTo = getArguments().getInt("ListID");
        return v;
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

    private String buildTaskURL(View v) {

        StringBuilder sb = new StringBuilder(LIST_ADD_URL);
        try {
            //cmd=add&textInput=test&list=61
            sb.append("cmd=add&textInput=");
            sb.append(mTaskTitleEditText.getText().toString());
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
        public void addTask(String url);
    }
}
