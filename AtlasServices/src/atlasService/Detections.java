package atlasService;

public class Detections
{
	public long timeGroup;	
	public int bs;
	public long tag;
	public long tx;
	public double time;
	public double samplesClk;
	public float snr;
	public float rssi;
	public float headroom;
	public float gain;
	
	public Detections(long tIME_GROUP, int bS, long tAG, long tX, double tIME,
			double sAMPLES_CLK, float sNR, float rSSI, float hEADROOM, float gAIN) {
		
		this.timeGroup = tIME_GROUP;
		this.bs = bS;
		this.tag = tAG;
		this.tx = tX;
		this.time = tIME;
		this.samplesClk = sAMPLES_CLK;
		this.snr = sNR;
		this.rssi = rSSI;
		this.headroom = hEADROOM;
		this.gain = gAIN;
	}
}
