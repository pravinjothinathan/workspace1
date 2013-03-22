import java.util.List;


public class Helper {

	public void Print(List<Double> mus) {
		// TODO Auto-generated method stub
		for (int i = 0; i < mus.size(); i++) {
			System.out.println("i:"+(i+1)+"val:"+mus.get(i));
		}
	}

	public void Print(Double[][] classProb) {
		// TODO Auto-generated method stub
		if (classProb == null)
				return;
		int size1 = classProb.length;
		int size2 = classProb[0].length;
		
		for (int i = 0; i < size1; i++) {
			for (int j = 0; j < size2; j++) {
				System.out.print(classProb[i][j]+"\t");
			}
			System.out.println();
		}
	}

	public void Print(Double[] mus) {
		// TODO Auto-generated method stub
		for (int i = 0; i < mus.length; i++) {
			System.out.println("i:"+(i+1)+"val:"+mus[i]);
		}
	}

	public void Print(double[] mus) {
		// TODO Auto-generated method stub
		for (int i = 0; i < mus.length; i++) {
			System.out.println("i:"+(i+1)+"val:"+mus[i]);
		}
	}

}
