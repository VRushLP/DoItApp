package teamten.tacoma.uw.edu.doit;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * Robotium testing for DoIt app.
 *
 * Start test when already logged.
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

//    public void testList1_StationView() {
//        boolean fragmentLoaded = solo.searchText("Station");
//        assertTrue("Station fragment not loaded", fragmentLoaded);
//    }
//
//    public void testList2_AddFragmentShows() {
//        solo.clickOnView(getActivity().findViewById(R.id.action_add_list));
//        boolean textFound = solo.searchText("Create a List");
//        assertTrue("Add a list fragment not loaded", textFound);
//    }
//
//    public void testList3_AddListButton() {
//        solo.clickOnView(getActivity().findViewById(R.id.action_add_list));
//        solo.enterText(0, "I am a list title");
//        solo.clickOnButton("Create");
//        boolean textFound = solo.searchText("I am a list title");
//        assertTrue("List add failed", textFound);
//    }
//
//    public void testList4_StationViewAfterListAdded() {
//        boolean fragmentLoaded = solo.searchText("I am a list title");
//        assertTrue("Station fragment recycler not loaded ", fragmentLoaded);
//    }
//
//    public void testList5_Update() {
//        solo.clickLongInRecycleView(0);
//        solo.clickOnText("Update");
//        solo.enterText(0, "Updated list title");
//        solo.clickOnButton("OK");
//        boolean textFound = solo.searchText("Station");
//        assertTrue("List update failed", textFound);
//    }
//
//    public void testList6_Delete() {
//        solo.clickLongInRecycleView(0);
//        solo.clickOnText("Delete");
//        boolean textFound = solo.searchText("Station");
//        assertTrue("List delete failed", textFound);
//    }



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

        // use below for going back and deleting list entirely.
//        solo.goBack();
//        boolean foundLists = solo.searchText("Station");
//        assertTrue("Start in station, then click into list's task view and then back to list " +
//                "view not working", foundLists);
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
        solo.clickOnText("Update");
        solo.enterText(0, "Updated task description");
        solo.clickOnButton("OK");
        boolean textFound = solo.searchText("Updated task description");
        assertTrue("Task description update failed", textFound);
    }

    public void testTask5_Delete() {
        solo.clickInRecyclerView(0);
        solo.clickLongInRecycleView(0);
        solo.clickOnText("Delete");
        boolean textFound = solo.searchText("List1");
        assertTrue("Task delete failed", textFound);
    }

    // Test task dependencies and features

    // Test delete/update on list with tasks?

//    public void testLogout() {
//        solo.clickOnView(getActivity().findViewById(R.id.action_logout));
//        boolean textFound = solo.searchText("Enter your userid");
//        assertTrue("Login fragment loaded", textFound);
//        solo.enterText(0, "userid@");
//        solo.enterText(1, "somepassword");
//        solo.clickOnButton("Sign In");
//        boolean worked = solo.searchText("Station");
//        assertTrue("Sign in worked!", worked);
//    }
}
