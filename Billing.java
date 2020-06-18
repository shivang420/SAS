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

class Billing{
	//Defining format of decimal to be used for cost
	private static DecimalFormat df2 = new DecimalFormat(".##");
	
	//Defining the values of Mysql database
	//Can be modified as per personal use
	private static final String url="jdbc:mysql://localhost/sas?useSSL=false";
	private static final String usr="demouser";
	private static final String pass="241420";
	Statement stmt=null;
	
	//Some global used variables
	String[][] billArray=new String[40][5];
	String[][] billList=new String[40][5];
	int sizeB=0;
	double payT=0;
	String[] names;
	
	//Defining Main Frame of Bill				
	JFrame billF=new JFrame("Bill");
	Billing(){
		try{
			//Mysql commands
			//Creating commands to access database
			Connection con= DriverManager.getConnection(url,usr,pass);
			System.out.println("Success");
			stmt = con.createStatement();
			
			//Calculating the size of table
			String sql="SELECT id FROM inventory";
			ResultSet rs= stmt.executeQuery(sql);
			int size=0;
			if(rs!=null){
				rs.last();
				size=rs.getRow();
			}
			int i=0;
			
			//Retrieving name of all the items in database
			names= new String[size];
			stmt = con.createStatement();
			String sql1="SELECT name FROM inventory";
			ResultSet rs1=stmt.executeQuery(sql1);
			while(rs1.next()){
				names[i]=rs1.getString("name");
				System.out.println(names[i]);
				i++;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		//Creating a drop-down of names of all items in database
		JComboBox<String> nameList=new JComboBox<>(names);
		JPanel namePanel = new JPanel();
		namePanel.add(nameList);
		
		
		//Creating an close application button
		JButton exit=new JButton("X");
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.exit(1);
			}
		});
		
		//Defining various JSwing Components
		//Quantity text field is used to take input the 'quantity of an item' to be added to bill
		JTextField quantity= new JTextField();
		//Label to specify what the text field entry is
		JLabel quantityL=new JLabel("Enter quantity :");
		//This button adds the selected item,quantity to Bill list 	
		JButton addtoBill = new JButton("Add to Bill");
		
		
		//Makes bill as a table and append values to end of it
		DefaultTableModel model = new DefaultTableModel();
	    	JTable bill = new JTable(model);
	    	//Various Columns of the Bill
		//Name of item
		model.addColumn("Name");
		//Cost of 1 unit of item
	    	model.addColumn("Price/unit");
	    	//Number of item purchased by customer
	    	model.addColumn("Quantity");
	    	//Total cost
	    	model.addColumn("Cost");
	    	
	    	
		//Print bill button
		//Opens a text file with all the items bought by user
		//Updates Sales statistics
		//Opens new bill window
		JButton printB=new JButton("Print Bill");
		printB.setPreferredSize(new Dimension(600,50));
		
