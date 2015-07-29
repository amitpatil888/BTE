package com.boxtoeat.test;

import com.boxtoeat.helper.LoginHelper;

public class LoginTester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		LoginHelper helper=new LoginHelper();
//		boolean result=helper.saveLoginInformation("Joe", "black");
//		if(result)
//		{
//			System.out.println("credentials saves sucessfully");
//		}
//		else	{
//			System.out.println("credentials failed");
//		}
//if(result)
//{
	System.out.println(helper.authenticate("joe", "black"));
//}
	}

}
