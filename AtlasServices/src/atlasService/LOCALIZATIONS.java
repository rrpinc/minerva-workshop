package atlasService;

import java.util.Date;

public class LOCALIZATIONS {
private long ID;
private long TAG;
private long TX;
private String TIME;
private double X;
private double Y;
private double Z;
private int STATIONS_NUM;
private double Latitude;
private double Longitude;
private Date date_time;
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
public long getID() {
	return ID;
}
public long getTAG() {
	return TAG;
}
public long getTX() {
	return TX;
}
public String getTIME() {
	return TIME;
}
public double getX() {
	return X;
}
public double getY() {
	return Y;
}
public double getZ() {
	return Z;
}
public int getSTATIONS_NUM() {
	return STATIONS_NUM;
}
public double getLatitude() {
	return Latitude;
}
public void setLatitude(double Latitude) {
	this.Latitude = Latitude;
}
public double getLongitude() {
	return Longitude;
}
public void setWGS84Y(double Longitude) {
	this.Longitude = Longitude;
}
public Date getDate_time() {
	return date_time;
}
public void setDate_time(Date date_time) {
	this.date_time = date_time;
}
}
