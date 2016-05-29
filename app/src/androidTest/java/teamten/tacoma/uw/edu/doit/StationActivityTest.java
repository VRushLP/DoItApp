package teamten.tacoma.uw.edu.doit;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * Robotium testing for DoIt app.
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
    // Robotium testing for Lists
    /////////////////////////////////

    public void testStationView() {
        boolean fragmentLoaded = solo.searchText("Station");
        assertTrue("Station fragment not loaded", fragmentLoaded);
    }

    public void testList_AddFragmentShows() {
        solo.clickOnView(getActivity().findViewById(R.id.action_add_list));
        boolean textFound = solo.searchText("Create a List");
        assertTrue("Add a list fragment not loaded", textFound);
    }

    public void testList_AddListButton() {
        solo.clickOnView(getActivity().findViewById(R.id.action_add_list));
        solo.enterText(0, "I am a list title");
        solo.clickOnButton("Create");
        boolean textFound = solo.searchText("I am a list title");
        assertTrue("List add failed", textFound);
    }

    public void testStationView_AfterListAdded() {
        boolean fragmentLoaded = solo.searchText("I am a list title");
        assertTrue("Station fragment recycler not loaded ", fragmentLoaded);
    }

    public void testList_Delete() {

    }

    public void testList_Update() {

    }


    /////////////////////////////////
    // Robotium testing for Tasks
    /////////////////////////////////


    public void testTask_DoItListDisplayFragment() {
        solo.clickInRecyclerView(0);
        boolean foundListDisplayFragment = solo.searchText("I am a list title");
        assertTrue("DoItList Display fragment not loaded", foundListDisplayFragment);
        solo.goBack();
        boolean foundLists = solo.searchText("Station");
        assertTrue("Back to Station view not working", foundLists);
    }

    public void testTask_AddFragmentWorks() {
//        solo.clickOnView(getActivity().findViewById(R.id.floating_action_button));
//        boolean textFound = solo.searchText("Create a task");
//        assertTrue("Add a list fragment loaded", textFound);
    }

    public void testTask_AddTaskButton() {
//        solo.clickOnView(getActivity().findViewById(R.id.floating_action_button));
//        solo.enterText(0, "I am a list title");
//        solo.clickOnButton("Create");
//        boolean textFound = solo.searchText("I am a task input");
//        assertTrue("List add failed", textFound);
    }

    public void testStationView_AfterTaskAdded() {
//        boolean fragmentLoaded = solo.searchText("I am a task input");
//        assertTrue("Station fragment recycler loaded ", fragmentLoaded);
    }

    public void testTask_Update() {

    }

    public void testTask_Delete() {

    }


    public void testLogout() {
//        solo.clickOnView(getActivity().findViewById(R.id.action_logout));
//        boolean textFound = solo.searchText("Enter your userid");
//        assertTrue("Login fragment loaded", textFound);
//        solo.enterText(0, "userid@");
//        solo.enterText(1, "somepassword");
//        solo.clickOnButton("Sign In");
//        boolean worked = solo.searchText("Station");
//        assertTrue("Sign in worked!", worked);
    }



}
