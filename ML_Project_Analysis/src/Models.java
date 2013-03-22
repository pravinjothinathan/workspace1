import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.core.Instance;
import weka.core.Instances;


public class Models {

	public void LogisticAnalysis(Instances data) {
		// TODO Auto-generated method stub
		
		System.out.println("Logistic :");
		
		Logistic model = new Logistic();
		int error=0;
		
		try{
			model.buildClassifier(data);
		}
		catch(Exception e){
			System.out.println("Error in Logistic model creation !!!");
			e.printStackTrace();
			return;
		}
		
		try{
			for (Instance instance : data) {
				double predictedVal = model.classifyInstance(instance);
				double actualVal = instance.classValue();
				//System.out.println("predicted value -"+predictedVal+"Actual value -"+actualVal);
				if(predictedVal!=actualVal)
				{
					//System.out.println("error++");
					error++;
				}
			}
		}
		catch(Exception e){
			System.out.println("Error in instance classification !!!");
			e.printStackTrace();
			return;
		}
		
		System.out.println("Percentage Accuracy :"+(1-(double)error/data.size())*100);
	}

	public void NaiveBayesAnalysis(Instances data) {
		// TODO Auto-generated method stub
		System.out.println("Naive Bayes :");
		
		NaiveBayes model = new NaiveBayes();
		int error=0;
		
		try {
			model.buildClassifier(data);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		try{
			for (Instance instance : data) {
				double predictedVal = model.classifyInstance(instance);
				double actualVal = instance.classValue();
				if(predictedVal!=actualVal)
					error++;
			}
		}
		catch(Exception e){
			System.out.println("Error in instance classification !!!");
			e.printStackTrace();
			return;
		}
		
		System.out.println("Percentage Accuracy :"+(1-(double)error/data.size())*100);
	}

	public void SupportVectorAnalysis(Instances data) {
		// TODO Auto-generated method stub
		System.out.println("SMO Polynomial Kernel :");
		
		SMO model = new SMO();
		int error=0;
		try {
			model.setOptions(weka.core.Utils.splitOptions("-C 1.0 -L 0.0010 -P 1.0E-12 -N 0 -V -1 -W 1 -K \"weka.classifiers.functions.supportVector.PolyKernel -C 250007 -E 1.0\""));
			model.buildClassifier(data);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		try{
			for (Instance instance : data) {
				double predictedVal = model.classifyInstance(instance);
				double actualVal = instance.classValue();
				if(predictedVal!=actualVal)
					error++;
			}
		}
		catch(Exception e){
			System.out.println("Error in instance classification !!!");
			e.printStackTrace();
			return;
		}
		
		System.out.println("Percentage Accuracy :"+(1-(double)error/data.size())*100);
		
	}

	public void KNNAnalyis(Instances data) {
		System.out.println("KNN Analysis");
		
		IBk model = new IBk();
		
		int error=0;
		try {
			model.setOptions(weka.core.Utils.splitOptions("-K 5 -W 0 -X -E -A \"weka.core.neighboursearch.LinearNNSearch -A \\\"weka.core.EuclideanDistance -R first-last\\\"\""));
			model.buildClassifier(data);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error in KNN model creation!!!");
			return;
		}
		
		try{
			for (Instance instance : data) {
				double predictedVal = model.classifyInstance(instance);
				double actualVal = instance.classValue();
				if(predictedVal!=actualVal)
					error++;
			}
		}
		catch(Exception e){
			System.out.println("Error in instance classification !!!");
			e.printStackTrace();
			return;
		}
		
		System.out.println("Percentage Accuracy :"+(1-(double)error/data.size())*100);
		// TODO Auto-generated method stub
		
	}	
}
