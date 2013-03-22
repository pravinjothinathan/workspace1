import java.util.ArrayList;
import java.util.List;

//import java.util.StringTokenizer;


public class Tokenizer {
	
	public String[] Tokenize(String S)
	{
		//S = S.replace(".", "");
		return S.split(" ");
	}
	
	public void PrintTokens(String[] S)
	{
		for(int i=0;i<S.length;i++)
		{
			System.out.println(S[i]);
		}
	}
	
	public String[] DistinctTokens(String Corpus)
	{
		List<String> DistinctStrings = new ArrayList<String>();
		
		String[] CorpusWords = Corpus.split(" ");
		
		for(int i=0;i<CorpusWords.length;i++)
		{
			if(DistinctStrings.contains(CorpusWords[i])==false)
			{
				DistinctStrings.add(CorpusWords[i]);
			}
		}
		
		String[] temp = DistinctStrings.toArray(new String[0]);
		
		return temp;
	}

}
