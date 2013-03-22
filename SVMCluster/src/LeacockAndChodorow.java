/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package svmcluster;

import java.util.*;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;


public class LeacockAndChodorow {

        private final double n30 = 19.0D;
  private final double v30 = 12.0D;

  private final double n21 = 18.0D;
  private final double v21 = 12.0D;

  private final double n20 = 17.0D;
  private final double v20 = 12.0D;

  private final double n171 = 17.0D;
  private final double v171 = 11.0D;

  private final double n17 = 15.0D;
  private final double v17 = 11.0D;

  private final double n16 = 15.0D;
  private final double v16 = 11.0D;

  private IDictionary dict = null;

  private double noundepth = 0.0D;
  private double verbdepth = 0.0D;

  private ArrayList<ISynsetID> roots = null;

  public LeacockAndChodorow(IDictionary paramIDictionary, ArrayList<ISynsetID> paramArrayList)
  {
//    System.out.println("... LeacockAndChodorow");
 //   System.out.println("... calculating depths of <roots> ...");
    this.dict = paramIDictionary;
    this.roots = paramArrayList;

    String str = paramIDictionary.getVersion().toString();
    if (str.equals("3.0"))
    {
      this.noundepth = 19.0D;
      this.verbdepth = 12.0D;
    }
    if (str.equals("2.1"))
    {
      this.noundepth = 18.0D;
      this.verbdepth = 12.0D;
    }
    if (str.equals("2.0"))
    {
      this.noundepth = 17.0D;
      this.verbdepth = 12.0D;
    }
    if (str.equals("1.7.1"))
    {
      this.noundepth = 17.0D;
      this.verbdepth = 11.0D;
    }
    if (str.equals("1.7"))
    {
      this.noundepth = 15.0D;
      this.verbdepth = 11.0D;
    }
    if (str.equals("1.6"))
    {
      this.noundepth = 15.0D;
      this.verbdepth = 11.0D;
    }

    this.noundepth += 1.0D;
    this.verbdepth += 2.0D;
  }

  public double lch(String paramString1, int paramInt1, String paramString2, int paramInt2, String paramString3)
  {
    double d1 = 0.0D;
    IIndexWord localIIndexWord1 = null;
    IIndexWord localIIndexWord2 = null;

    if (paramString3.equalsIgnoreCase("n"))
    {
      localIIndexWord1 = this.dict.getIndexWord(paramString1, POS.NOUN);
      localIIndexWord2 = this.dict.getIndexWord(paramString2, POS.NOUN);
    }
    if (paramString3.equalsIgnoreCase("v"))
    {
      localIIndexWord1 = this.dict.getIndexWord(paramString1, POS.VERB);
      localIIndexWord2 = this.dict.getIndexWord(paramString2, POS.VERB);
    }

    if (localIIndexWord1 == null)
    {
      System.out.println(paramString1 + "(" + paramString3 + ") not found in WordNet " + this.dict.getVersion());
      return 0.0D;
    }
    if (localIIndexWord2 == null)
    {
      System.out.println(paramString2 + "(" + paramString3 + ") not found in WordNet " + this.dict.getVersion());
      return 0.0D;
    }

    List localList1 = localIIndexWord1.getWordIDs();
    List localList2 = localIIndexWord2.getWordIDs();
    if (paramInt1 > localList1.size())
    {
      System.out.println(paramString1 + " sense: " + paramInt1 + " not found in WordNet " + this.dict.getVersion());
      return 0.0D;
    }
    if (paramInt2 > localList2.size())
    {
      System.out.println(paramString2 + " sense: " + paramInt2 + " not found in WordNet " + this.dict.getVersion());
      return 0.0D;
    }

    IWordID localIWordID1 = localIIndexWord1.getWordIDs().get(paramInt1 - 1);
    ISynset localISynset1 = this.dict.getWord(localIWordID1).getSynset();

    IWordID localIWordID2 = localIIndexWord2.getWordIDs().get(paramInt2 - 1);
    ISynset localISynset2 = this.dict.getWord(localIWordID2).getSynset();

    double d2 = 0.0D;
    if (localISynset1.equals(localISynset2))
    {
      d2 = 1.0D;
    }
    else
    {
      d2 = getShortestPath(localISynset1, localISynset2);
    }

    double d3 = 0.0D;
    if (paramString3.equalsIgnoreCase("n"))
    {
      d3 = this.noundepth;
    }
    if (paramString3.equalsIgnoreCase("v"))
    {
      d3 = this.verbdepth;
    }

    d1 = -Math.log(d2 / (2.0D * d3));
    return d1;
  }

