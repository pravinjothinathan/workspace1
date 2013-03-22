import java.util.List;


public class Start {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		FileHandler fhFile = new FileHandler();
		List<Double> data = fhFile.FetchData(args[0]);
		
		System.out.println("data length"+data.size());
		
		Approach1(data);
		
		Approach2(data);
		}

	private static void Approach2(List<Double> data) {
		// TODO Auto-generated method stub
		//given 3 classes 
		//Approach 1
		
		//Initialization step
		//Random Assignments to all 9 (3 X 3) params
		
		//step1 -> Pick random data centers form the data
		EMHelper emhHelper = new EMHelper();
		Helper h = new Helper();
		double[] mus = emhHelper.GetRandomMU(data,3);
		//double[] mus = new double[3];
		//mus[0] = 5; mus[1]=15; mus [2]=25;
		System.out.println("Initial mus :");
		h.Print(mus);
		
		//step2 -> Initialize the covariance matrix
		double[] sigma = new double[3];
		sigma[0]=1.0;sigma[1]=1.0;sigma[2]=1.0;
		System.out.println("Initial sigmas :");
		h.Print(sigma);
		
		//step3 -> Assuming probabilities
		//Assumption is that the data is split equally among all three classes
		double[] prob = new double[3];
		prob[0]=1.0/3;prob[1]=1.0/3;prob[2]=1.0/3;
		System.out.println("Initial prob :");
		h.Print(prob);
		
		double[] prevParams = new double[3*3]; 
		prevParams = EMHelper.StoreVals(mus,sigma,prob);
		
		boolean gottaloop= true;
		int iter=0;
		while(gottaloop)
		{
			System.out.println("iteration count"+iter++);
			//E Step
			//hardcoded 3 because of the assumption that 3 classes are there 
			Double[][] ClassProb = new Double[data.size()][3];
			for (int i = 0; i < data.size(); i++) {
				for (int j = 0; j < 3; j++) {
					//ClassProb[i][j] =
					double x = data.get(i);
					double mu_j = mus[j];
					double sigma_j = sigma[j];
					
					double expPart = Math.exp(-Math.pow(x-mu_j,2)/(sigma_j*sigma_j*2)) * prob[j];
					double den = Math.sqrt(2*3.14)*sigma_j;
					
					double temp = expPart/den;
					if(temp<10E-100)
						temp = 10E-100;
					if(temp<10E+100)
						temp = 10E+100;
					ClassProb[i][j] = temp;
				}
			}
			System.out.println("Before Normalization");
			//h.Print(ClassProb);
			
			ClassProb = emhHelper.NormalizeProb(ClassProb);
			System.out.println("After Normalization");
			//h.Print(ClassProb);
			
			
			//M Step
			//update mu
			double[] dens = new double[3];
			for (int j = 0; j < 3; j++) {
				double num =0;
				dens[j]=0;
				for (int i = 0; i < ClassProb.length; i++) {
					num += ClassProb[i][j] * data.get(i);
					dens[j] += ClassProb[i][j];
				}
				mus[j]=num / dens[j];
			}
			
			System.out.println("updated mus");
			h.Print(mus);
			
			h.Print(sigma);
			
			//update probabilities
			int m = ClassProb.length;
			for (int j = 0; j < 3; j++) {
				prob[j]=dens[j]/m;
			}			
			
			System.out.println("updated prob");
			h.Print(prob);
			
			gottaloop = EMHelper.CheckData(prevParams,mus,sigma,prob);
			
			if(gottaloop == true)
				prevParams = EMHelper.StoreVals(mus, sigma, prob);
		}

	}

	private static void Approach1(List<Double> data) {
		//given 3 classes 
				//Approach 1
				
				//Initialization step
				//Random Assignments to all 9 (3 X 3) params
				
				//step1 -> Pick random data centers form the data
				EMHelper emhHelper = new EMHelper();
				Helper h = new Helper();
				double[] mus = emhHelper.GetRandomMU(data,3);
				System.out.println("Initial mus :");
				h.Print(mus);
				
				//step2 -> Initialize the covariance matrix
				double[] sigma = new double[3];
				sigma[0]=1.0;sigma[1]=1.0;sigma[2]=1.0;
				System.out.println("Initial sigmas :");
				h.Print(sigma);
				
				//step3 -> Assuming probabilities
				//Assumption is that the data is split equally among all three classes
				double[] prob = new double[3];
				prob[0]=0.333;prob[1]=0.333;prob[2]=0.333;
				System.out.println("Initial prob :");
				h.Print(prob);
				
				double[] prevParams = new double[3*3]; 
				prevParams = EMHelper.StoreVals(mus,sigma,prob);
				
				boolean gottaloop= true;
				int iter=0;
				while(gottaloop)
				{
					System.out.println("iteration count"+iter++);
					//E Step
					//hardcoded 3 because of the assumption that 3 classes are there 
					Double[][] ClassProb = new Double[data.size()][3];
					for (int i = 0; i < data.size(); i++) {
						for (int j = 0; j < 3; j++) {
							//ClassProb[i][j] =
							double x = data.get(i);
							double mu_j = mus[j];
							double sigma_j = sigma[j];
							
							double expPart = Math.exp(-Math.pow(x-mu_j,2)/(sigma_j*sigma_j*2)) * prob[j];
							double den = Math.sqrt(2*3.14)*sigma_j;
							double temp = expPart/den;
							if (temp<10E-100)
								temp = 10E-100;
							if(temp>10E+100)
								temp = 10E+100;
							
							ClassProb[i][j]=temp;
							//System.out.println("j:"+j+"temp"+temp);
						}
					}
					System.out.println("Before Normalization");
					//h.Print(ClassProb);
					
					ClassProb = emhHelper.NormalizeProb(ClassProb);
					System.out.println("After Normalization");
					//h.Print(ClassProb);
					
					
					//M Step
					//update mu
					double[] dens = new double[3];
					for (int j = 0; j < 3; j++) {
						double num =0;
						dens[j]=0;
						for (int i = 0; i < ClassProb.length; i++) {
							num += ClassProb[i][j] * data.get(i);
							dens[j] += ClassProb[i][j];
						}
						mus[j]=num / dens[j];
					}
					
					System.out.println("updated mus");
					h.Print(mus);
					
					//update Sigma
					for (int j = 0; j < 3; j++) {
						double num=0;
						double mu = mus[j];
						//int den=0;
						for (int i = 0; i < ClassProb.length; i++) {
							double x = data.get(i);
							num += ClassProb[i][j] * (x-mu) * (x-mu);
						}
						System.out.println("var - num"+num);
						sigma[j] = num/dens[j];
					}
					System.out.println("updated variance");
					h.Print(sigma);
					
					//update probabilities
					int m = ClassProb.length;
					for (int j = 0; j < 3; j++) {
						prob[j]=dens[j]/m;
					}
					
					System.out.println("updated prob");
					h.Print(prob);
					
					gottaloop = EMHelper.CheckData(prevParams,mus,sigma,prob);
					
					if(gottaloop == true)
						prevParams = EMHelper.StoreVals(mus, sigma, prob);
					
					if(iter>1000)
						gottaloop=false;
					
					//gottaloop=false;
				}

	}

}
