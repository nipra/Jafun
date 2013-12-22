INTRODUCTION

This is a short document explaining the structure of the code examples of the 
chapter two and the instructions to import and run the examples in Eclipse.

STRUCTURE

There are nine examples in this chapter. One example per recipe except the recipe 1
which has two projects. Every example are sent as an Eclipse project in a separate 
subfolder. These projects are

-ch2_recipe1_problem: This example presents the problems we can find when we work
with shared data in concurrent applications. The example simulates a bank account
and two process, one that inserts money and one that takes off money. The final
results obtained are inconsistent.
  
-ch2_recipe1_solution: This example is a version of the previous example. We have
added the syncrhonized keyword to resolve the problem presented in that example.
 
-ch2_recipe2: This example shows the use of the syncrhonized keyword to
synchronize a block of code instead of a complete method. It implements a program
that stores the number of people inside a building and two sensors that
control the people that come in or go out the building.

-ch2_recipe3: This example shows how two work with independent shared attributes
inside an object and the syncrhonized keyword to get a good performance. It
implements a program that control the vacancies of two cinemas and two ticket
offices that can sell tickets for one of the two cinemas.

-ch2_recipe4: This example shows how to use the syncrhonized keyword and the
wait(), signal() and signalAll() methods to implement the producer-consumer problem

-ch2_recipe5: This example shows how to use locks in Java to syncrhonize a block
of code. It implements a program that simulates a print queue.

-ch2_recipe6: This example shows how to use read/write locks. It implements a
program that reads and writes the information of the prices of two products.

-ch2_recipe7: This example shows how to modify the fairness of the locks. It
modifies the program that simulates the print queue implemented in the recipe5.

-ch2_recipe8: This example shows how to use multiple conditions in a lock. It
implements the producer-consumer problem.

RUNNING THE EXAMPLES

To run the examples, use the import option of the Eclipse IDE. In the menu File, 
select the option Import. Then, select the option Existing Projects in the 
Workspace, select one of the directories and click the Finish button.

Then, run the examples as a normal project in Eclipse.