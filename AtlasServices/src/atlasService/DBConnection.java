package atlasService;

import java.sql.*;

public class DBConnection {
	public Connection mConn = null;

	public void Connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			 mConn =
			 DriverManager.getConnection("jdbc:mysql://mysqlsrv.cs.tau.ac.il/atlas","atlas",
			 "atlas");
			
			//mConn = DriverManager.getConnection("jdbc:mysql://127.0.0.1/atlas",
			//		"atlas", "atlas");
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
