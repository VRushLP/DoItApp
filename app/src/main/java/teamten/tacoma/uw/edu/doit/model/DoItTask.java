package teamten.tacoma.uw.edu.doit.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DoItTask implements Serializable {

    public static final String TAG = "DO_IT_TASK";

    public String mName;
    public int mTaskID;
    public int mCheckedOff;
    public int mDependency;

    private final static String TEXT_INPUT = "textInput";
    private final static String TASK_ID = "taskID";
    private static final String IS_DELETED = "isDeleted";
    private static final String DEPENDENCY = "dependsOn";


    public DoItTask(String taskName, int taskId, int checked,int dependsOn){
        mName = taskName;
        mTaskID = taskId;
        mCheckedOff = checked;
        mDependency = dependsOn;
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
                            obj.getInt(DoItTask.TASK_ID), obj.getInt(DoItTask.IS_DELETED), obj.getInt(DoItTask.DEPENDENCY));

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
