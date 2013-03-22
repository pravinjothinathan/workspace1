import java.util.List;
import java.util.Map;


public class Start {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String strTrainingFiles = args[0];
		String strTestFiles = args[1];
		String strStopWordFile = args[2];
		double[] eta = { 0.1, 0.01, 0.001, 0.0001, 0.00001};
		int[] iterations= { 25 , 50, 100, 250, 500};
		
		System.out.println(args[0]);
		System.out.println(args[1]);
		System.out.println(args[2]);
		
		Model M = new Model();
		
	//	System.out.println("Naive Bayes ");
		FileManager fm = new FileManager();
		Map<Integer,FilewithClassInfo> totFiles = fm.FetchData(strTrainingFiles);		
		System.out.println("tot files :" +totFiles.size());
		List<String> vocab = fm.getUniqueWords(); 
		System.out.println("vocab - "+vocab.size());
		//double[] priors = M.getNBPriors(totFiles);
		//Map<Integer,Map<String,Double>> condProb = M.getConditionalProb(totFiles,vocab);
		FileManager fm2 = new FileManager();
		Map<Integer,FilewithClassInfo> filesToTest = fm2.FetchData(strTestFiles);
		//M.TestNBModel(filesToTest,priors,condProb);
		
		//Linear regression
		Helper h = new Helper();
		////System.out.println("Linear Regression ");
		System.out.println("Perceptron:");
		int[][] filetoWordcountMatrix = h.formMatrixfromdata(vocab,totFiles);
		int[] fileClassInfo = h.getClassInfo(totFiles);
		
		//for(int )
		for (int i = 0; i < iterations.length; i++) {
			for (int j = 0; j < eta.length; j++) {
				System.out.println("Iterations - "+iterations[i]+"Eta -"+eta[j]);
				M.LearnPerceptron(filetoWordcountMatrix,fileClassInfo,eta[i],iterations[i],filesToTest,vocab);
			}
		}
		
		System.out.println("With Stop Words");
		FileManager fmtest = new FileManager();
			StopWords sw = new StopWords(strStopWordFile);
			List<String> lststopWords = sw.getStopWords();
			Map<Integer,FilewithClassInfo> totFileswithSw = fmtest.FetchData(strTrainingFiles,lststopWords);
		//	System.out.println("tot files :" +totFileswithSw.size());
			List<String> vocabSw = fmtest.getUniqueWords(); 
			int[][] filetoWordcountMatrixsw = h.formMatrixfromdata(vocabSw,totFileswithSw);
			FileManager fm2test = new FileManager();
			Map<Integer,FilewithClassInfo> filesToTestsW = fm2test.FetchData(strTestFiles,lststopWords);
			for (int i = 0; i < iterations.length; i++) {
				for (int j = 0; j < eta.length; j++) {
					System.out.println("Iterations - "+iterations[i]+"Eta -"+eta[j]);
					M.LearnPerceptron(filetoWordcountMatrixsw,fileClassInfo,eta[i],iterations[i],filesToTestsW,vocabSw);
				}
			}
		///*double[] weightMatrix = */M.LinerRegressionMCAP(filetoWordcountMatrix,fileClassInfo,eta,lambda,iterations,filesToTest,vocab);
		//Testing files
		//int[][] tstfiletoWordcountMatrix = h.formMatrixfromdata(vocab,filesToTest);
		//int[] tstfileClassInfo = h.getClassInfo(filesToTest);

		///System.out.println("Arff File Creation");
		//FileHandler fh = new FileHandler();
		//fh.WriteArff("train_ds2.arff",filetoWordcountMatrix,fileClassInfo,vocab,"train_spam_ham");
		//fh.WriteArff("test_ds2.arff",tstfiletoWordcountMatrix,tstfileClassInfo,vocab,"tst_spam_ham");
		
		//NB After Stop words implementation
		//System.out.println("Naive Bayes without Stopwords ");
	///	FileManager fmtest = new FileManager();
	//	StopWords sw = new StopWords(strStopWordFile);
	//	List<String> lststopWords = sw.getStopWords();
	//	Map<Integer,FilewithClassInfo> totFileswithSw = fmtest.FetchData(strTrainingFiles,lststopWords);
	//	System.out.println("tot files :" +totFileswithSw.size());
	//	List<String> vocabSw = fmtest.getUniqueWords(); 
	//	System.out.println("vocab - "+vocabSw.size());
	//	double[] priorsSw = M.getNBPriors(totFileswithSw);
	//	Map<Integer,Map<String,Double>> condProbSw = M.getConditionalProb(totFileswithSw,vocabSw);
///		FileManager fm2test = new FileManager();
//		Map<Integer,FilewithClassInfo> filesToTestsW = fm2test.FetchData(strTestFiles,lststopWords);
//		M.TestNBModel(filesToTestsW,priorsSw,condProbSw);
//		
	//	System.out.println("Linear Regression without Stopwords ");
	//	int[][] filetoWordcountMatrixsw = h.formMatrixfromdata(vocabSw,totFileswithSw);
		///*double[] weightMatrixsw = */M.LinerRegressionMCAP(filetoWordcountMatrixsw,fileClassInfo,eta,lambda,iterations,filesToTestsW,vocabSw);
		
		

	}

}
