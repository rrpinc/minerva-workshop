package atlasWebServices;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import atlasService.AvailableTagsReader;
import atlasService.Detections;
import atlasService.DetectionsReader;
import atlasService.Localization;
import atlasService.Tag;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GetDetections extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private DetectionsReader detectionsReader = new DetectionsReader();
    private QueryStringParser paramParser;
    private static final int LIMIT = 1000;
    
    public GetDetections() {
        super();
    }

	
    protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

    	//response.setContentType("application/json");
		PrintWriter print = response.getWriter();		
		Gson gson = new GsonBuilder().setPrettyPrinting()
				.setDateFormat(DateFormat.FULL, DateFormat.FULL).create();

		String detectionsArr = null; 
		JsonResult<Detections> json;
		paramParser = new QueryStringParser(request);
		long startTime = (long)paramParser.GetValueOrDefault("startTime", 0);
		long endTime = (long)paramParser.GetValueOrDefault("endTime", 0);
		
//		long startTime = paramParser.GetValueOrDefault("startTime", 0);
//		long endTime = paramParser.GetValueOrDefault("endTime", 0);
		
//    	long startTime = Long.parseLong(request.getParameter("startTime"));
//    	long endTime = Long.parseLong(request.getParameter("endTime"));
    	
		if (startTime <= 0 || startTime > System.currentTimeMillis() || endTime <= 0 || endTime > System.currentTimeMillis())
		{
			json = new JsonResult<Detections>("Invalid input: entries must be greater than zero and valid", null);
			print.println(gson.toJson(json));
			return;
		}

		detectionsArr = detectionsReader.getDETECTIONSbyTime(LIMIT, startTime, endTime);
		
		if(detectionsArr != null)
		{
			
			JsonObject responseJson = new JsonObject();
			responseJson.addProperty("detectionsArr", detectionsArr);
		    response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    String jsonn = new Gson().toJson(responseJson);
		    print.write(jsonn);
		    
//			print.println(gson.toJson(json));
		}
		else
		{
			json = new JsonResult<Detections>("Error : DB Connection Failed", null);
			print.println(gson.toJson(json));
		}
	}
    
}
