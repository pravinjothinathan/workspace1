
public class Input {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double[][] Vals = {{0.0,	0.85,	0.1,	0.01,	0.04,	0.0},
				   {0.0,	0.0,	0.7,	0.1,	0.1,	0.09},
				   {0.0,	0.0,	0.0,	0.85,	0.1,	0.04},
				   {0.0,	0.0,	0.0,	0.0,	0.8,	0.1},
				   {0.0,	0.2,	0.3,	0.1,	0.0,	0.3},
				   {1.0,	0.0,	0.0,	0.0,	0.0,	0.0}};
		//she speaks french often
		//French = 500, often =1000, speaks = 2000 and She = 4000
		//corpus has 250000
		double[] unigram={2500,4000,2000,500,1000,2500};
		double VCount =6;
		//double Wcount = 4000 +2000 +500 +1000;
		
		Bigram B = new Bigram();
		
		double[][] BigramCounts = B.BGCgivenBGPandUGC(Vals,unigram);
		//Problem2 a
		System.out.println("Problem2 a-");
		System.out.println("BG Count:");
		B.Print(BigramCounts);
		
		//Problem2 b
//		She Speaks French => p<s><She> * p <She><Speaks> * p<Speaks><French> *p<French></s>
		System.out.println("Problem2 b-");
		double pSent1 = 0.85 * 0.7 * 0.85 * 0.1;
//		She Speaks French Often => p<s><she> * p<She><Speaks> * p<Speaks><French> *p<French><Often> *p<Often></s>
		double pSent2 = 0.85 * 0.7 * 0.85 * 0.8 * 0.3;
		
		System.out.println("Probability of She Speaks French      :"+ pSent1);
		System.out.println("Probability of She Speaks French Often:"+ pSent2);

		//problem2 c(i)
		System.out.println("Problem2 c(i)-");
		//Adjusted Counts
		double[][] LSAdjstCount = B.LSAdjustedCount(BigramCounts,unigram,VCount);
		System.out.println("LS Adjst Count:");
		B.Print(LSAdjstCount);
		
		//Checking LS
		double[][] LSCount = B.LSCount(BigramCounts);
		
		System.out.println("LS BG Count:");
		B.Print(LSCount);
		//problem2 c(ii)
		System.out.println("Problem2 c(ii)-");
		double[] LSUnigram = B.AddVocab(unigram, VCount);
		double[][] LSProb1 = B.BGProbability(LSCount,LSUnigram);
		System.out.println("LS Prob1:");
		B.Print(LSProb1);
		//problem2 d
		System.out.println("Problem2 d");
		double[][] bigram2= {
							{0.0,	0.85,	0.1,	0.01,	0.04,	0.0,	0.0},
							{0.0,	0.0,	0.7,	0.1,	0.1,	0.01,	0.09},
							{0.0,	0.0,	0.0,	0.85,	0.1,	0.01,	0.04},
							{0.0,	0.0,	0.0,	0.0,	0.8,	0.1,	0.1},
							{0.0,	0.2,	0.3,	0.1,	0.0,	0.1,	0.3},
							{0.0,	0.02,	0.0,	0.2,	0.6,	0.0,	0.0},
							{1.0,	0.0,	0.0,	0.0,	0.0,	0.0,	0.0},
							};
		
		double[] unigram2 = {2500,4000,2000,500,1000,15000,2500};
		double[][] BigramCounts2 = B.BGCgivenBGPandUGC(bigram2,unigram2);
		System.out.println("BigramCounts2 :");
		B.Print(BigramCounts2);

		double[][] LSCount2 = B.LSCount(BigramCounts2);
		double count2 = 2500+4000+2000+500+1000+15000+2500;
		System.out.println("Count 2: "+ count2);
		
		double[] LSUnigram2 = B.AddVocab(unigram2, 7);
		double[][] LSProb2 = B.BGProbability(LSCount2,LSUnigram2);
		System.out.println("LS Prob2:");
		B.Print(LSProb2);
		
		double[] unigramP2 = B.UnigramProbability(unigram2, count2);
		System.out.println("Unigram Prob 2:");
		B.PrintUnigram(unigramP2);
		
		double[][] KatzBO = B.KatzBackOff(bigram2, BigramCounts2,unigramP2,LSProb2);
		System.out.println("Katz Backoff :");
		B.Print(KatzBO);
		
		//problem2 e
		String Sample = "<s> home speaks she often french </s>";
		System.out.println("Problem2 e - Sentence : "+ Sample);
		//Perplexity
		String[] Set = {"<s>","she","speaks","french","often","home","</s>"};
		
		System.out.println("Perplexity using LS : "+B.Perplexity(LSProb2, Set, Sample));
		System.out.println("Perplexity using KATZ : "+B.Perplexity(KatzBO, Set, Sample));
		
		System.out.print(Math.pow((double)0.000000010022330244, -(1/5)));
		System.out.print(Math.pow((double)0.0000000039133489, -(1/5)));
		
	}

}
