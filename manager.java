import javax.swing.*;
import javax.swing.JEditorPane;
import java.awt.*;
import java.text.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.time.format.*;
import javax.swing.table.*;
import java.io.*;
import javax.print.*;
//Main Class
//Opens the manager GUI on call
//Contains three buttons
//View Inventory,Change Price of item,View Sales Statistics
//Which calls the respective GUI on clicking(ActionListener)
class manager						
{
	//Defining format of decimal to be used for cost
	private static DecimalFormat df2 = new DecimalFormat(".##");

	//Creating and initializing url,usr,pass and stmt
	private static final String url="jdbc:mysql://localhost/sas?useSSL=false";
	//username for mysql database
	private static final String usr="demouser";
	//password for mysql database
	private static final String pass="241420";
	Statement stmt=null;
	
	//Creating the JFrame f
	JFrame f;
	Font heading=new Font("Times New Roman",Font.BOLD,30)	;
	
	//Constructor
	//Creates and adds all the swing components in JFrame f
	manager()						
	{
		//main JFrame
		f=new JFrame("Manager");
		JButton viewB, changeB, salesB;
		viewB = new JButton("View Inventory");
		changeB= new JButton("Change price of item");
		salesB = new JButton("View sales Statistics");
		JButton exit=new JButton("X");
		
		//Action Listener for exit
		//Closes the application 
		exit.addActionListener(new ActionListener(){			
			public void actionPerformed(ActionEvent e){			
				System.exit(1);
			}
		});
		
		//Action Listener for viewB
		//Opens GUI for viewInventory
		viewB.addActionListener(new ActionListener(){			
			public void actionPerformed(ActionEvent e)
			{			
				System.out.println("Clicked");
				new viewInventory();
			}
		});
		
		//Action Listener for changeB
		//Opens GUI for changePrice
		changeB.addActionListener(new ActionListener(){			
			public void actionPerformed(ActionEvent e)
			{			
				System.out.println("Clicked");
				new changePrice();
			}
		});
		
		//Action Listener for salesB
		//Opens GUI for salesStatistics					
		salesB.addActionListener(new ActionListener(){			
			public void actionPerformed(ActionEvent e){			
				System.out.println("Clicked");
				new salesStatistics();
			}
		});
		
		//Setting bounds of all the buttons
		exit.setBounds(500,50,50,50);							
		viewB.setBounds(150,50,300,100);
		salesB.setBounds(150,350,300,100);
		changeB.setBounds(150,200,300,100);
		
		//Adding all the swing component to the JFrame f
		//Setting Bounds and Dimensions of the JFrame f
		f.add(exit);											 
		f.add(viewB);
		f.add(changeB);
		f.add(salesB);
		f.setLayout(null);
		f.setSize(600,800);
		f.setLocation(600,150);
		f.setVisible(true);
	}
	
	//Main method
	//Opens the GUI of manager
	public static void main(String []args)
	{
		new manager();
	}
	
	//Class viewInventory
	//Displays the inventory in the form of a table
	//Containg exit button that closes the GUI upon clicking
	class viewInventory
	{
		//Creates the JFrame viewF
		JFrame viewF= new JFrame("View Inventory");
		String[][] data;
		
		//Constructor
		//Creates and adds all the swing componenets to JFrame viewF
		viewInventory()
		{
			try
			{
				//Connecting to the database 'sas'
				Connection con= DriverManager.getConnection(url,usr,pass);
				System.out.println("Success");
				stmt = con.createStatement();
				String sql1="SELECT * FROM inventory";
				
				//Executes a query to store all the data present in the table inventory to ResultSet rs 
				ResultSet rs= stmt.executeQuery(sql1);
				int i=0;
				while(rs.next())
				{
					i++;
				}
				System.out.println(i);
				ResultSet rs1= stmt.executeQuery(sql1);
				
				//Creates a 2-D array of strings which stores all the data in the table inventory
				data=new String[i][5];
				
				//Strores all the data in array
				while(rs1.next())
				{
					data[rs1.getRow()-1][0]=rs1.getString("id");
					data[rs1.getRow()-1][1]=rs1.getString("name");
					data[rs1.getRow()-1][2]=rs1.getString("cost_price");
					data[rs1.getRow()-1][3]=rs1.getString("retail_price");
					data[rs1.getRow()-1][4]=rs1.getString("quantity");
				}
				String[] colNames={"id","name","Cost Price","Retail Price","Quantity"};
				
				//Creates and initializes the JTable inventortT 
				JTable inventoryT=new JTable (data,colNames);
				inventoryT.setBounds(50,100,500,600);
				
				//Creating a JScrollPane
				JScrollPane inventorySP=new JScrollPane(inventoryT);
				
				//Creating a JLabel and setting its Bounds
				JLabel headingL=new JLabel("Inventory");
				headingL.setFont(heading);
				headingL.setBounds(50,50,200,50);	
				
				inventorySP.setBounds(50,100,500,600);
				
				//Creates button exit and sets it bounds
				JButton exit= new JButton("exit");
				exit.setBounds(250,700,100,50);
				
				//Action Listener for exit
				//Closes the viewInventory GUI
				exit.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ee){
						System.out.println("Clicked");
						viewF.setVisible(false);
					}
				});
				
