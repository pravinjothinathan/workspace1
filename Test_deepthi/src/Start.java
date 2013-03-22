
public class Start {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileHandler myFileHandler = new FileHandler();
		String value = myFileHandler.ReadFileintoaString("/Users/pravinjothinathan/Documents/Semester2/AOS/Assignment1/Readme.rtf");
		System.out.println(value);
		//value = value.replace("@");
		String[] linebyline = value.split("@");
		for (int i = 0; i < linebyline.length; i++) {
			System.out.println(linebyline[i]);
		}
	}

}
