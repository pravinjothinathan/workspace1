import java.util.List;


public class EMHelper {

	//this function generates random mus for the first step in E
	//Basically a randomizer function
	public double[] GetRandomMU(List<Double> data, int i) {
		double[] mus = new double[i];
		for (int j = 0; j < i;j++) {
			mus[j] = data.get((int)(Math.random() * data.size()));
			//mus.add(data.get(temp));
		}
		return mus;
	}

	public Double[][] NormalizeProb(Double[][] classProb) {
		if(classProb==null)
			return null;
		int size1 = classProb.length;
		int size2 = classProb[0].length;
		for (int i = 0; i < size1; i++) {
			double sum=0;
			for (int j = 0; j < size2; j++) {
				sum += classProb[i][j];
			}
			for (int j = 0; j < size2; j++) {
				classProb[i][j] = classProb[i][j]/sum;
			}
		}
		return classProb;
	}

	public static double[] StoreVals(double[] mus, double[] sigma, double[] prob) {
		// TODO Auto-generated method stub
		double[] prevParams = new double[3*3];
		for (int i = 0; i < prevParams.length; i++) {
			if(i<3)
			{
				prevParams[i]=mus[i];
			}
			else if(i<6)
			{
				prevParams[i]=sigma[i-3];
			}
			else
			{
				prevParams[i]=prob[i-6];
			}
		}
		return prevParams;
	}

	public static boolean CheckData(double[] prevParams, double[] mus,
			double[] sigma, double[] prob) {
		// TODO Auto-generated method stub
		//boolean gottaloop = false;
		for (int i = 0; i < prevParams.length; i++) {
			if(i<3)
			{
				if(prevParams[i]!=mus[i])
					return true;
			}
			else if(i<6)
			{
				if(prevParams[i]!=sigma[i-3])
					return true;
			}
			else
			{
				if(prevParams[i]!=prob[i-6])
					return true;
			}
		}
		return false;	
	}

}
