This example is more obvious in older versions of JRockit.

1. Run Finalizers. 
2. Finalizers are expensive. Are there many around?
3. How can you easily get rid of them?

Tips below:







































1. In JRA, go to Memory | GC.
2. Sort on Longest Pause.
3. Go to the Pause Time tab in details and sort it on time. The handling of Finalizers seem to take a long time.
4. Select References and Finalizers in the combo box next to the graph.
5. There are a lot of finalizers around to handle each gc...
6. Fix by removing the finalize method in DataBearerHierarchic, it does nothing to help anyway.