package teamten.tacoma.uw.edu.doit.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a single list within the application that holds
 * a collection of tasks.
 */
public class DoItList implements Serializable {

    String mTitle;
    int mIsDeleted;
    public List<Task> mList;
    public static final String TITLE = "title", ISDELETED = "isDeleted";

    public DoItList(String theTitle, int theIsDeleted) {
        mTitle = theTitle;
        //mList = new ArrayList<Task>();
        mIsDeleted = theIsDeleted;
    }

    public void addTask(Task item) { mList.add(item); }

    public void removeTask(Task item) {mList.remove(item);}

    public String getTitle() { return this.mTitle; }

    public void setTitle(String theNewTitle) {
        if (theNewTitle != null) {
            mTitle = theNewTitle;
        } else {
            mTitle = "Title Default";
        }
    }

    public void setIsDeleted(int mIsDeleted) {
        this.mIsDeleted = mIsDeleted;
    }
    public int getIsDeleted() { return this.mIsDeleted; }

//    private String makeDetails(int position) {
//        StringBuilder builder = new StringBuilder();
//        builder.append("Details about Item: ").append(position);
//        for (int i = 0; i < position; i++) {
//            builder.append("\nMore details information here.");
//        }
//        return builder.toString();
//    }

    /**
     * Parses the json string, returns an error message if unsuccessful.
     * Returns list of tasks if success.
     * @param listOfTasksJSON
     * @return reason or null if successful.
     */
    public static String parseListOfTasksJSON(String listOfTasksJSON, List<DoItList> list) {
        String reason = null;
        if (listOfTasksJSON != null) {
            try {
                JSONArray arr = new JSONArray(listOfTasksJSON);

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    DoItList single_list = new DoItList(obj.getString(DoItList.TITLE), obj.getInt(DoItList.ISDELETED));
                    list.add(single_list);
                }
            } catch (JSONException e) {
                reason =  "JSON string parse was unsuccessful, Reason: " + e.getMessage();
            }
        }
        return reason;
    }

    /**
     * Class representing a single task.
     */
    public static class Task {
        public String taskContent;
        public static final String TEXT_INPUT = "textInput";
        public Task(String content) {
            this.taskContent = content;
        }

        public String getTaskContent() { return this.taskContent;}
        public void setTaskContent(String theNewContent) { this.taskContent = theNewContent;}
    }
}