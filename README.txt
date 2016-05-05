Minimum requirements:
Uses web services to retrieve or store data for the app.
   Our app uses Webservices just for logging in at the moment.

Provides Registration and Sign-In with custom account or using a social media account.
    Registration is done with custom accounts, which are just a person's email. Users can then log in with their custom accounts. Both the password and the email of the user are checked against a database table to make sure they're actually registered. 

Use Case 15: Logout a user
Fully implemented. The user can log out by pressing the settings dots at the top right of the station activity and selected the "Logout" option. This returns them to the AuthenticationActivity ready to sign in a different user.