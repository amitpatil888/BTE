package com.boxtoeat.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.boxtoeat.helper.PhoneVerificationCodeHelper;
import com.boxtoeat.jdbc.DatabaseConnector;
import com.sun.jersey.multipart.FormDataParam;

@Path("/phoneVerify")
public class PhoneVerify {
	@POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
	 public Response uploadFile(@FormDataParam("telephone") String telephone,
			 @FormDataParam("actionType") String actionType,@FormDataParam("code1") String code1,
			 @FormDataParam("code2") String code2,@FormDataParam("code3") String code3,
			 @FormDataParam("code4") String code4,@FormDataParam("code5") String code5,
			 @FormDataParam("code6") String code6
			 )
{
		if(actionType.equalsIgnoreCase("resend"))
		{
			new PhoneVerificationCodeHelper().createVerificationCode("", Long.parseLong(telephone));
		}
		else
		if(actionType.equalsIgnoreCase("verify"))
		{
			try
			{
			String code=code1+code2+code3+code4+code5+code6;
			DatabaseConnector dbConn=new DatabaseConnector();
			Connection conn=dbConn.getConnection();
			
			String stmtStr="select username from TELEPHONE_VERIFY_LIST where TELEPHONE='"+telephone+"' and VERIFICATION_CODE='"+code+"'";
			Statement stmt=conn.createStatement();
			boolean bexecuted=stmt.execute(stmtStr);
				if(bexecuted)
				{
					ResultSet rs=stmt.getResultSet();
					while(rs.next())
					{
						String verifiedUserName=rs.getString(1);
						Connection conn2=dbConn.getConnection();
						
						PreparedStatement stmt2=conn2.prepareStatement("update vitals set TELEPHONE_VERIFIED=? where TELEPHONE=?");
						stmt2.setString(1, "Y");
						stmt2.setLong(2, Long.parseLong(telephone));
						int executed2Count=stmt2.executeUpdate();
						if(executed2Count>0)
						{
							 return Response.status(200).entity("Thank you for verifying your phone number").build();
						}
						
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
		
		System.out.println("service called");
		
		return null;
}
}
