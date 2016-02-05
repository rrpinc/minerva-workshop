package atlasService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

public class LoginReader {
	
	private DBConnection connection;
	
	public LoginReader()
	{
		connection = new DBConnection();
		connection.Connect();
	}
	
	public Boolean loginToWebsite(String email, String password) {
		
		//TODO
		return true;

	}
	

}
