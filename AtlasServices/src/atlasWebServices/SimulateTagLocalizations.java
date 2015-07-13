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
       
    public SimulateTagLocalizations() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		TagSimulator simulator = new TagSimulator();
		
		ArrayList<Location> a = simulator.SimulateTagLocalizations(0, 0, 0);
		
		JsonResult<Location> result = new JsonResult<Location>("sim : ", a);
		
		PrintWriter print = response.getWriter();		
		Gson gson = new GsonBuilder().setPrettyPrinting()
				.setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
		
		print.println(gson.toJson(result));
		
	}
}
