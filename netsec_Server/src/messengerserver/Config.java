/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messengerserver;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Properties;

/**
 *
 * @author alagaranandrajendran
 */
class Config 
{
    Properties configFile;
    String Filename;

    public Config(String FileName) {
        configFile = new java.util.Properties();
        this.Filename = FileName;
        //configFile.store(new FileOutputStream(this.Filename),null);
    }

    public String getProperty(String key) {
        String value = "";
        try {
            configFile.load(new FileInputStream(this.Filename));
            value = this.configFile.getProperty(key);
        }
        catch (Exception e) {
            System.out.println("Exception at File Write: " + e.getMessage());
        }
        return value;
    }
    
    public String Access_File(String Case, String Data, String Key)
    {
        String value = "";
        if(Case.compareTo("Read_Key")==0)
        {
            try {
                configFile.load(new FileInputStream(this.Filename));
                value = this.configFile.getProperty(Key);
            }
            catch (Exception e) {
                System.out.println("Exception at File Write: " + e.getMessage());
            }
        }
        else if(Case.compareTo("Key_Exist")==0){
            //Write the value to the key;
            try {
                configFile.load(new FileInputStream(this.Filename));
                if(this.configFile.containsKey(Key))
                {
                    value = "true";
                }
            }
            catch (Exception e) {
                System.out.println("Exception at File Write: " + e.getMessage());
            }
            
        }
        
        /*
        else if(Case.compareTo("Write_Key")==0){
            //Write the value to the key;
            try {
                configFile.load(new FileInputStream(this.Filename));
                this.configFile.setProperty(Key, Data);
                configFile.store(new FileOutputStream(this.Filename),null);
            }
            catch (Exception e) {
                System.out.println("Exception at File Write: " + e.getMessage());
            }
            
        }
         * 
         */
        else if(Case.compareTo("Write_Data")==0){
            //Write the data to the file; when new user is created
            try
            {
                // Create file 
                FileWriter fstream = new FileWriter(Filename, true);
                BufferedWriter out = new BufferedWriter(fstream);
                
                out.write(Data);
                //Close the output stream
                out.close();
            }
            catch (Exception e){//Catch exception if any
                System.out.println("Exception at File Write: " + e.getMessage());
            }
        }
        else{
            System.out.println("Config file case not found");
        }
        return value;
    }
}
