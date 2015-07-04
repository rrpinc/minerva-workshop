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

import atlasService.LOCALIZATIONS;
import atlasService.LocalizationsReader;

/**
 * Servlet implementation class GetLOCALIZATION
 */
public class GetLOCALIZATION extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private LocalizationsReader localizationsReader = new LocalizationsReader();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetLOCALIZATION() {
		super();
	}

	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter print = response.getWriter();		
		Gson gson = new GsonBuilder().setPrettyPrinting()
				.setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
		JsonResult<LOCALIZATIONS> json;
		
		String tag_id = request.getParameter("tag");
		String entries = request.getParameter("entries");
		if (tag_id == null) {
			tag_id = "-1";
		}
		if (entries == null) {
			entries = "0";
		}
		int id = -1, count = 0;
		try {
			id = Integer.parseInt(tag_id);
		} catch (Exception e) {
			json = new JsonResult<LOCALIZATIONS>("Invalid input: tag id", null);
			print.println(gson.toJson(json));
			return;
		}
		try {
			count = Integer.parseInt(entries);
		} catch (Exception e) {
			json = new JsonResult<LOCALIZATIONS>("Invalid input: entries", null);
			print.println(gson.toJson(json));
			return;
		}

		ArrayList<LOCALIZATIONS> result = localizationsReader.getLOCALIZATIONS(count, id);
		if(result !=null)
		{
		json = new JsonResult<LOCALIZATIONS>(result.size() + " : Entries", result);
		print.println(gson.toJson(json));
		}
		else
		{
			json = new JsonResult<LOCALIZATIONS>("0 : Entries", null);
			print.println(gson.toJson(json));
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
