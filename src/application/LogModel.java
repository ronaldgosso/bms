package application;

import java.sql.*;

public class LogModel {
	//This will check whether the connection is successfully or not.....
	
  Connection connection;
	
	public LogModel(){
		
		connection =Database.Connector();//reference to the previous class <object> in static way
		if(connection == null){//resolving null in Database class <cheeck if the connection is null or else>
			System.exit(1); 
		}
	}
	public boolean isDbConnected(){
		try{
			return !connection.isClosed();// connection is not closed
		}catch(Exception e){
			e.printStackTrace();//otherwise
			return false;
		}
		}
	public boolean isLogin(String user,String pass) throws Exception{
		PreparedStatement statement = null;
		ResultSet set = null;
		String query = " select * from supervisor where username = ? and password = ?";
		try{
			statement = connection.prepareStatement(query);
			statement.setString(1,user);
			statement.setString(2,pass);
			
			set = statement.executeQuery();
			if(set.next()){
				return true;
			}else{
				return false;
			}
					
			
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			statement.close();

		}
	}
	}

