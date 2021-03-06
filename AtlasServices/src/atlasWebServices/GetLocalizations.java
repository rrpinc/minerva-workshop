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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import atlasService.Localization;
import atlasService.LocalizationsReader;
import atlasService.Tag;

public class GetLocalizations extends HttpServlet
{
    private static final int LIMIT = 1000;
	private static final long serialVersionUID = 1L;
	private LocalizationsReader localizationsReader;
	private QueryStringParser paramParser;

	public GetLocalizations() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter print = response.getWriter();		
		Gson gson = new GsonBuilder().setPrettyPrinting()
				.setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
		JsonResult<Localization> json;
		paramParser = new QueryStringParser(request);
		int id = paramParser.GetValueOrDefault("tag", -1);
		
		String localizationArr = null; 
		paramParser = new QueryStringParser(request);
		long startTime = (long)paramParser.GetLongValueOrDefault("startTime", 0);
		long endTime = (long)paramParser.GetLongValueOrDefault("endTime", 0);
		
    	
		if (startTime <= 0 || endTime <= 0 )
		{
			json = new JsonResult<Localization>("Invalid input: entries must be greater than zero and valid : "+ startTime +" "+ endTime, null);
			print.println(gson.toJson(json));
			return;
		}

		localizationsReader = new LocalizationsReader();
		
		try {
			localizationArr = localizationsReader.getLocalizationsByTime(LIMIT, startTime, endTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(localizationArr != null)
		{
			
			JsonObject responseJson = new JsonObject();
			responseJson.addProperty("localizationArr", localizationArr);
			responseJson.addProperty("tags", localizationsReader.getAvailableTagsString());
		    response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    String jsonn = new Gson().toJson(responseJson);
		    print.write(jsonn);
		    
		}
		else
		{
			json = new JsonResult<Localization>("Error : DB Connection Failed", null);
			print.println(gson.toJson(json));
		}
		
	}


}
