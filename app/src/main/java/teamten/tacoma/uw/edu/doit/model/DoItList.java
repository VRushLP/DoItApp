package teamten.tacoma.uw.edu.doit.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a single list within the application that holds
 * a collection of tasks.
 */
public class DoItList {

    private String mTitle;
    public List<Task> mList;

   public static final List<Task> ITEMS = new ArrayList<Task>();
    public static final Map<String, Task> ITEM_MAP = new HashMap<String, Task>();

    public DoItList(String theTitle) {
        mTitle = theTitle;
        mList = new ArrayList<Task>();
    }
    public void addTask(Task item) { mList.add(item); }

    public void removeTask(Task item) {mList.remove(item);}

    public void setTitle(String theNewTitle) {
        if (theNewTitle != null) {
            mTitle = theNewTitle;
        } else {
            mTitle = "Title Default";
        }
    }

    private String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * Parses the json string, returns an error message if unsuccessful.
     * Returns list of tasks if success.
     * @param taskListJSON
     * @return reason or null if successful.
     */
    public static String parseCourseJSON(String taskListJSON, List<Task> taskList) {
        String reason = null;
        if (taskListJSON != null) {
            try {
                JSONArray arr = new JSONArray(taskListJSON);

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    Task task = new Task(obj.getString(Task.contentText));
                    taskList.add(task);
                }
            } catch (JSONException e) {
                reason =  "This part of the string seems unneeded, Reason: " + e.getMessage();
            }
        }
        return reason;
    }

    /**
     * Class representing a single task.
     */
    public static class Task {
        public String content;
        public static final String contentText = "textInput";
        public Task(String content) {
            this.content = content;
        }
        public String getTaskContent() { return content;}
        public void setContent(String theNewContent) { this.content = theNewContent;}

        @Override
        public String toString() {
            return content;
        }
    }
}