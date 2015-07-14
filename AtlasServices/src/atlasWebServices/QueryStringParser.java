package atlasWebServices;

import javax.servlet.http.HttpServletRequest;

public class QueryStringParser
{
	HttpServletRequest request;
	
	public QueryStringParser(HttpServletRequest request)
	{
		this.request = request;
	}
	
	public int GetValueOrDefault(String paramName, int defaultValue)
	{
		String param = request.getParameter(paramName);
		if (param == null)
			return defaultValue;
		
		try
		{
			int parsedParam = Integer.parseInt(param);
			return parsedParam;
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}
	
	public double GetValueOrDefault(String paramName, double defaultValue)
	{
		String param = request.getParameter(paramName);
		if (param == null)
			return defaultValue;
		
		try
		{
			double parsedParam = Double.parseDouble(param);
			return parsedParam;
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}

}
