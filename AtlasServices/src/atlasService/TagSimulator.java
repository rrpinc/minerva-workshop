package atlasService;

public class TagSimulator
{
	
	
	private double CalculateDeltaInMeters(double lat1, double long1, double lat2, double long2)
	{
		    double earthRadiusKM = 6378.137; // Radius of earth in KM
		    double deltaLat = (lat2 - lat1) * Math.PI / 180;
		    double deltaLong = (long2 - long1) * Math.PI / 180;
		    double a = Math.sin(deltaLat/2) * Math.sin(deltaLat/2) +
		    Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
		    Math.sin(deltaLong/2) * Math.sin(deltaLong/2);
		    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		    double d = earthRadiusKM * c;
		    return d * 1000; // meters
	}
}
