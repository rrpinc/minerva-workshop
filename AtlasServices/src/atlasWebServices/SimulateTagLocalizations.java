package atlasWebServices;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import atlasService.Location;
import atlasService.TagSimulator;

public class SimulateTagLocalizations extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private static int defaultMovementTime = 6;
	private static int defaultResultsPerHour = 10;
	private static double defaultVelocity = 5;
	private QueryStringParser paramsParser;
	
    public SimulateTagLocalizations() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{		
		paramsParser = new QueryStringParser(request);
		int movementTime = paramsParser.GetValueOrDefault("movement-time", defaultMovementTime);
		int resultsPerHour = paramsParser.GetValueOrDefault("results-per-hour", defaultResultsPerHour);
		double velocity = paramsParser.GetValueOrDefault("velocity", defaultVelocity);
		
		TagSimulator simulator = new TagSimulator();
		
		ArrayList<Location> a = simulator.SimulateTagLocalizations(movementTime, resultsPerHour, velocity);
		
		JsonResult<Location> result = new JsonResult<Location>("Simulated Results", a);
		PrintWriter print = response.getWriter();		
		Gson gson = new GsonBuilder().setPrettyPrinting()
				.setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
		
		print.println(gson.toJson(result));
	}
}
