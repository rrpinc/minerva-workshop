package atlasService;
public class DETECTIONS {
private long TIME_GROUP;

private int BS;
private long TAG;
private long TX;
private double TIME;
private double SAMPLES_CLK;
private float SNR;
private float RSSI;
private float HEADROOM;
private float GAIN;
public DETECTIONS(long tIME_GROUP, int bS, long tAG, long tX, double tIME,
		double sAMPLES_CLK, float sNR, float rSSI, float hEADROOM, float gAIN) {
	
	TIME_GROUP = tIME_GROUP;
	BS = bS;
	TAG = tAG;
	TX = tX;
	TIME = tIME;
	SAMPLES_CLK = sAMPLES_CLK;
	SNR = sNR;
	RSSI = rSSI;
	HEADROOM = hEADROOM;
	GAIN = gAIN;
}
public long getTIME_GROUP() {
	return TIME_GROUP;
}
public int getBS() {
	return BS;
}
public long getTAG() {
	return TAG;
}
public long getTX() {
	return TX;
}
public double getTIME() {
	return TIME;
}
public double getSAMPLES_CLK() {
	return SAMPLES_CLK;
}
public float getSNR() {
	return SNR;
}
public float getRSSI() {
	return RSSI;
}
public float getHEADROOM() {
	return HEADROOM;
}
public float getGAIN() {
	return GAIN;
}


}
