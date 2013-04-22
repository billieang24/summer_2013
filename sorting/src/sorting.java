import javax.swing.JOptionPane;
public class sorting {
	public static void main(String args[])
	{
		int x = Integer.parseInt(JOptionPane.showInputDialog("How many numbers are going to be sorted?"));
		int array[] = new int[x];
		for(int i = 0; i <x ; i++)
			 array[i]= Integer.parseInt(JOptionPane.showInputDialog("Enter a number:"));
		for(int i = 0; i <x ; i++)
			for(int j = i+1; j< x ;j++)
				if(array[i]>array[j])
				{
					array[i]= array[i]+array[j];
					array[j]=array[i]-array[j];
					array[i]=array[i]-array[j];
					j= i+1;
				}
		String out = "Ascending: ";
		for(int i = 0; i <x ; i++)
		{
			if(i==x-1)
				out = out + array[i];
			else
				out = out + array[i]+",";
		}
		System.out.println(out);
		for(int i = 0; i <x ; i++)
			for(int j = i+1; j< x ;j++)
				if(array[i]<array[j])
				{
					array[i]= array[i]+array[j];
					array[j]=array[i]-array[j];
					array[i]=array[i]-array[j];
					j= i+1;
				}
		out = "Descending: ";
		for(int i = 0; i <x ; i++)
		{
			if(i==x-1)
				out = out + array[i];
			else
				out = out + array[i]+",";
		}
		System.out.println(out);
	}
}
