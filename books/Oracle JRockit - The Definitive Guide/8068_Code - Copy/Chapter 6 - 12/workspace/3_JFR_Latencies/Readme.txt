This example features a logger that is shared between a bunch of worker threads.
Do a one minute recording. If you want to, enable the custom event producer.

1. The idea with the worker threads is to get work done in parallel. How many 
   threads are actually running in parallel? Why?
   
2. Can you change the application by simply removing a key word to change this?

3. Can you think of another way to change the application to do this?

Help below if you get stuck.



































1. Do a recording and go to the Events | Graph tab. Green is good - the thread is running. Red is bad, the thread is waiting on entering a Java monitor.

2. As can be seen, only one thread at a time is getting to run. The application is doing work in a sequential fashion, one thread at a time.

3. The blocking events all seem to be due to calls to the Log method. See CPU/Threads | Contention tab.

4. Removing the synchronization call, or making sure each thread has it's own logger will fix the problem.

5. New recording after the fix will show lot's of green in parallel. All red is gone.