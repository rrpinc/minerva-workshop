package atlasService;

import java.util.ArrayList;
import java.util.Random;

public class TagSimulator
{
	private static double EarthRadius = 6378.137; // Radius of earth in KM 
	
	private Location LatLongToXYZ(double lat, double lon)
	{
	    double latitude = lat * Math.PI / 180;
	    double longtitude = lon * Math.PI / 180;
	    double x = -EarthRadius * Math.cos(latitude) * Math.cos(longtitude);
	    double y =  EarthRadius * Math.sin(latitude); 
	    double z =  EarthRadius * Math.cos(latitude) * Math.sin(longtitude);
	    
	    return new Location(lat, lon, x, y, z);
	}
	
	private Location calculateNextLocation(Location loc, double distance)
	{
		Random rand = new Random();
		int minAngle = 0;
		int maxAngle = 360;
		int randomAngle = rand.nextInt((maxAngle - minAngle) + 1) + minAngle;
		double bearing = Math.toRadians(randomAngle);

		double lat1 = Math.toRadians(loc.Latitude);
		double lon1 = Math.toRadians(loc.Longtitude);

		double lat2 = Math.asin(Math.sin(lat1) * Math.cos(distance / EarthRadius) +
		     Math.cos(lat1) * Math.sin(distance / EarthRadius) * Math.cos(bearing));

		double lon2 = lon1 + Math.atan2(Math.sin(bearing) * Math.sin(distance / EarthRadius) * Math.cos(lat1),
		             Math.cos(distance / EarthRadius) - Math.sin(lat1) *Math.sin(lat2));

		lat2 = Math.toDegrees(lat2);
		lon2 = Math.toDegrees(lon2);
		
		return LatLongToXYZ(lat2, lon2);
	} 
	
	public ArrayList<Location> SimulateTagLocalizations(int timePeriodInHours, int resultsPerHour, double velocity)
	{
		int resultsCount = timePeriodInHours * resultsPerHour;
		double stepDistance = velocity / resultsCount;

		Location startPoint = new Location(33.120675660801325, 35.59343085169261, 255753.1501, 780658.9078, 70);
		ArrayList<Location> simulations = new ArrayList<Location>();
		
		Location nextPoint = startPoint;
		for(int i = 0; i < resultsCount; i++)
		{
			simulations.add(nextPoint);
			nextPoint = calculateNextLocation(nextPoint, stepDistance);
		}		
		
		return simulations;
	}
}
