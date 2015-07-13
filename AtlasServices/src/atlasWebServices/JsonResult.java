package atlasWebServices;

import java.util.ArrayList;

public class JsonResult<T> {
	public String Message;
	public ArrayList<T> Results;

	public JsonResult(String msg, ArrayList<T> results) {
		super();
		Message = msg;
		Results = results;
	}
}
