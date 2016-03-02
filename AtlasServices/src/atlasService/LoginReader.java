package atlasService;

import java.io.File;
import java.util.Scanner;

import com.sun.org.apache.bcel.internal.util.ClassLoader;

public class LoginReader {
		
	public LoginReader(){}
	
	public Boolean loginToWebsite(String email, String password) {
		
		try {
			String f = (new File("users.csv")).getAbsolutePath();
			
			Scanner scanner = new Scanner(new File(f));
	        String tmp_email;
	        scanner.useDelimiter(",");

	        while (scanner.hasNext()) 
	        {
	        	tmp_email = scanner.next();
	        	tmp_email = tmp_email.replaceAll("\\s+","");
	            if (tmp_email.equals(email)){
	        		return scanner.next().equals(password) ? true : false;
	        	}
	        }
	         
	        scanner.close();
			return false;
			
		} catch (Exception e){
			System.err.print("Exception: ");
			System.err.println(e.getMessage());
			return false;

		}
		
	}
	

}
