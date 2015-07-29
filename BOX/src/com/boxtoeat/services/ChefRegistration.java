package com.boxtoeat.services;
import java.io.InputStream;
import java.net.URI;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.boxtoeat.helper.PhoneVerificationCodeHelper;
import com.boxtoeat.jdbc.DatabaseConnector;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;


@Path("/chefRegister")
public class ChefRegistration {
	  // Find your Account Sid and Token at twilio.com/user/account
	  public static final String ACCOUNT_SID = "AC730d289b9395275587fb6e302f6e5fcf";
	  public static final String AUTH_TOKEN = "5a90f2bbeb958ccf0edc5c715875eee5";
	@POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
	 public Response uploadFile(
        @FormDataParam("avataar") InputStream uploadedInputStream,
        @FormDataParam("avataar") FormDataContentDisposition fileDetail, @FormDataParam("email") String email, @FormDataParam("username") String username,
        @FormDataParam("password") String password,@FormDataParam("firstName") String firstName,
        @FormDataParam("lastName") String lastName , @FormDataParam("addressline1") String addressline1,@FormDataParam("addressline2") String addressline2,
        @FormDataParam("city") String city , @FormDataParam("state") String state,@FormDataParam("zip") long zip,@FormDataParam("phone") long telephone   	
    		) {
			System.out.println("Hellow"+telephone);
			// save it
			writeToDB(uploadedInputStream, fileDetail,username,password,firstName,lastName,addressline1,addressline2,city,zip,state,email,telephone);
			addPhoneverification(username, telephone);
			addEmailverification(username,email);
			try
			{
				String urlString="http://localhost:8080/BOX/verifyPhone.html?tel="+Long.toString(telephone);
				System.out.println(urlString);
            return Response.seeOther(new URI(urlString)).build();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
            return Response.status(200).entity("registration failure").build();
    }
	
	
	private void writeToDB(InputStream uploadedInputStream,FormDataContentDisposition fileDetail,
			String username,String password,String firstName,String lastName,String addressline1,String addressline2,String city,long zip,String state,String email,long telephone) {

		try {
			
			DatabaseConnector dbConn=new DatabaseConnector();
			Connection conn=dbConn.getConnection();
			
			String stmt="INSERT INTO VITALS (USERNAME, FIRST_NAME, LAST_NAME,EMAIL,TELEPHONE,ADDRESS_LINE1,ADDRESS_LINE2,CITY,ZIP,COUNTRY,CREATE_DTTM,AVATAAR) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
			
			PreparedStatement ps=conn.prepareStatement(stmt);
			ps.setString(1, username);
			ps.setString(2, firstName);
			ps.setString(3, lastName);
			ps.setString(4, email);
			ps.setLong(5, telephone);
			ps.setString(6, addressline1);
			ps.setString(7, addressline2);
			ps.setString(8, city);
			ps.setLong(9, zip);
			ps.setString(10, "USA");
			ps.setDate(11, new Date(System.currentTimeMillis()));
			ps.setBinaryStream(12, uploadedInputStream);
			
			int count = ps.executeUpdate();
			ps.close();
			
			
			
		} catch (Exception e) {

			e.printStackTrace();
		}

	}
	
	private void addEmailverification(String username,String telephone)
	{
		
	}
	
	
	
	private void addPhoneverification(String username,long telephone)
	{
		new PhoneVerificationCodeHelper().
	}
	
	
	
	public  int generateRandomNumber(int min, int max) {

	   
	    Random rand = new Random();

	    
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	
}