		//Total is the bill total of all the items in bill right now
		double total=0;
		JLabel totalL= new JLabel("Total - 0");
		totalL.setFont(new Font("Times New Roman", Font.BOLD,24));
		totalL.setPreferredSize(new Dimension(300,50));
	
	
		//Describes the action performed by Button Add to Bill
		//It takes input from quantity text field, item name from drop down
		//On click it adds the respective amount for the item to the Bill
		//The value of total is also updated
		addtoBill.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ee){
				//Defining various various variables to be used
				
				//Selects the name of item selected from drop down
				String nm=(String)nameList.getSelectedItem();
				
				//Price is the MRP of the item
				//To be extracted from inventory database
				double price=0;
				
				//Cost Price of item
				//To be used for sales statistics
				double cost_p=0;
				
				//Profit/unit of item
				//(= MRP-Cost_Price)
				double profit=0;
				
				//Current value of selected item in inventory database
				int available=0;
				
				
				try{
					//Mysql commands
					//Creating commands to access database
					Connection con= DriverManager.getConnection(url,usr,pass);
					System.out.println("Success");
					stmt = con.createStatement();
					
					//Retrieving the value of cost_price, MRP, quantity of various inventory 
					String sql1="SELECT cost_price,retail_price,quantity FROM inventory where name ='"+nm+"'";
					ResultSet rs= stmt.executeQuery(sql1);
					while(rs.next()){
						price=Double.parseDouble(rs.getString("retail_price"));
						cost_p=Double.parseDouble(rs.getString("cost_price"));
						available=Integer.parseInt(rs.getString("quantity"));
						//Comment //System.out.println(price);
						//Comment //System.out.println("Avail " +available);
					}
					
					
					//Comment //System.out.println(quant);
					
					// calculates value of total cost of items and adds it to bill table
					
					//prevents multiple clicks of button "Add to bill"
					if(!quantity.getText().equals("") ){
						//Prevents unnecessary symbols
						if(quantity.getText().matches(".*[a-zA-Z]+.*")){
								JFrame popUp=new JFrame("Error");
								JOptionPane.showMessageDialog(popUp,"Enter Valid Value");
								quantity.setText("");
						}
						else
						{
							int quant=Integer.parseInt(quantity.getText());
							//checks if the quantity of item in stock is more than entered value
							if(quant>available){
									JFrame popUp=new JFrame("Error");
									JOptionPane.showMessageDialog(popUp,"Enter Valid Value");
									quantity.setText("");
							}
							//else
							//enters to the bill
							//removes from inventory
							else{	
								System.out.println(quant);
								
								//total cost
								double cost=quant*price;
								
								//new row in the bill
								model.addRow(new Object[] {nm,price,quant,cost});
								quantity.setText("");
								
								//calculations
								profit=price-cost_p;
								profit*=quant;
								cost_p*=quant;
								
								//date of purchase
								DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
								Date date = new Date();
								System.out.println(dateFormat.format(date));
								String dt=(String) dateFormat.format(date);
								
								//array to be used for sales statistics
								billArray[sizeB][0]=dt;
								billArray[sizeB][1]=nm;
								billArray[sizeB][2]=String.valueOf(df2.format(cost_p));
								billArray[sizeB][3]=String.valueOf(df2.format(profit));
								billArray[sizeB][4]=String.valueOf(quant);
						
								//array to be used to display to bill
								billList[sizeB][0]=dt;
								billList[sizeB][1]=nm;
								billList[sizeB][2]=String.valueOf(df2.format(price));
								billList[sizeB][4]=String.valueOf(df2.format(price*quant));
								billList[sizeB][3]=String.valueOf(quant);
						
								sizeB++;
								
								//change value of available in the inventory
								available-=quant;
								String sql="UPDATE inventory SET quantity = '"+available+"' WHERE name = '"+nm+"'";
								
								//recalculate the total
								payT+=price*quant;
								totalL.setText("Total ="+payT);
								
								//Mysql command to execute above command
								stmt.executeUpdate(sql);
							}
						}
					}
				}
				//In case of any expection
				catch(Exception eee){
					eee.printStackTrace();
				}
			}				
				
		});
		
		//Describes Action of Print Bill
		printB.addActionListener(new ActionListener(){
			//bill number
			int num=0;
			
			public void actionPerformed(ActionEvent ee){
				try{
						//Mysql commands
						//Creating commands to access database
						Connection con= DriverManager.getConnection(url,usr,pass);
						System.out.println("Success");
						stmt = con.createStatement();
						
						//Insert sales data into sales database
						for(int x=0;x<sizeB;x++){
							String sql="INSERT INTO sales(date,name,cost,profit,quantity) "+"VALUES('"+billArray[x][0]+"','"+billArray[x][1]+"','"+df2.format(Double.parseDouble(billArray[x][2]))+"','"+df2.format(Double.parseDouble(billArray[x][3]))+"','"+Integer.parseInt(billArray[x][4])+"')";			
							stmt.executeUpdate(sql);
							
							//get value of bill number from last value
							sql="SELECT * FROM sales";
							ResultSet rs= stmt.executeQuery(sql);
							rs.last();
							num=rs.getRow();	
						}
						
						//Write bill to a txt file
						usingFileWriter(billList,sizeB,num,payT);
							
				}
				catch(Exception eeeee)
				{
					eeeee.printStackTrace();
				}
				
				//Bill completed pop-up
				JFrame popUp=new JFrame("Bill Generated");
				JOptionPane.showMessageDialog(popUp,"Generate new Bill");
				billF.dispose();
				new Billing();
					
				//comment //billF.setVisible(false);
				
			}
		});
		
		
		//Various components of bill JFrame
		quantity.setPreferredSize(new Dimension(100,20));
		
		addtoBill.setBounds(100,110,200,40);
		exit.setBounds(350,10,100,50);
		bill.setBounds(500,400,400,400);
		
		billF.add(namePanel);
		billF.add(quantityL);
		billF.add(quantity);
		billF.add(addtoBill);
		billF.add(new JScrollPane(bill));
		billF.add(printB);
		billF.add(totalL);
		billF.add(exit);	
		
		
		billF.setSize(500,600);
		billF.setLocation(500,100);
		billF.setVisible(true);
		billF.setResizable(false);
		billF.setLayout(new FlowLayout());
	}
	
	
	//mehtod used to write the bill to a txt file
	//uses fileWriter to write to a txt file
	static void usingFileWriter(String [][]array,int size,int num, double payT) throws IOException
	{
		//initialize the string
		String fileContent="\n";
		
		//writing to the string
		fileContent = "\tName\t\tPrice/unit\t\tQuantity\t\tTotal Price";
		for(int i=0;i<size;i++){
			fileContent +="\n";
			fileContent = fileContent +"\t"+array[i][1]+"\t\t"+array[i][2]+"\t\t\t"+array[i][3]+"\t\t\t"+array[i][4];
		}
		fileContent += "\n\t\t\t\t\tTHANK YOU FOR SHOPPING\n\t\t\t\t\tYOUR TOTAL IS Rs. "+payT+"\n\t\t\t\t\t Your Bill Number is "+num+"\n\t\t\t\t\t   Date: "+array[0][0];
		
		//specifying the loaction and name of the file
		FileWriter fileWriter = new FileWriter("/home/shivu/Desktop/Project/Codes/Billing/Bills/bill"+num+".txt");
		fileWriter.write(fileContent);
		fileWriter.close();
		
		//opening the txt file
		File file = new File("/home/shivu/Desktop/Project/Codes/Billing/Bills/bill"+num+".txt");
        
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
	
	public static void main(String []args){
		new Billing();		
	}
}