				//Adds all the swing components to the JFrame viewF
				viewF.add(headingL);
				viewF.add(exit);
				viewF.add(inventorySP);
				
				//Setting Bounds and Dimensions of the JFrame viewF
				viewF.setSize(600,800);
				viewF.setLocation(600,150);
				viewF.setLayout(null);
				viewF.setVisible(true);
			}
			
			//Catches any exceptions
			//Prints the Stack Trace
			catch(Exception e)
			{
				e.printStackTrace();
			}
	
		}
	}
	
	//Class changePrice
	//Opens the changePrice GUI
	class changePrice
	{
		JFrame changeF=new JFrame("Change Price");
		String[] names;
		
		//Constructor
		changePrice()
		{
			try
			{
				//Connecting to the database 'sas'
				Connection con= DriverManager.getConnection(url,usr,pass);
				System.out.println("Success");
				stmt = con.createStatement();
				
				//finding number of different items in inventory
				String sql="SELECT id FROM inventory";
				ResultSet rs= stmt.executeQuery(sql);
				int size=0;
				if(rs!=null)
				{
					rs.last();
					size=rs.getRow();
				}
				
				//finding names of all items
				int i=0;
				names= new String[size];
				stmt = con.createStatement();
				String sql1="SELECT name FROM inventory";
				ResultSet rs1=stmt.executeQuery(sql1);
				while(rs1.next())
				{
					names[i]=rs1.getString("name");
					System.out.println(names[i]);
					i++;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			
			//various JFrame components 
			JButton updatePrice = new JButton("Update Price");
			JTextField newPrice= new JTextField("");
			JLabel newPriceL= new JLabel("Enter New Retail Price");
			newPrice.setBounds(350,100,150,50);
			newPriceL.setBounds(150,100,200,50);
			JTextField newCostPrice= new JTextField("");
			JLabel newCostPriceL= new JLabel("Enter New Cost Price");
			newCostPrice.setBounds(350,200,150,50);
			newCostPriceL.setBounds(150,200,150,50);
			
			
			//Drop down for list of names
			JComboBox<String> nameList=new JComboBox<>(names);
			JPanel namePanel = new JPanel();
			namePanel.add(nameList);
			nameList.setPreferredSize(new Dimension(200,30));
			nameList.setLocation(100,100);
			updatePrice.setBounds(250,350,150,50);
			JButton exit= new JButton("exit");
			exit.setBounds(275,450,100,50);
			
			//Action Listener for exit
			//Closes the changePrice GUI
			exit.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ee){
					System.out.println("Clicked");
					changeF.setVisible(false);
				}
			});
			
			//Adding swing components to JFrame
			changeF.add(exit);
			changeF.add(updatePrice);
			changeF.add(newPrice);
			changeF.add(newPriceL);
			changeF.add(newCostPrice);
			changeF.add(newCostPriceL);
			changeF.add(namePanel);
			changeF.setSize(650,600);
			changeF.setLocation(600,200);
			//comment //changeF.setLayout(null);
			changeF.setVisible(true);
			System.out.println(nameList.getSelectedItem());
			
			//Action Listener for updatePrice
			//Changes price of the selected items
			updatePrice.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					System.out.println("Clicked");
					String find=(String) nameList.getSelectedItem();
					try
					{
						//Mysql commands
						//Creating commands to access database
						Connection con= DriverManager.getConnection(url,usr,pass);
						System.out.println("Success");
						stmt = con.createStatement();
						
						//check empty condition
						//prevent random data entry	
						if((!newPrice.getText().equals(""))&&(!newCostPrice.getText().equals("")))
						{
							if(!newPrice.getText().matches(".*[a-zA-Z]+.*") && !newCostPrice.getText().matches(".*[a-zA-Z]+.*"))
							{
								double updateValue=Double.parseDouble(newPrice.getText());
								double updateCostValue=Double.parseDouble(newCostPrice.getText());
								String sql="UPDATE inventory SET retail_price = '"+updateValue+"' WHERE name = '"+find+"'";
								stmt.executeUpdate(sql);
								sql="UPDATE inventory SET cost_price = '"+updateCostValue+"' WHERE name = '"+find+"'";
								stmt.executeUpdate(sql);
								changeF.setVisible(false);
								con.close();
							}
							//error pop-up
							else
							{
								JFrame popUp=new JFrame("Error");
								JOptionPane.showMessageDialog(popUp,"Enter Valid Value");
							}
						}
						else
						{
							JFrame popUp=new JFrame("Error");
							JOptionPane.showMessageDialog(popUp,"Enter Valid Value");
							changeF.dispose();
							new changePrice();
						}
					}
					catch(Exception eee)
					{
						eee.printStackTrace();
					}
				}
			});
		
		}
	}
	
	//Class salesStatistics
	//presents the sales for a enterer duration
	class salesStatistics
	{
		JFrame salesF=new JFrame("Sales Statistics");
		JFrame statsF=new JFrame("Sales Stats");
		String[][] data;
		
		//Constructor
		salesStatistics()
		{
			//various list of years available to choose from
			String[] year=new String[]{"Year","2018","2019","2020"};
			
			//various list of months available to choose from
			String[] month=new String[]{"Month","01", "02", "03", "04", "05", "06", "07", "08", "09","10", "11", "12"};
			
			//list of available dates
			String[] day=new String[]{"Day","01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
			
			
			//various swing components
			JLabel date1L=new JLabel("Enter first date (YYYY-MM-DD)");
			JLabel date2L=new JLabel("Enter second date(YYYY-MM-DD)");
			JComboBox<String> year1=new JComboBox<>(year);
			JComboBox<String> month1=new JComboBox<>(month);
			JComboBox<String> day1=new JComboBox<>(day);
			
			//create panel1
			//contains 1st year, month, date
			JPanel panel1= new JPanel();
			
			//Adding the combobox to the panel
			panel1.setLayout(new GridLayout(0,3,20,200));
			panel1.add(year1);
			panel1.add(month1);
			panel1.add(day1);
			
			JComboBox<String> year2=new JComboBox<>(year);
			JComboBox<String> month2=new JComboBox<>(month);
			JComboBox<String> day2=new JComboBox<>(day);
			
			//create panel2
			//contains 2nd year, month, date
			JPanel panel2= new JPanel();
			//Adding combobox to the panel
			panel2.setLayout(new GridLayout(0,3,20,20));
			panel2.add(year2);
			panel2.add(month2);
			panel2.add(day2);
			
			
			JButton showResult=new JButton("Show Result");
			JButton exit1= new JButton("exit");
			exit1.setBounds(350,500,100,50);
			//Action Listener for exit
			//Closes the GUI
			exit1.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ee){
					System.out.println("Clicked");
					salesF.setVisible(false);
				}
			});
			
			//create panel 3
			//contains show result
			//contains exit
			JPanel panel3=new JPanel();
			panel3.setLayout(new GridLayout(0,2,50,20));
			panel3.add(showResult);
			panel3.add(exit1);
			
			
			//Adding swing components to the JFrame
			salesF.setLayout(new GridLayout(3,1,100,50));
			salesF.add(panel1);
			salesF.add(panel2);
			salesF.add(panel3);
			salesF.pack();
			salesF.setLocation(800,450);
			salesF.setVisible(true);
			
			//Action Listener for showResult
			//Displays the sales statistics for the requested time period
			showResult.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					System.out.println("Clicked");
					try
					{
						Connection con= DriverManager.getConnection(url,usr,pass);
						System.out.println("Success");
						stmt = con.createStatement();
						String date1="";
						String date2="";
						
						//Checking if input dates are valid
						if((!((String)year1.getSelectedItem()).equals("Year") && !((String)year2.getSelectedItem()).equals("Year") && !((String)month1.getSelectedItem()).equals("Month") && !((String)month2.getSelectedItem()).equals("Month") && !((String)day1.getSelectedItem()).equals("Day")) && !((String)day2.getSelectedItem()).equals("Day"))
						{
							date1=(String)year1.getSelectedItem()+"/"+(String)month1.getSelectedItem()+"/"+(String)day1.getSelectedItem();
						
							date2=(String)year2.getSelectedItem()+"/"+(String)month2.getSelectedItem()+"/"+(String)day2.getSelectedItem();							
							String date11=(String)year1.getSelectedItem()+"_"+(String)month1.getSelectedItem()+"_"+(String)day1.getSelectedItem();
						
							String date22=(String)year2.getSelectedItem()+"_"+(String)month2.getSelectedItem()+"_"+(String)day2.getSelectedItem();
							System.out.println("Compare : "+date1.compareTo(date2));
							
							//Comparing the input dates
							//if given in reverse order(Taken care)
							if(date1.compareTo(date2) > 0)
							{
								String temp=date1;
								date1=date2;
								date2=temp;
							}
							System.out.println(date1);
							System.out.println(date2);
							
							
							String sql="SELECT name FROM inventory";
							//Executes a query to store all the data present in the table inventory to ResultSet rs 
							ResultSet rs2= stmt.executeQuery(sql);
							int size=0;
							String[] name;
							while(rs2.next())
							{
								System.out.println(rs2.getString("name"));
								size++;
							}
							name=new String[size];
							int s=0;
							rs2=stmt.executeQuery(sql);
							while(rs2.next()){
								name[s]=rs2.getString("name");
								s++;
							}								
							System.out.println(size);
							//Finds all data available between date1 and date2
							
							data=new String[size][4];
							
							for(int i=0;i<size;i++)
							{
								String sql1="SELECT * FROM sales WHERE date BETWEEN '"+date1+"' AND  '"+date2+"' AND name = '"+name[i]+"'";
								ResultSet rs= stmt.executeQuery(sql1);
								double costT=0,profitT=0;
								int quantityT=0;
								while(rs.next())
								{
									costT+=Double.parseDouble(rs.getString("cost"));
									profitT+=Double.parseDouble(rs.getString("profit"));
									quantityT+=Integer.parseInt(rs.getString("quantity"));
								}
								data[i][0]=name[i];
								data[i][1]=String.valueOf(df2.format(costT));
								data[i][2]=String.valueOf(df2.format(profitT));
								data[i][3]=String.valueOf(quantityT);
							}
							
							
							//various components of Frame
							JLabel headingL=new JLabel("Sales Statistics for "+date1+" to "+date2);
							headingL.setBounds(50,50,900,150);
							String[] colNames={"Name","Total Cost","Total Profit","Quantity Sold"};
							JTable salesT=new JTable (data,colNames);
							salesT.setBounds(150,200,700,700);
							JScrollPane salesSP=new JScrollPane(salesT);
							salesSP.setBounds(150,200,700,700);
							//comment //inventorySP.add(inventoryT);
							
							//Action Listener for exits
							//Reopens the manager frame
							JButton exits= new JButton("exit");
							exits.setBounds(150,910,250,80);
							exits.addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent e){
									System.out.println("Clicked");
									statsF.dispose();
									salesF.dispose();
								}
							});
							int sizef=size;
							JButton printSales=new JButton("Print Sales");
							printSales.addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent e){
									System.out.println("Clicked");
									try{
										usingFileWriter(data,sizef,date11,date22);
									}
									catch(Exception er){
									er.printStackTrace();
									}
								}
							});
							printSales.setBounds(600,910,250,80);
							statsF.add(printSales);
							headingL.setFont(heading);
							statsF.add(headingL);
							statsF.add(exits);
							statsF.add(salesSP);
							statsF.setSize(1000,1100);
							statsF.setLocation(500,0);
							statsF.setLayout(null);
							statsF.setVisible(true);
							
						}
						
						//If any invalid input is entered
						else
						{
							JFrame popUp=new JFrame("Error");
							JOptionPane.showMessageDialog(popUp,"Enter Valid Value");
						}
			
					}
					catch(Exception ae)
					{
						ae.printStackTrace();
					}
				}
			});	
		}
		
	}
	
	static void usingFileWriter(String [][]array,int size,String date1, String date2) throws IOException
	{
		//initialize the string
		String fileContent="\n";
		fileContent ="\t\tSALES STATISTICS for "+date1+" to "+date2+"\n";
		//writing to the string
		fileContent += "\tName\t\t\t\t\t\tTotal Cost\t\t\tTotal Profit\t\t\tQuantity Sold";
		for(int i=0;i<size;i++){
			fileContent +="\n";
			fileContent = fileContent +"\t"+array[i][0]+"\t\t\t\t"+array[i][1]+"\t\t\t\t\t"+array[i][2]+"\t\t\t\t"+array[i][3];
		}
		
		//specifying the loaction and name of the file
		FileWriter fileWriter = new FileWriter("/home/shivu/Desktop/Project/Codes/Manager/Sales/Sales_"+date1+"to"+date2+".txt");
		fileWriter.write(fileContent);
		fileWriter.close();
		
		//opening the txt file
		File file = new File("/home/shivu/Desktop/Project/Codes/Manager/Sales/Sales_"+date1+"to"+date2+".txt");
        
		//first check if Desktop is supported by Platform or not
		if(!Desktop.isDesktopSupported()){
		    System.out.println("Desktop is not supported");
		    return;
		}
		
		//Display the file
		Desktop desktop = Desktop.getDesktop();
		if(file.exists()){
			desktop.open(file);
		}
	}
}
				
