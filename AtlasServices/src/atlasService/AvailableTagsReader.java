package atlasService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AvailableTagsReader extends DBConnection {
	
	private DBConnection connection;
	
	public AvailableTagsReader()
	{
		connection = new DBConnection();
		connection.Connect();
	}
	
	public ArrayList<Tag> getAvailableTags()
	{
		try {
			if (!connection.isConnected())
			{
				return null;
			}
			
			String query =  "SELECT TAG, MAX(TIME) FROM LOCALIZATIONS GROUP BY TAG ORDER BY MAX(TIME) DESC";
			
			Statement st = connection.mConn.createStatement();
			ResultSet rs = st.executeQuery(query);
			
			ArrayList<Tag> ret = ParseResult(rs);
			st.close();
			
			return ret;
		} 
		
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		finally
		{
			if (connection.isConnected())
			{
				
				try {
					connection.mConn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	private ArrayList<Tag> ParseResult(ResultSet rs) throws SQLException
	{
		ArrayList<Tag> result = new ArrayList<Tag>();
		while (rs.next()) {
			Tag tag = new Tag(rs.getLong("TAG"));
			result.add(tag);
		}
		
		return result;
	}

}
