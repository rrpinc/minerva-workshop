package atlasTests;
import java.util.ArrayList;

import atlasService.DETECTIONS;
import atlasService.DetectionsReader;


public class Test {

	public static void main(String[] args) {
		DetectionsReader reader = new DetectionsReader();

		ArrayList<DETECTIONS> x = reader.getDETECTIONS(2, -1);
		for(DETECTIONS l : x)
		{
			System.out.println(l.TAG);
		}
	}

}
