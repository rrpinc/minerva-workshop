package atlasService;

import java.util.Date;

public class LOCALIZATIONS {
	public long ID;
	public long TAG;
	public long TX;
	public String TIME;
	public double X;
	public double Y;
	public double Z;
	public int STATIONS_NUM;
	public double Latitude;
	public double Longitude;
	public Date date_time;
	
	public LOCALIZATIONS(long iD, long tAG, long tX, String tIME, double x,
			double y, double z, int sTATIONS_NUM, double wGS84X,double wGS84Y,Date date_time) {
		super();
		ID = iD;
		TAG = tAG;
		TX = tX;
		TIME = tIME;
		X = x;
		Y = y;
		Z = z;
		STATIONS_NUM = sTATIONS_NUM;
		Latitude = wGS84X;
		Longitude = wGS84Y;
		this.date_time=date_time;
	}
}
