package teamten.tacoma.uw.edu.doit.model;

import java.util.ArrayList;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DoItTask {

    public String mName;
    int mCheckedOff;
    public final static ArrayList<DoItTask> DUMMY_TASKS = new ArrayList<>();

    static {
        for(int i = 0; i < 25; i++){
            DUMMY_TASKS.add(new DoItTask("" + i));
        }
    }

    public DoItTask(String taskName){
        mName = taskName;
        mCheckedOff = 0;
    }

    public void checkOff() {
        if(mCheckedOff == 0){
            mCheckedOff = 1;
        }
        else{
            mCheckedOff = 0;
        }
    }
}
