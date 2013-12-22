This application has a leak in it.

1. Change the preferences for Memleak. Open Window | Preferences, filter for Trend. 
   Set Lowest heap usage to report to 0, and refresh interval to 2.
2. Start the application.
3. Connect with Memleak.
4. Filter the Trend tab on classes starting with Ad.
5. Add and remove a few contacts. Can you spot the leak?
6. Who is holding on to the contacts?
7. Can you fix the leak?

Hints below:








































1. If no contacts should be in the system, every reference to a contact is a leaked reference. 
   It's easier to find the bad ones if they are all are removed.

2. Remove all contacts in the app, then list all instances of AddressBook$Contact.

3. Pick an instance and add it to the Instance Graph.

4. In the instance graph, expand to root.

5. Two HashMaps in the numberToContact field contains leaked instances. How are they used?

6. Ensure that the contact is removed from the index ([number|phone]ToContact) maps.