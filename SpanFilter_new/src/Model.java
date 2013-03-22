import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {

	public double[] getNBPriors(Map<Integer, FilewithClassInfo> totFiles) {
		// TODO Auto-generated method stub
		double[] priors = new double[2];
		
		for(int j=0;j<2;j++)
		{
			int counter =0;
			
			for(int i=0;i<totFiles.size();i++)
			{
				if(totFiles.get(i).ClassInfo==j)
					counter++;
			}
			
			priors[j] = (double)counter/totFiles.size();
			System.out.println("priors - j"+j+"-"+priors[j]);
		}
		return priors;
	}

	public Map<Integer,Map<String,Double>> getConditionalProb(
			Map<Integer, FilewithClassInfo> totFiles, List<String> vocab) {
		// TODO Auto-generated method stub
		int counter =0;
		Map<Integer,Map<String,Double>> CondProb = new HashMap<Integer, Map<String,Double>>();
		for(int j=0;j<2;j++)
		{
			Map <String,Integer> map = new HashMap<String, Integer>();
			int count =0;
			counter =0;
			
			
			for(int i=0;i<totFiles.size();i++)
			{
				if(totFiles.get(i).ClassInfo==j)
				{
					counter++;
					Map<String,Integer> temp = totFiles.get(i).WordMap;
					
					for (String word : temp.keySet()) {
						if(map.containsKey(word)) 
						{
							int n = map.get(word);
							int currn = temp.get(word);
							count = count + currn;
							map.remove(word);
							map.put(word, n+currn);
						}
						else
						{
							count = count+temp.get(word);
							map.put(word, temp.get(word));
						}
					}
				}
			}
			
			System.out.println("counter :"+counter);
			Map<String,Double> probTable = new HashMap<String, Double>();
			int c=0;
			for (String token : vocab) 
			{
				if(map.containsKey(token))
				{
					//System.out.println("token :"+token+"count :"+map.get(token));
					probTable.put(token, (double)(map.get(token)+1)/(count+vocab.size()));
				}
				else
					probTable.put(token, (double)(1)/(count+vocab.size()));
			}
			
			CondProb.put(j, probTable);
			System.out.println("count per classs :"+map.size()+" count :"+count+" j val"+j+" c val"+c);
		}
		return CondProb;
	}

	public void TestNBModel(Map<Integer, FilewithClassInfo> filesToTest,
			double[] priors, Map<Integer, Map<String, Double>> condProb) {
		// TODO Auto-generated method stub
		
		//int percentageErrorCounter = 0;
		int percentageAccuracyCounter = 0;
		int percentageErrorCounter = 0;
		Helper h = new Helper();
		
		for(int  i=0;i<filesToTest.size();i++)
		{
			List<Double> vals = new ArrayList<Double>();
			for(int j=0;j<2;j++)
			{
				Map<String,Double> probMap = condProb.get(j);
				double prob = Math.log10(priors[j]);
				
				for (String token : filesToTest.get(i).WordMap.keySet()) {
					if(probMap.containsKey(token))
						prob += Math.log10(probMap.get(token));
				}
				
				vals.add(prob);
			}
			
			if(filesToTest.get(i).ClassInfo == h.getMaxValIndex(vals))
				percentageAccuracyCounter++;
			else
				percentageErrorCounter++;
				
		}
		
		System.out.println("percentageAccuracyCounter"+percentageAccuracyCounter+"percentageErrorCounter"+percentageErrorCounter);
		System.out.println("Percentage : "+ (double)(percentageAccuracyCounter*100)/filesToTest.size());
	}

	public double[] LinerRegressionMCAP(int[][] filetoWordcountMatrix,
			int[] fileClassInfo, double eta, double lambda,int iterations, Map<Integer, FilewithClassInfo> filesToTest, List<String> vocab) {
		// TODO Auto-generated method stub
		if(filetoWordcountMatrix==null)
			return null;
		double bestpercentageAccuracyCounter = 0;
		/*to test*/
		Helper h = new Helper();
		double[] returnweight = new double[filetoWordcountMatrix[0].length+1];
		System.out.println("z size - "+filetoWordcountMatrix.length+"Y Size"+filetoWordcountMatrix[0].length);
		
		double[] weight = new double[filetoWordcountMatrix[0].length+1];
		
		for(int iter=0;iter<iterations;iter++)
		{
			//System.out.println(iter);
		double[] phat = PreComputePHat(weight,filetoWordcountMatrix);
		
		for(int i=0;i<weight.length;i++)
		{
			if(i==0)
			{
				double jterm=0;
				
				for(int j=0;j<filetoWordcountMatrix.length-1;j++)
				{
					
					 jterm += ((double)fileClassInfo[j]-phat[j]/*PHat(weight,filetoWordcountMatrix[j])*/);
				}
				//System.out.print(weight[i]);
				weight[i] = weight[i] + eta * ( (-lambda * weight[i]) + jterm);
				//System.out.println("\t"+weight[i]);
				
			}
			else
			{
				double jterm=0;
				
				for(int j=0;j<filetoWordcountMatrix.length-1;j++)
				{
					
					 jterm += filetoWordcountMatrix[j][i-1] * ((double)fileClassInfo[j]-phat[j]/*PHat(weight,filetoWordcountMatrix[j])*/);
				}
				//System.out.print(weight[i]);
				weight[i] = weight[i] + eta * ( (-lambda * weight[i]) + jterm);
				//System.out.println("\t"+weight[i]);
			}
		}
		
		//Testing
		int PercentAccuracy =0;
		int PercentError =0;
		for(int t=0;t<filesToTest.size();t++)
		{
			int [] countMat = h.getCountsfromMap(filesToTest.get(t).WordMap,vocab);
			
			double result = DotProduct(weight, countMat);
			
			int classInfo = filesToTest.get(t).ClassInfo;
			
			if((result>0 && classInfo==1)||(result<=0 && classInfo==0))
			{
				PercentAccuracy++;
			}
			else
			{
				PercentError++;
			}
		}
		double temp = (double)PercentAccuracy*100/filesToTest.size();
		if(temp > bestpercentageAccuracyCounter)
		{
			returnweight = weight;
			bestpercentageAccuracyCounter = temp;
			System.out.println(iter);
			System.out.println("total "+filesToTest.size()+" Accuracy "+temp/*(double)PercentAccuracy*100/filesToTest.size()*/+" Percent Error "+(double)PercentError*100/filesToTest.size());
		}
		}
		return returnweight;
	}

	private double[] PreComputePHat(double[] weight,
			int[][] filetoWordcountMatrix) {
		// TODO Auto-generated method stub
		double[] pHat = new double [weight.length-1];
		for(int i=0;i<filetoWordcountMatrix.length;i++)
			pHat [i] = PHat(weight,filetoWordcountMatrix[i]);
		return pHat;
	}

	private double PHat(double[] weight, int[] is) {
		// TODO Auto-generated method stub
		double sum = DotProduct(weight, is);
		///double num = (sum);
		//if(sum!)
		//double den = Math.log(1+Math.exp(-sum));
		//System.out.println("num-"+num+" den"+den);
		return 1/(1+Math.exp(-sum));
		//return (num-den);
	}
	
	private double DotProduct(double[] weight,int[] is)
	{
		double sum = weight[0];
		
		for(int i=0;i<is.length;i++)
			sum += weight[i+1]*is[i];
		
		return sum;
	}

	public void LearnPerceptron(int[][] filetoWordcountMatrix,
			int[] fileClassInfo, double eta, int iterations,
			Map<Integer, FilewithClassInfo> filesToTest, List<String> vocab) {
		double[] weight = new double[filetoWordcountMatrix[0].length+1];
		double bstPercentageAcc = 0;
		String toPrint ="";
		for(int i=0;i<iterations;i++)
		{
			for (int j = 0; j < filetoWordcountMatrix.length; j++) 
			{
				int tminuso = fileClassInfo[j] - GetClass(weight,filetoWordcountMatrix[j]);
				weight[0] += eta * tminuso;
				for (int k = 1; k < filetoWordcountMatrix[0].length; k++) 
				{
					weight[k] += eta * tminuso * filetoWordcountMatrix[j][k-1]; 
				}
			}
			//Testing
			Helper h = new Helper();
			int PercentAccuracy =0;
			int PercentError =0;
			for(int t=0;t<filesToTest.size();t++)
			{
				int [] countMat = h.getCountsfromMap(filesToTest.get(t).WordMap,vocab);
				double result = DotProduct(weight, countMat);
				int classInfo = filesToTest.get(t).ClassInfo;
				if((result>0 && classInfo==1)||(result<=0 && classInfo==0))
				{
					PercentAccuracy++;
				}
				else
				{
					PercentError++;
				}
			}
			double temp = (double)PercentAccuracy*100/filesToTest.size();
			if(temp>bstPercentageAcc)
			{
				bstPercentageAcc = temp;
				toPrint = "iter "+ i +" Accuracy "+temp/*(double)PercentAccuracy*100/filesToTest.size()*/+" Percent Error "+(double)PercentError*100/filesToTest.size();
			}
		}
		
		System.out.println(toPrint);
				
	}

	private int GetClass(double[] weight, int[] is) {
		// TODO Auto-generated method stub
		double tempobjVal = DotProduct(weight, is);
		
		if(tempobjVal>0)
			return 1;
		else
			return 0;
	}

}