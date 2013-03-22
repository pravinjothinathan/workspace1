import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weka.classifiers.functions.Logistic;
import weka.core.Instance;
import weka.core.Instances;


public class LogisticModel {
	List<Instances> data;
	List<Logistic> models;
	
	public LogisticModel() {
		// TODO Auto-generated constructor stub
	}

	public LogisticModel(String[] lstFiles, String path) {
		// TODO Auto-generated constructor stub
		FileHandler FH = new FileHandler();
		data = new ArrayList<Instances>();
		models = new ArrayList<Logistic>();
		int modelIterator=1;
		for (String file : lstFiles) {
			System.out.println("Building Model -> "+modelIterator++);
			String nwPath = path + "/" + file;
			System.out.println(nwPath);
			Instances tInstances = FH.getInstances(nwPath);
			data.add(tInstances);
			try {
				Logistic model = new Logistic();
				model.buildClassifier(tInstances);
				models.add(model);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error in Building Classifier !!!");
			}
		}
	}
	
	public double CalculateTrainingAccuracy()
	{
		//for each model
		//List<List<Double>> PredictedVals = new ArrayList<List<Double>>();
		double[][] predVals = new double[data.get(0).size()][models.size()];
		for (int i = 0; i < models.size(); i++) {
			Logistic model = models.get(i);
			Instances inst = data.get(i);
			for (int j = 0; j < inst.size(); j++) {
				Instance tInstance = inst.get(j);
				try {
					Double PredValue = model.classifyInstance(tInstance);
					predVals[j][i]=PredValue;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Error in Predicting values!!!");
					e.printStackTrace();
				}
			}
		}
		System.out.println("Trainging");
		Helper h = new Helper();
		h.Print(predVals);
		
		int AccuracyCounter =0;
		int totalCount=0;
		if(data.get(0)!=null){
			Instances finInsatances = data.get(0);
			totalCount = finInsatances.size();
			for (int i = 0; i < finInsatances.size(); i++) {
				///List<Double> ClassValues = PredictedVals.get(i);
				if(finInsatances.get(i).classValue()==MaxProbOutcome(predVals[i]))
				{
					AccuracyCounter++;
				}
			}
		}
		return ((double)AccuracyCounter/totalCount)*100;

	}

	private double MaxProbOutcome(double[] ds) {
		// TODO Auto-generated method stub
		Map<Double,Integer> Vals = new HashMap<Double, Integer>();
		for (Double val : ds) {
			if(Vals.containsKey(val))
			{
				int newVal = Vals.get(val) +1;
				Vals.remove(val);
				Vals.put(val, newVal);
			}
			else
			{
				Vals.put(val, 1);
			}
		}
		Double Max=0.0;
		int MaxCount=0;
		for (Double key : Vals.keySet()) {
			int count = Vals.get(key);
			if(MaxCount<count)
			{
				MaxCount = count;
				Max = key;
			}
		}
		return Max;

	}

	public double getAccuracy() {
		List<List<Double>> PredictedVals = new ArrayList<List<Double>>();
		//This loop iterates thorough the no of classifiers
		for (int i = 0; i < data.size(); i++) {
			//This loop iterates through the no of instances in each classifier
			for (int j = 0; j < data.get(i).size(); j++) {
				Instance tInstance = data.get(i).get(j);
				List<Double> tPredVals = new ArrayList<Double>();
				//This loop is to iterate through the no of models
				System.out.println("k:");
				for (int k = 0; k < models.size(); k++) {
					Logistic tmodel = models.get(k);
					try {
						Double CurrPredVal = tmodel.classifyInstance(tInstance);
						System.out.println(CurrPredVal);
						tPredVals.add(CurrPredVal);
					} catch (Exception e) {
						System.out.println("Error in Classify Instance!!!");
					}
				}
				PredictedVals.add(tPredVals);
			}
		}
		int AccuracyCounter =0;
		int totalCount=0;
		if(data.get(0)!=null){
			Instances finInsatances = data.get(0);
			totalCount = finInsatances.size();
			for (int i = 0; i < finInsatances.size(); i++) {
				List<Double> ClassValues = PredictedVals.get(i);
				if(finInsatances.get(i).classValue()==MaxProbOutcome(ClassValues))
				{
					AccuracyCounter++;
				}
			}
		}
		return ((double)AccuracyCounter/totalCount)*100;
	}

	private double MaxProbOutcome(List<Double> classValues) {
		// TODO Auto-generated method stub
		Map<Double,Integer> Vals = new HashMap<Double, Integer>();
		for (Double val : classValues) {
			if(Vals.containsKey(val))
			{
				int newVal = Vals.get(val) +1;
				Vals.remove(val);
				Vals.put(val, newVal);
			}
			else
			{
				Vals.put(val, 1);
			}
		}
		Double Max=0.0;
		int MaxCount=0;
		for (Double key : Vals.keySet()) {
			int count = Vals.get(key);
			if(MaxCount<count)
			{
				MaxCount = count;
				Max = key;
			}
		}
		return Max;
	}

	public double CalculateTestAccuracy(List<Instances> testInstances) {
		double[][] predVals = new double[testInstances.get(0).size()][models.size()];
		for (int i = 0; i < models.size(); i++) {
			Logistic model = models.get(i);
			Instances inst = testInstances.get(i);
			for (int j = 0; j < inst.size(); j++) {
				Instance tInstance = inst.get(j);
				try {
					Double PredValue = model.classifyInstance(tInstance);
					predVals[j][i]=PredValue;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Error in Predicting values!!!");
					e.printStackTrace();
				}
			}
		}
		System.out.println("Testing");
		Helper h = new Helper();
		h.Print(predVals);
		
		int AccuracyCounter =0;
		int totalCount=0;
		if(testInstances.get(0)!=null){
			Instances finInsatances = testInstances.get(0);
			totalCount = finInsatances.size();
			for (int i = 0; i < finInsatances.size(); i++) {
				///List<Double> ClassValues = PredictedVals.get(i);
				double orgClassValue = finInsatances.get(i).classValue(); 
				double MaxPredVal = MaxProbOutcome(predVals[i]);
				System.out.println("orgClassValue"+orgClassValue+"MaxPredVal"+MaxPredVal);
				if(orgClassValue==MaxPredVal)
				{
					AccuracyCounter++;
				}
			}
		}
		return ((double)AccuracyCounter/totalCount)*100;

	}

}
