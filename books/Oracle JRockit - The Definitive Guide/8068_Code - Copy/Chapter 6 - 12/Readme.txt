This is an eclipse workspace containing examples for chapters 6 to 12.

This is how to configure:
1. Download the latest version of JRockit.
2. Download a 3.5 version of Eclipse.
3. Next modify the setEnv.bat file to point to wherever you installed JRockit and Eclipse.
4. Next run the startEclipse.bar file. It can be started by double clicking it.
5. Install the JRMC eclipse plug-ins in Eclipse.
5a. Go to:
       http://www.oracle.com/technology/products/jrockit/missioncontrol/index.html
5b. Click Update Site (icon to the right).
5c. Follow the instructions.
6. Import all projects in the workspace. File | Import -> General | Existing Projects into Workspace. 
   Browse to the workspace for root and click finish.
7. To run something, go to Run | Run configurations... and check under Java Application.
7b. To stop something from running, go to Window | Open Perspective | Debug, select the application to 
    stop from the Debug View, and click the stop button.
8. To open JRMC, go to Window | Open Perspective | Other, and select Mission Control from the Dialog that opens.
9. To open a JFR recording, simply double click it in the Package Explorer.