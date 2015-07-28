package com.boxtoeat.services;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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
        @FormDataParam("city") String city , @FormDataParam("state") String state,@FormDataParam("zip") long zip,@FormDataParam("telephone") long telephone   	
    		) {
			System.out.println("Hellow");
			// save it
			writeToDB(uploadedInputStream, fileDetail,username,password,firstName,lastName,addressline1,addressline2,city,zip,state,email,telephone);
			addPhoneverification(username, telephone);
			addEmailverification(username,email);

		return Response.status(200).entity("registration successful").build();
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
		try {
			DatabaseConnector dbConn=new DatabaseConnector();
			Connection conn=dbConn.getConnection();
		int random=generateRandomNumber(100000, 999998);
		String stmt="INSERT INTO TELEPHONE_VERIFY_LIST (USERNAME, COUNTRY, TELEPHONE,VERIFICATION_CODE,CREATE_DTTM) VALUES (?,?,?,?,?)";
		
		PreparedStatement ps=conn.prepareStatement(stmt);
		ps.setString(1, username);
		ps.setString(2, "USA");
		ps.setLong(3, telephone);
		ps.setLong(4, random);
		ps.setDate(5, new Date(System.currentTimeMillis()));
		int count = ps.executeUpdate();
		ps.close();
		
		 TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
		
		    List<NameValuePair> params = new ArrayList<NameValuePair>();
		    params.add(new BasicNameValuePair("Body", "Aloha !! Your box to eat verification code is "+random+".This token will expire after 24 hours."));
		    params.add(new BasicNameValuePair("To", "+19253535182"));
		    params.add(new BasicNameValuePair("From", "+14695182382"));
		  
		     
		    MessageFactory messageFactory = client.getAccount().getMessageFactory();
		    Message message = messageFactory.create(params);
		    System.out.println(message.getSid());
		
		
		
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		
		
		
		
	}
	
	
	
	public  int generateRandomNumber(int min, int max) {

	   
	    Random rand = new Random();

	    
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	
}
