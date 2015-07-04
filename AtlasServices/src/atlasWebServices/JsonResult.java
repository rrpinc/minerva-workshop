package atlasWebServices;

import java.util.ArrayList;

import atlasService.LOCALIZATIONS;

public class JsonResult<T> {
private String Message;
private ArrayList<T> Results;
public JsonResult(String msg, ArrayList<T> results) {
	super();
	this.Message = msg;
	this.Results = results;
}

}
