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

import atlasService.Localization;
import atlasService.LocalizationsReader;

public class GetLocalizations extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private LocalizationsReader localizationsReader = new LocalizationsReader();
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
		int limit = paramParser.GetValueOrDefault("entries", 0);
		int minutes = paramParser.GetValueOrDefault("minutes", 0);
		
		if (limit <= 0 && minutes <= 0)
		{
			json = new JsonResult<Localization>("Invalid input: entries or minutes must be greater than zero", null);
			print.println(gson.toJson(json));
			return;
		}

		ArrayList<Localization> result = null;
		try
		{
			result = (limit > 0 || id > 0) ?
				localizationsReader.getLocalizations(limit, id) :
				localizationsReader.getLocalizationsByTime(minutes);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		if(result != null)
		{
			json = new JsonResult<Localization>(result.size() + " : Entries", result);
			print.println(gson.toJson(json));
		}
		else
		{
			json = new JsonResult<Localization>("Error : DB Connection Failed", null);
			print.println(gson.toJson(json));
		}
	}

}
