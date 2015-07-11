package atlasService;

import java.util.Date;

public class Localization {
	public long id;
	public long tag;
	public long tx;
	public String time;
	public double x;
	public double y;
	public double z;
	public int stationNum;
	public double latitude;
	public double longtitude;
	public Date dateTime;
	
	public Localization(long id, long tag, long tx, String time, double x,
			double y, double z, int stationNum, double wGS84X,double wGS84Y,Date dateTime) {
		super();
		this.id = id;
		this.tag = tag;
		this.tx = tx;
		this.time = time;
		this.x = x;
		this.y = y;
		this.z = z;
		this.stationNum = stationNum;
		this.latitude = wGS84X;
		this.longtitude = wGS84Y;
		this.dateTime=dateTime;
	}
}
