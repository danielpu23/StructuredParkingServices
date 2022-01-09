import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Scanner;


public class StructuredParkingServices {
	private static Connection connection = null;
	private static PreparedStatement preparedStatement = null;

	/**
	 * @param argv
	 * @throws SQLException
	 */
	public static void main(String[] argv) throws SQLException {
		// For testing purposes the password to access employee functions is SPSpassword
		// The password for Admin Functions is AdminOnly
		// The password for DBA Functions is DBAOnly
		
		connection = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/StructuredParkingServices?serverTimezone=UTC", "root", "password");  

		Scanner myObj = new Scanner(System.in); // Create a Scanner object
		String input;
		ResultSet rs;
		int vID = 0;
		String name;
		boolean check = true; // outer loop to check if we should keep running general functions
		boolean check2 = true; // inner loop to check if we should keep running specific admin functions
		
		System.out.println("Welcome to Structured Parking Services!\n");
		
		System.out.println("Enter password to access employee functions: ");
		
		input = myObj.nextLine();
		while(check) {
			if(input.contentEquals("SPSpassword")) {  // employee has to enter correct password to access functions
				break;
			}
			System.out.println("Incorrect Password! ");
			System.out.println("Enter password to access employee functions: ");
			input = myObj.nextLine();
		}	

		while(check) {
			System.out.println("\nEnter a number to select an option from below"); // employee chooses an option
			System.out.println("Register Client: 1 \nRegister Car: 2 \nDelete Car: 3 \nPark Car: 4 \nUnpark Car: 5 "
					+ "\nCheck Number of Certain Make: 6 \nAdmin Functions (Requires Admin Password): 7 \nDBA Functions (Requires DBA Password): 8 \nExit: 9");
			    input = myObj.nextLine(); // Read
			    
				switch (input) {
			
					case "1": // Register Client
						System.out.println("Enter name: ");
						name = myObj.nextLine();
			
						System.out.println("Registration successful, client has been assigned id " + registerClient(name));
						break;
			
					case "2": // Register Car
						System.out.println("Enter cID: ");
						String cID = myObj.nextLine();
						System.out.println("Enter license plate number: ");
						String licensePlate = myObj.nextLine();
						System.out.println("Enter make: ");
						String make = myObj.nextLine();
						System.out.println("Enter model: ");
						String model = myObj.nextLine();
			
						registerCars(cID, licensePlate, make, model);
			
						break;
			
					case "3": // Delete Car
						System.out.println("Enter license plate number: ");
						String licensePlate2 = myObj.nextLine();
						deleteCar(licensePlate2);
						break;
			
					case "4": // Park Car
						System.out.println("Enter cID: ");
						String cID2 = myObj.nextLine();
						System.out.println("Enter license plate number: ");
						String licensePlate3 = myObj.nextLine();
						parkCar(cID2, licensePlate3);
						break;
			
					case "5": // Unpark Car
						System.out.println("Enter cID: ");
						String cID3 = myObj.nextLine();
						System.out.println("Enter license plate number: ");
						String licensePlate4 = myObj.nextLine();
						unParkCar(cID3, licensePlate4);
						break;
					
					case "6": // Number of Certain Make 
						System.out.println("Enter make: "); make = myObj.nextLine(); 
						if(makeCount(make) > 0) System.out.println("Number of parked cars of that make: " + makeCount(make)); 
						else System.out.println("No such cars of that make parked!"); 
						break;
						
					case "7": // Admin Function
						System.out.println("Enter password: "); 
						String password = myObj.nextLine();
						if(password.contentEquals("AdminOnly")) { // Admin has to enter correct password to access functions
							System.out.println("\nSuccessfully signed in as admin. ");
							while(check2) {
								System.out.println("\nSelect an option from below.");
								System.out.println("Total Revenue: 1 \nBlacklist Client: 2 \nClear Transaction History: 3 \nAverage Park Time: 4"
										+ "\nClients Who Never Parked: 5 \nClients With Multiple Cars: 6 \nRemove Valet: 7 \nAdd Valet: 8 "
										+ "\nLongest Time Parked: 9 \nArchive Past Transactions: 10 \nCheck Names of All Clients and Valet Employees: 11 \nQuit Admin: 12");
								input = myObj.nextLine(); // Read
								switch (input) {
								
									case "1":  // Total Revenue
										getTotalRevenue(); 
										break;
										
									case "2":  // Blacklist Client
										System.out.println("Enter cID: "); 
										String cID4 = myObj.nextLine();
										blacklistClient(cID4); 
										break;
										
									case "3":  // Clear Transaction History
										clearTransactions(); 
										break;
										
									case "4":  // Check Average Time Parked
										checkAverageTimeParked(); 
										break;
										
									case "5": // Clients Who Have Never Parked
										System.out.println("Clients that have never used parking service:"); 
										ghostClients(); 
										break;
										
									case "6": // Clients With Multiple Cars
										System.out.println("Multi-car Clients:"); 
										multiCarClients();
										break;
									
									case "7": // Remove Valet
										System.out.println("Enter vID: "); 
										vID = myObj.nextInt();
										myObj.nextLine(); 
										try 
										{ 
											deleteValet(vID); 
										} 
										catch (SQLException e1) 
										{
										  System.out.println("No such valet exists!"); 
										} 
										break;
										
									case "8": // Add Valet
										System.out.println("Enter vID: "); 
										vID = myObj.nextInt();
										myObj.nextLine();
										System.out.println("Enter name: "); 
										String name1 = myObj.nextLine(); 
										try 
										{ 
											addValets(vID, name1); 
										} 
										catch (SQLException e1) 
										{
											e1.printStackTrace(); 
										} 
										break;
									
									case "9": // Maximum Time Parked
										maxParkTime();
										break;
									
									case "10": // Archive
							            archive();
							            break;
							
									case "11": // Find All Names of Clients and Valets
										findAllNames(); 
										break;
										
									case "12": // Exit
										check2 = false;
										System.out.println("Exited out of Admin functions.");
										break;
										
									default:
										System.out.println("Option not recognized!");
										break;
								}
							}
							check2 = true;
						}
						else System.out.println("Incorrect password!");
						break;
						
					case "8":
						System.out.println("Enter password: "); // DBA has to enter correct password to access functions
						password = myObj.nextLine();
						if(password.contentEquals("DBAOnly")) {
							System.out.println("\nSuccessfully signed in as Database Admin.");
							while(check2) {
								System.out.println("\nSelect an option from below.");
								System.out.println("Add Parking Space: 1 \nRemove Parking Space: 2 \nExit DBA Functions: 3");
								input = myObj.nextLine(); // Read
								switch (input) {
								
									case "1": // Add Parking Space
										addParkingSpot(); 
										break;
										
									case "2":  // Remove Parking Space
										removeParkingSpot();
										break;
										
									case "3": // Exit
										check2 = false;
										System.out.println("Exited out of DBA functions.");
										break;
										
									default:
										System.out.println("Option not recognized!");
										break;
								}
							}
							check2 = true;
						}
						else System.out.println("Incorrect password!");
						break;	
						
					case "9": // Exit
						check = false;
						break;
						
					case "18": // This function is hidden in the application and is just meant to check all of the tables easily
								
						preparedStatement = connection.prepareStatement(" ");
						System.out.println("Client Table: ");
						rs = preparedStatement.executeQuery("Select * from Clients");
						printResultSetFromClient(rs);
						System.out.println("Cars Table: ");
						rs = preparedStatement.executeQuery("Select * from Cars");
						printResultSetFromCars(rs);
						System.out.println("Valets Table: ");
						rs = preparedStatement.executeQuery("Select * from Valets");
						printResultSetFromValets(rs);
						System.out.println("ParkingSpots Table: ");
						rs = preparedStatement.executeQuery("Select * from ParkingSpots");
						printResultSetFromParkingSpots(rs);
						System.out.println("Transactions Table: ");
						rs = preparedStatement.executeQuery("Select * from Transactions");
						printResultSetFromTransactions(rs);
						break;
						
					default:
						System.out.println("Option not recognized!");
						break;
					}
				}
		    System.out.println("Application Exited Successfully.");
		    myObj.close();
		}

