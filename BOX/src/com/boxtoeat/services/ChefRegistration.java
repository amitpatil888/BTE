package com.boxtoeat.services;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.boxtoeat.jdbc.DatabaseConnector;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;


@Path("/chefRegister")
public class ChefRegistration {

	@POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
        @FormDataParam("avataar") InputStream uploadedInputStream,
        @FormDataParam("avataar") FormDataContentDisposition fileDetail, @FormDataParam("username") String username,
        @FormDataParam("name") String name , @FormDataParam("addressline1") String addressline1,@FormDataParam("addressline2") String addressline2,
        @FormDataParam("city") String city , @FormDataParam("state") String state,@FormDataParam("zip") long zip,@FormDataParam("email") String email,@FormDataParam("telephone") long telephone   	
    		) {
			System.out.println("Hellow");
			// save it
			writeToDB(uploadedInputStream, fileDetail,username,name,addressline1,addressline2,city,zip,state,email,telephone);
		

		return Response.status(200).entity("registration successful").build();
    }
	
	
	private void writeToDB(InputStream uploadedInputStream,FormDataContentDisposition fileDetail,
			String username,String name,String addressline1,String addressline2,String city,long zip,String state,String email,long telephone) {

		try {
			
			DatabaseConnector dbConn=new DatabaseConnector();
			Connection conn=dbConn.getConnection();
			
			String stmt="INSERT INTO CHEF_PROFILE (USERNAME, NAME, ADDRESS_LINE_1,ADDRESS_LINE_2,CITY,ZIP,EMAIL,TELEPHONE,VERIFIED_FLG,PROFILE_PIC) VALUES (?,?,?,?,?,?,?,?,?,?)";
			
			PreparedStatement ps=conn.prepareStatement(stmt);
			ps.setString(1, username);
			ps.setString(2, name);
			ps.setString(3, addressline1);
			ps.setString(4, addressline2);
			ps.setString(5, city);
			ps.setLong(6, zip);
			ps.setString(7, email);
			ps.setLong(8, telephone);
			ps.setString(9, "N");
			ps.setBinaryStream(10, uploadedInputStream);
			
			int count = ps.executeUpdate();
			ps.close();
			
		} catch (Exception e) {

			e.printStackTrace();
		}

	}
}
