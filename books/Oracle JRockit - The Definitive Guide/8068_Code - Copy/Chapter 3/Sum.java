public class Sum {
    
    
public static void main(String[] args)
    {
    System.out.println("Addition of two numbers!");
    int a = Integer.parseInt(args[0]);
    int b = Integer.parseInt(args[1]);
    
    System.out.println("Sum: " + add(a,b));
    }
    public static Integer sum(Integer[] array) {
	Integer sum = 0;
	for (int i = 0 ; i<array.length ; i++) {
	    sum += array[i];
	}
	return sum;
}
}

