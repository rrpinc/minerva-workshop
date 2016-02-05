package atlasService;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import atlasTools.IsraelCoordinatesTransformations;

public class LocalizationsReader extends DBConnection {
	
	private static final double TO_MINUTS = 0;
	private DBConnection connection;
	
	public ArrayList<Localization> getLocalizations(int count, long tag) throws SQLException
	{
		if (count < 1)
			return new ArrayList<Localization>();

		try
		{
			String query = (tag >= 0) ? 
				"SELECT * FROM LOCALIZATIONS WHERE TAG=" + tag
				+ " ORDER BY TIME DESC LIMIT " + count :
				"SELECT * FROM LOCALIZATIONS ORDER BY TIME DESC LIMIT " + count;
			
			return Read(query);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<Localization> getLocalizationsByTime(int minutes, long tag) throws SQLException
	{
		if (minutes < 1)
			return new ArrayList<Localization>();

		try
		{
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MINUTE, -minutes);
			
			String tagSelector = (tag > 0) ? "TAG = " + tag + " AND ": "";
			
			String query =
				"SELECT * FROM LOCALIZATIONS WHERE " + tagSelector + "TIME >= " + cal.getTimeInMillis()/1000L+ " ORDER BY TIME DESC LIMIT 2000";
			
			return Read(query);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<Localization> getLocalizationsByTimeDelta(long start ,long end, long tag) throws SQLException
	{
		
		if ((end-start)/1000L < 1)
			return new ArrayList<Localization>();

		try
		{
			String tagSelector = (tag > 0) ? "TAG = " + tag + " AND ": "";
			
			String query =
				"SELECT * FROM LOCALIZATIONS WHERE " + tagSelector + "TIME >= " + start/1000L + " AND TIME <= " + end/1000L + " ORDER BY TIME DESC LIMIT 2000";
			
			return Read(query);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	private ArrayList<Localization> Read(String query) throws SQLException
	{
		try
		{
			connection = new DBConnection();
			connection.Connect();
			
			if (!connection.isConnected())
			{
				return null;
			}
			
			Statement st = connection.mConn.createStatement();
			ResultSet rs = st.executeQuery(query);
			
			ArrayList<Localization> ret = ParseResult(rs);
			st.close();
			return ret;
		}
		catch (SQLException e)
		{
			return null;
		}
		
		finally
		{
			if (connection.isConnected())
				connection.mConn.close();
		}
	}

	
	private ArrayList<Localization> ParseResult(ResultSet rs) throws SQLException
	{
		ArrayList<Localization> result = new ArrayList<Localization>();
		while (rs.next()) {
			double[] wgs84 = IsraelCoordinatesTransformations
					.itm2wgs84(new double[] { rs.getDouble("Y"),
							rs.getDouble("X") });
			String sql_time = rs.getString("TIME");
			String sql_milisecond = 
					(sql_time.indexOf('.') < 0 || sql_time.indexOf('.') == sql_time.length()-1) ?
					"000000" :
					sql_time.substring(sql_time.indexOf('.')+1);
			
			sql_time = sql_time.substring(0,
					sql_time.indexOf('.') <= 0 ?
						sql_time.length() - 1
						: sql_time.indexOf('.'));
			
			long time_parse = 0;
			try
			{
			 time_parse = Long.parseLong(sql_time);
			}
			catch(Exception e)
			{
				time_parse=0;
			}
			
			try
			{
				if(sql_milisecond.length()>3)
					sql_milisecond=sql_milisecond.substring(0,3);
				else if (sql_milisecond.length()==2)
					sql_milisecond+="0";
				else if (sql_milisecond.length()==1)
					sql_milisecond+="00";
				else if (sql_milisecond.length()==0)
					sql_milisecond+="000";
			}
			catch(Exception e)
			{
				
			}
			
			Date time = null;
			if (time_parse != 0)
				time = new Date((time_parse * 1000L));
			Localization tmp = new Localization(rs.getLong("ID"),
					rs.getLong("TAG"), rs.getLong("TX"),
					rs.getString("TIME"), rs.getDouble("X"),
					rs.getDouble("Y"), rs.getDouble("Z"),
					rs.getInt("STATIONS_NUM"), wgs84[0], wgs84[1], time);
			result.add(tmp);
		}
		
		return result;
	}
}
