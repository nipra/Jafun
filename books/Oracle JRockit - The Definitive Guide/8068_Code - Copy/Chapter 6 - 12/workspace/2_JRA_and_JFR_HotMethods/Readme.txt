This example illustrates the hot methods tab in JRA and JFR. 

1. Make a recording and check where most of the time is spent. 
2. Can you make the application run faster?

Tips below if you get stuck...













































Tip 1: You can actually make the program run a lot faster by simply changing one line.

Tip 2: We spend a lot of time in the LinkedList.contains(Object) method.

Tip 3: Contains in a linked list is proportional to the number of entries.

Tip 4: A HashSet will on average do the trick in constant time.

Tip 5: Change line 7 in Initiator.java to read: list = new HashSet<Integer>(); Run and compare.
