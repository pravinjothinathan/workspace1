public class Input {

	/* the input file have to specify the size and the difficulty level*/
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int Size = 4;
		Helper Help = new Helper();
		PGenerator pg = new PGenerator();
		int[][] mat = pg.Generate(Size,Difficulty.Easy);
		Help.Print(mat);
	}

}
