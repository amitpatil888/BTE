package com.boxtoeat.helper;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import javax.xml.bind.DatatypeConverter;

import com.boxtoeat.jdbc.DatabaseConnector;

public class EmailVerificationCodeHelper {

	private final static int ITERATION_NUMBER = 878;
	
	public boolean saveEmailVerifactionCode(String username,String email) {
//deletes an email record if it already exists
		deleteEmailRecord(email);
		
		//create a new email record
		DatabaseConnector dbConn = new DatabaseConnector();
		Connection conn = dbConn.getConnection();

		PreparedStatement ps = null;
		try {
			if (email != null &&  email.length() <= 100) {
				// Uses a secure Random not a simple Random
				SecureRandom random = new SecureRandom("BTE5211".getBytes());
				// Salt generation 64 bits long
				byte[] bSalt = new byte[8];
				random.nextBytes(bSalt);
				// Digest computation
				byte[] bDigest = getHash(ITERATION_NUMBER, email, bSalt);
				String sDigest = DatatypeConverter.printBase64Binary(bDigest);
				

				ps = conn
						.prepareStatement("INSERT INTO LOGIN_DETAILS (USERNAME, EMAIL, RANDOM_CODE) VALUES (?,?,?)");
				ps.setString(1, username);
				ps.setString(2, email);
				ps.setString(3, sDigest);
				ps.executeUpdate();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		
		}
		return false;
	}
	
	
	public boolean checkEmailVerifactionCode(String email,String code) {
	
		
		return false;
	}
	
	
	
	
	
	/**
	 *this method deletes email verification code if it exists 
	 */
	private void deleteEmailRecord(String email)
	{
		try
		{
		DatabaseConnector dbConn=new DatabaseConnector();
		Connection conn=dbConn.getConnection();
		String stmtStr="delete from EMAIL_VERIFY_LIST where EMAIL='"+email+"'";
		Statement stmt=conn.createStatement();
		stmt.execute(stmtStr);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * From a password, a number of iterations and a salt, returns the
	 * corresponding digest
	 * 
	 * @param iterationNb
	 *            int The number of iterations of the algorithm
	 * @param password
	 *            String The password to encrypt
	 * @param salt
	 *            byte[] The salt
	 * @return byte[] The digested password
	 */
	public byte[] getHash(int iterationNb, String password, byte[] salt) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.reset();
			digest.update(salt);
			byte[] input = digest.digest(password.getBytes("UTF-8"));
			for (int i = 0; i < iterationNb; i++) {
				digest.reset();
				input = digest.digest(input);
			}
			return input;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
