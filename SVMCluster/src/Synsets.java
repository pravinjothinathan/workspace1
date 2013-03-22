/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package svmcluster;

import java.util.*;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.morph.WordnetStemmer;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;
import java.net.URL;
/**
 *
 * @author Nidhi
 */
public class Synsets {
    
    URL url;
    IDictionary dictionary;
    ArrayList localArrayList = new ArrayList();

    public Synsets() throws Exception
    {
         url = new URL("file",null,"/Users/pravinjothinathan/Downloads/dict");
         dictionary = new Dictionary(url);
         dictionary.open();

        ISynset localISynset = null;
        Iterator localIterator1 = null;
        List localList1 = null;
        List localList2 = null;
        localIterator1 = dictionary.getSynsetIterator(POS.VERB);
        while (localIterator1.hasNext())
        {
            localISynset = (ISynset)localIterator1.next();
            localList1 = localISynset.getRelatedSynsets(Pointer.HYPERNYM);
            localList2 = localISynset.getRelatedSynsets(Pointer.HYPERNYM_INSTANCE);
            if ((!localList1.isEmpty()) || (!localList2.isEmpty()))
                continue;
            localArrayList.add(localISynset.getID());
        }
    }

    public ISynsetID getSynID(String ev)
    {
        ISynsetID ID=null;
        try{
            WordnetStemmer root = new WordnetStemmer(dictionary);
            List<String> stems = root.findStems(ev, POS.VERB);
            String stem1=ev;
            if(stems.size()>=1)
            {
                stem1 = stems.get(0);
                IIndexWord idxWord = dictionary.getIndexWord(stem1, POS.VERB );
                IWordID wordID = idxWord.getWordIDs().get(0);
                IWord word_new = dictionary.getWord(wordID);
                ISynset synword = word_new.getSynset();
                ID = synword.getID();
            }
            else
            {
            stems = root.findStems(ev, POS.NOUN);
            stem1=ev;
            if(stems.size()>=1)
            {
                stem1 = stems.get(0);
                IIndexWord idxWord = dictionary.getIndexWord(stem1, POS.NOUN );
                IWordID wordID = idxWord.getWordIDs().get(0);
                IWord word_new = dictionary.getWord(wordID);
                ISynset synword = word_new.getSynset();
                ID = synword.getID();
            }

            }

        }catch(Exception e){return(ID);}
            return(ID);
    }

      public int getSynoffset(String ev)
    {
        int offset = -99;
        try{

            WordnetStemmer root = new WordnetStemmer(dictionary);
            List<String> stems = root.findStems(ev, POS.VERB);
            String stem1=ev;


            if(stems.size()>=1)
            {
                stem1 = stems.get(0);
            IIndexWord idxWord = dictionary.getIndexWord(stem1, POS.VERB );
            IWordID wordID = idxWord.getWordIDs().get(0);
            IWord word_new = dictionary.getWord(wordID);
            ISynset synword = word_new.getSynset();
            offset = synword.getOffset();
            }
             else
            {
            stems = root.findStems(ev, POS.NOUN);
            stem1=ev;
            if(stems.size()>=1)
            {
                stem1 = stems.get(0);
                IIndexWord idxWord = dictionary.getIndexWord(stem1, POS.NOUN );
                IWordID wordID = idxWord.getWordIDs().get(0);
                IWord word_new = dictionary.getWord(wordID);
                ISynset synword = word_new.getSynset();
                offset = synword.getOffset();
            }
            }
        }catch(Exception e){return(-99);}
            return(offset);
    }

    public ISynsetID getRelatedSysnsets(ISynsetID syn1,ISynsetID syn2)
    {
        
        ISynsetID ID = null;

            try{
            ISynset synword = dictionary.getSynset(syn1);
            List<ISynsetID> related1 = synword.getRelatedSynsets(Pointer.HYPERNYM);
            synword = dictionary.getSynset(syn2);
            List<ISynsetID> related2 = synword.getRelatedSynsets(Pointer.HYPERNYM);

             ISynsetID common=null;
             if(!related1.isEmpty() && !related2.isEmpty())
             {

                for(ISynsetID c1: related1)
                {
                    if(related2.contains(c1))
                    {
                        common = c1;
                        break;
                    }
                }
             }
            else if(!related1.isEmpty())
                common = related1.get(0);
            else if(!related2.isEmpty())
                common = related2.get(0);
             else
                 common = syn1;
             
                ID = common;

            
             }catch(Exception e){
                 if(syn1!=null)
                     ID = syn1;
                else if(syn2 != null)
                     ID = syn2;
                 return(ID);}
        return(ID);
    }


    
    public int getParentSynsetOffset(String ev1,String ev2)
    {
        int offset=-99;
        try{

         WordnetStemmer root = new WordnetStemmer(dictionary);
            List<String> stems = root.findStems(ev1, POS.VERB);
            String stem1=ev1;
            if(stems.size()>=1)
                stem1 = stems.get(0);
            IIndexWord idxWord = dictionary.getIndexWord(stem1, POS.VERB );
            IWordID wordID = idxWord.getWordIDs().get(0);
            IWord word_new = dictionary.getWord(wordID);
            ISynset synword = word_new.getSynset();
            List<ISynsetID> related1 = synword.getRelatedSynsets(Pointer.HYPERNYM);
            stems = root.findStems(ev1, POS.VERB);
            stem1=ev2;
            if(stems.size()>=1)
                stem1 = stems.get(0);
            idxWord = dictionary.getIndexWord(stem1, POS.VERB );
            wordID = idxWord.getWordIDs().get(0);
            word_new = dictionary.getWord(wordID);
            synword = word_new.getSynset();
            List<ISynsetID> related2 = synword.getRelatedSynsets(Pointer.HYPERNYM);

            ISynsetID common=null;
             if(!related1.isEmpty() | !related2.isEmpty())
             {

                for(ISynsetID c1: related1)
                {
                    if(related2.contains(c1))
                    {
                        common = c1;
                        break;
                    }
                }
             if(common==null)
                  offset = -99;
            else
                offset = common.getOffset();

            }
            else
                offset=-99;
             }catch(Exception e){return(-99);}
        return(offset);
    }

    public Double getLCSimilarity(String ev1, String ev2)
    {

        Double similarity=0.0;
        try{
            WordnetStemmer root = new WordnetStemmer(dictionary);
            String stem1=ev1, stem2 = ev2;

            List<String> stems = root.findStems(ev1, POS.VERB);
            if(stems.size()>=1)
                stem1 = stems.get(0);
            List<String> stems2 = root.findStems(ev2, POS.VERB);
            if(stems2.size()>=1)
              stem2 = stems2.get(0);
            int sense1 = (dictionary.getIndexWord(stems.get(0), POS.VERB).getWordIDs()).size();
            int sense2 = (dictionary.getIndexWord(stems2.get(0), POS.VERB).getWordIDs()).size();

        
            LeacockAndChodorow lc = new LeacockAndChodorow(dictionary, localArrayList);
            similarity = lc.lch(stems.get(0), sense1, stems2.get(0), sense2, "V");
        }catch(Exception ec){return(0.0);}
        return(similarity);
    }


      public Double getLCSimilarity(ISynsetID syn1 , ISynsetID syn2)
    {

        Double similarity=0.0;
        try{
        ISynset sy1 = dictionary.getSynset(syn1);
        ISynset sy2 = dictionary.getSynset(syn2);
            LeacockAndChodorow lc = new LeacockAndChodorow(dictionary, localArrayList);
            similarity = lc.lchID(sy1, sy2, "V");
        }catch(Exception ec){return(0.0);}
        return(similarity);
    }

}