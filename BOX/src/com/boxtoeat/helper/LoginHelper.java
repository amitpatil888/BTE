package com.boxtoeat.helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import javax.xml.bind.DatatypeConverter;

import com.boxtoeat.jdbc.DatabaseConnector;

public class LoginHelper {

	private final static int ITERATION_NUMBER = 888;
	
	

	public boolean saveLoginInformation(String login, String password) {

		DatabaseConnector dbConn = new DatabaseConnector();
		Connection conn = dbConn.getConnection();

		PreparedStatement ps = null;
		try {
			if (login != null && password != null && login.length() <= 100) {
				// Uses a secure Random not a simple Random
				SecureRandom random = new SecureRandom("BTE54211".getBytes());
				// Salt generation 64 bits long
				byte[] bSalt = new byte[8];
				random.nextBytes(bSalt);
				// Digest computation
				byte[] bDigest = getHash(ITERATION_NUMBER, password, bSalt);
				String sDigest = DatatypeConverter.printBase64Binary(bDigest);
				String sSalt = DatatypeConverter.printBase64Binary(bSalt);

				ps = conn
						.prepareStatement("INSERT INTO LOGIN_DETAILS (USERNAME, PASSWORD, SALT) VALUES (?,?,?)");
				ps.setString(1, login);
				ps.setString(2, sDigest);
				ps.setString(3, sSalt);
				ps.executeUpdate();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		close(ps);
		}
		return false;
	}

	
	
	
	   /**
	    * Authenticates the user with a given login and password
	    * If password and/or login is null then always returns false.
	    * If the user does not exist in the database returns false.
	    * @param con Connection An open connection to a databse
	    * @param login String The login of the user
	    * @param password String The password of the user
	    * @return boolean Returns true if the user is authenticated, false otherwise
	    * @throws SQLException If the database is inconsistent or unavailable (
	    *           (Two users with the same login, salt or digested password altered etc.)
	    * @throws NoSuchAlgorithmException If the algorithm SHA-1 is not supported by the JVM
	    */
	   public boolean authenticate(String login, String password)
	          {
		   try
		   {
			DatabaseConnector dbConn = new DatabaseConnector();
			Connection conn = dbConn.getConnection();
	       boolean authenticated=false;
	       PreparedStatement ps = null;
	       ResultSet rs = null;
	       
	           boolean userExist = true;
	           // INPUT VALIDATION
	           if (login==null||password==null){
	               // TIME RESISTANT ATTACK
	               // Computation time is equal to the time needed by a legitimate user
	               userExist = false;
	               login="";
	               password="";
	           }
	 
	           ps = conn.prepareStatement("SELECT PASSWORD, SALT FROM LOGIN_DETAILS WHERE USERNAME = ?");
	           ps.setString(1, login);
	           rs = ps.executeQuery();
	           String digest, salt;
	           if (rs.next()) {
	               digest = rs.getString("PASSWORD");
	               salt = rs.getString("SALT");
	               // DATABASE VALIDATION
	               if (digest == null || salt == null) {
	                   throw new SQLException("Database inconsistant Salt or Digested Password altered");
	               }
	               if (rs.next()) { // Should not append, because login is the primary key
	                   throw new SQLException("Database inconsistent two CREDENTIALS with the same LOGIN");
	               }
	           } else { // TIME RESISTANT ATTACK (Even if the user does not exist the
	               // Computation time is equal to the time needed for a legitimate user
	               digest = "000000000000000000000000000=";
	               salt = "00000000000=";
	               userExist = false;
	           }
	 
	           byte[] bDigest = DatatypeConverter.parseBase64Binary(digest);
	           byte[] bSalt = DatatypeConverter.parseBase64Binary(salt);
	 
	           // Compute the new DIGEST
	           byte[] proposedDigest = getHash(ITERATION_NUMBER, password, bSalt);
	 
	           return Arrays.equals(proposedDigest, bDigest) && userExist;
	       
		   }
		   catch(Exception e)
		   {
			   e.printStackTrace();
		   }
		   return false;
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

	/**
	 * Closes the current statement
	 * 
	 * @param ps
	 *            Statement
	 */
	public void close(Statement ps) {
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException ignore) {
			}
		}
	}
	
	   /**
	    * Closes the current resultset
	    * @param ps Statement
	    */
	   public void close(ResultSet rs) {
	       if (rs!=null){
	           try {
	               rs.close();
	           } catch (SQLException ignore) {
	           }
	       }
	   }
	 

}
