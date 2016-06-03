Team 10 : DoIt

Meeting link:
    https://docs.google.com/document/d/1-QMMZ7j5MxSHD7q8xYEWgbdtzyI9FCB7KzLXsRUbr18/edit?usp=sharing 


----------------------------------------
Minimum requirements: CONTENTS OF THIS FILE
----------------------------------------
  * Bug Fixes
  * Use Cases
  * Saved Data : Device Storage
  * Web Services
  * Content Sharing
  * Sign-In with custom accounts


----------------------------------------
Bug Fixes (From Phase 1 feedback)
----------------------------------------

 Issue: 
    When clicking the phone’s back button, nothing happened.

 Fix: 
    This is changed so that after the phone’s back button is clicked at the Station, a dialog box
    appears and asks the user if they want to close the app or cancel their decision. Hitting the 
    back button again closes the dialog. Hitting the back button when not logged in closes the 
    app without confirmation.

----------------------------------------
Use cases
----------------------------------------

1) Register a user
    Fully implemented. Registration is done via custom accounts, which requires a user to supply
    an email and password. Users can then log in with that custom account.

2) Login a user
    Fully implemented. After registering, users can attempt to log in. Both the password and email
    are checked against a database table to make sure they are actually registered. These values
    are then put into a SharedPreferences file on the device.

3) View user's Station of To-Do lists
    Fully implemented. Viewing the station is the first thing to happen after logging in, and 
    users can see lists they made previously, or make new ones.

4) Create a new To-Do list
    Fully implemented. While viewing the display of all Do It Lists (the user’s station) a user 
    can click the button at the top right that looks like a list icon with a plus icon to add 
    a new list.

5) Sort Station
    Not implemented. We decided our time developing would be better spent on other use cases
    that were more core to the functionality of the app and considered this a quality of life 
    feature that wouldn’t be missed.

6) Add a task to the selected To-do list
    Fully implemented. As described, the user hits the button in the bottom right of the screen 
    (the checkmark icon) and is prompted for a task name. Users are not able to declare
    dependencies during task creation. It ended up being too error prone.

7) Select Compact View on a particular To-Do List
    Fully implemented. We ended up changing the design of how tasks were displayed. Instead of
    having non-top level tasks displayed, tasks are now hidden when there is a task that comes
    before them. There were too many issues associated with getting them to appear “behind” as 
    specified and this ended up being an easier solution. Tasks are declared as dependent by 
    doing a long click on the task, selecting the appropriate option, and passing the number of 
    the task that you want it to depend on.

8) Scroll through tasks in compact view
    Fully implemented. Users can cross off tasks and have the dependent tasks display by clicking
    on them.

9) Select verbose view on a particular To-Do List
    Fully implemented. Works similar to declaring Compact View. All tasks display and if they are
    not considered a top-level task, they are listed in grey instead of black. All tasks are still 
    able to be crossed off.

10) Delete a task on a To-Do List
    Fully implemented. As described, the user holds down their finger on a particular task and
    selects the "Permanently delete task" option.

11) Delete a To-Do List
    Fully implemented. Long clicks on Lists while at the station will give permanently deleting a 
    list as one of the options.

12) Edit a task’s name
    Fully implemented. Long clicks on Tasks while on a particular list will give editing the name 
    as one of the options. Users are then able to input a new name.

13) Edit a To-Do List name
    Fully implemented. Long clicks on Lists while at the Station will give editing the name as one 
    of the options. Users are then able to input a new name.

14) Edit task placements on a To-Do List
    Not implemented. Re-arranging the tasks to properly interact with a RecyclerViewAdapter and
    each other turned out to be too much of a hassle at best and app-breakingly buggy at worst.
    We concluded that the time was better spent elsewhere. Instead, users can declare
    dependencies by long clicking a task, selecting the appropriate menu option, and typing in 
    the id of a different task for the selected task to depend on. Users are able to clear
    dependencies in a similar vein: long click the task you want to clear the dependency of, then
    select the appropriate option. There is no more interaction with clearing a dependency.

15) Logout a user
    Fully implemented. The user can logout by pressing the settings dots at the top 
    right of the station activity and selecting the "Logout" option. This returns 
    them to the AuthenticationActivity ready to re-login or sign in a different user.


----------------------------------------
Saved Date : Device Storage
----------------------------------------
Do It uses SQLite to store user’s lists and tasks locally but prefers to try internet 
communication to retrieve lists and tasks. It also uses Shared Preferences to save user login 
information and presence, and how they would like the lists to be displayed at the moment.

 - using SQLite to store user’s lists and tasks.
 - using SharedPreferences for user login presence, and how they would like lists to be displayed.


----------------------------------------
Web Services
----------------------------------------
  Using web services within the following files:

     RegistrationFragment.java:
          -Registers a new user within the database for later retrieval.

     AuthenticationActivity.java:
          -Verifies a user is in the database. This is a necessary step to display the correct 
           lists and tasks to the correct user.

     StationFragment.java:
          -Downloads all of the user’s data from the database.

     StationActivity.java:
          -Support for adding lists, as well as updating and deleting both lists and tasks. These
           updates and deletes can be handled with the same task because they don’t require 
           parsing the return value, and only issue commands.

----------------------------------------
Content Sharing
----------------------------------------
  After creating a list, click on the list to see a "share" button at the top and the user can use 
  any form of messaging app to send a motivation text to themselves or to their friends.


----------------------------------------
Sign-In with custom accounts
----------------------------------------
  Registration is done with custom accounts. The user will need a valid email
  address (provide an '@' and "." symbol somewhere within the address) and 
  have a password of at least 6 characters in length. After registering an account,
  the user can then use the same email and password used to make their custom 
  account to sign-in to the app. Both the user's password and email will be used to 
  confirm that this user is in the system by checking their credentials against a 
  database.