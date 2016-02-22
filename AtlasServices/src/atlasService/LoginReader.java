package atlasService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

public class LoginReader {
	
	private UsersDBConnection connection;
	
	public LoginReader()
	{
		connection = new UsersDBConnection();
		connection.Connect();
	}
	
	public Boolean loginToWebsite(String email, String password) {
		
		try {
			String query = "SELECT * FROM credentials WHERE email='"+email+"'";
			Statement st = connection.mConn.createStatement();
			ResultSet rs = st.executeQuery(query);
				
			if (rs.next()) {
				if (rs.getString("password").equals(password)) {
					st.close();
					return true;
				}
			}
			st.close();
			return false;
		} catch (Exception e){
			System.err.print("Exception: ");
			System.err.println(e.getMessage());
			return false;

		} finally {
			try {
				connection.mConn.close();
			}
			catch (Exception e) {
				System.err.print("Exception: ");
				System.err.println(e.getMessage());

			}		
		}
		
	}
	

}
