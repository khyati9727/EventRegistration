package com.project.utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class MobileMessage {
	
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//			PLEASE ADD THE API KEY OF FAST TO SMS ID
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	public static void message(long smsToNumber,String eventName,String email) throws Exception {
	   
	  try {  
		  
	    String message=" YOUR BOOKING FOR "+ eventName +" MADE SUCCESSFULLY. Your tickets have been mailed to you on "+ email +". For more bookings visit BookingNow@24.7";
	    String apiKey="";	// PASTE API KEY HERE
	    String sendId="FSTSMS";
	    message=URLEncoder.encode(message,"UTF-8");
	    String language="english";
	    String route="p";
	    
	    String myUrl="https://www.fast2sms.com/dev/bulk?authorization="+apiKey+"&sender_id="+sendId+"&message="+message+"&language="+language+"&route="+route+"&numbers="+smsToNumber;
	    
	    URL url=new URL(myUrl);
	    HttpsURLConnection con=(HttpsURLConnection)url.openConnection();
	    
	    con.setRequestMethod("GET");
	    con.addRequestProperty("User-Agent","Mozilla/5.0");
	    con.addRequestProperty("cache-control","no-cache");
	     	   		
		StringBuffer response=new StringBuffer();
		
		BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
		
		while(true)
		{
			String line=br.readLine();
			if(line==null)
			{
				break;
			}
			response.append(line);
		}
	    }
	  	catch (Exception e) {
	  				e.printStackTrace();
	  	}
	}

}
