import java.io.BufferedReader;
import java.io.FileReader;

import weka.core.Instances;


public class FileHandler {

	public Instances getInstances(String nwPath) {
		// TODO Auto-generated method stub
		try {
			BufferedReader Reader = new BufferedReader(new FileReader(nwPath));
			Instances tInstances = new Instances(Reader); 
			tInstances.setClassIndex(tInstances.numAttributes()-1);
			Reader.close();
			return tInstances;
		} catch (Exception e) {
			System.out.println("Error in Instances Creation!!!");
			e.printStackTrace();
			// TODO: handle exception
		}
		
		return null;
	}

	
}
