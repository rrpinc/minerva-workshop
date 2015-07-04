package atlasWebServices;

import java.util.ArrayList;

public class JsonResult<T> {
	private String Message;
	private ArrayList<T> Results;

	public JsonResult(String msg, ArrayList<T> results) {
		super();
		Message = msg;
		Results = results;
	}

}
