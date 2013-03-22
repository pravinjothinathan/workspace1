import java.io.File;
import java.io.FilenameFilter;


public class Start {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String TrainPath = args[0];
		String TestPath = args[1];
		
		System.out.println("Train Folder :"+TrainPath);
		System.out.println("Test Folder :"+TestPath);
		
		File files = new File(TrainPath);
		File testFiles = new File(TestPath);
		
		FilenameFilter filter = new FilenameFilter() { 
            public boolean accept(File dir, String filename)
                 { return filename.endsWith(".arff"); }
		};
		
		String[] lstFiles = files.list(filter);
		String[] lstTestFiles = testFiles.list(filter);
		
		LogisticModel lm = new LogisticModel(lstFiles,TrainPath);
		TestData tstData = new TestData(lstTestFiles, TestPath);
		
		double LogisticAccuracy = lm.CalculateTrainingAccuracy();//lm.getAccuracy();
		double LogisticTestAccuracy = lm.CalculateTestAccuracy(tstData.getTestInstances());
		
		System.out.println("LogisticAccuracy"+LogisticAccuracy);
		System.out.println("LogisticTestAccuracy"+LogisticTestAccuracy);
	}

}
