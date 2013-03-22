import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class FileHandler {
	
	public List<Double> FetchData(String FileName){
		List<Double> vals = new ArrayList<Double>();
		 try{
			BufferedReader input =  new BufferedReader(new FileReader(FileName));
			String line = null; 
	        while (( line = input.readLine()) != null){
	        	vals.add(Double.parseDouble(line));
	        }
	        input.close();
		 } 
		 catch (Exception e){
			e.printStackTrace();
		 }
		return vals;
	}

}
