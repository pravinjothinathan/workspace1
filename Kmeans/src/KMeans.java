/*** Author :Vibhav Gogate
The University of Texas at Dallas
*****/


//import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
 

public class KMeans {
	static int countCount=0;
	static int countVal=0;
	static Map<Integer,Color> InitialColors;
	static Map<Integer,Color> PrevColors = new HashMap<Integer, Color>();
    public static void main(String [] args){
	if (args.length < 3){
	    System.out.println("Usage: Kmeans <input-image> <k> <output-image>");
	    return;
	}
	try{
	    BufferedImage originalImage = ImageIO.read(new File(args[0]));
	    int k=Integer.parseInt(args[1]);
	    BufferedImage kmeansJpg = kmeans_helper(originalImage,k);
	    ImageIO.write(kmeansJpg, "png", new File(args[2])); 
	    
	}catch(IOException e){
	    System.out.println(e.getMessage());
	}	
    }
    
    private static BufferedImage kmeans_helper(BufferedImage originalImage, int k){
	int w=originalImage.getWidth();
	int h=originalImage.getHeight();
	BufferedImage kmeansImage = new BufferedImage(w,h,originalImage.getType());
	Graphics2D g = kmeansImage.createGraphics();
	g.drawImage(originalImage, 0, 0, w,h , null);
	// Read rgb values from the image
	int[] rgb=new int[w*h];
	int count=0;
	for(int i=0;i<w;i++){
	    for(int j=0;j<h;j++){
		rgb[count++]=kmeansImage.getRGB(i,j);
	    }
	}
	// Call kmeans algorithm: update the rgb values
	rgb = kmeans(rgb,k);

	// Write the new rgb values to the image
	count=0;
	for(int i=0;i<w;i++){
	    for(int j=0;j<h;j++){
		kmeansImage.setRGB(i,j,rgb[count++]);
	    }
	}
	return kmeansImage;
    }

    // Your k-means code goes here
    // Update the array rgb by assigning each entry in the rgb array to its cluster center
    private static int[] kmeans(int[] rgb, int k){
    	
    	Map<Integer,Color> comp_rgb = GetRGBComponents(rgb);
    	
    	InitialColors = InitializeColors(rgb,k);
    	
    	boolean NotConvergerd = true;
    	
    	
    	//for (int i = 0; i < 100; i++)
    	int i=0;
    	while(NotConvergerd)
    	{
    		System.out.println("Interation "+(++i));
    		Map<Integer,List<Color>> DistMap = getDistanceMap(comp_rgb,InitialColors,k);	
    		NotConvergerd = updateInitialColors(InitialColors,DistMap);
    		
    		//if(i==100)
    			//break;
    	}
    	
    	int[] newrgb = InvGetRGBComponents(InitialColors,rgb,comp_rgb);
    	
    	return newrgb;
    }


	private static int[] InvGetRGBComponents(Map<Integer, Color> initialColors,
			int[] rgb, Map<Integer, Color> comp_rgb) {
		// TODO Auto-generated method stub
		int[] nrgb = new int[rgb.length];
		double min = 999999999;
		int index;
		
		for (int i = 0; i < nrgb.length; i++) {
			min = 999999999;
			index = -1;
				//System.out.println("i - " + i);
				for (int j = 0; j < initialColors.size(); j++) {
					//System.out.println("j - " + j);
					double dist = initialColors.get(j).Distance(comp_rgb.get(i));
					//System.out.println("dist"+dist);
					if(min>dist)
					{
						index = j;
						min = dist;
						//System.out.println("set index to k" +index);
					}
				}
				Color c = initialColors.get(index);
				int nrgbv = (int)c.red;
				nrgbv = (nrgbv << 8) + (int)c.green;
				nrgbv = (nrgbv << 8) + (int)c.blue;
				nrgb[i]=nrgbv;
			}

		return nrgb;
	}

	private static boolean updateInitialColors(
			Map<Integer, Color> initialColors, Map<Integer, List<Color>> distMap) {
		int count=0;
		PrevColors.clear();
		
		System.out.println("new k");
		for (int i = 0; i < initialColors.size(); i++) {
			PrevColors.put(i, initialColors.get(i));
			//Color c = new Color();
			double r=0,g=0,b=0;
			List<Color> lstColors = distMap.get(i);
			for (int j = 0; j < lstColors.size(); j++) {
				Color cur = lstColors.get(j);
				r = r+cur.red;
				g = g+cur.green;
				b = b+cur.blue;
			}
			int len = lstColors.size();
			double nr=0,ng=0,nb=0;
			nr = r/len;
			ng = g/len;
			nb = b/len;
			Color c = new Color((int)nr,(int)ng,(int)nb);
			c.Print();
			System.out.println("list count"+lstColors.size());
			initialColors.remove(i);
			initialColors.put(i, c);
		}
	
		for (int i = 0; i < PrevColors.size(); i++) {
			Color prev = PrevColors.get(i);
			for (int j = 0; j < initialColors.size(); j++) {
				Color cur = InitialColors.get(j);
				
				if(prev.compare(cur))
					count++;
			}
		}
		
		System.out.println("count : "+count);
		
		if(count==PrevColors.size())
			return false;
		else
			return true;
		/*for (Color prev : PrevColors) {
			for (Color curr : initialColors) {
				
			}
		}*/
	}

	private static Map<Integer, List<Color>> getDistanceMap(Map<Integer, Color> comp_rgb, Map<Integer, Color> initialColors, int k) {
		int pixCount = comp_rgb.size();
		Map<Integer,List<Color>> DistMap = new HashMap<Integer, List<Color>>();
		double min = 999999999;
		int index = -1;
		System.out.println("pixel count :"+pixCount);
		for (int i = 0; i < pixCount; i++) {
			min = 999999999;
			index = -1;
			for (int j = 0; j < k; j++) {
				double dist = initialColors.get(j).Distance(comp_rgb.get(i));
				if(min>dist)
				{
					index = j;
					min = dist;
				}
			}
			if(DistMap.containsKey(index))
			{
				DistMap.get(index).add(comp_rgb.get(i));
			}
			else
			{
				List<Color> temp = new ArrayList<Color>();
				temp.add(comp_rgb.get(i));
				DistMap.put(index, temp);
			}
		}
		
		return DistMap;
	}

	private static Map<Integer, Color> InitializeColors(int[] rgb, int k) {
		Map<Integer, Color> init = new HashMap<Integer, Color>();
		System.out.println("Initialization Vector");
		for(int i=0;i<k;i++)
		{
			int rand = (int)(Math.random() * rgb.length);
			Color c = new Color(rgb[rand]);
			
			c.Print();
			init.put(i,c);
		}
		
		return init;
	}

	private static Map<Integer, Color> GetRGBComponents(int[] rgb) {
		Map<Integer,Color> colorMap = new HashMap<Integer,Color>();
		
		for (int i = 0; i < rgb.length; i++) {
			colorMap.put(i, new Color(rgb[i]));
		}
		
		return colorMap;
	}

}
