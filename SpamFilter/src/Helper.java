import java.util.List;
import java.util.Map;


public class Helper {
	
	public void Print(String[] inputs)
	{
		for(int i=0;i<inputs.length;i++)
		{
			System.out.println(inputs[i]);
		}
	}

	public Map<String, Integer> getVocab(
			Map<Integer, FilewithClassInfo> totFiles) {
		// TODO Auto-generated method stub
		for(int i=0;i<totFiles.size();i++)
		{
			FilewithClassInfo temp = totFiles.get(i);
			
			Map<String,Integer> temp1 = temp.WordMap;
			
			for(int j=0;j<temp1.size();j++)
			{
				
			}
		}
		return null;
	}

	public int[][] formMatrixfromdata(List<String> vocab,
			Map<Integer, FilewithClassInfo> totFiles) {
		// TODO Auto-generated method stub
		
		int[][] matrix = new int[totFiles.size()][vocab.size()];
		
		for(int i=0;i<totFiles.size();i++)
		{
			String[] StringsinFile = (String[])totFiles.get(i).WordMap.keySet().toArray(new String[0]);
			
			FilewithClassInfo currFileInfo = totFiles.get(i);
			
			for(int j=0;j<StringsinFile.length;j++)
			{
				Map<String,Integer> temp = currFileInfo.WordMap;
				
				int val = temp.get(StringsinFile[j]);
				
				int colIndex = vocab.indexOf(StringsinFile[j]);
				
				matrix[i][colIndex]=val;
			}
		}
		
		return matrix;
	}

	public int[] getClassInfo(Map<Integer, FilewithClassInfo> totFiles) {
		// TODO Auto-generated method stub
		int[] classInfo = new int[totFiles.size()];
		
		for(int i=0;i<totFiles.size();i++)
		{
			classInfo[i]= totFiles.get(i).ClassInfo;
		}
		return classInfo;
	}

	public void PrintMap(Map<String, Integer> map) {
		// TODO Auto-generated method stub
		for (String token : map.keySet()) {
			System.out.println(token +":"+map.get(token));
		}
	}

	public int getMaxValIndex(List<Double> vals) {
		// TODO Auto-generated method stub
		double max = vals.get(0);
		int index =0;
		for(int i=1;i<vals.size();i++)
		{
			if(max<vals.get(i))
			{
				max = vals.get(i);
				index = i;
			}
		}
		return index;
	}

	public void Print(double[] weightMatrix) {
		// TODO Auto-generated method stub
		System.out.println("Weight Matrix");
		for (double d : weightMatrix) {
			System.out.println(d);
		}
	}

	public int[] getCountsfromMap(Map<String, Integer> wordMap, List<String> vocab) {
		// TODO Auto-generated method stub
		int[] countMat = new int[vocab.size()];
		
		for(int i=0;i<vocab.size();i++)
		{
			if(wordMap.containsKey(vocab.get(i)))
			{
				countMat[i]=wordMap.get(vocab.get(i));
			}
			else
			{
				countMat[i]=0;
			}
		}
		return countMat;
	}

}