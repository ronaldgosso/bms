package application;
import java.sql.*;
public class Database {


	public static Connection Connector(){
		
		try{
			//String db = "jdbc:mysql://localhost:3306/log";
			//String newDb = "jdbc:ucanaccess://C:\\\\Users\\\\G19 JEE\\\\Documents\\\\log.accdb;ignoreCase=true;memory=true";
			String selfDb = "jdbc:ucanaccess://resources\\db\\log.accdb;ignoreCase=true;memory=true";
			//String user = "root";
			//String pw = "ronny1805";
			
		//Class.forName("com.mysql.jdbc.Driver"); //depricated since Java 6.0
			
			Connection con = DriverManager.getConnection(selfDb);
			//System.out.println("Connected");
			return con;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	
		
	}
	

}
