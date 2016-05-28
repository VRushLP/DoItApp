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

    public int getListID() { return this.mListID; }

    public void setListID(int theListID) {
        this.mListID = theListID;
    }

    public String getTitle() { return this.mTitle; }

    public int getId() {return this.mListID; };

    public void setTitle(String theNewTitle) {
        if (theNewTitle != null) {
            mTitle = theNewTitle;
        } else {
            mTitle = "Title Default";
        }
    }

    public ArrayList<DoItTask> getTasks() { return this.mList; }


    public void setIsDeleted(int mIsDeleted) {
        this.mIsDeleted = mIsDeleted;
    }
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

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);

                    DoItList single_list = new DoItList(obj.getInt(DoItList.LIST_ID), obj.getString(DoItList.TITLE), obj.getInt(DoItList.ISDELETED));

//                    DoItList single_list = new DoItList(obj.getString(DoItList.TITLE), obj.getInt(DoItList.LIST_ID), obj.getInt(DoItList.ISDELETED));

                    list.add(single_list);
                }
            } catch (JSONException e) {
                reason =  "JSON string parse was unsuccessful, Reason: " + e.getMessage();
            }
        }
        return reason;
    }

    public int addTask(DoItTask theTask){
        mList.add(theTask);
        return mList.indexOf(theTask);
    }

}