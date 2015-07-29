package com.boxtoeat.helper;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.boxtoeat.jdbc.DatabaseConnector;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;

/**
 * @author Amit
 * 
 * This class helps with the maintainence of phone verification codes
 * 
 * TODO:We need to add code for someone manipulating to send multiple codes to the same phone
 *
 */
public class PhoneVerificationCodeHelper {
	 public static final String ACCOUNT_SID = "AC730d289b9395275587fb6e302f6e5fcf";
	  public static final String AUTH_TOKEN = "5a90f2bbeb958ccf0edc5c715875eee5";
	//TODO:need to add code to avoid numerous attempts
	  
	
	/**
	 * @param username
	 * @param telephone
	 * This method generates a verification code and inserts it into telephone verify list table
	 * 
	 */
	public void createVerificationCode(String username,long telephone)
	{
		try {
			
			//delete a phone verification record if it already exists
			
			deletePhoneRecord(telephone);
			//create a new phone verification record
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
		
		//send out verification code to the customer via Twilio
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
	
	/**
	 *this method deletes phone verification code if it exists 
	 */
	private void deletePhoneRecord(long telephone)
	{
		try
		{
		DatabaseConnector dbConn=new DatabaseConnector();
		Connection conn=dbConn.getConnection();
		String stmtStr="delete from TELEPHONE_VERIFY_LIST where TELEPHONE='"+telephone+"'";
		Statement stmt=conn.createStatement();
		stmt.execute(stmtStr);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	/**
	 * @param min
	 * @param max
	 * @return
	 * this method generates random number
	 */
	private  int generateRandomNumber(int min, int max) {

		   
	    Random rand = new Random();

	    
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	}
