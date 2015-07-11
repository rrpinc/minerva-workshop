package atlasWebServices;

import java.util.ArrayList;

import atlasService.Tag;

public class tagResults
{
	public ArrayList<Tag> items;
	public int count;
	
	public tagResults(ArrayList<Tag> results, int count)
	{
		items = results;
		this.count = count;
	}

}
