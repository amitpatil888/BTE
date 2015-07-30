package com.boxtoeat.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.boxtoeat.jdbc.DatabaseConnector;

@Path("/emailVerify")
public class EmailVerify {

	@GET
	public Response processRequest(@QueryParam("email") String email,
			@QueryParam("verificationCode") String code) {

		try {
			DatabaseConnector dbConn = new DatabaseConnector();
			Connection conn = dbConn.getConnection();

			String stmtStr = "select email from EMAIL_VERIFY_LIST where EMAIL='"
					+ email + "' and RANDOM_CODE='" + code + "'";
			Statement stmt = conn.createStatement();
			
			boolean bexecuted=stmt.execute(stmtStr);
			if(bexecuted)
			{
				ResultSet rs=stmt.getResultSet();
				while(rs.next())
				{
					String emailStr=rs.getString(1);
					
					DatabaseConnector dbConn2 = new DatabaseConnector();
					Connection conn2 = dbConn2.getConnection();

					String stmtStr2 = "update vitals set EMAIL_VERIFIED=? where EMAIL=?";
					PreparedStatement stmt2 = conn2.prepareStatement(stmtStr2);
					stmt2.setString(1, "Y");
					stmt2.setString(2, emailStr);
					int i=stmt2.executeUpdate();
				}
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
