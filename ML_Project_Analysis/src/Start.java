import java.io.BufferedReader;
import java.io.FileReader;

import weka.core.Instances;


public class Start {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedReader reader =null;
		Instances data =null;
		
		String file = args[0];
		try
		{
			reader = new BufferedReader(new FileReader(file));
		}
		catch(Exception e)
		{
			System.out.println("Error in File read !!!");
		}
		
		try
		{
			data = new Instances(reader);
			data.setClassIndex(data.numAttributes()-1);
		}
		catch(Exception e)
		{
			System.out.println("Error in creating instances from arff!!!");
		}
		
		Models m = new Models();
		
		m.LogisticAnalysis(data);
		
		m.NaiveBayesAnalysis(data);
		
		m.SupportVectorAnalysis(data);
		
		m.KNNAnalyis(data);
	}

}
