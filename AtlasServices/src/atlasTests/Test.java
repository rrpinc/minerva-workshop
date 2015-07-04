package atlasTests;
import java.util.ArrayList;

import atlasService.DBConnection;
import atlasService.DETECTIONS;


public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DBConnection con = new DBConnection();
		con.Connect();
		if(con.isConnected())
		{
			ArrayList<DETECTIONS> x = con.getDETECTIONS(2, -1);
			for(DETECTIONS l : x)
			{
				System.out.println(l.getTAG());
			}
		}
	}

}
