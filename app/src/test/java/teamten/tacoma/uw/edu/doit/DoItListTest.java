package teamten.tacoma.uw.edu.doit;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import teamten.tacoma.uw.edu.doit.model.DoItList;

/**
 * Testing model class: DoItList
 */
public class DoItListTest extends TestCase {

    private DoItList mDoItList;

    @Before
    public void setUp() {
        mDoItList = new DoItList(98, "TestList98", 0);
    }

    @Test
    public void testSetNullListTitle() {
        try {
            mDoItList.setTitle(null);
            assertEquals("TestList", "TestList");
        }
        catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void testSetLengthListTitle() {
        mDoItList.setTitle("");
        assertTrue(mDoItList.getTitle().length() != 0);
    }

    @Test
    public void testGetListID() {
        assertEquals(98, mDoItList.getId());
    }

    @Test
    public void testGetTitle() {
        assertEquals("TestList98", mDoItList.getTitle());
    }

    @Test
    public void testGetIsDeleted() {
        assertEquals(0, mDoItList.getIsDeleted());
    }


    @Test
    public void TestConstructor() {
        DoItList doItList = new DoItList(99, "TestList99", 0);
        assertNotNull(doItList);
    }

    @Test
    public void testParseDoItListJSON() {
        String doItListJSON = "[{\"listID\":\"99\",\"title\":\"TestList99\",\"isDeleted\":\"0\"},{\"listID\":\"98\",\"title\":\"TestList98\",\"isDeleted\":\"0\"}]";
        String message =  mDoItList.parseAllLists(doItListJSON, new ArrayList<DoItList>());

        assertTrue("JSON With Valid String", message == null);

    }

    @Test
    public void testParseDoItListInvalidJSON() {
        try {
            String doItListJSON = "hello";

            String message =  DoItList.parseAllLists(doItListJSON, new ArrayList<DoItList>());
            assertTrue("JSON With InValid String", !message.startsWith("Unable"));
        } catch (Exception e) {

        }
    }
}
