package atlasService;

import java.sql.*;
import java.util.ArrayList;

import atlasTools.IsraelCoordinatesTransformations;

public class DBConnection {
	private Connection mConn = null;

	public void Connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			 mConn =
			 DriverManager.getConnection("jdbc:mysql://mysqlsrv.cs.tau.ac.il/atlas","atlas",
			 "atlas");
			
			//mConn = DriverManager.getConnection("jdbc:mysql://127.0.0.1/atlas",
			//		"atlas", "atlas");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
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

	public ArrayList<LOCALIZATIONS> getLOCALIZATIONS(int count, long TAG) {
		if (count < 1)
			return null;
		try {
			if (mConn == null || mConn.isClosed()) {
				return null;
			}
			String query = "SELECT * FROM LOCALIZATIONS ORDER BY TIME DESC LIMIT "
					+ count;
			if (TAG >= 0)
				query = "SELECT * FROM LOCALIZATIONS WHERE TAG=" + TAG
						+ " ORDER BY TIME DESC LIMIT " + count;
			Statement st = mConn.createStatement();
			ResultSet rs = st.executeQuery(query);
			ArrayList<LOCALIZATIONS> ret = new ArrayList<LOCALIZATIONS>();
			while (rs.next()) {
				double[] wgs84 = IsraelCoordinatesTransformations
						.itm2wgs84(new double[] { rs.getDouble("Y"),
								rs.getDouble("X") });
				String sql_time = rs.getString("TIME");
				String sql_milisecond = sql_time.indexOf('.') < 0 || sql_time.indexOf('.')==sql_time.length()-1 ? "000000":sql_time.substring(sql_time.indexOf('.')+1);
				sql_time = sql_time.substring(0,
						sql_time.indexOf('.') <= 0 ? sql_time.length() - 1
								: sql_time.indexOf('.'));
				
				long mili=0;
				long time_parse =0;
				try{
				 time_parse = Long.parseLong(sql_time);
				}
				catch(Exception e)
				{
					time_parse=0;
				}
				try{
					if(sql_milisecond.length()>3)
						sql_milisecond=sql_milisecond.substring(0,3);
					else if (sql_milisecond.length()==2)
						sql_milisecond+="0";
					else if (sql_milisecond.length()==1)
						sql_milisecond+="00";
					else if (sql_milisecond.length()==0)
						sql_milisecond+="000";
					mili=Long.parseLong(sql_milisecond);
				}
				catch(Exception e)
				{
					mili = 0;
				}
				Date time = null;
				if (time_parse != 0)
					time = new Date((time_parse * 1000L));
				LOCALIZATIONS tmp = new LOCALIZATIONS(rs.getLong("ID"),
						rs.getLong("TAG"), rs.getLong("TX"),
						rs.getString("TIME"), rs.getDouble("X"),
						rs.getDouble("Y"), rs.getDouble("Z"),
						rs.getInt("STATIONS_NUM"), wgs84[0], wgs84[1], time);
				ret.add(tmp);
			}
			st.close();
			return ret;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public ArrayList<DETECTIONS> getDETECTIONS(int count, long TAG) {
		if (count < 1)
			return null;
		try {
			if (mConn == null || mConn.isClosed()) {
				return null;
			}
			String query = "SELECT * FROM DETECTIONS ORDER BY TIME DESC LIMIT "
					+ count;
			if (TAG >= 0)
				query = "SELECT * FROM DETECTIONS WHERE TAG=" + TAG
						+ " ORDER BY TIME DESC LIMIT " + count;
			Statement st = mConn.createStatement();
			ResultSet rs = st.executeQuery(query);
			ArrayList<DETECTIONS> ret = new ArrayList<DETECTIONS>();
			while (rs.next()) {
				DETECTIONS tmp = new DETECTIONS(rs.getLong("TIME_GROUP"),
						rs.getInt("BS"), rs.getLong("TAG"), rs.getLong("TX"),
						rs.getDouble("TIME"), rs.getDouble("SAMPLES_CLK"),
						rs.getFloat("SNR"), rs.getFloat("RSSI"),
						rs.getFloat("HEADROOM"), rs.getFloat("GAIN"));
				ret.add(tmp);
			}
			st.close();
			return ret;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
}
