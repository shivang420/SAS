import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
class inventoryClerk {
	
	//Defining the values of Mysql database
	//Can be modified as per personal use
	private static final String url="jdbc:mysql://localhost/sas?useSSL=false";
	private static final String usr="demouser";
	private static final String pass="241420";
	Statement stmt=null;
	
	//Main JFrame
	JFrame f;
	
	//constructor
	inventoryClerk(){
		f = new JFrame();
		
		//Heading
		JLabel Heading= new JLabel("Inventory Clerk");
		//comment //Font font1= new Font("Times New Roman", Font.BOLD, 36);
		Heading.setFont(new Font("Times New Roman",Font.BOLD,36));
		
		//adding various components of frame
		JButton addNew= new JButton("Add new item");
		JButton addExisting= new JButton("Add existing item");
		
		//opens addNewItem window
		//used to add new items to inventory database
		addNew.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//comment //System.out.println("Clicked");
				new addNewItem();
			}
		});
		
		//opens add exisiting item window
		//item can be selected from drop down box
		addExisting.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//comment //System.out.println("Clicked");
				new addExistingItem();
			}
		});
		
		//adds exit button
		//exits the application
		JButton exitX=new JButton("X");
		exitX.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.exit(1);
			}
		});
		
		
		//adds various components to the JFrame
		//Specifies location and size of components
		f.add(exitX);
		Heading.setBounds(150,50,450,50);
		exitX.setBounds(550,50,45,45);
		addNew.setBounds(200,150,200,150);
		addExisting.setBounds(200,350,200,150);
		f.add(Heading);
		f.add(addNew);
		f.add(addExisting);
		f.setLocation(600,400);
		f.setSize(600,600);
		f.setLayout(null);
		f.setVisible(true);
	}
	
	//main method
	public static void main(String[] args){
		new inventoryClerk();
	}

	// this is to add new items to inventory using a GUI
	class addNewItem{
	
		//Frame of add new items
		JFrame addN;
		
		//constructor
		addNewItem(){
		
			addN= new JFrame();
			
			//initialising various components of JFrame
			//Heading
			JLabel Heading=new JLabel("ADD NEW ITEM");
			Heading.setFont(new Font("Times New Roman",Font.BOLD,36));
			
			//Various input fields and their labels
			JTextField name, cost, retail, quantity;
			//Submit button to make changes to the inventory
			JButton submit=new JButton("Add to inventory");
			
			//Name of the item entered by the Inventory Clerk
			name=new JTextField("");
			JLabel nameL = new JLabel("Enter name of item"); 
			
			//Cost price of item
			cost=new JTextField("");
			JLabel costL = new JLabel("Enter cost price");
			
			//MRP of the item 
			retail=new JTextField("");
			JLabel retailL = new JLabel("Enter MRP");
			
			//Enter quantity of the item
			quantity=new JTextField("");
			JLabel quantityL = new JLabel("Enter quantity");
			
			//setting bounds of size
			Heading.setBounds(350,50,400,50);
			name.setBounds(250,150,200,50);
			cost.setBounds(650,150,200,50);
			retail.setBounds(250,250,200,50);
			quantity.setBounds(650,250,200,50);
			nameL.setBounds(100,150,200,50);
			costL.setBounds(500,150,200,50);
			retailL.setBounds(100,250,200,50);
			quantityL.setBounds(500,250,200,50);
			submit.setBounds(350,350,200,50);		
			
			//exit button
			JButton exit= new JButton("exit");
			exit.setBounds(400,450,100,50);
			exit.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ee){
					//comment //System.out.println("Clicked");
					addN.setVisible(false);
				}
			});
			
			//adding components
			addN.add(exit);
			addN.add(Heading);
			addN.add(name);
			addN.add(cost);
			addN.add(retail);
			addN.add(quantity);
			addN.add(nameL);
			addN.add(costL);
			addN.add(retailL);
			addN.add(quantityL);
			addN.add(submit);
			addN.setLocation(500,400);
			addN.setSize(880,550);
			addN.setLayout(null);
			addN.setVisible(true);
			
			//submit button
			//adds data to the inventory database
			submit.addActionListener(new ActionListener(){
		
				public void actionPerformed(ActionEvent e){
					//comment //System.out.println("Clicked");
					String nameS,costS,retailS,quantityS;
					nameS=name.getText();
					costS=cost.getText();
					retailS=retail.getText();
					quantityS=quantity.getText();
					int key;
					try{
						//Mysql commands
						//Creating commands to access database
						Connection con= DriverManager.getConnection(url,usr,pass);
						//comment //System.out.println("Success");
						stmt = con.createStatement();
						String sql1="SELECT id FROM inventory";
						ResultSet rs= stmt.executeQuery(sql1);
						key=0;
						if(rs!=null){
							rs.last();
							key=rs.getRow();
						}
						key++;
						String sql = "INSERT INTO inventory(id,name,cost_price,retail_price,quantity) "+"VALUES('"+key+"','"+nameS+"','"+costS+"','"+retailS+"','"+quantityS+"')";
						stmt.executeUpdate(sql);				
					}
					catch(Exception ee)
					{
						ee.printStackTrace();
					}
					addN.setVisible(false);		
			 	}
			});

		}
	}
	
	//adds existing item 
	//select item to add from drop down
	//enter the quantity 
	//that number of items are added to the inventory database
	class addExistingItem{
	
		//JFrame
		JFrame addE=new JFrame("Add existing items");;
		
		//Name of all the items in the inventory
		String[] names;
		
		//Constructor
		addExistingItem(){
			try{
				//Mysql commands
				//Creating commands to access database
				Connection con= DriverManager.getConnection(url,usr,pass);
				//comment //System.out.println("Success");
				stmt = con.createStatement();
				String sql="SELECT id FROM inventory";
				ResultSet rs= stmt.executeQuery(sql);
				int size=0;
				if(rs!=null){
					rs.last();
					size=rs.getRow();
				}
				int i=0;
				names= new String[size];
				stmt = con.createStatement();
				String sql1="SELECT name FROM inventory";
				ResultSet rs1=stmt.executeQuery(sql1);
				while(rs1.next()){
					names[i]=rs1.getString("name");
					//comment //System.out.println(names[i]);
					i++;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			JButton update = new JButton("Update Quantity");
			JTextField quantity= new JTextField();
			JLabel quantityL= new JLabel("Enter Quantity");
			quantityL.setFont(new Font("Times New Roman", Font.BOLD, 24));
			quantity.setBounds(350,100,200,80);
			quantityL.setBounds(100,100,200,80);
			//addExistingItem aet = new addExistingItem();
			
			JComboBox<String> nameList=new JComboBox<>(names);
			JPanel namePanel = new JPanel();
			namePanel.add(nameList);
			update.setBounds(200,250,200,100);
			//nameList.setSelectedIndex(0);
			//nameList.addItemListener(aet);
			JButton exit= new JButton("exit");
			exit.setBounds(250,380,100,50);
			exit.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ee){
					//comment //System.out.println("Clicked");
					addE.setVisible(false);
				}
			});
			
			//adding components to the Frame
			addE.add(exit);	
			addE.add(update);
			addE.add(quantity);
			addE.add(quantityL);
			addE.add(namePanel);
			addE.setSize(600,500);
			addE.setLocation(600,350);
			addE.setVisible(true);
			addE.setLayout(null);
			//comment ////comment //System.out.println(nameList.getSelectedItem());
			
			//updates the inventory
			update.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					//Comment ////comment //System.out.println("Clicked");
					String find=(String) nameList.getSelectedItem();
					int quantityToAdd=Integer.parseInt(quantity.getText());
					int updateValue=0;
					try{
						//Mysql commands
						//Creating commands to access database
						Connection con= DriverManager.getConnection(url,usr,pass);
						//comment //System.out.println("Success");
						stmt = con.createStatement();
						
						//Finding list of all items in the inventory
						String sql1="SELECT * FROM inventory";
						ResultSet rs= stmt.executeQuery(sql1);
						while(rs.next()){
							if(find.equals(rs.getString("name"))){
								updateValue=rs.getInt("quantity");
								//comment //System.out.println("current value of "+find+" is "+updateValue);
								break;
							}
						}
						//calculates updated value
						updateValue+=quantityToAdd;
						//makes change to database
						String sql="UPDATE inventory SET quantity = '"+updateValue+"' WHERE name = '"+find+"'";
						stmt.executeUpdate(sql);
						addE.setVisible(false);
						con.close();
					}
					catch(Exception eee){
						eee.printStackTrace();
					}
				}
			});
			
		}
	}
}
