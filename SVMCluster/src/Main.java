/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package svmcluster;
import edu.mit.jwi.item.ISynsetID;
import java.util.*;
import java.io.*;
import weka.core.Instances;
import weka.core.Instance;
import weka.classifiers.functions.SMO;
import weka.classifiers.*;

/**
 *
 * @author Nidhi
 */
public class Main {

    /**
     * @param args the command line arguments
     */

    HashMap<Double,String> dummy = new HashMap<Double,String>();
    static String model= "/Users/pravinjothinathan/Documents/NLP/test2.arff";
    static String testListFile;
    static String testHtmlFile;
    HashMap<String,ISynsetID> SynsetIDs = new HashMap<String,ISynsetID>();

    public static void main(String[] args) throws Exception {
/*
        System.out.println("\nDo you need to check the format of the annotated Files? (yes/no)");
        Scanner in = new Scanner(System.in);
        String answer = in.nextLine();
        answer = answer.toLowerCase();
        if(answer.equals("yes"))
        {
         //     System.out.println("\nEnter full path of folder which contains all files to be tested for format.");
        Scanner ins = new Scanner(System.in);
        String fileNames = ins.nextLine();
        File folder = new File(fileNames);
         File[] listOfFiles = folder.listFiles();
        for(int i=0;i<listOfFiles.length;i++)
        {
            String fn = listOfFiles[i].getName();
            System.out.println("\nFile:" +fn);
            Format obj = new Format(fn);
            try {
                boolean flagFormat = obj.checkFormat(listOfFiles[i].getAbsoluteFile());
                if (flagFormat == true) {
                    obj.checkTree();
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        }
        }
        else
        */
        {
          /*  System.out.println("Do you want to create new training file?(yes/no)");
            Scanner ina = new Scanner(System.in);
            String ans = ina.nextLine();
            ans = ans.toLowerCase();*/
            Base b;
            //if(ans.equals("yes"))
            {
               /* System.out.println("Enter full path for the folder containing all the test and training .list and .html files");
                Scanner ine = new Scanner(System.in);
                String subeventFolder = ine.nextLine();
                System.out.println("Enter full path for the folder containing all the .tree annotated files to be used for training only");
                ine = new Scanner(System.in);
                String annotationFolder = ine.nextLine();
                System.out.println("Enter full path with new File name to save the output of the ARFF file prodced from the training data");
                ine = new Scanner(System.in);
                model = ine.nextLine();
*/
                //b = new Base("C:/Users/Nidhi/Documents/Natural Language Processing/Project/subevents/subevents","C:/Users/Nidhi/Documents/Natural Language Processing/Project/NLP Tree_Nidhi/Annotations",model);
                
                b = new Base("/Users/pravinjothinathan/Downloads/subevents","/Users/pravinjothinathan/Documents/NLP/Annotations/temp/","/Users/pravinjothinathan/Documents/NLP/test2.arff");
                
                Configuration c = new Configuration();
                System.out.println(c.getAnnotations());
                System.out.println(c.getArffFile());
                System.out.println(c.getSubevent());
                //b.SerializeData("", "");
                //b.DeSerializeData("", "");
                
            }
           /* else
            {
                b = new Base();
                System.out.println("Please enter the ARFF trianing file");
                Scanner inb = new Scanner(System.in);
                model = inb.nextLine();
            }*/
      
        Instances  data = new Instances(new BufferedReader(new FileReader(model)));
      
        SMO smo = new SMO();
        smo.setC(0.1);
        data.setClassIndex(data.numAttributes()-1);
        smo.setBuildLogisticModels(true);
        smo.buildClassifier(data);
      
        Main obj = new Main();
        System.out.println("Choose one option:\n1.Annotate tree for test file.\n2.Evaluate model");
        Scanner inc = new Scanner(System.in);
        int o = inc.nextInt();
        switch(o)
        {
            case 1:
            System.out.println("Enter name of the file (without extension) to be annotated:");
            Scanner ind = new Scanner(System.in);
            String fileName = ind.nextLine();
            String HTMLname = fileName+".html";
            String LISTname = fileName+".list";
            System.out.println("Enter full path for the folder containing .list and .html test file for annotations:");
            ind = new Scanner(System.in);
            String Folder = ind.nextLine();
            /*File folder = new File(Folder);
            File[] listOfFiles = folder.listFiles();
            for(int i = 0;i<listOfFiles.length;i++)
            {
                if(HTMLname.equals(listOfFiles[i].getName()))
                        testHtmlFile = listOfFiles[i].getName();
                if(LISTname.equals(listOfFiles[i].getName()))
                        testListFile = listOfFiles[i].getName();
            }*/
            testListFile = Folder+"\\"+LISTname;
            testHtmlFile = Folder+"\\"+HTMLname;
            Synsets sn = new Synsets();
                ArrayList testpts = obj.getTestFile(b, testListFile,sn);
            obj.cluster(b,smo,testpts,data,sn);
            break;

            case 2:
             Evaluation eval = new Evaluation(data);
              Random rand = new Random(1);
             eval.crossValidateModel(smo, data, 10, rand);
             System.out.println("Detailed Statistics for 10-fold Cross Validation:");
              System.out.println(eval.toClassDetailsString());
              System.out.println("Confusion Matrix:");
               System.out.println(eval.toMatrixString());

            break;
         }

        }
    }