	// print functions for testing purposes
	public static void printResultSetFromClient(ResultSet rs) throws SQLException {
		while (rs.next()) {
			int cID = rs.getInt("cID");
			String name = rs.getString("name");
			System.out.println("cID: " + cID + " name: " + name);
		}
	}

	public static void printResultSetFromCars(ResultSet rs) throws SQLException {
		while (rs.next()) {
			int cID = rs.getInt("cID");
			String licensePlate = rs.getString("licensePlate");
			String make = rs.getString("make");
			String model = rs.getString("model");
			System.out
					.println("cID: " + cID + " licensePlate: " + licensePlate + " make: " + make + " model: " + model);
		}
	}

	public static void printResultSetFromValets(ResultSet rs) throws SQLException {
		while (rs.next()) {
			int vID = rs.getInt("vID");
			String name = rs.getString("name");
			System.out.println("vID: " + vID + " name: " + name);
		}
	}

	public static void printResultSetFromParkingSpots(ResultSet rs) throws SQLException {
		while (rs.next()) {
			int sID = rs.getInt("sID");
			int vID = rs.getInt("vID");
			String licensePlate = rs.getString("licensePlate");
			System.out.println("sID: " + sID + " vID:  " + vID + " licensePlate: " + licensePlate);
		}
	}

