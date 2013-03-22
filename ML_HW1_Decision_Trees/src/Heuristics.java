import java.util.ArrayList;
import java.util.List;


public class Heuristics {
//Information Gain
	public int SelectBestColumnonInformationGain(int[][] Mat,int length)
	{
		int col=0;
		List<Double> vals = new ArrayList<Double>();
		double Entropy =CalcEntropy(Mat);
		System.out.println("Entropy :"+Entropy);
		//calculate entropy for the entire set		
		for(int i=0;i<length;i++)
		{
		
			double Gain = CalculateGain(Mat,i,Entropy);
			vals.add(Gain);
			System.out.println("Gain :"+Gain+"col:"+col+" i:"+i);
		}
		Helper H = new Helper();
		return H.Max(vals);
	}

private double CalculateGain(int[][] mat, int i, double Entropy) {
	// TODO Auto-generated method stub
	Helper H = new Helper();
	double probatColi =CalcProb(mat, i);
	//int[][] ZeroMat =
	double ZeroEntropy = CalcEntropy(H.ZeroSplitonData(mat, i));
	double OneEntropy = CalcEntropy(H.OneSplitonData(mat, i));
	System.out.println("Probati"+probatColi+"ZeroEntropy"+ZeroEntropy+"OneEntropy"+OneEntropy);
	double Gain = Entropy - (probatColi * ZeroEntropy) - (1-probatColi)*OneEntropy; 
	return Gain;
}


private double CalcProb(int[][]mat,int col)
{
	int zeroCounter=0;
	for(int i=0;i<mat.length;i++)
	{
		if(mat[i][col]==0)
			zeroCounter++;
	}
	//System.out.println("zero count"+zeroCounter);
	return (double)zeroCounter/mat.length;
}

private double CalcEntropy(int[][] mat) {
	// TODO Auto-generated method stub
	if(mat.length==0)
		return 0;
	int ZeroCounter = 0;
	int OneCounter = 0;
	
	for(int i=0;i<mat.length;i++)
	{
		if(mat[i][mat[i].length-1]==0)
			ZeroCounter++;
		else
			OneCounter++;
	}
	
	double ZeroProb = (double)ZeroCounter/mat.length;
	double OneProb = (double)OneCounter/mat.length;
	
	System.out.println("Zero Prob"+ZeroProb+"one prob"+OneProb);
	
	if(ZeroProb==0||OneProb==0)
	{
		if(ZeroProb==0)
			return -(OneProb)*(Math.log(OneProb)/Math.log(2));
		if(OneProb==0)
			return -(ZeroProb)*(Math.log(ZeroProb)/Math.log(2));
	}
	return  -(ZeroProb)*(Math.log(ZeroProb)/Math.log(2))-(OneProb)*(Math.log(OneProb)/Math.log(2)) ;
	
}

	public int SelectBestColumnon1StepLookAhead(int[][] Mat,int length)
	{
		List<Integer> Vals = new ArrayList<Integer>();
		
		for(int i=0;i<length;i++)
		{
			int ErrorCount = CountError(Mat,i);
			Vals.add(ErrorCount);
		}
		
		Helper H = new Helper();
		return H.Min(Vals);
	}

	private int CountError(int[][] mat, int col) {
		// TODO Auto-generated method stub
		Helper H = new Helper();
		int[][] ZeroMatrix = H.ZeroSplitonData(mat, col);
		int[][] OneMatrix = H.OneSplitonData(mat, col);
		if(ZeroMatrix.length==0||OneMatrix.length==0)
			return 1000000;
		int lcZero = GetLeastCount(ZeroMatrix);
		int lcOne = GetLeastCount(OneMatrix); 
		System.out.println("mat size"+mat.length+"zero:"+lcZero+"/"+ZeroMatrix.length+"one:"+lcOne+"/"+OneMatrix.length+"count"+(lcZero+lcOne)+"col:"+col);
		return lcZero+lcOne;
	}

	private int GetLeastCount(int[][] matrix) {
		// TODO Auto-generated method stub
		int zeroCount = 0 ;
		
		for(int i=0;i<matrix.length;i++)
		{
			if(matrix[i][matrix[i].length-1]==0)
				zeroCount++;
		}
		
		int oneCount = matrix.length - zeroCount;
		
		if(zeroCount>oneCount)
			return oneCount;
		else 
			return zeroCount;
	}

	public int SelectBestColumnonVarianceImpurity(int[][] mat,int length) {
		// TODO Auto-generated method stub
		List<Double> Vals = new ArrayList<Double>();
		
		for(int i=0;i<length;i++)
		{
			double varImpurity = CalcVarianceImpurity(mat,i);
			System.out.println("Variance Impurity -"+varImpurity);
			Vals.add(varImpurity);
		}
		
		Helper H = new Helper();
		return H.Max(Vals);
		
		
	}

	private double CalcVarianceImpurity(int[][] mat, int col) {
		// TODO Auto-generated method stub
		
		Helper H = new Helper();
		int[][] ZeroMatrix = H.ZeroSplitonData(mat, col);
		int[][] OneMatrix = H.OneSplitonData(mat, col);
		
		double vi = VI(CalcProb(mat,mat[0].length-1));
		
		double prob = CalcProb(mat,col);
		
		double zeroVi = VI(CalcProb(ZeroMatrix,mat[0].length-1));
		double oneVi = VI(CalcProb(OneMatrix,mat[0].length-1));
		
		System.out.println("vi"+vi+"prob"+prob+"zeroVi"+zeroVi+"oneVi"+oneVi);
		
		return vi - prob * zeroVi - (1-prob) * oneVi;
	}

	private double VI(double calcProb) {
		// TODO Auto-generated method stub
		return calcProb * (1-calcProb);
	}
}



/*private double CalculateGain(int[][] mat, int n, double entropy) {
// TODO Auto-generated method stub
int ZeroCounter = 0;
int OneCounter =0;
int leftclasZeroCounter = 0;
int leftclasOneCounter =0;
int rightclasZeroCounter = 0;
int rightclasOneCounter =0;

for(int i=0;i<mat.length;i++)
{
	if(mat[i][n]==1)
	{
		OneCounter++;
		
		if(mat[i][mat[i].length-1]==1)
			leftclasOneCounter++;
		else
			leftclasZeroCounter++;
			
	}
	else
	{
		ZeroCounter++;
		
		if(mat[i][mat[i].length-1]==1)
			rightclasOneCounter++;
		else
			rightclasZeroCounter++;
			
	}
}
//double zeroProb = (double)leftclasOneCounter/OneCounter;
double zeroentropy = -((double)leftclasOneCounter/OneCounter)*Math.log(((double)leftclasOneCounter/OneCounter))-((double)leftclasZeroCounter/OneCounter)*Math.log(((double)leftclasZeroCounter/OneCounter));
double oneentropy = -((double)rightclasOneCounter/OneCounter)*Math.log(((double)rightclasOneCounter/OneCounter))-((double)rightclasZeroCounter/OneCounter)*Math.log(((double)rightclasZeroCounter/OneCounter));		
double gain = entropy - (((double)OneCounter/mat.length)*zeroentropy) - (((double)ZeroCounter/mat.length)*oneentropy);
return gain;
}
*/