package atlasTests;
import java.util.ArrayList;

import atlasService.Detections;
import atlasService.DetectionsReader;


public class Test {

	public static void main(String[] args) {
		DetectionsReader reader = new DetectionsReader();

		ArrayList<Detections> x = reader.getDETECTIONS(2, -1);
		for(Detections l : x)
		{
			System.out.println(l.tag);
		}
	}

}