   public double lchID( ISynset localISynset1,ISynset localISynset2,String paramString3)
    {
       double d1 = 0.0D;
       double d2 = 0.0D;
    if (localISynset1.equals(localISynset2))
    {
      d2 = 1.0D;
    }
    else
    {
      d2 = getShortestPath(localISynset1, localISynset2);
    }

    double d3 = 0.0D;
    if (paramString3.equalsIgnoreCase("n"))
    {
      d3 = this.noundepth;
    }
    if (paramString3.equalsIgnoreCase("v"))
    {
      d3 = this.verbdepth;
    }

    d1 = -Math.log(d2 / (2.0D * d3));
    return d1;

    }

  private double getShortestPath(ISynset paramISynset1, ISynset paramISynset2)
  {
    double d1 = 0.0D;
    ArrayList localArrayList = new ArrayList();

    ISynsetID localISynsetID1 = paramISynset1.getID();
    ISynsetID localISynsetID2 = paramISynset2.getID();

    if (localISynsetID1.equals(localISynsetID2))
    {
      return 1.0D;
    }

    HashSet localHashSet1 = new HashSet();
    localHashSet1.add(localISynsetID1);
    TreeMap localTreeMap1 = new TreeMap();
    localTreeMap1.put(Double.valueOf(1.0D), localHashSet1);
    getHypernyms(1.0D, localISynsetID2, localHashSet1, new HashSet(), localTreeMap1);

    HashSet localHashSet2 = new HashSet();
    localHashSet2.add(localISynsetID2);
    TreeMap localTreeMap2 = new TreeMap();
    localTreeMap2.put(Double.valueOf(1.0D), localHashSet2);
    getHypernyms(1.0D, localISynsetID1, localHashSet2, new HashSet(), localTreeMap2);
 Double localDouble1;
    HashSet localHashSet3;
    for (Iterator localIterator1 = localTreeMap1.keySet().iterator(); localIterator1.hasNext(); ) { localDouble1 = (Double)localIterator1.next();

      localHashSet3 = new HashSet();
      localHashSet3.addAll((Collection)localTreeMap1.get(localDouble1));
      if (localHashSet3.contains(localISynsetID2))
      {
        localArrayList.add(localDouble1);
      }
      for (Object localDouble2 : localTreeMap2.keySet())
      {
        HashSet localHashSet4 = new HashSet();
        localHashSet4.addAll((Collection)localTreeMap2.get(localDouble2));

        if (localHashSet4.contains(localISynsetID1))
        {
          localArrayList.add(localDouble2);
        }
        localHashSet4.retainAll(localHashSet3);
        if (!localHashSet4.isEmpty())
        {
          localArrayList.add(Double.valueOf(localDouble1.doubleValue() + (Double)localDouble2 - 1.0D));
        }
      }
    }

    if (localArrayList.isEmpty())
    {
      double d2 = getShortestRoot(localTreeMap1);
      double d3 = getShortestRoot(localTreeMap2);
      d1 = d2 + d3 + 1.0D;
    }
    else
    {
      Collections.sort(localArrayList);
      d1 = ((Double)localArrayList.get(0)).doubleValue();
    }

    return d1;
  }

  private double getShortestRoot(TreeMap<Double, HashSet<ISynsetID>> paramTreeMap)
  {
    double d = 0.0D;
Double localDouble;
    for (Iterator localIterator1 = paramTreeMap.keySet().iterator(); localIterator1.hasNext(); ) { localDouble = (Double)localIterator1.next();

      HashSet<ISynsetID> localHashSet = paramTreeMap.get(localDouble);
      for (ISynsetID localISynsetID : localHashSet)
      {
        if (this.roots.contains(localISynsetID))
        {
          return localDouble.doubleValue();
        }
      }
    }
    return d;
  }

  private void getHypernyms(double paramDouble, ISynsetID paramISynsetID, HashSet<ISynsetID> paramHashSet1, HashSet<ISynsetID> paramHashSet2, TreeMap<Double, HashSet<ISynsetID>> paramTreeMap)
  {
    paramDouble += 1.0D;
    HashSet localHashSet = new HashSet();
    for (ISynsetID localISynsetID : paramHashSet1)
    {
      if (!paramHashSet2.contains(localISynsetID))
      {
        ISynset localISynset = this.dict.getSynset(localISynsetID);
        localHashSet.addAll(localISynset.getRelatedSynsets(Pointer.HYPERNYM));
        localHashSet.addAll(localISynset.getRelatedSynsets(Pointer.HYPERNYM_INSTANCE));
      }
    }
    if (!localHashSet.isEmpty())
    {
      if (localHashSet.contains(paramISynsetID))
      {
        paramTreeMap.put(Double.valueOf(paramDouble), localHashSet);
        return;
      }

      paramTreeMap.put(Double.valueOf(paramDouble), localHashSet);
      paramHashSet2.addAll(paramHashSet1);
      getHypernyms(paramDouble, paramISynsetID, localHashSet, paramHashSet2, paramTreeMap);
    }
  }

