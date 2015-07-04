package atlasService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DetectionsReader {
	
	private DBConnection connection;
	
	public DetectionsReader()
	{
		connection = new DBConnection();
		connection.Connect();
	}
	
	public ArrayList<DETECTIONS> getDETECTIONS(int count, long TAG) {
		if (count < 1)
			return null;
		try {
			if (!connection.isConnected()) {
				return new ArrayList<DETECTIONS>();
			}
			String query = "SELECT * FROM DETECTIONS ORDER BY TIME DESC LIMIT "
					+ count;
			if (TAG >= 0)
				query = "SELECT * FROM DETECTIONS WHERE TAG=" + TAG
						+ " ORDER BY TIME DESC LIMIT " + count;
			Statement st = connection.mConn.createStatement();
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

}
