
/*
 * class sample1
 * 
 * This code has been generated using C2J++
 * C2J++ is based on Chris Laffra's C2J (laffra@ms.com)
 * Read general disclaimer distributed with C2J++ before using this code
 * For information about C2J++, send mail to Ilya_Tilevich@ibi.com
 */

class sample1
{
int num;

/**
* sample1
* @param n
*/
public
sample1(int n)
{
	num = n;
}

/**
* getNum
* @return int
*/
public
int getNum()
{
	return num;
}

/**
* @return int
*/
static int main()
{
	sample1 obj(2112);
/** @c2j++ Replacement from cout << obj.getNum(); System.out.print(String.valueOf(obj.getNum()));*/ 
 System.out.print(String.valueOf(obj.getNum()));
	return 0;
}
}