  public TreeMap<String, Double> lch(String paramString1, String paramString2, String paramString3)
  {
    TreeMap localTreeMap = new TreeMap();

    IIndexWord localIIndexWord1 = null;
    IIndexWord localIIndexWord2 = null;

    if (paramString3.equalsIgnoreCase("n"))
    {
      localIIndexWord1 = this.dict.getIndexWord(paramString1, POS.NOUN);
      localIIndexWord2 = this.dict.getIndexWord(paramString2, POS.NOUN);
    }
    if (paramString3.equalsIgnoreCase("v"))
    {
      localIIndexWord1 = this.dict.getIndexWord(paramString1, POS.VERB);
      localIIndexWord2 = this.dict.getIndexWord(paramString2, POS.VERB);
    }
    List<IWordID> localList2;
    int i;
    if ((localIIndexWord1 != null) && (localIIndexWord2 != null))
    {
      List<IWordID> localList1 = localIIndexWord1.getWordIDs();
      localList2 = localIIndexWord2.getWordIDs();
      i = 1;
      Object localObject1 = null;
      Object localObject2 = null;
      for (IWordID localIWordID1 : localList1)
      {
        int j = 1;
        for (IWordID localIWordID2 : localList2)
        {
          double d = lch(paramString1, i, paramString2, j, paramString3);
          localTreeMap.put(paramString1 + "#" + paramString3 + "#" + i + "," + paramString2 + "#" + paramString3 + "#" + j, Double.valueOf(d));
          j++;
        }
        i++;
      }
    }
    else
    {
      return localTreeMap;
    }
    return localTreeMap;
  }

  public TreeMap<String, Double> lch(String paramString1, String paramString2, int paramInt, String paramString3)
  {
    TreeMap localTreeMap = new TreeMap();

    IIndexWord localIIndexWord1 = null;
    IIndexWord localIIndexWord2 = null;

    if (paramString3.equalsIgnoreCase("n"))
    {
      localIIndexWord1 = this.dict.getIndexWord(paramString1, POS.NOUN);
      localIIndexWord2 = this.dict.getIndexWord(paramString2, POS.NOUN);
    }
    if (paramString3.equalsIgnoreCase("v"))
    {
      localIIndexWord1 = this.dict.getIndexWord(paramString1, POS.VERB);
      localIIndexWord2 = this.dict.getIndexWord(paramString2, POS.VERB);
    }
    int i;
    if ((localIIndexWord1 != null) && (localIIndexWord2 != null))
    {
      List<IWordID> localList = localIIndexWord1.getWordIDs();
      i = 1;
      for (IWordID localIWordID : localList)
      {
        double d = lch(paramString1, i, paramString2, paramInt, paramString3);
        localTreeMap.put(paramString1 + "#" + paramString3 + "#" + i + "," + paramString2 + "#" + paramString3 + "#" + paramInt, Double.valueOf(d));
        i++;
      }
    }
    else
    {
      return localTreeMap;
    }
    return localTreeMap;
  }

  public TreeMap<String, Double> lch(String paramString1, int paramInt, String paramString2, String paramString3)
  {
    TreeMap localTreeMap = new TreeMap();
    IIndexWord localIIndexWord1 = null;
    IIndexWord localIIndexWord2 = null;

    if (paramString3.equalsIgnoreCase("n"))
    {
      localIIndexWord1 = this.dict.getIndexWord(paramString1, POS.NOUN);
      localIIndexWord2 = this.dict.getIndexWord(paramString2, POS.NOUN);
    }
    if (paramString3.equalsIgnoreCase("v"))
    {
      localIIndexWord1 = this.dict.getIndexWord(paramString1, POS.VERB);
      localIIndexWord2 = this.dict.getIndexWord(paramString2, POS.VERB);
    }
    int i;
    if ((localIIndexWord1 != null) && (localIIndexWord2 != null))
    {
      List<IWordID> localList = localIIndexWord2.getWordIDs();
      i = 1;
      for (IWordID localIWordID : localList)
      {
        double d = lch(paramString1, paramInt, paramString2, i, paramString3);
        localTreeMap.put(paramString1 + "#" + paramString3 + "#" + paramInt + "," + paramString2 + "#" + paramString3 + "#" + i, Double.valueOf(d));
        i++;
      }
    }
    else
    {
      return localTreeMap;
    }
    return localTreeMap;
  }

  public double max(String paramString1, String paramString2, String paramString3)
  {
    double d1 = 0.0D;
    TreeMap<String,Double> localTreeMap = lch(paramString1, paramString2, paramString3);
    for (String str : localTreeMap.keySet())
    {
      double d2 = localTreeMap.get(str).doubleValue();
      if (d2 > d1)
      {
        d1 = d2;
      }
    }
    return d1;
  }

}
