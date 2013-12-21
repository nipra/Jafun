
public class IntegerRepresentation {
    public static void main(String[] args) {
        int positiveInt = +41;    // 051, 0x29
        int negativeInt = -41;    // 037777777727, -051, 0xffffffd7, -0x29

        System.out.println("String representation for decimal value: "
                           + positiveInt);
        integerStringRepresentation(positiveInt);
        System.out.println("String representation for decimal value: "
                           + negativeInt);
        integerStringRepresentation(negativeInt);
    }

    public static void integerStringRepresentation(int i) {
        System.out.println("    Binary:\t\t" + Integer.toBinaryString(i));
        System.out.println("    Hex:\t\t"    + Integer.toHexString(i));
        System.out.println("    Octal:\t\t"  + Integer.toOctalString(i));
        System.out.println("    Decimal:\t"  + Integer.toString(i));

        System.out.println("    Using toString(int i, int base) method:");
        System.out.println("    Base 2:\t\t" + Integer.toString(i, 2));
        System.out.println("    Base 16:\t"  + Integer.toString(i, 16));
        System.out.println("    Base 8:\t\t" + Integer.toString(i, 8));
        System.out.println("    Base 10:\t"  + Integer.toString(i, 10));
    }
}