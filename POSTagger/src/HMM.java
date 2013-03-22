import java.util.Arrays;
import java.util.List;


public class HMM {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		double[][] A ={	{0.378521127,	0.316901408,	0.088028169,	0.002640845,	0.001760563,	0.107394366,	0.100352113,	0.003521127,	0.000880282},
						{0.0 		,	0.377246103,	0.004963765, 	0.003971012, 	0.002978259, 	0.476521394, 	0.129057877, 	0.000297826, 	0.004963765},
						{0.000258777,	0.138014319,	0.146640214,	0.198395584,	0.215647373,	0.003450358,	0.004312947,	0.189769689,	0.103510739},
						{0.167119565,	0.194293478,	0.002717391,	0.005434783,	0.006793478,	0.3125,			0.108695652,	0.012228261,	0.190217391},
						{0.089005236,	0.479057592,	0.002617801,	0.010471204,	0.013089005,	0.002617801,	0.007853403,	0.002617801,	0.392670157},
						{0.043360434,	0.474254743,	0.005420054,	0.016260163,	0.008130081,	0.002710027,	0.010840108,	0.005420054,	0.433604336},
						{0.205724508,	0.001788909,	0.107334526,	0.134168157,	0.152057245,	0.295169946,	0.098389982,	0.002683363,	0.002683363},
						{0.005110733,	0.05451448,		0.357751278,	0.068143101,	0.051107325,	0.001703578,	0.068143101,	0.001703578,	0.391822828},
						{0.001,			0.310596106,	0.215028073,	0.203082069,	0.262812089,	0.0011946,		0.004778402,	0.00011946,		0.0011946}
		};

		double[][]B ={
				{0.5,	0.5,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0},
				{0.0,	0.0,	0.19,	0.24,	0.18,	0.23,	0.0,	0.0,	0.028,	0.09,	0.04,	0.0,	0.0,	0.0},
				{0.0,	0.0,	0.41,	0.14,	0.44,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0},
				{0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	1.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0},
				{0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.6,	0.39,	0.0,	0.0,	0.0,	0.0,	0.0},
				{0.0,	0.0,	0.0,	0.0,	0.06,	0.08,	0.0,	0.0,	0.12,	0.31,	0.42,	0.0,	0.0,	0.0},
				{0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	1.0,	0.0,	0.0},
				{0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.5,	0.5}
		};
		
		
		String[] BRows ={"DT","NN","VB","VBZ","VBN","JJ","RB","IN"};
		String[] BCols ={"a","the","chair","chairman","board","road","is","was","found","middle","bold","completely","in","of"};
		String Sentence1="the chairman of the board is completely bold";
		String Sentence2="a chair was found in the middle of the road";
		ViterbiItem[][] VB1  = ViterbiMatrix(A,B,BRows,BCols,Sentence1);
		ViterbiItem[][] VB2  = ViterbiMatrix(A,B,BRows,BCols,Sentence2);
		System.out.println("Viterbi Matrix of Sentence 1:");
		Print(VB1);
		System.out.println("Viterbi Matrix of Sentence 2:");
		Print(VB2);
		
	}

	private static ViterbiItem[][] ViterbiMatrix(double[][] a, double[][] b,
			String[] bRows, String[] bCols, String sentence1) 
	{
		// TODO Auto-generated method stub
		Tokenizer T1 = new Tokenizer();
		String[] s1Tokens = T1.Tokenize(sentence1);
		//double[][] ViterbiMatrix = new double[bRows.length][s1Tokens.length];
		ViterbiItem[][] ViterbiMatrix = new ViterbiItem[bRows.length][s1Tokens.length];
		//List<String> lstbRows = Arrays.asList(bRows);
		List<String> lstbCols = Arrays.asList(bCols);
		
		for(int i=0;i<bRows.length;i++)
		{
			for(int j=0;j<s1Tokens.length;j++)
			{
				ViterbiMatrix[i][j] = new ViterbiItem(); 
			}
		}
		//first column
		for(int i=0;i<bRows.length;i++)
		{
			ViterbiMatrix[i][0].Probability = a[0][i]*b[i][lstbCols.indexOf(s1Tokens[0])];
			ViterbiMatrix[i][0].POS= bRows[i];
		}
		//furthur columns
		for(int i=1;i<s1Tokens.length;i++)
		{
			for(int j=0;j<bRows.length;j++)
			{
				//int max;
				double max = 0;
				int Pos=0;
				//maximizing logic
				for(int k=0;k<bRows.length;k++)
				{
					//System.out.println("Viterbi "+ViterbiMatrix[k][i-1]+"Transition Matrix:"+a[j][k]);
					double temp = ViterbiMatrix[k][i-1].Probability*a[j][k];
					if(temp>max)
					{
						max=temp;
						Pos = k;
					}
				}
				ViterbiMatrix[j][i].Probability = max*b[j][lstbCols.indexOf(s1Tokens[i])];
				if(ViterbiMatrix[j][i].Probability!=0)
				{
						ViterbiMatrix[j][i].contributor = Pos;
						ViterbiMatrix[j][i].POS= bRows[j];
				}
			}
		}
		//final probability
		double max=0;
		int ipos=0;
		for(int i=0;i<bRows.length;i++)
		{
			double temp = ViterbiMatrix[i][s1Tokens.length-1].Probability*a[i][bRows.length];
			if(max<temp)
			{
				max =temp;
				ipos = i;
			}
		}
		
		//Back Track
		String temp="";
		//int j=0;
		for(int i=s1Tokens.length-1;i>0;i--)
		{
			temp =  s1Tokens[i]+ ":"+ViterbiMatrix[ipos][i].POS+" "+temp;
			ipos = ViterbiMatrix[ipos][i].contributor;
		}
		temp =  s1Tokens[0]+ ":"+ViterbiMatrix[ipos][0].POS+" "+temp;
		
		System.out.println("Final Prob :"+max);
		System.out.println("POS - "+temp);
		
		return ViterbiMatrix;
	}
	
	static void Print(ViterbiItem[][] n)
	{
		for(int i=0;i<n.length;i++)
		{
			for(int j=0;j<n[0].length;j++)
			{
				System.out.print(String.format("%25s:%3s:%2s", n[i][j].Probability,n[i][j].POS,n[i][j].contributor));
			}
			System.out.println();
		}
	}

}
