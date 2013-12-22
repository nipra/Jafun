This example is unfortunately only available for JRA, as the application under
analysis has been fixed and the problem does not exist anymore. The same 
analysis can be done (even more conveniently) using JFR.

1. Open the recording in JRA.
2. In LAT, enable the Object Allocation event and make make a histogram on Class Name.
3. From what class do we have most of the object allocation events?
4. From where are they allocated?
5. Which method should be changed, or called less, to lessen the pressure on the memory system the most?

Tips below:













































1. Latency | Histogram is the tab to check. 
2. Don't forget to enable the Object Allocation event in the Event Types view.
3. The traces under the histogram table are folded by default, click them open.
4. Strings are allocated a lot.
4. The traces for the String allocations show that readPaddedAsciiString is called a lot.