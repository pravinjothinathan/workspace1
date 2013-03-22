
public class Color {
	
	double red;
	double green;
	double blue;

	public Color(int rgb)
	{
		red = (rgb >> 16) & 0xFF;
		green = (rgb >> 8) & 0xFF;
		blue = (rgb ) & 0xFF;
		
		//Print();
	}
	
	public Color(double r,double g,double b)
	{
		red = r;
		green = g;
		blue = b;
		
		//Print();
	}
	
	public Color(Color c)
	{
		red = c.red;
		green = c.green;
		blue = c.blue;
	}
	
	
	
	public double Distance(Color c)
	{
		return Math.pow((red-c.red),2) + Math.pow((green-c.green),2) + Math.pow((blue-c.blue),2); 
	}
	
	public void Print()
	{
		System.out.println("red :"+red+"blue :"+blue+"green"+green);
	}

	public boolean compare(Color cur) {
		if(red==cur.red&&blue==cur.blue&&green==cur.green)
			return true;
		else
			return false;
	}
}
