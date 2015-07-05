package atlasService;

public class DETECTIONS
{
	public long TIME_GROUP;	
	public int BS;
	public long TAG;
	public long TX;
	public double TIME;
	public double SAMPLES_CLK;
	public float SNR;
	public float RSSI;
	public float HEADROOM;
	public float GAIN;
	
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
}
