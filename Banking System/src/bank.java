import javax.swing.JOptionPane;

public class bank
{
	String name, details, branch,location;
	account firstAccount, lastAccount;
	bank nextBank;
	bank()
	{
		firstAccount = lastAccount = null;
	}
	void transaction()
	{
		String choice = (JOptionPane.showInputDialog("Please choose a transaction:\n1 - Add new account\n2 - Cash Deposit\n3 - Cash Withdrawal\n4 - View Accounts"));
		if(choice == null || choice.length() != 1 || choice.charAt(0) > 52 || choice.charAt(0) < 49 )
			if(msg("Invalid Input. Do you want to try again?","Error"))
				transaction();
			else
				return;
		else if(choice.charAt(0)==49)
			addAcct();
		else if(choice.charAt(0)==50)
			changeBalance("deposit");
		else if(choice.charAt(0)==51)
			changeBalance("withdraw");
		else if(choice.charAt(0)==52)
			print();
	}
	void addAcct()
	{
		String acctNum = (JOptionPane.showInputDialog("Please enter a new account number."));
		if(acctNum == null || acctNum.length() == 0 || illegalChar(acctNum) || acctNum.contains("."))
			if(msg("Invalid input. Do you want to try again?","Error"))
				addAcct();
			else
				transaction();
		else if(firstAccount==null)
		{
			String firstName = JOptionPane.showInputDialog("Please enter your first name:");
			firstName = checkInfo(firstName, "first name");
			if(firstName.equals("*"))
			{	
				transaction();
				return;
			}
			String lastName = JOptionPane.showInputDialog("Please enter your last name:");
			lastName = checkInfo(lastName, "last name");
			if(lastName.equals("*"))
			{	
				transaction();
				return;
			}
			String balance =  JOptionPane.showInputDialog("Please enter your initial deposit amount (Minimum of 100PhP):\n");
			balance = checkInfo(balance, "initial deposit amount");
			if(balance.equals("*"))
			{	
				transaction();
				return;
			}
			firstAccount = lastAccount = new account();
			addAcctOk(acctNum,firstName,lastName,balance);
		}
		else if (firstAccount==lastAccount)
		{
			if(firstAccount.acctNum.equals(acctNum))
				if(msg("Account number already exists. Do you want to try again?","Error"))
					addAcct();
				else
					transaction();
			else
			{
				String firstName = JOptionPane.showInputDialog("Please enter your first name:");
				firstName = checkInfo(firstName, "first name");
				if(firstName.equals("*"))
				{	
					transaction();
					return;
				}
				String lastName = JOptionPane.showInputDialog("Please enter your last name:");
				lastName = checkInfo(lastName, "last name");
				if(lastName.equals("*"))
				{	
					transaction();
					return;
				}
				String balance =  JOptionPane.showInputDialog("Please enter your initial deposit amount (Minimum of 100PhP):\n");
				balance = checkInfo(balance, "initial deposit amount");
				if(balance.equals("*"))
				{	
					transaction();
					return;
				}
				firstAccount.nextAccount = lastAccount = new account();
				addAcctOk(acctNum,firstName,lastName,balance);
			}
		}
		else
			for(account marker = firstAccount;;marker=marker.nextAccount)
			{
				if(marker.acctNum.equals( acctNum))
				{
					if(msg("Account number already exists. Do you want to try again?","Error"))
						addAcct();
					else
						transaction();
					break;
				}
				else if( marker == lastAccount )
				{
					String firstName = JOptionPane.showInputDialog("Please enter your first name:");
					firstName = checkInfo(firstName, "first name");
					if(firstName.equals("*"))
					{	
						transaction();
						return;
					}
					String lastName = JOptionPane.showInputDialog("Please enter your last name:");
					lastName = checkInfo(lastName, "last name");
					if(lastName.equals("*"))
					{	
						transaction();
						return;
					}
					String balance =  JOptionPane.showInputDialog("Please enter your initial deposit amount (Minimum of 100PhP):\n");
					balance = checkInfo(balance, "initial deposit amount");
					if(balance.equals("*"))
					{	
						transaction();
						return;
					}
					lastAccount = lastAccount.nextAccount = new account();
					addAcctOk(acctNum,firstName,lastName,balance);
					break;
				}
			}
	}
	void changeBalance(String type)
	{
		account marker = firstAccount;
		if(firstAccount==null)
			if(msg("You have no accounts in this bank. Do you want to do another account transaction?","Error"))
			{
				transaction();
				return;
			}
			else
				return;
		String acctNum = (JOptionPane.showInputDialog("Please enter your account number."));
		if(acctNum == null || acctNum.length() == 0 || illegalChar(acctNum) || acctNum.contains("."))
		{
			if(msg("Invalid input. Do you want to try again?","Error"))
				changeBalance(type);
			else
				transaction();
			return;
		}
		else
			for(marker = firstAccount;;marker=marker.nextAccount)
			{
				if(marker.acctNum.equals( acctNum))
					break;
				else if( marker == lastAccount )
				{
					if(msg("Account Number was not found. Do you want to try again?","Error"))
						changeBalance(type);
					else
						transaction();
					return;
				}
			}
		String change =  JOptionPane.showInputDialog("Please enter the amount you want to "+type+":");
		while(change == null || change.length() == 0 || illegalChar(change)) 
		{
			if(msg("Invalid input. Do you want to try again?","Error"))
			{
				change =  JOptionPane.showInputDialog("Please enter amount you want to "+type+":");
				continue;
			}
			else
			{
				transaction();
				return;
			}
		}
		if(type.equals("withdraw"))
			if (marker.balance-Double.parseDouble(change)>=0)
				marker.balance = marker.balance-Double.parseDouble(change);
			else
			{
				if(msg("Your account has insufficient balance to withdraw "+marker.balance+"\nDo you want to do another account transaction?","Success"))
					transaction();
				return;
			}
		else
			marker.balance = marker.balance+Double.parseDouble(change);
		if(msg("Your account's new balance is Php "+marker.balance+"\nDo you want to do another account transaction?","Success"))
			transaction();
	}
	void print()
	{
		int printCount=1;
		if(firstAccount==null)
			if(msg("You have no accounts in this bank. Do you want to do another account transaction?","Error"))
				transaction();
			else
				return;
		else
		{
			for(account marker = firstAccount;;marker=marker.nextAccount)
			{
				marker.print(printCount++);
				if (marker == lastAccount)
						break;
			}
			if(msg("Do you want to do another transaction?","Success"))
				transaction();
			else
				return;
		}	
	}
	void print(int printCount)
	{
		JOptionPane.showMessageDialog(null,"("+printCount+")\nName: "+name+"\nDetails: "+details+"\nBranch: "+branch+"\nLocation: "+location);
	}
	String checkInfo(String input,String infoType)
	{
		if(infoType.equals("initial deposit amount"))
		{
			while( input == null || input.length() == 0 || illegalChar(input) || (input.length()==1 && input.charAt(0)==46))
			{
				if(msg("Invalid input. Do you want to try again?","Error"))
				{
					input = JOptionPane.showInputDialog("Please enter your "+infoType+" (Minimum of 100PhP):\n");
					continue;
				}
				else
				{
					transaction();
					return "*";
				}
			}
			if (Double.parseDouble(input)<100)
			{
				JOptionPane.showMessageDialog(null,"Sorry, your initial deposit is below minimum. Do you want to do another account transaction?");
				return "*";
			}
			return input;
		}
		else
		{
			while(input == null || input.length() == 0)
			{
				if(msg("Invalid input. Do you want to try again?","Error"))
				{
					input = JOptionPane.showInputDialog("Please enter your "+infoType+":");
					continue;
				}
				else
				{
					transaction();
					return "*";
				}
			}
			return input;
		}
	}
	void addAcctOk(String acctNum,String firstName,String lastName,String balance)
	{
		lastAccount.acctNum = acctNum;
		lastAccount.firstName = firstName;
		lastAccount.lastName = lastName;
		lastAccount.balance =  Double.parseDouble(balance);
		lastAccount.nextAccount = null;
		if(msg("New account was successfully added. Do you want to do another account transaction?","Success"))
				transaction();
		else
			return;
	}
	boolean illegalChar(String input)
	{
		int  dot = 0;
		for(int i = 0; i<input.length(); i++)
			if (input.charAt(i) == 46)
				dot++;
		for(int i = 0; i<input.length(); i++)
			if(((input.charAt(i)<48 || input.charAt(i)>57)&& input.charAt(i) != 46) || dot > 1)
				return true;
		return false;
	}
	boolean msg(String msg,String typeOfMsg)
	{
		return JOptionPane.showConfirmDialog(null,msg,typeOfMsg,JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION;
	}
}