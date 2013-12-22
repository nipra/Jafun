This example shows allocation behavior due to a suboptimal choice in datatypes. The text below is for 
Mission Control 4.0.0 and Flight Recorder, but it is similar for JRA.

1. Do a recording of the allocator.
2. What kind of object seems to be the one mostly allocated.
3. What is causing the allocations?
4. What is causing the garbage collections?
5. Can a simple change of data type help?
6. After fixing, how many garbage collections do you get?
7. What does the allocation behaviour look like?

Help below if you get stuck below:









































1. Memory | Allocation tab helps.
2. Integer allocations are abundant.
3. Trace shows that valueOf causes the allocations.
4. Line 30 in Allocator autoboxes int to Integer, causing the allocations.
5. Switching to Integer in the MyAlloc inner class will help (replace all int with Integer). Typically, 
when primitive types are used  as index in HashMaps, storing the object version is a better idea than 
going back and forth between the primitive type and Object version.
6. After fixing, there are almost no garbage collections anymore, except for the ones triggered to collect
data for JFR. Check and compare the Memory | GC Graph tabs. 