import java.util.Arrays;
import java.util.List;


public class Bigram {

	public double[][] BGCgivenBGPandUGC(double[][] bGP,double[] uGC)
	{
		// Calculating the Bigram Count Matrix from the probabilities
		double[][] bGC= new double[uGC.length][uGC.length];
		
		for(int i=0;i<uGC.length;i++)
		{
			for(int j=0;j<uGC.length;j++)
			{
				bGC[i][j]= uGC[i]*bGP[i][j];
			}
		}
		
		return bGC;
	}
	
	public void Print(double[][] n)
	{
		//printing the n*n matrix
		for(int i=0;i<n.length;i++)
		{
			for(int j=0;j<n.length;j++)
			{
				System.out.print(String.format("%25s",n[i][j]));
			}
			System.out.println();
		}
	}
	
	public void PrintUnigram(double[] n)
	{
		for(int i=0;i<n.length;i++)
		{
			System.out.print(String.format("%10s",n[i]));
			System.out.println();
		}
	}
	
	public double[][] BGProbability(double[][] bigramMat, double[] unigram) 
	{
		//calculating the bigram probability
		double[][] BigramProb = new double[bigramMat.length][bigramMat.length];
		for(int i=0;i<bigramMat.length;i++)
		{
			for(int j=0;j<bigramMat.length;j++)
			{
				if(unigram[i]!=0)
				{
					BigramProb[i][j] = (double)bigramMat[i][j]/unigram[i];
				}
				else
				{
					//divide by zero
					BigramProb[i][j]=-1;
				}
			}
		}
		return BigramProb;
	}
	
	public double[][] LSCount(double[][] bigramMat) 
	{
		//Laplace Add 1 matrix
		double[][] LSBigramProb = new double[bigramMat.length][bigramMat.length];
		for(int i=0;i<bigramMat.length;i++)
		{
			for(int j=0;j<bigramMat.length;j++)
			{
				LSBigramProb[i][j]=bigramMat[i][j]+1;
			}
		}
		return LSBigramProb;
	}
	
	public double[][] LSAdjustedCount(double[][] bGC,double[] uGC,double vCount) 
	{
		//calculating the adjusted count
		double[][] LSAdjstCnt = new double[bGC.length][bGC.length];
		for(int i=0;i<bGC.length;i++)
		{
			for(int j=0;j<bGC.length;j++)
			{
				LSAdjstCnt[i][j]=(bGC[i][j]+1.0)*(uGC[i]/(uGC[i]+vCount));
			}
		}
		return LSAdjstCnt;
	}

	public double[] AddVocab(double[] unigram,double count) 
	{
		//Adding V Count to the unigram matrix for LS Smoothing (N+V)
		// TODO Auto-generated method stub
		double[] unigram1 = new double[unigram.length];
		for(int i=0;i<unigram1.length;i++)
		{
			unigram1[i]=unigram[i]+count;
		}
		return unigram1;
	}
	
	public double[][] KatzBackOff(double[][] bigram,double[][] bigramCounts,double[] unigram,double[][] smoothed)
	{
		//Katz Back off
		double[][] kBO = new double[bigram.length][bigram.length];
		double[] Beta = new double[bigram.length];
		
		double[] alpha= new double[bigram.length];
		 
		for(int i=0;i<bigram.length;i++)
		{
			double temp =0;
			for(int j=0;j<smoothed.length;j++)
			{
				if(bigramCounts[i][j]>0)
				temp = temp + smoothed[i][j];
			}
			//calculating my beta values form the non zero probabilities 
			Beta[i] = (double)1 - temp;
			//calculating alpha values dividing by the corresponding unigram
			alpha[i] = Beta[i]/(1-unigram[i]);
		}
		System.out.println("beta:");
		PrintUnigram(Beta);
		System.out.println("alpha:");
		PrintUnigram(alpha);
		
		for(int i=0;i<bigram.length;i++)
		{
			for(int j=0;j<bigram.length;j++)
			{
				if(bigram[i][j]==0)
				{
					//calculating the backed off probability form the alpha and the unigram probability
					kBO[i][j]=alpha[i]*unigram[j];
				}
				else
				{
					//talking the Laplace smoothed prob for the non zero values
					kBO[i][j]=smoothed[i][j];
				}
			}
		}
		return kBO;
	}
	
	public double[] UnigramProbability(double[] unigram,double count)
	{
		//calculating the unigram probability
		double[] unigramP = new double[unigram.length];
		for(int i=0;i<unigram.length;i++)
		{
			unigramP[i] = unigram[i]/count;
		}
		return unigramP;
	}
	
	public double Perplexity(double[][]Bigram,String[] Vals,String text)
	{
		//Calculating the perplexity
		double Perplex = 1 ;
			String[] tokens = text.split(" ");
			//tokenizing the input string
			List<String> ValsList = Arrays.asList(Vals);
			
			for(int i=0;i<tokens.length-1;i++)
			{
				//ValsList.indexOf(tokens[i]) -> Gives the corresponding row value
				//ValsList.indexOf(tokens[i+1]) -> column value
				Perplex = Perplex * Bigram[ValsList.indexOf(tokens[i])][ValsList.indexOf(tokens[i+1])];
			}
			//calculating the -1/N th power
		return Math.pow(Perplex, -(1.0/(tokens.length-1)));
	}
}


