Minimum requirements:
- Uses web services to retrieve or store data for the app.
  Currently, our app uses web services for authentication: Users registering and 
  logging in are both checked against a database before their actions are completed. There’s a known issue where   attempting to register and exempting a “.com”, “.net” or similar causes the registration to fail due to a php   file’s strict filter validating email, but the user to be taken back to the log-in fragment like it succeeded.

- Provides Registration and Sign-In with custom account or using a social media account.
  Registration is done with custom accounts, which require a user to supply an email and password (and reconfirm the   password).  Users can then log in with their custom accounts. Both the password and the email of the user are   checked against a database table to make sure they're actually registered. 

Custom Use cases:
Use Case 4: Create a new To-Do list
Partially implemented. In view of the android portion, a button is available to the use on a user’s station (aka home page). At this time, a toast message will appear recognizing that the user has clicked on the button. Due to sql/php issues, it is not fully implemented and needs the connection fixed.

Use Case 15: Logout a user
Fully implemented. The user can log out by pressing the settings dots at the top right 
of the station activity and selecting the "Logout" option. This returns them to the 
AuthenticationActivity ready to re-login or sign in a different user. 