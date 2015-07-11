package atlasWebServices;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import atlasService.AvailableTagsReader;
import atlasService.Tag;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GetAvailableTags extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private AvailableTagsReader tagsReader = new AvailableTagsReader();
	
    public GetAvailableTags() {
        super();
    }

	
    protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter print = response.getWriter();		
		Gson gson = new GsonBuilder().setPrettyPrinting()
				.setDateFormat(DateFormat.FULL, DateFormat.FULL).create();

		ArrayList<Tag> tags = tagsReader.getAvailableTags(); 
		
		
		
		if(tags != null)
		{
			tagResults result = MapTags(tags);
			
			JsonElement je = gson.toJsonTree(result);
		    JsonObject jo = new JsonObject();
		    jo.add("tagResults", je);
			
			print.println(gson.toJson(jo));
		}
		else
		{
			print.println(gson.toJson("Error : DB Connection Failed"));
		}
	}
    
    private tagResults MapTags(ArrayList<Tag> tags)
    {
    	return new tagResults(tags, tags.size());
    }
}
