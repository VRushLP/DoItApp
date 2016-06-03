package teamten.tacoma.uw.edu.doit;

import android.graphics.Color;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.robotium.solo.Solo;

import teamten.tacoma.uw.edu.doit.model.DoItTask;

/**
 * Robotium testing for DoIt app: StationActivity and all of its associated fragments.
 *
 * Start test when already logged-in.
 */
public class StationActivityTest extends
        ActivityInstrumentationTestCase2<StationActivity> {

    private Solo solo;

    public StationActivityTest() {
        super(StationActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        // tearDown() is run after a test case has finished.
        // finishOpenedActivities() will finish all the activities that have
        // been opened during the test execution.
        solo.finishOpenedActivities();
    }

    /////////////////////////////////
    // Robotium testing for List features
    /////////////////////////////////

    public void testList1_StationView() {
        boolean fragmentLoaded = solo.searchText("Station");
        assertTrue("Station fragment not loaded", fragmentLoaded);
    }

    public void testList2_AddFragmentShows() {
        solo.clickOnView(getActivity().findViewById(R.id.action_add_list));
        boolean textFound = solo.searchText("Create a List");
        assertTrue("Add a list fragment not loaded", textFound);
    }

    public void testList3_AddListButton() {
        solo.clickOnView(getActivity().findViewById(R.id.action_add_list));
        solo.enterText(0, "I am a list title");
        solo.clickOnButton("Create");
        boolean textFound = solo.searchText("I am a list title");
        assertTrue("List add failed", textFound);
    }

    public void testList4_StationViewAfterListAdded() {
        boolean fragmentLoaded = solo.searchText("I am a list title");
        assertTrue("Station fragment recycler not loaded ", fragmentLoaded);
    }

    public void testList5_Update() {
        solo.clickLongInRecycleView(0);
        solo.clickOnText("Update");
        solo.enterText(0, "Updated list title");
        solo.clickOnButton("OK");
        boolean textFound = solo.searchText("Station");
        assertTrue("List update failed", textFound);
    }

    public void testList6_Delete() {
        solo.clickLongInRecycleView(0);
        solo.clickOnText("Delete");
        boolean textFound = solo.searchText("Station");
        assertTrue("List delete failed", textFound);
    }

    /////////////////////////////////
    // Robotium testing for Task features
    /////////////////////////////////


    public void testTask1_DoItListDisplayFragment() {
        // create list to then add tasks
        solo.clickOnView(getActivity().findViewById(R.id.action_add_list));
        solo.enterText(0, "List1");
        solo.clickOnButton("Create");

        // view task recycler section
        solo.clickInRecyclerView(0);
        boolean foundListDisplayFragment = solo.searchText("List1");
        assertTrue("DoItList Display fragment not loaded", foundListDisplayFragment);
    }

    public void testTask2_AddFragmentWorks() {
        solo.clickInRecyclerView(0);
        solo.clickOnView(getActivity().findViewById(R.id.task_add_floating_button));
        boolean textFound = solo.searchText("Create");
        assertTrue("Add a task fragment not loaded", textFound);
    }

    public void testTask3_AddTaskButton() {
        solo.clickInRecyclerView(0);
        solo.clickOnView(getActivity().findViewById(R.id.task_add_floating_button));
        solo.enterText(0, "I am a task description");
        solo.clickOnButton("Create");
        boolean textFound = solo.searchText("I am a task description");
        assertTrue("Task add failed", textFound);
    }

    public void testTask4_Update() {
        solo.clickInRecyclerView(0);
        solo.clickLongInRecycleView(0);
        solo.clickOnText("Update Task Description");
        solo.enterText(0, "task1");
        solo.clickOnButton("OK");
        boolean textFound = solo.searchText("task1");
        assertTrue("Task description update failed", textFound);
    }

    public void testTask5_CreateDependency() {
        // add task
        solo.clickInRecyclerView(0); // view of DoItListDisplaylFragment
        solo.clickOnView(getActivity().findViewById(R.id.task_add_floating_button));
        solo.enterText(0, "task2");
        solo.clickOnButton("Create");

        // retrieve first tasks's ID
        TextView firstTaskTV = solo.clickInRecyclerView(0).get(0);
        solo.clickInRecyclerView(0); // to uncross the task
        String s = firstTaskTV.getText().toString();
        String[] splitStr = s.split(" ");
        int strLength = splitStr[0].length();
        String parsedString = splitStr[0].substring(1, strLength-1);

        // create dependency
        solo.clickLongInRecycleView(1);
        solo.clickOnText("Update Task Dependency");  // click on dialog item
        solo.enterText(0, parsedString); // give position of item it should depend on
        solo.clickOnButton("OK");

//        solo.clickInRecyclerView(1); // to uncross the task
        TextView text = solo.clickInRecyclerView(1).get(0);
        int c = text.getCurrentTextColor();  // compare on Color.BLACK Color.LTGRAY
        boolean dependencyFound = false;
        if (c != Color.BLACK) {
            dependencyFound = true; // search for grey text color on task
        }

        assertTrue("Task create dependency failed", dependencyFound);
    }

    public void testTask6_RemoveDependency() {
        solo.clickInRecyclerView(0); // view of ListDetailFragment
        solo.clickLongInRecycleView(1);
        solo.clickOnText("Remove Task Dependency");  // click on dialog item

        solo.clickInRecyclerView(1); // to uncross the task
        TextView text = solo.clickInRecyclerView(1).get(0);
        int c = text.getCurrentTextColor();  //compare on Color.BLACK/Color.LTGRAY
        boolean dependencyFound = false;
        if (c == Color.BLACK) {
            dependencyFound = true; // search for black text color on task
        }
        assertTrue("Task remove dependency failed", dependencyFound);
    }

    public void testTask7_Delete() {
        solo.clickInRecyclerView(0);
        solo.clickLongInRecycleView(0);
        solo.clickOnText("Permanently Delete this task");
        boolean textFound = solo.searchText("List1");
        assertTrue("Task delete failed", textFound);
    }

}