	public static void printResultSetFromTransactions(ResultSet rs) throws SQLException {
		while (rs.next()) {
			int tID = rs.getInt("tID");
			int cID = rs.getInt("cID");
			String licensePlate = rs.getString("licensePlate");
			int bill = rs.getInt("bill");
			
			String startDate;
			if(rs.getTimestamp("startDate") == null) {
				startDate = "null";
			}
			else {
				startDate = rs.getTimestamp("startDate").toString();
			}
			
			String datePaid;
			if( rs.getTimestamp("datePaid") == null) {
				datePaid = "null";
			}
			else {
				datePaid = rs.getTimestamp("datePaid").toString();	
			}
			System.out.println("tID: " + tID + " cID:  " + cID + " licensePlate: " + licensePlate + " bill: " + bill
					+ " startDate: " + startDate + " datePaid: " + datePaid);
		}
	}
	
	
    // Actual functions for the program start here
	// 1
	public static int registerClient(String name) throws SQLException {
		String sql = null;

		sql = "INSERT INTO clients " + "(cid, name) VALUES" + "(null, ?)"; // insert into Clients table
		preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, name);

		preparedStatement.executeUpdate();

		ResultSet rs = preparedStatement.executeQuery("SELECT cID from clients"); // return the client's cID
		int cID = 0;
		while (rs.next()) {
			cID = rs.getInt("cID");
		}
		return cID;

	}

	// 2
	public static void registerCars(String cID, String licensePlate, String make, String model) throws SQLException {
		
		String sql = "Select * from Clients where cID = " + cID; // try to find if a client with that id exists
		preparedStatement = connection.prepareStatement(sql);
		ResultSet rs = preparedStatement.executeQuery(sql);
		if(!rs.next()) {
			System.out.println("Client not registered or has been blacklisted from the service");
			return;
		}
		sql = "INSERT INTO Cars " + "(cID, licensePlate, make, model) VALUES (" + cID + ", '" + licensePlate // insert corresponding car info with client as owner
				+ "', '" + make + "', '" + model + "')";
		preparedStatement = connection.prepareStatement(sql);
		preparedStatement.executeUpdate();
		System.out.println("Car has been successfully registered");
	}

	// 3
	public static void deleteCar(String licensePlate) throws SQLException {
		String sql = "delete from Cars where licensePlate = '" + licensePlate + "'"; // delete car with corresponding licensePlate
		preparedStatement = connection.prepareStatement(sql);
		preparedStatement.executeUpdate();
		System.out.println("Car has been deleted successfully");
	}

	// 4
	public static void parkCar(String cID, String licensePlate) throws SQLException {
		String sql = "select * from Cars where cID = " + cID + " and licensePlate = '" + licensePlate + "'"; // see if there is a car with specified license plate that belongs to client
		preparedStatement = connection.prepareStatement(sql);
		ResultSet rs = preparedStatement.executeQuery(sql);
		if(!rs.next()) {
			System.out.println("No car with specified license plate found belonging to specified user");
			return;
		}
		
	    rs = preparedStatement.executeQuery("select sID from ParkingSpots where licensePlate is null"); // get first parking spot that is empty
		int sID;
		if (rs.next()) {
			sID = rs.getInt("sID");
		} 
		else {
			System.out.println("Can't add car since all parking spots are full"); // if all parking spots full then don't put car in
			return;
		}

		rs = preparedStatement.executeQuery("select vID from Valets where vID not in"   // select valet with least cars under their supervision
				+ " (select vID from ParkingSpots where vID is not null group by vID having count(*) > "
				+ "any ( select count(*) from ParkingSpots where vID is not null  group by vID))");

		int vID;
		if (rs.next()) {
			vID = rs.getInt("vID");
		} else {
			System.out.println("Can't add car since there are no available valet employees");
			return;
		}

		String date = getCurrentDate(); // get the current datetime

		sql = "update ParkingSpots set vID = " + vID + ", licensePlate = '" + licensePlate + "' where sID = " + sID; // put car in parking spot
		preparedStatement = connection.prepareStatement(sql);
		preparedStatement.executeUpdate();

		sql = "insert into Transactions (tID, cID, licensePlate, bill, startDate, datePaid) values (null, " + cID  // start a transaction with corresponding information
				+ ", '" + licensePlate + "', null,'" + date + "', null)";
		// System.out.println(sql);
		preparedStatement = connection.prepareStatement(sql);
		preparedStatement.executeUpdate();
		
		sql = "select * from Transactions";
		rs = preparedStatement.executeQuery(sql);  
		while(rs.next()) {
			if(rs.getTimestamp("startDate") == null) {    // startDate is null if Transaction date fails insert trigger (current time < 6 AM and greater than 11 PM)
				System.out.println("Cannot park a car at this time!");
				break;
			}
		}
		sql = "delete from Transactions where startDate is null";  // remove all Transactions that failed insert trigger
		preparedStatement.executeUpdate(sql);
	}

	// 5
	public static void unParkCar(String cID, String licensePlate) throws SQLException {
		preparedStatement = connection.prepareStatement(" ");
        String sql = "select startDate from Transactions where cID =" + cID
				+ " and licensePlate = '" + licensePlate + "' and datePaid is null";  // check if there's an unfinished transaction with the corresponding car and owner
		ResultSet rs = preparedStatement.executeQuery(sql); 
		String startDate;
		if (rs.next()) {
			startDate = rs.getTimestamp("startDate").toString(); 
		} else {      
			System.out.println("No such parked car belonging to the user found");
			return;
		}

		String endDate = getCurrentDate(); // get the current datetime

		int bill = calculateBill(startDate, endDate); // calculate bill based on the two dates
		sql = "update Transactions set bill = " + bill + ", datePaid = '" + endDate + "' where licensePlate = '" // update appropriate Transaction
				+ licensePlate + "' and cID = " + cID + " and datePaid is null"; 
		preparedStatement = connection.prepareStatement(sql);
		preparedStatement.executeUpdate();
		
		sql = "select * from Transactions where licensePlate = '" + licensePlate + "' and datePaid is null"; // if Transaction rejected by update trigger (current time < 6 AM and greater than 11 PM) then datePaid is reset to null
		rs = preparedStatement.executeQuery(sql);
		if(rs.next()) {
			System.out.println("Cannot unpark a car at this time!");
			return;
		}
		sql = "update ParkingSpots set vID = null, licensePlate = null where licensePlate = '" + licensePlate // remove car from parking spot 
				+ "'";
		preparedStatement = connection.prepareStatement(sql);
		preparedStatement.executeUpdate();
	}

	// 6
	public static void getTotalRevenue() throws SQLException {
		String sql = "select sum(bill) from Transactions";
		preparedStatement = connection.prepareStatement(sql);
		ResultSet rs = preparedStatement.executeQuery(sql);
		if (rs.next()) {
			System.out.println("Total money made by service is $" + rs.getInt("sum(bill)"));
		} else {
			System.out.println("No money was made by the service so far");
		}

	}

	// 7
	public static void addParkingSpot() throws SQLException {
		String sql = "insert into ParkingSpots (sID, vID, licensePlate) values (null, null, null)"; // add a parking spot, due to auto_increment sID will be set for us and the other nulls are to represent no car and no valet employee supervising car
		preparedStatement = connection.prepareStatement(sql);
		preparedStatement.executeUpdate();
		System.out.println("Space added successfully");
	}

	// 8
	public static void removeParkingSpot() throws SQLException {
		String sql = "delete from ParkingSpots where sID = (select * from (select max(PS2.sID) from " // delete the last parking spot, which is the parking spot with the largest sID
				+ "ParkingSpots PS2) max_sID)";
		preparedStatement = connection.prepareStatement(sql);
		preparedStatement.executeUpdate();
		System.out.println("Space deleted successfully");
	}

	// 9
	public static void blacklistClient(String cID) throws SQLException { // remove client
		String sql = "delete from Clients where cID = " + cID;
		preparedStatement = connection.prepareStatement(sql);
		preparedStatement.executeUpdate();
	    System.out.println("Client has been blacklisted from the service");
	}

	// 10
	public static void clearTransactions() throws SQLException {
		String sql = "delete from Transactions";
		preparedStatement = connection.prepareStatement(sql);
		preparedStatement.executeUpdate();
		System.out.println("Transaction history cleared");
	}

	// 11
	public static void checkAverageTimeParked() throws SQLException {
		String sql = "select avg(timestampdiff(second, startDate, datePaid)) as avg_time_parked from Transactions"; // get the time difference in seconds, which will be rounded to hours
		preparedStatement = connection.prepareStatement(sql);														// note, we chose seconds here because if we used hour in timestampdiff, it would round off by hour. using seconds, we can get a better estimate when we convert to hours
		ResultSet rs = preparedStatement.executeQuery(sql);
		if (rs.next()) {
			System.out.println("Average time clients spent parking is "
					+ Math.round((rs.getDouble("avg_time_parked")/3600) * Math.pow(10, 5))/Math.pow(10, 5) + " hours"); // round off to hours and 5 decimal places
		} else {
			System.out.println("No clients have used this service so far");
		}
	}
	
	
	 // 12
	  public static int makeCount(String make) throws SQLException {
		  String sql = " ";
		  preparedStatement = connection.prepareStatement(sql);
		  ResultSet rs = preparedStatement.executeQuery("SELECT count(*) AS make_count FROM ParkingSpots NATURAL JOIN Cars WHERE make = '" + make + "'"); // find how many of a certain make in the parking lot
		  
		  if(rs.next()) {
			  return rs.getInt("make_count");
		  }
		  return 0;
	  }
	  
	  
	  // 13
	  public static void ghostClients() throws SQLException {
		  String sql = " ";
		  preparedStatement = connection.prepareStatement(sql);
		  ResultSet rs = preparedStatement.executeQuery("SELECT distinct c1.cID, c1.name FROM Clients c1 LEFT OUTER JOIN TransArchive t1 ON c1.cID = t1.cID WHERE t1.tID IS NULL and c1.cID in (SELECT c2.cID FROM Clients c2 LEFT OUTER JOIN Transactions t2 ON c2.cID = "
		  		+ "t2.cID WHERE t2.tID IS NULL)"); // find clients who aren't in Transactions and TransArchive, meaning they never participated in a Transaction to use the service
		  printResultSetFromClient(rs);
	  }

	  // 14
	  public static void multiCarClients() throws SQLException {
		  String sql = " ";
		  preparedStatement = connection.prepareStatement(sql);
		  ResultSet rs = preparedStatement.executeQuery("SELECT cID, name FROM Clients NATURAL JOIN Cars GROUP BY cID HAVING count(*) >= 2"); // find clients with more than or equal to 2 cars
		  printResultSetFromClient(rs);
	  }
	  
	  
	  // 15
	  public static void deleteValet(int vID) throws SQLException {
		  String sql = "DELETE FROM Valets where vID = " + vID;
		  preparedStatement = connection.prepareStatement(sql);
		  preparedStatement.executeUpdate();
	  }
	  
	  // 16
	  public static void addValets(int vID, String name) throws SQLException {
	      String sql = "INSERT INTO Valets " + "(vId, name) VALUES (" +
	                      vID + ", '" + name +"')";
	      preparedStatement = connection.prepareStatement(sql);
	      preparedStatement.executeUpdate();    
	  }

	  
	  // 17
	  public static void maxParkTime() throws SQLException{
		  String sql = " ";
		  	preparedStatement = connection.prepareStatement(sql);
		  	ResultSet rs = preparedStatement.executeQuery("select max(timestampdiff( second, startDate, datePaid)) AS max_time_parked FROM Transactions;"); // find largest date difference
		  
		  	if (rs.next()) {
				System.out.println("Maximum time that a client spent parking is "
						+ Math.round((rs.getDouble("max_time_parked")/3600) * Math.pow(10, 5))/Math.pow(10, 5) + " hours"); // round off to hours and 5 decimal places
			} else {
				System.out.println("No clients have used this service so far");
			}

	  }  
	  
	  // 18
	  public static void findAllNames() throws SQLException {
		  String sql = "select name from Clients union all select name from Valets";  // find all names from Clients and Valets table
		  preparedStatement = connection.prepareStatement(sql);
		  	ResultSet rs = preparedStatement.executeQuery(sql);
		  	System.out.println("The names of all the clients and valet employees are:");
		  	while(rs.next()) {
		  		System.out.println(rs.getString("name"));
		  	}
	  }
	  
	  // 19
	  public static void archive() throws SQLException {
	        Date dt = new Date();
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // get current datetime
	        String currentDate = sdf.format(dt);

	        CallableStatement cs = null;
	        
	        cs = connection.prepareCall("{call ArchiveTrans('" + currentDate + "')}"); // archive all completed transactions before current datetime
	        cs.executeQuery();

	        System.out.println("Completed transactions older than " + currentDate + " have been archived!");
	    }

	public static int calculateBill(String beginDate, String endDate) throws SQLException {

		String sql = "select timestampdiff(hour, '"+beginDate + "', '"+ endDate+  "') as time_diff"; // calculate time difference in hours
		preparedStatement = connection.prepareStatement(sql);
		ResultSet rs = preparedStatement.executeQuery(sql);
		double timeDiff = 0;
		if(rs.next()) {
			timeDiff = rs.getDouble("time_diff");
		}
		
		return (int)(timeDiff + 1) * 20;  // find the cost by hour rounded up at a rate of 20 dollars an hour
	}
	
	
	public static String getCurrentDate() {
		  Date dt = new Date();    // gets the current datetime in SQL format
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  String currentDate = sdf.format(dt);
		  return currentDate;
	  }
}
