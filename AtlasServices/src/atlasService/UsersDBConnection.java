package atlasService;

import java.sql.*;

public class UsersDBConnection {
	public Connection mConn = null;

	public void Connect() {
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			 mConn =
			 DriverManager.getConnection("jdbc:ucanaccess://C:/Users/user/git/minerva-workshop/AtlasServices/WebContent/users.accdb","",
			 "");
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public boolean isConnected() {
		if (mConn == null)
			return false;
		try {
			return !mConn.isClosed();
		} catch (SQLException e) {
			return false;
		}
	}

	
}
