import java.sql.*;
import javax.swing.JOptionPane;
public class MysqlSample
{
    public Connection con = null;
    public Statement statement= null;
    String year,month,date;
    public void Connect()
    {
        try
        {
        	Class.forName("com.mysql.jdbc.Driver");
        	con = DriverManager.getConnection("jdbc:mysql://localhost/sample?user=root");
        	statement= con.createStatement();
        	while(true)
        	{
        		String table = JOptionPane.showInputDialog("Choose a table:\n1 - Customer\n2 - Item\n3 - Order\n4  - Exit");
        		if (table.length()!=1 || table.charAt(0) > 52 || table.charAt(0) < 49 )
        			if(JOptionPane.showConfirmDialog(null,"Invalid choice. Do you want to try again?","Error",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION)
        				continue;
        			else
        				break;
        		else if(table.equals("1"))
        			table = "customer";
        		else if(table.equals("2"))
        			table = "item";
        		else if(table.equals("3"))
        			table = "order_line";
        		else if(table.equals("4"))
        			System.exit(0);
        		ResultSet rs=statement.executeQuery("select * from "+table);
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnsNumber = rsmd.getColumnCount();
				while(true)
        		{
					String action = "";
					if (table.equals("order_line"))
					{
						action = JOptionPane.showInputDialog("Choose an action:\n1 - View Table\n2 - Insert new row\n3 - Update\n4 - Search\n5 - Exit");
						if (action.length()!=1 || action.charAt(0) > 53 || action.charAt(0) < 49)
							if(JOptionPane.showConfirmDialog(null,"Invalid choice. Do you want to try again?","Error",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION)
								continue;
							else
								break;
						action = (action.equals("2")?"3":action.equals("3")?"5":action.equals("4")?"6":action.equals("5")?"7":action);
        			}
					else
					{
						action = JOptionPane.showInputDialog("Choose an action:\n1 - View Table\n2 - View transactions of "+(table.equals("item")?"an":"a")+" "+table+"\n3 - Insert new row\n4 - Delete\n5 - Update\n6 - Search\n7 - Exit");
						if (action.length()!=1 || action.charAt(0) > 55 || action.charAt(0) < 49)
							if(JOptionPane.showConfirmDialog(null,"Invalid choice. Do you want to try again?","Error",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_NO_OPTION)
								continue;
							else
								break;
        			}
        			rs=statement.executeQuery("select * from "+table);
        			rsmd = rs.getMetaData();
        			String id = "",output = "";
        			int exit = 0,rowCount=0;
        			if(action.equals("7"))
        				break;
					switch( Integer.parseInt(action))
        			{
        				case 1: // CASE FOR VIEW
        					if(table.equals("order_line"))
        						rs=statement.executeQuery("select concat(lname,\", \",fname,\" \",ifNull(mname,\"\")),i.name,i.price,o.quantity,transaction_date,format(o.quantity*i.price,2) from order_line as o,item as i ,customer as c where i.id = o.item and i.id = o.item and c.id = o.customer order by customer;");
        					else
        						rs=statement.executeQuery("select * from "+table);
                			rsmd = rs.getMetaData();
                			while (rs.next())
        							for(int i = 1; i <= rsmd.getColumnCount(); i++)
        								output+= (i == rsmd.getColumnCount()?(rs.getString(i)+"\n"):rs.getString(i)+"*****");
        					JOptionPane.showMessageDialog(null,output);
        					break;
        				case 2:
        					id=JOptionPane.showInputDialog("Input "+table+" id"); 
    						if(id.length()== 0 || numbers(id))
    				    	{
        						JOptionPane.showMessageDialog(null, "Invalid "+table+" id. You will be directed back to the menu.");
        						break;
        					}
    						rs=statement.executeQuery("select concat(lname,\", \",fname,\" \",ifNull(mname,\"\")),i.name,i.price,o.quantity,transaction_date,format(o.quantity*i.price,2) from order_line as o,item as i ,customer as c where "+table+" = "+"'"+id+"'"+" and i.id = o.item and i.id = o.item and c.id = o.customer order by customer;");
    						rsmd = rs.getMetaData();
    						while (rs.next())
    							for(int i = 1; i <= rsmd.getColumnCount(); i++)
    								output+= (i == rsmd.getColumnCount()?(rs.getString(i)+"\n"):rs.getString(i)+"*****");
    						JOptionPane.showMessageDialog(null,output);
    						break;
        				case 3: // CASE FOR INSERT
        					if(table.equals("order_line"))
        					{
        						String customer=JOptionPane.showInputDialog("Input customer id"); 
        						if(customer.length()== 0 || numbers(customer))
        				    	{
            						JOptionPane.showMessageDialog(null, "Invalid customer id. You will be directed back to the menu.");
            						break;
            					}
        						rs = statement.executeQuery("select count(*) from customer");
        						rs.next();
        						rowCount = Integer.parseInt(rs.getString(1));
        						rs = statement.executeQuery("select * from customer");
        						for(int i = 1 ; i<= rowCount;i++)
        						{
        							rs.next();
        							if(customer.equals(rs.getString(1)))
        								break;
        							if(i==rowCount)
        							{
        								JOptionPane.showMessageDialog(null, "Customer id does not exist. You will be directed back to the menu.");
        								exit = 1;
        							}
        						}
        						if(exit == 1)
        							break;
        						String item=JOptionPane.showInputDialog("Input item id"); 
        						if(item.length()== 0 || numbers(item))
        				    	{
            						JOptionPane.showMessageDialog(null, "Invalid item id. You will be directed back to the menu.");
            						break;
            					}
        						rs = statement.executeQuery("select count(*) from item");
        						rs.next();
        						rowCount = Integer.parseInt(rs.getString(1));
        						rs = statement.executeQuery("select * from item");
        						for(int i = 1 ; i<= rowCount;i++)
        						{
        							rs.next();
        							if(item.equals(rs.getString(1)))
        								break;
        							if(i==rowCount)
        							{
        								JOptionPane.showMessageDialog(null, "Item id does not exist. You will be directed back to the menu.");
        								exit = 1;
        							}
        						}
        						if(exit == 1)
        							break;
        						if(setDate("transaction's"))
        							break;
        						String quantity=JOptionPane.showInputDialog("Input item quantity:"); // numbers and .
        						if(quantity.length()== 0 || dotNumbers(quantity) )
            				   	{
                					JOptionPane.showMessageDialog(null, "Invalid item quantity. You will be directed back to the menu.");
                					break;
                				}
        						rs = statement.executeQuery("select * from item where id = "+item);
        						rs.next();
        						double difference = Double.parseDouble(rs.getString(5))-Double.parseDouble(quantity);
        						if(difference<0)
        						{
                					JOptionPane.showMessageDialog(null, "Insufficient item quantity. You will be directed back to the menu.");
                					break;
                				}
        						statement.execute("insert into order_line(customer,item,quantity,transaction_date)values('"+customer+"','"+item+"','"+quantity+"','"+(year+"-"+month+"-"+date)+"');");
        						statement.execute("update item set quantity = "+difference+" where id = "+item);
        				    	break;
        					}
        					else if(table.equals("item"))
        					{
        						String name=JOptionPane.showInputDialog("Input item name:"); //letters space
        						if(name.length()== 0 || letterSpace(name) || name.contains("  ") || name.charAt(0) == ' ' )
        						{
            						JOptionPane.showMessageDialog(null, "Invalid item name. You will be directed back to the menu.");
            						break;
            					}
        						String desc=JOptionPane.showInputDialog("Input item description:"); //letters space
        						if(desc.length()== 0 || letterSpace(desc) || desc.contains("  ") || desc.charAt(0) == ' ' )
        						{
            						JOptionPane.showMessageDialog(null, "Invalid description. You will be directed back to the menu.");
            						break;
            					}
        						String price=JOptionPane.showInputDialog("Input item price:"); // numbers and .
        						if(price.length()== 0 || dotNumbers(price) )
            				   	{
                					JOptionPane.showMessageDialog(null, "Invalid item price. You will be directed back to the menu.");
                					break;
                				}
        						String quantity=JOptionPane.showInputDialog("Input item quantity:"); // numbers and .
        						if(quantity.length()== 0 || dotNumbers(quantity) )
            				   	{
                					JOptionPane.showMessageDialog(null, "Invalid item quantity. You will be directed back to the menu.");
                					break;
                				}
        						statement.execute("insert into item(name,description,price,quantity)values('"+name+"','"+desc+"','"+price+"','"+quantity+"');");
        				    	break;
        					}
        					else if(table.equals("customer"))
        					{
        						String fname=JOptionPane.showInputDialog("Input customer's first name:");
        						if(fname.length()== 0 || letterSpace(fname) || fname.contains("  ") || fname.charAt(0) == ' ' )
        						{
            						JOptionPane.showMessageDialog(null, "Invalid first name. You will be directed back to the menu.");
            						break;
            					}
        				    	String mname=JOptionPane.showInputDialog("Input customer's middle name:"); 
        				    	if( mname.length()== 0 || letterSpace(mname) || mname.contains("  ") || mname.charAt(0) == ' ')
        				    	{
            						JOptionPane.showMessageDialog(null, "Invalid middle name. You will be directed back to the menu.");
            						break;
            					}
        				    	String lname=JOptionPane.showInputDialog("Input customer's last name:");
        				    	if(lname.length()== 0 || letterSpace(lname) || lname.contains("  ") || lname.charAt(0) == ' ')
        				    	{
            						JOptionPane.showMessageDialog(null, "Invalid last name. You will be directed back to the menu.");
            						break;
            					}
        				    	String address=JOptionPane.showInputDialog("Input customer's address:");
        				    	if(address.length()== 0 || address.contains("  ") || address.charAt(0) == ' ')
        				    	{
            						JOptionPane.showMessageDialog(null, "Invalid address. You will be directed back to the menu.");
            						break;
            					}
        				    	String contact=JOptionPane.showInputDialog("Input customer's contact number:");
        				    	if(contact.length()== 0 || numbers(contact) )
            				   	{
                					JOptionPane.showMessageDialog(null, "Invalid contact number. You will be directed back to the menu.");
                					break;
                				}
        				    	if(setDate("customer's birth"))
        				    		break;
        				    	statement.execute("insert into customer(fname,mname,lname,birthdate,address,contact_no)values('"+fname+"','"+mname+"','"+lname+"','"+(year+"-"+month+"-"+date)+"','"+address+"','"+contact+"');");
        				    	break;
        					}
        				case 4: // CASE FOR DELETE
        					rs=statement.executeQuery("select count(*) from "+table);
        					rs.next();
        					rowCount = Integer.parseInt(rs.getString(1));
        					String id3 = JOptionPane.showInputDialog("Input id of the data you want to delete:");
        					if(id3.length()==0 || numbers(id3))
        					{
        						JOptionPane.showMessageDialog(null, "Invalid input. You will be directed back to the menu.");
        						break;
        					}
        					rs = statement.executeQuery("select * from "+table);
        					for (int i = 1; i<=rowCount;i++)
        					{	
        						rs.next();
        						if(id3.equals(rs.getString(1)))
        							break;
        						if(i==rowCount)
        						{
        							JOptionPane.showMessageDialog(null, "ID does not exist. You will be directed back to the menu.");
        							exit = 1 ;
        						}
        					}
        					if(exit==1)
        						break;
        					rs=statement.executeQuery("select count(*) from order_line");
        					rs.next();
        					rowCount = Integer.parseInt(rs.getString(1));
        					rs = statement.executeQuery("select * from order_line");
        					for (int i = 1; i<=rowCount;i++)
        					{	
        						rs.next();
        						if(id3.equals(rs.getString(1)))
        						{
        							statement.execute("delete from order_line where "+table+" ="+id3+";");
        							break;
        						}
        					}
    						JOptionPane.showMessageDialog(null, "Delete successful. You will be directed back to the menu.");
    						statement.execute("delete from "+table+" where id ="+id3+";");
        					break;
        				case 5:    // case for UPDATE
        					if(table.equals("order_line"))
        					{
        						String customerId = JOptionPane.showInputDialog("Input id of customer whose data you would like to update:");
        						if(customerId.length()== 0 || numbers(customerId))
        				    	{
            						JOptionPane.showMessageDialog(null, "Invalid customer id. You will be directed back to the menu.");
            						break;
            					}
        						rs = statement.executeQuery("select count(*) from order_line");
        						rs.next();
        						rowCount = Integer.parseInt(rs.getString(1));
        						rs = statement.executeQuery("select * from order_line");
        						for(int i = 1; i <= rowCount; i++ )
        						{
        							rs.next();
        							if(customerId.equals(rs.getString(1)))
        								break;
        							if(i==rowCount)
        								exit = 1;
        						}		
        						if(exit == 1)
        						{
        							JOptionPane.showMessageDialog(null, "ID does not exist. You will be directed back to the menu.");
        							break;
        						}
        						if(setDate("transaction's"))
        							break;
        						rs = statement.executeQuery("select count(*) from order_line");
        						rs.next();
        						rowCount = Integer.parseInt(rs.getString(1));
        						rs = statement.executeQuery("select * from order_line");
        						java.text.DecimalFormat f = new java.text.DecimalFormat("##00.#");
        						for(int i = 1; i <= rowCount; i++ )
        						{
        							rs.next();
        							if((year+"-"+f.format(Double.parseDouble(month))+"-"+f.format(Double.parseDouble(date))).equals(rs.getString(3)))
        								break;
        							if(i==rowCount)
        								exit = 1;
        						}		
        						if(exit == 1)
        						{
        							JOptionPane.showMessageDialog(null, "No transaction was dated "+year+"-"+month+"-"+date+". You will be directed back to the menu.");
        							break;
        						}
        				    	rs=statement.executeQuery("select concat(c.lname,\", \",c.fname,\" \",ifNull(c.mname,\"\")),i.name,o.quantity,o.transaction_date from order_line as o,customer as c,item as i where i.id = o.item and o.customer = "+customerId+" and c.id = o.customer and transaction_date = '"+year+"-"+month+"-"+date+"' group by item;");
        						while (rs.next())
    							{
    								for(int i = 1; i <= columnsNumber; i++)
    									output+=(rs.getString(i) +" ");
    								output+= "\n";
    							}
        						JOptionPane.showMessageDialog(null,output);
        						String itemId = JOptionPane.showInputDialog("Input id of the item whose data you want to change:");
        						if(itemId.length()== 0 || numbers(itemId))
        				    	{
            						JOptionPane.showMessageDialog(null, "Invalid item id. You will be directed back to the menu.");
            						break;
            					}
        						String column = JOptionPane.showInputDialog("Choose the data you want to update:\n1 - Item\n2 - Quantity");
    							column=(column.equals("1")?"item":column.equals("2")?"quantity":"*");
    							if(column.equals("*"))
    							{
            						JOptionPane.showMessageDialog(null, "Invalid column. You will be directed back to the menu.");
            						break;
            					}
    							String newValue = JOptionPane.showInputDialog("Input the new "+column);
    							if(column.equals("item"))
    							{
    								if(newValue.length()== 0 || numbers(newValue))
            				    	{
                						JOptionPane.showMessageDialog(null, "Invalid new item. You will be directed back to the menu.");
                						break;
                					}
    								rs = statement.executeQuery("select count(*) from item");
            						rs.next();
            						rowCount = Integer.parseInt(rs.getString(1));
            						rs = statement.executeQuery("select * from item");
            						for(int i = 1; i <= rowCount; i++ )
            						{
            							rs.next();
            							if(newValue.equals(rs.getString(1)))
            								break;
            							if(i==rowCount)
            								exit = 1;
            						}
    							}
    							else if(newValue.length()== 0 || dotNumbers(newValue) )
                				{
                    				JOptionPane.showMessageDialog(null, "Invalid new quantity. You will be directed back to the menu.");
                    				break;
                    			}
    							statement.execute("update order_line set "+column+" ='"+newValue+"'where item ="+itemId+" and transaction_date = '"+(year+"-"+month+"-"+date)+"';");
    							break;
        					}
        					else
        					{
        						String newValue = "",date="",year="",month="";
        						id = JOptionPane.showInputDialog("Input id of "+table+" whose data you would like to update:");
        						if(id.length()== 0 || numbers(id))
        				    	{
            						JOptionPane.showMessageDialog(null, "Invalid "+table+" id. You will be directed back to the menu.");
            						break;
            					}
        						rs=statement.executeQuery("select count(*) from "+table);
            					rs.next();
            					rowCount = Integer.parseInt(rs.getString(1));
            					rs = statement.executeQuery("select * from "+table);
            					for (int i = 1; i<=rowCount;i++)
            					{	
            						rs.next();
            						if(i==rowCount && !id.equals(rs.getString(1)))
            						{
            							JOptionPane.showMessageDialog(null, table + " id does not exist. You will be directed back to the menu.");
            							exit = 1 ;
            						}
            						if(id.equals(rs.getString(1)))
            							break;
            					}
            					if(exit==1)
            						break;
        						String column = "Choose a column that you would like to update:";
        						int i;
        						for (i = 2; i <= rsmd.getColumnCount();i++)
        						column += "\n"+(i-1)+" - "+rsmd.getColumnName(i);
        						column = JOptionPane.showInputDialog(column);
        						if(column.length()== 0 || numbers(id) || Integer.parseInt(column) > (i-1)|| Integer.parseInt(column) < 1)
        				    	{
            						JOptionPane.showMessageDialog(null, "Invalid column. You will be directed back to the menu.");
            						break;
            					}
        						if(rsmd.getColumnName(Integer.parseInt(column)+1).equals("name")
        								|| rsmd.getColumnName(Integer.parseInt(column)+1).equals("description")
        								|| rsmd.getColumnName(Integer.parseInt(column)+1).equals("lname")
        								|| rsmd.getColumnName(Integer.parseInt(column)+1).equals("fname")
        								|| rsmd.getColumnName(Integer.parseInt(column)+1).equals("mname"))
        						{	
        							newValue = JOptionPane.showInputDialog("Input new value:");
        							if(newValue.length()== 0 || letterSpace(newValue) || newValue.contains("  ") || newValue.charAt(0) == ' ' )
            						{
                						JOptionPane.showMessageDialog(null, "Invalid new value. You will be directed back to the menu.");
                						break;
                					}
        						}
        						else if(rsmd.getColumnName(Integer.parseInt(column)+1).equals("price") || rsmd.getColumnName(Integer.parseInt(column)+1).equals("quantity"))
        						{
        							newValue = JOptionPane.showInputDialog("Input new value:");
            						if(newValue.length()== 0 || dotNumbers(newValue) )
                    				   	{
                        					JOptionPane.showMessageDialog(null, "Invalid new value. You will be directed back to the menu.");
                        					break;
                        				}	
        						}
        						else if(rsmd.getColumnName(Integer.parseInt(column)+1).equals("birthdate"))
            				    	if(setDate("customer's new birth"))
            				    		break;
        						else if(rsmd.getColumnName(Integer.parseInt(column)+1).equals("address"))
        						{
        							newValue = JOptionPane.showInputDialog("Input new value:");
            						if(newValue.length()== 0 || newValue.contains("  ") || newValue.charAt(0) == ' ')
        							{
        								JOptionPane.showMessageDialog(null, "Invalid address. You will be directed back to the menu.");
        								break;
        							}
        						}
        						else
            				   	{
        							newValue = JOptionPane.showInputDialog("Input new value:");
        							if(newValue.length()== 0 || numbers(newValue) )
        								JOptionPane.showMessageDialog(null, "Invalid contact number. You will be directed back to the menu.");
                					break;
                				}
        						if(rsmd.getColumnName(Integer.parseInt(column)+1).equals("birthdate"))
        							statement.execute("update "+table+" set "+rsmd.getColumnName(Integer.parseInt(column)+1)+" = '"+(year+"-"+month+"-"+date)+"' where id = "+id+";");
        						else
            						statement.execute("update "+table+" set "+rsmd.getColumnName(Integer.parseInt(column)+1)+" = '"+newValue+"' where id = "+id+";");
        						break;
        					}
        				case 6:
        					String keyword = "",keyword2 = "";
    						if(table.equals("order_line"))
    						{
    							keyword = JOptionPane.showInputDialog("Input a date before your transaction:");
    							keyword2 = JOptionPane.showInputDialog("Input a date after your transaction:");
    							rs=statement.executeQuery("select concat(lname,\", \",fname,\" \",ifNull(mname,\"\")),i.name,i.price,o.quantity,transaction_date,format(o.quantity*i.price,2) from order_line as o,item as i ,customer as c where transaction_date between '"+keyword+"' and '"+keyword2+"' and i.id = o.item and i.id = o.item and c.id = o.customer order by customer;");
    							rsmd = rs.getMetaData();
        						while (rs.next())
    							{
    								for(int i = 1; i <= rsmd.getColumnCount(); i++)
    									output+=(rs.getString(i) +" ");
    								output+= "\n";
    							}
    							JOptionPane.showMessageDialog(null,output.equals("")?"No results found":output);
    							break;
        					}
        					else
        					{
        						keyword = JOptionPane.showInputDialog("Search:");
        						rs = statement.executeQuery("select * from "+table+" where "+(table.equals("item")?"name like '%"+keyword+"%'":"fname like '%"+keyword+"%' or lname like '%"+keyword+"%'"));
        						rsmd = rs.getMetaData();
        						while (rs.next())
    							{
    								for(int i = 1; i <= rsmd.getColumnCount(); i++)
    									output+=(rs.getString(i) +" ");
    								output+= "\n";
    							}
    							JOptionPane.showMessageDialog(null,output.equals("")?"No results found":output);
    							break;
        					}
        			}
        		}
        	}
        }
        catch (Exception e)
        {
        	String x[]={""};
        	if(JOptionPane.showConfirmDialog(null, e.getMessage(),"Error",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
        			System.exit(0);
        	else
        		main(x); 
        }
    }
    public boolean setDate(String dateType)
    {
    	String year=JOptionPane.showInputDialog("Input "+dateType+" year:");
    	if(year.length()== 0 || numbers(year) || Integer.parseInt(year) > 9999 || Integer.parseInt(year) <1000)
    	{
			JOptionPane.showMessageDialog(null, "Invalid year. You will be directed back to the menu.");
			return true;
		}
    	String month=JOptionPane.showInputDialog("Input "+dateType+" month:");
    	if(month.length()== 0 || numbers(month) || Integer.parseInt(month) > 12 || Integer.parseInt(month) < 1)
    	{
			JOptionPane.showMessageDialog(null, "Invalid month. You will be directed back to the menu.");
			return true;
		}
    	String date=JOptionPane.showInputDialog("Input "+dateType+" date:");
    	if(date.length()== 0 || numbers(date) || Integer.parseInt(date) > 31 || Integer.parseInt(date) < 1)
    	{
			JOptionPane.showMessageDialog(null, "Invalid date. You will be directed back to the menu.");
			return true;
		}
    	if(Integer.parseInt(year)%4==0)
    		if(Integer.parseInt(month)==2)
    			if(Integer.parseInt(date)>29)
    			{
					JOptionPane.showMessageDialog(null, "Invalid date, "+year+"-"+month+"-"+date+". You will be directed back to the menu.");
					return true;
				}
    	if(Integer.parseInt(year)%4!=0)
    		if(Integer.parseInt(month)==2)
    			if(Integer.parseInt(date)>28)
    			{
					JOptionPane.showMessageDialog(null, "Invalid date, "+year+"-"+month+"-"+date+". You will be directed back to the menu.");
					return true;
				}
    	if(Integer.parseInt(month) == 2 || Integer.parseInt(month) == 4 || Integer.parseInt(month) == 6 || Integer.parseInt(month) == 9 || Integer.parseInt(month) == 11)
    		if(Integer.parseInt(date)>30)
			{
				JOptionPane.showMessageDialog(null, "Invalid date, "+year+"-"+month+"-"+date+". You will be directed back to the menu.");
				return true;
			}
    	return false;
    }
    public boolean numbers(String input)
    {
    	for(int i = 0; i<input.length();i++)
    		if(!Character.isDigit(input.charAt(i)))
    			return true;
    	return false;
    }
    public boolean dotNumbers(String input)
    {
    	for(int i = 0; i<input.length();i++)
    		if( !Character.isDigit(input.charAt(i)) && input.charAt(i)!= 46)
    			return true;
    	return false;
    }
    public boolean letterSpace(String input)
    {
    	for(int i = 0; i<input.length();i++)
    		if(!Character.isLetter(input.charAt(i)) && !Character.isWhitespace(input.charAt(i)) )
    			return true;
    	return false;
    }
    public static void main( String args[] )
    {
        MysqlSample conn=new MysqlSample();
        conn.Connect();
    }
}