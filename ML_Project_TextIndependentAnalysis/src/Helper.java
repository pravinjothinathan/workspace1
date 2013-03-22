
public class Helper {

	public void Print(double[][] predVals) {
		// TODO Auto-generated method stub
		for (int i = 0; i < predVals.length; i++) {
			for (int j = 0; j < predVals[i].length; j++) {
				System.out.print(predVals[i][j]+"\t");
			}
			System.out.println();
		}
	}

}
