import java.io.BufferedReader;
import java.io.FileReader;

import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.core.Instances;


public class Start {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			weka.classifiers.functions.Logistic logi = new Logistic();
		//weka.classifiers.functions.supportVector.PolyKernel pk = new PolyKernel();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader("training.arff"));
			Instances data = new Instances(reader);
			data.setClassIndex(data.numAttributes() - 1);
			//pk.buildKernel(data);
			//pk.eval(arg0, arg1, arg2)
			logi.buildClassifier(data);
			for (int i = 0; i < data.size(); i++) {
				System.out.println("i="+i +"val-"+logi.classifyInstance(data.get(i)));
			}
			
			//svm.buildClassifier(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
