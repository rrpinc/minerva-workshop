package atlasWebServices;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import atlasService.LoginReader;

import com.google.gson.JsonObject;

public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private LoginReader loginReader = new LoginReader();

	public static final int HTTP_STATUS_OK = 200;
	public static final int HTTP_STATUS_CREATED = 201;
	public static final int HTTP_STATUS_ERROR = 500;
    private QueryStringParser paramParser;
    

    public Login() {
        super();
    }

	
    protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
//    	request.getRequestDispatcher("/WebContent/WEB-INF/login/Login.jsp").forward(request, response);
	    	
    	paramParser = new QueryStringParser(request);
    	String email = (String) paramParser.GetValueOrNull("email");
    	String password = (String) paramParser.GetValueOrNull("pass");
    	
//    	String jsonString = IOUtils.toString(request.getInputStream(), "UTF-8");
//		JsonParser parser = new JsonParser();
//		JsonObject requestJson = (JsonObject)parser.parse(jsonString);
//		String email = (String) requestJson.get("email").getAsString();
//		String password = (String) requestJson.get("pass").getAsString();
    	
		if (email == null || password == null) {
			response.setStatus(HTTP_STATUS_ERROR);
			return;
		}
		
		boolean logged = loginReader.loginToWebsite(email, password);
		
		if (logged) {
			request.getSession().setAttribute("userid", email);
		}
		else {
			request.getSession().setAttribute("userid", null);
		}
		JsonObject responseJson = new JsonObject();
		responseJson.addProperty("userlogged", logged ? "true" : "false");
		response.setStatus(HTTP_STATUS_OK);
		response.setContentType("application/json");
		response.getOutputStream().println(responseJson.toString());	
    
    }
    
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException ,IOException {
//    	
//    	String email = req.getParameter("email");
//    	String pwd = req.getParameter("pwd");
//    	
//    	req.setAttribute("email", email);
//    	req.setAttribute("pwd", pwd);
//
//    	req.getRequestDispatcher("/WebContent/WEB-INF/index.jsp").forward(req, resp);
//    }
}
