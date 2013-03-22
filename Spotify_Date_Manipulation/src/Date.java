
public class Date {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String s = "11/5/88";
		
		String_Tokenizer St = new String_Tokenizer();
		String[] SplitString = St.Tokenize(s);
		System.out.print(SplitString[0]+" "+SplitString[1]+" "+SplitString[2]);
	}
	
	

}
