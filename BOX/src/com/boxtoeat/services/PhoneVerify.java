package com.boxtoeat.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
			
		}
		else
		if(actionType.equalsIgnoreCase("verify"))
		{
			
		}
		
		System.out.println("service called");
		
		return null;
}
}
