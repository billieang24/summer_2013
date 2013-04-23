import java.util.Scanner;
public class reverseString
{
	public static void main(String args[])
	{
		Scanner in = new Scanner(System.in);
		String input = in.nextLine();
		for(int i = input.length()-1; i>=0 ; i--)
			System.out.print(input.charAt(i));
	}
}
