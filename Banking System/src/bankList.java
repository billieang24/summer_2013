import javax.swing.JOptionPane;

public class bankList
{
	bank firstBank;
	bank lastBank;
	bankList()
	{
		firstBank = lastBank = null;
	}
	public static void main(String args[])
	{
		bankList list = new bankList();
		list.transaction();
	}
	void transaction()
	{
		String choice = (JOptionPane.showInputDialog("Please choose a transaction:\n1 - Add new bank\n2 - Choose bank to transact with\n3 - View current banks"));
		if(choice == null || choice.length() != 1 || choice.charAt(0) > 51 || choice.charAt(0) < 49 )
			if(msg("Invalid Input. Do you want to try again?","Error"))
				transaction();
			else
				System.exit(0);
		else if(choice.charAt(0)==49)
			addBank();
		else if(choice.charAt(0)==50)
			chooseBank();
		else if(choice.charAt(0)==51)
			print();
	}
	void chooseBank()
	{
		int printCount = 0;
		String choices = "";
		if(firstBank==null)
			if(msg("You have no banks. Do you want to do another bank transaction?","Error"))
			{
				transaction();
				return;
			}
			else
				System.exit(0);
		else 
			for(bank marker = firstBank;;marker=marker.nextBank)
			{
				choices = choices+ "\n"+(++printCount)+" - "+marker.name+" "+marker.details+" "+marker.branch+" "+marker.location;
				if (marker == lastBank)
						break;
			}
		String choice = (JOptionPane.showInputDialog("Please choose a bank:"+choices));
		if(choice == null || choice.length() != 1 || illegalChar(choice) || choice.contains(".") || Integer.parseInt(choice) > printCount  || choice.equals("0"))
			if(msg("Invalid Input. Do you want to try again?","Error"))
				chooseBank();
			else
				System.exit(0);
		else
		{
			bank marker = firstBank;
			for(int i = 1;i<Integer.parseInt(choice);i++)
				marker=marker.nextBank;
			marker.transaction();
			if(msg("Do you want to do another bank transaction?","Success"))
				transaction();
			else
				System.exit(0);
		}
	}
	void addBank()
	{
		if(firstBank==null)
		{
			String name = JOptionPane.showInputDialog("Please enter a new bank name:");
			name = checkInfo(name, "desired bank name");
			if(name.equals("*"))
			{	
				transaction();
				return;
			}
			String details = JOptionPane.showInputDialog("Please enter bank's details:");
			details = checkInfo(details, "bank's details");
			if(details.equals("*"))
			{	
				transaction();
				return;
			}
			String branch =  JOptionPane.showInputDialog("Please enter your bank's branch name:");
			branch = checkInfo(branch, "bank's branch name");
			if(branch.equals("*"))
			{	
				transaction();
				return;
			}
			String location =  JOptionPane.showInputDialog("Please enter your bank's location:");
			location = checkInfo(location, "bank's location");
			if(location.equals("*"))
			{	
				transaction();
				return;
			}
			firstBank = lastBank = new bank();
			addBankOk(name,details,branch,location);
		}
		else if (firstBank==lastBank)
		{
			String name = JOptionPane.showInputDialog("Please enter a new bank name:");
			name = checkInfo(name, "desired bank name");
			if(name.equals("*"))
			{	
				transaction();
				return;
			}
			else
			{
				String details = JOptionPane.showInputDialog("Please enter bank's details:");
				details = checkInfo(details, "bank's details");
				if(details.equals("*"))
				{	
					transaction();
					return;
				}
				String branch =  JOptionPane.showInputDialog("Please enter your bank's branch name :");
				branch = checkInfo(branch, "bank's branch name");
				if(branch.equals("*"))
				{	
					transaction();
					return;
				}
				if(firstBank.name.equals(name) && firstBank.branch.equals(branch))
				{
					if(msg("Bank already exists. Do you want to try again?","Error"))
						addBank();
					else
						transaction();
					return;
				}
				String location =  JOptionPane.showInputDialog("Please enter your bank's location :");
				location = checkInfo(location, "bank's location");
				if(location.equals("*"))
				{	
					transaction();
					return;
				}
				firstBank.nextBank = lastBank = new bank();
				addBankOk(name,details,branch,location);
			}
		}
		else
		{
			String name = JOptionPane.showInputDialog("Please enter a new bank name:");
			name = checkInfo(name, "desired bank name");
			if(name.equals("*"))
			{	
				transaction();
				return;
			}
			String details = JOptionPane.showInputDialog("Please enter bank's details:");
			details = checkInfo(details, "bank's details");
			if(details.equals("*"))
			{	
				transaction();
				return;
			}
			String branch =  JOptionPane.showInputDialog("Please enter your bank's branch name :");
			branch = checkInfo(branch, "bank's branch name");
			if(branch.equals("*"))
			{	
				transaction();
				return;
			}
			for(bank marker = firstBank;;marker=marker.nextBank)
			{
				if(marker.name.equals(name) && marker.branch.equals(branch))
				{
					if(msg("Bank already exists. Do you want to try again?","Error"))
						addBank();
					else
						transaction();
					break;
				}
				else if( marker == lastBank )
				{
					String location =  JOptionPane.showInputDialog("Please enter your bank's location :");
					location = checkInfo(location, "bank's location");
					if(location.equals("*"))
					{	
						transaction();
						return;
					}
					lastBank = lastBank.nextBank = new bank();
					addBankOk(name,details,branch,location);
					break;
				}
			}
		}
	}
	void print()
	{
		int printCount=1;
		if(firstBank==null)
			if(msg("You have no banks. Do you want to do another transaction?","Error"))
				transaction();
			else
				System.exit(0);
		else
		{
			for(bank marker = firstBank;;marker=marker.nextBank)
			{
				marker.print(printCount++);
				if (marker == lastBank)
						break;
			}
			if(msg("Do you want to do another bank transaction?","Success"))
				transaction();
			else
				System.exit(0);
		}	
	}
	void addBankOk(String name,String details,String branch,String location)
	{
		lastBank.name = name;
		lastBank.details = details;
		lastBank.branch = branch;
		lastBank.location = location;
		lastBank.nextBank = null;
		if(msg("New bank was successfully added. Do you want to do another bank transaction?","Success"))
				transaction();
		else
			System.exit(0);
	}
	String checkInfo(String input,String infoType)
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