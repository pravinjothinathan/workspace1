/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package svmcluster;

import java.util.*;
import java.io.*;

/**
 *
 * @author Nidhi
 */
public class Format {
     HashMap<String, String> tree = new HashMap<String, String>();
    Set nodes = new TreeSet<String>();
    String file;

    public Format(String file)
    {
        this.file = file;
        this.tree.clear();
        this.nodes.clear();
    }

    public static void main(String[] args) {

        System.out.println("\nEnter full path of folder which contains all files to be tested for format.");
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

    public boolean checkFormat(File fn) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(fn));
        String nxtline;
        String[] line;
        int nline = 0, index;
        boolean flag = false, formatFlag = true;
        String patternString = "[a-zA-Z]+[0-9]*|[a-zA-Z]*[0-9]+|[a-zA-Z]+[0-9]* [a-zA-Z]+[0-9]*|[a-zA-Z]*[0-9]+ [a-zA-Z]*[0-9]+|[a-zA-Z]+ [a-zA-Z]+|[a-zA-Z]+ [0-9]+|[0-9]+ [a-zA-Z]+|[0-9]+ [0-9]+";
        while ((nxtline = in.readLine()) != null)
        {
            nline++;
            if(nxtline.isEmpty())
            {
                 System.out.println("Found a new line character at line(Check ahead from that line if there are more than 1 such errors) : " + nline + ": " + nxtline + " in file");
                 formatFlag = false;
            }
            else
            {
                if (nxtline.matches(patternString))
                {
                    line = nxtline.split(" ");
                    if (line.length == 2)
                    {
                        if (!(line[0].equals(line[1])))
                        {
                            if (!(this.tree.containsKey(line[0])))
                            {
                                this.tree.put(line[0], line[1]);
                                this.nodes.add(line[0]);
                                this.nodes.add(line[1]);
                            }
                            else
                            {
                                System.out.println("Please check, child already has one parent in line: " + nline + ": " + nxtline + " in file");
                                formatFlag = false;
                            }
                        }
                        else
                        {
                            System.out.println("Please check, child is its own parent in line: " + nline + ": " + nxtline + " in file");
                            formatFlag = false;
                        }
                    /*}
                    else
                    {
                        System.out.println("Please check format for line: " + nline + ": " + nxtline + " in file");
                        formatFlag = false;
                    }*/
                }
                else
                {
                    if ((line.length == 1))
                    {
                        if (flag == false)
                        {
                            if (nxtline.matches("[a-zA-Z]+[0-9]*|[a-zA-Z]*[0-9]+]"))
                            {
                                flag = true;
                                this.tree.put("root", line[0]);
                            }
                        }
                        else
                        {
                            if (nxtline.matches("[a-zA-Z]+[0-9]*|[a-zA-Z]*[0-9]+]"))
                            {
                                System.out.println("There seem to be two roots, please check!");
                            }
                        }
                    }
                    else
                    {
                        System.out.println("Please check format for line: " + nline + ": " + nxtline + " in file");
                        formatFlag = false;
                    }
                }
            }
                else
                    {
                        System.out.println("Please check format for line: " + nline + ": " + nxtline + " in file");
                        formatFlag = false;
                    }
        }
        }
        return(formatFlag);
   }

    public void checkTree()
    {
       String root = tree.get("root");
       String child;
       for(Map.Entry<String, String> entry : this.tree.entrySet())
       {
           child = entry.getKey();
           nodes.remove(child);
        }
        String nd;

        if(nodes.isEmpty()==false)
        {
           for (Iterator i = nodes.iterator(); i.hasNext();)
           {
                nd = (String) i.next();
                if(!(nd.equals(root)))
                {
                    System.out.println("Please check, "+nd+" does not have a valid parent.");
                }
           }
       }
        String parent,temp,e="root"; int number = 0;
        ArrayList<String> plus2 = new ArrayList<String>();
        for(Map.Entry<String, String> entry : this.tree.entrySet())
       {
            number =0;

            if(entry.getKey().equals("root"))
                continue;
            parent = entry.getValue();
            for(Map.Entry<String, String> enter : this.tree.entrySet())
            {
                if(enter.getKey().equals("root"))
                    continue;
                temp = enter.getValue();
                if(temp.equals(parent))
                {
                    number++;
                }
            }
            if(number<=1)
            {
                System.out.println("Parent has only one child, please check node: "+parent);
            }
            if(number>2)
            {

                if(!plus2.contains(parent))
                {
                    plus2.add(parent);
                }
            }
       }
        if(!plus2.isEmpty())
        System.out.println("*If strictly binary is needed please check: Parent has more than two children, please check nodes: "+plus2.toString());
        plus2.clear();
    }


}
