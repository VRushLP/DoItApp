package teamten.tacoma.uw.edu.doit.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Array;
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
    int mIsDeleted, mListID;
    public ArrayList<DoItTask> mList;
    public static final String LIST_ID = "listID", TITLE = "title", ISDELETED = "isDeleted";

    public DoItList(int theListID, String theTitle, int theIsDeleted) {
        mListID = theListID;
        mTitle = theTitle;
        mIsDeleted = theIsDeleted;
        mList = new ArrayList<DoItTask>();
    }

    /**
     * @return The id of the list, frequently used to retrieve tasks associated with the list.
     */
    public int getListID() { return this.mListID; }

    /**
     * @return The title of the list.
     */
    public String getTitle() { return this.mTitle; }

    /**
     * Sets the title of the list. Ensures the list title is non-null, and not a blank string.
     * @param theNewTitle
     */
    public void setTitle(String theNewTitle) {
        if (theNewTitle != null && theNewTitle.length() > 0 && !theNewTitle.equals("")) {
            mTitle = theNewTitle;
        } else {
            mTitle = "Title Default";
        }
    }

    /**
     * @return All DoItTasks associated with this DoItList.
     */
    public ArrayList<DoItTask> getTasks() { return this.mList; }

    /**
     * @return whether or not a list is deleted.
     */
    public int getIsDeleted() { return this.mIsDeleted; }


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
                if (arr !=null) {
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        DoItList single_list = new DoItList(obj.getInt(DoItList.LIST_ID),
                                obj.getString(DoItList.TITLE), obj.getInt(DoItList.ISDELETED));
                        list.add(single_list);
                    }
                }
                return reason;
            } catch (JSONException e) {
                reason =  "Unable to parse data, Reason: " + e.getMessage();
            }
        }
        return reason;
    }
}