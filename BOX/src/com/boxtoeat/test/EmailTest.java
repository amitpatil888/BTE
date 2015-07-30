package com.boxtoeat.test;

import com.boxtoeat.helper.EmailVerificationCodeHelper;
import com.sendgrid.SendGrid;
import com.sendgrid.SendGrid.Email;

public class EmailTest {

	public static void main(String[] args)
	{
	 new EmailVerificationCodeHelper().saveEmailVerifactionCode("amitpatil8889", "amitpatil8889@gmail.com");
	}
}