    public ArrayList<Double> getTestFile(Base b,String file, Synsets sn) throws Exception
    {
        ArrayList<Double> testPoints = new ArrayList<Double>();
        BufferedReader in = new BufferedReader(new FileReader(file));
        String event;
        String[] ev;
        while((event = in.readLine())!=null)
        {
            ev = event.split(" ");
            double tr = b.GetDoubleEventValue(ev[0]);
            dummy.put(tr, ev[0]);
            SynsetIDs.put(ev[0], sn.getSynID(ev[0]));
            testPoints.add(tr);
        }
        return testPoints;
    }

    public void cluster(Base b,SMO smo, ArrayList testPoints,Instances data,Synsets sn) throws Exception
    {
         //ArrayList<Double> testPoints = new ArrayList<Double>();
         String merge,temps;
         String newm = "";
         ArrayList<String> treeData = new ArrayList<String>();
         int number=1;
         while(testPoints.size()>1)
         {
            merge = SVMDistance(smo,testPoints,data,sn);
            String[] m = merge.split(" ");
            newm="";
            double parent = b.GetDoubleParentValue(m[0], m[1]);

            temps = "p"+number;
            dummy.put(parent, temps);
            SynsetIDs.put(temps, sn.getRelatedSysnsets(SynsetIDs.get(m[0]), SynsetIDs.get(m[1])) );
            number++;

            System.out.println("\nMerged: ");
            if(dummy.containsKey(Double.parseDouble(m[0])))
            {
                System.out.print(dummy.get(Double.parseDouble(m[0]))+" and ");
                newm = dummy.get(Double.parseDouble(m[0]))+" ";
             }
           /* else
            {
                temps = "p"+number;
                dummy.put(Double.parseDouble(m[0]),temps);
                number++;
                System.out.print(temps);
            }*/
            if(dummy.containsKey(Double.parseDouble(m[1])))
            {
                System.out.print(dummy.get(Double.parseDouble(m[1]))+" at "+temps);
                newm = newm + dummy.get(Double.parseDouble(m[1]))+" "+temps;
             }
            treeData.add(newm);
            /*else
            {
                temps = "p"+number;
                dummy.put(Double.parseDouble(m[1]),temps);
                number++;
                System.out.print(temps);
             }*/
            
            testPoints.remove(Double.parseDouble(m[0]));
            testPoints.remove(Double.parseDouble(m[1]));
            testPoints.add(parent);
        }
    }


    public String SVMDistance(SMO smo,ArrayList<Double> testPoints,Instances data, Synsets sn) throws Exception
    {
        String closest="";

        String events=""; //String for events
        Double ev1val,ev2val;
        String ev1,ev2;
        TreeMap<String,Double> vals = new TreeMap<String,Double>();
        FileHandler fH = new FileHandler();
            int Fileno = fH.GetFileNo();
        for(int i = 0 ;i<testPoints.size();i++)
        {
            Instance test = new Instance(10);
            ev1val = testPoints.get(i);
            test.setValue(0, ev1val);
            ev1 = dummy.get(ev1val);
            test.setDataset(data);
            String val = getOffset(ev1);
            if(val.equals("?"))
                test.setMissing(6);
            else
                test.setValue(6,Integer.parseInt(val));
            
            for(int j=0;j<testPoints.size();j++)
            {
                if(i!=j)
                {
                    ev2val = testPoints.get(j);
                    test.setValue(1, ev2val);
                    ev2 = dummy.get(ev2val);
                    test.setMissing(2);//Distance
                    test.setValue(3, Fileno);//DocNumber
                    Word w = fH.CheckLine(ev1,ev2,fH.ReadFromFile(testHtmlFile),"");
                    test.setValue(4, String.format("%s", w.SameLine));//SameLine
                    test.setValue(5, w.WordDist);//Word Distance
                    val = getOffset(ev2);
                    if(val.equals("?"))
                        test.setMissing(7);
                    else
                        test.setValue(7,Integer.parseInt(val));
                    Double sim = sn.getLCSimilarity(SynsetIDs.get(ev1),SynsetIDs.get(ev2));
                    test.setValue(8, sim);
                    test.setValue(9, "false");
                    double rslt = testPoint(test,smo);
                    events = testPoints.get(i)+" "+testPoints.get(j);
                    vals.put(events, rslt);
                }
            }
        }
        closest = getMax(vals);
        return(closest);
    }

    public String getMax(TreeMap<String,Double> vals)
    {
        Double max=0.0;
        String maxEvents="";
        for(Map.Entry<String,Double> entry : vals.entrySet()) {
                 String key = entry.getKey();
                double value = entry.getValue();
                if(value>max)
                {
                    max = value;
                    maxEvents = key;
                }
        }
        return(maxEvents);
}

        public double testPoint(Instance test, SMO smo) throws Exception
    {
      //File file = new File(File);

      //Sample sample = parseTestFile(file);
      

      double[] dist = smo.distributionForInstance(test);
       double clsLabel = smo.classifyInstance(test);
      /*if(clsLabel==0)
      {
          System.out.println("Classify instance for:" + test.toString() + "   " + test.classAttribute().value((int) clsLabel));
      //System.out.println("Distribution = " + Arrays.toString(dist));
      for (int i = 0; i < dist.length; i++) {
        System.out.println(i + ":" + dist[i]);
      }
        }*/
      return(dist[0]);
    }

public String getOffset(String ev)
{
    String val="?";
    ISynsetID id = SynsetIDs.get(ev);
    if(id==null)
        val="?";
    else
    {
        int v = id.getOffset();
        val = Integer.toString(v);
    }
    return(val);


}

}
