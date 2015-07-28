package com.boxtoeat.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
	
	private Connection conn=null;
	public Connection getConnection()
	{
		try{  
			//step1 load the driver class  
			Class.forName("oracle.jdbc.driver.OracleDriver");  
			  
			//step2 create  the connection object  
			conn=DriverManager.getConnection(  
			"jdbc:oracle:thin:@boxfood.cj5mrbp3omds.us-west-1.rds.amazonaws.com:1521:BOXFOOD","cisadm","Tiger100");  
			  
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	
	return conn;
	}
	private void closeConnection()
	{
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
