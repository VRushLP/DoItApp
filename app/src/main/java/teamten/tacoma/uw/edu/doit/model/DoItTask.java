package teamten.tacoma.uw.edu.doit.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DoItTask {

    public static final String TAG = "DO_IT_TASK";

    public String mName;
    int mTaskID;
    int mCheckedOff;

    private final static String TEXT_INPUT = "textInput";
    private final static String TASK_ID = "taskID";

    public DoItTask(String taskName, int taskId){
        mName = taskName;
        mTaskID = taskId;
        mCheckedOff = 0;
    }

    public void checkOff() {
        if(mCheckedOff == 0){
            mCheckedOff = 1;
        }
        else{
            mCheckedOff = 0;
        }
        Log.i(TAG, this.toString());
    }

    public String toString(){
        return this.mName + " " + this.mCheckedOff;
    }

    public boolean equals(Object o){
        if(o instanceof DoItTask){
            return ((DoItTask) o).mTaskID == this.mTaskID;
        }
        return false;
    }

    //parseJSONFile
    public static String parseTaskListJSON(String listOfTasksJSON, List<DoItTask> list) {
        String reason = null;
        if (listOfTasksJSON != null) {
            try {
                JSONArray arr = new JSONArray(listOfTasksJSON);

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    DoItTask parsedTask = new DoItTask(obj.getString(DoItTask.TEXT_INPUT),
                            obj.getInt(DoItTask.TASK_ID));

                    if(!list.contains(parsedTask)){
                        list.add(parsedTask);
                    }
                }
            } catch (JSONException e) {
                reason =  "JSON string parse was unsuccessful, Reason: " + e.getMessage();
            }
        }
        return reason;
    }
}
