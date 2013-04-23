import java.sql.*;
import javax.swing.JOptionPane;
public class MySqlSample
{
    public Connection con = null;
    public Statement statement= null;
    String c,d,a,b,x;
    boolean loop = true;
    public void Connect()
    {
        try
        {
        	Class.forName("com.mysql.jdbc.Driver");
        	con = DriverManager.getConnection("jdbc:mysql://localhost/sample?user=root");
        	statement= con.createStatement();
        	while(loop)
        	{
        		int action = 0;
        		String choice = JOptionPane.showInputDialog("Choose an action:\n1 - View Table\n2 - Insert new row\n3 - Delete\n4 - Update\n5 - Exit");
            	action = Integer.parseInt(choice);
            	if( action == 1)
            	{
                	String y = "";
            		ResultSet rs=statement.executeQuery("select * from item");
                	while (rs.next())
                		y+=(rs.getString(1) +" "+ rs.getString(2) +" "+ rs.getString(3) +" "+ rs.getString(4) +" "+ rs.getString(5) +"\n");
                	JOptionPane.showMessageDialog(null,y);
                }
            	else if (action == 2)
            	{
            		a = JOptionPane.showInputDialog("Insert a name");
            		b = JOptionPane.showInputDialog("Insert a description");
            		c = JOptionPane.showInputDialog("Insert a price");
            		d = JOptionPane.showInputDialog("Insert a quantity");
            		x = "insert into item (name,description,price,quantity)values('"+a+"', '"+b+"', "+c+", "+d+")";
            		statement.execute(x);
            	}
            	else if (action == 3)
            	{
            		String id = JOptionPane.showInputDialog("Input id of the data you want to delete:");
            		statement.execute("delete from item where id ="+id+";");
            	}
            	else if (action == 4)
            	{
            		int column = Integer.parseInt(JOptionPane.showInputDialog("Choose a column to update" +
            				":\n1 - Name\n2 - Description\n3 - Price\n4 - Quantity"));
            		String newValue = JOptionPane.showInputDialog("Input a new value:");
            		String id = JOptionPane.showInputDialog("Input id of the data you want to change:");
            		switch(column)
            		{
            		case 1:
            			x = "update item set name ='"+newValue+"'where id ="+id+";";
            			break;
            		case 2:
            			x = "update item set description ='"+newValue+"'where id ="+id+";";
            			break;
            		case 3:
            			x = "update item set price ="+newValue+"where id ="+id+";";
            			break;
            		case 4:
            			x = "update item set quantity ="+newValue+"where id ="+id+";";
            			break;
            		}
            		statement.execute(x);
            	}
            	else if (action == 5)
            	{
            		loop = false;
            	}
        	}
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,"not connected"+e.getMessage());
        }
    }
    public static void main( String args[] )
    {
        MySqlSample conn=new MySqlSample();
        conn.Connect();
    }
}