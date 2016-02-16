package atlasService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

public class DetectionsReader {

	private static final long TIME_DELIMITER = 1000;
	private static final int LIMIT = 1000;
	private DBConnection connection;

	public DetectionsReader()
	{
		connection = new DBConnection();
		connection.Connect();
	}

	public ArrayList<Detections> getDETECTIONS(int count, long TAG) {
		if (count < 1)
			return null;
		try {
			if (!connection.isConnected()) {
				return new ArrayList<Detections>();
			}
			String query = "SELECT * FROM DETECTIONS ORDER BY TIME DESC LIMIT "
					+ count;
			if (TAG >= 0)
				query = "SELECT * FROM DETECTIONS WHERE TAG=" + TAG
				+ " ORDER BY TIME DESC LIMIT " + count;
			Statement st = connection.mConn.createStatement();
			ResultSet rs = st.executeQuery(query);
			ArrayList<Detections> ret = new ArrayList<Detections>();
			while (rs.next()) {
				Detections tmp = new Detections(rs.getLong("TIME_GROUP"),
						rs.getInt("BS"), rs.getLong("TAG"), rs.getLong("TX"),
						rs.getDouble("TIME"), rs.getDouble("SAMPLES_CLK"),
						rs.getFloat("SNR"), rs.getFloat("RSSI"),
						rs.getFloat("HEADROOM"), rs.getFloat("GAIN"));
				ret.add(tmp);
			}
			st.close();
			return ret;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		finally {
			try {
				connection.mConn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
	public ArrayList<Detections> getDETECTIONSbyTime(int count, long time) {
		if (count < 1)
			return null;
		
		try {
			if (!connection.isConnected()) {
				return new ArrayList<Detections>();
			}
			String query = "SELECT * FROM DETECTIONS ORDER BY TIME DESC LIMIT "
					+ count;

			/*
			 * TIME>=time-500 AND TIME <time+500
			 */
			if (time >= 0 && time <= System.currentTimeMillis())
				query = "SELECT * FROM DETECTIONS WHERE TIME>="+ (time-500) +" AND TIME <"+(time+500)
				+ " LIMIT " + count;
			Statement st = connection.mConn.createStatement();
			ResultSet rs = st.executeQuery(query);
			ArrayList<Detections> ret = new ArrayList<Detections>();
			while (rs.next()) {
				Detections tmp = new Detections(rs.getLong("TIME_GROUP"),
						rs.getInt("BS"), rs.getLong("TAG"), rs.getLong("TX"),
						rs.getDouble("TIME"), rs.getDouble("SAMPLES_CLK"),
						rs.getFloat("SNR"), rs.getFloat("RSSI"),
						rs.getFloat("HEADROOM"), rs.getFloat("GAIN"));
				ret.add(tmp);
			}
			st.close();
			return ret;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		finally {
			try {
				connection.mConn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		/*
		ArrayList<Detections> ret = new ArrayList<Detections>();
		
		Detections tmp = new Detections(10000,
					133, 329898, 928365,
					2525.4353, 234235.2352,
					4, 5,
					2, 5);
			ret.add(tmp);
			tmp = new Detections(1000,
					133, 329898, 928365,
					25.4353, 2345.2352,
					4, 5,
					2, 5);
			ret.add(tmp);
			tmp = new Detections(10000,
					133, 32898, 928365,
					255.433, 234235.2352,
					4, 1,
					2, 5);
			ret.add(tmp);
		
		return ret;*/
	}
	public String getDETECTIONSbyTime(int count, long startTime, long endTime) {
		if (count < 1)
			return null;
		String arrString = "";
		/*
		if (!connection.isConnected()) {
			return null;
		}
		*/
		try {
			
			if (startTime >= 0 && startTime <= System.currentTimeMillis() && endTime >= 0 && endTime <= System.currentTimeMillis())
			{
				ArrayList<Detections> detectionsbytime;

//				int numOfItems = (int) ((endTime- startTime)/TIME_DELIMITER);
				arrString+= "["+ this.getDETECTIONSbyTime(LIMIT, startTime).size();
				for (long t = startTime+TIME_DELIMITER; t<=endTime; t+=TIME_DELIMITER){
					detectionsbytime = this.getDETECTIONSbyTime(LIMIT, t);
					arrString+= ","+ detectionsbytime.size() ;
				}
				arrString += "]";
			}
		}
		
		finally {
			try {
				if (connection.isConnected())
					connection.mConn.close();				
				return arrString;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return arrString;

	}



}
