package teamten.tacoma.uw.edu.doit.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DoItTask {

    String mName;
    int mCheckedOff;

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
