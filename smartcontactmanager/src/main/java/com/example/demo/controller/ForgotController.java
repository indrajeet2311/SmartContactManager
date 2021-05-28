package com.example.demo.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ForgotController {

Random random = new Random(1000);	

@RequestMapping("/forgot")
public String openEmailForm()
{
	return "forgot_email_form";
}
		

@PostMapping("/send-otp")
public String sendOTP(@RequestParam("email") String email,HttpSession session)
{
	System.out.println("EMAIL" + email);
	
	//Generating random otp of 4 characters
	
	
	int otp = random.nextInt(9999);
	
	System.out.println("OTP" + otp);
	
	String subject = "OTP From SCM";
	
	String message = "<h1> OTP = +  "+otp+" + </h1>";

	String to = email;
	
 boolean flag = true;
 
 if(flag)
 {
	 return "varify_otp";
 }
 else {
	 session.setAttribute("message" , "Check your email id!");
	 return "forgot_email_form";
 }
 
	
}
	
}
