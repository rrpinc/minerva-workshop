package atlasService;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import atlasTools.IsraelCoordinatesTransformations;

public class LocalizationsReader extends DBConnection {
	private static final long TIME_DELIMITER = 1000;
	private DBConnection connection;
	private List<Long> avaliableTags;

	public LocalizationsReader(){	
		avaliableTags = new ArrayList<Long>();
	}
	
	public String getAvailableTagsString(){
		String arr="";
		try
		{
			arr+="[";
			for (int i=0; i<avaliableTags.size(); i++){
				arr+= (i==0? avaliableTags.get(i) : ","+ avaliableTags.get(i));
			}
			arr+="]";
		}
		catch (Exception e)
		{
			return "";
		}
		return arr;
	}
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
		
		if ((end-start) < 1)
			return new ArrayList<Localization>();

		try
		{
			String tagSelector = (tag > 0) ? "TAG = " + tag + " AND ": "";
			
			String query =
				"SELECT * FROM LOCALIZATIONS WHERE " + tagSelector + "TIME >= " + start + " AND TIME <= " + end + " ORDER BY TIME DESC LIMIT 2000";
			
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
				return new ArrayList<Localization>();
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
			
			long timeInMillis = Long.parseLong(rs.getString("TIME"));
			Date time = new Date((timeInMillis));
			Localization tmp = new Localization(
					rs.getLong("TAG"), rs.getLong("TX"),
					rs.getString("TIME"), rs.getDouble("X"),
					rs.getDouble("Y"), rs.getDouble("Z"),
					wgs84[0], wgs84[1], time);
			result.add(tmp);
		}
		
		return result;
	}
	
	public String getLocalizationsByTime(int count, long startTime, long endTime) throws Exception {
		if (count < 1)
			return null;
		String arrString = "";
		ArrayList<Localization> localizationsbytime;
		int i=0;

		try {
			
			if (startTime >= 0 && startTime <= System.currentTimeMillis() && endTime >= 0 && endTime <= System.currentTimeMillis())
			{
				localizationsbytime = this.getLocalizationsByTimeDelta(startTime, endTime, -1);
				Map<Long, List<Localization>> localizationsByTags = new HashMap<Long, List<Localization>>();
				List<Localization> l;

				for (int j = 0; j<localizationsbytime.size(); j++){
					l = localizationsByTags.get(localizationsbytime.get(j).tag);
	                if (l == null){
						avaliableTags.add(localizationsbytime.get(j).tag);
	                	localizationsByTags.put(localizationsbytime.get(j).tag, l=new ArrayList<Localization>());
	                }
	                localizationsByTags.get(localizationsbytime.get(j).tag).add(localizationsbytime.get(j));
				}
				
				arrString+= "[";

				int j=0;
				for (Long tag : localizationsByTags.keySet()){
					arrString+= (j==0? "[": ",[");
					i=0;
					List<Localization> tagLocalizations = localizationsByTags.get(tag);
					for (Localization loc : tagLocalizations){
						arrString += (i==0? "[": ",[");
						if (i<localizationsbytime.size()){
							arrString += loc.dateTime.getTime() + "," + loc.latitude + "," +loc.longtitude;
						}
						else {
							break;
						}
						arrString += "]";
						i++;
					}
					
					arrString += "]";
					j++;
				}
				
				arrString += "]";
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		finally {
			try {
				if (connection != null && connection.isConnected())
					connection.mConn.close();				
				return arrString;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return arrString;

	}

}
