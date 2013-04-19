import javax.swing.JOptionPane;
class account
{
	String firstName, lastName, acctNum;
	double balance;
	account nextAccount;
	void print(int printCount)
	{
		JOptionPane.showMessageDialog(null,"("+printCount+")\nAccount No.: "+acctNum+"\nFirst Name: "+firstName+"\nLast Name: "+lastName+"\nBalance: Php "+balance);
	}
}