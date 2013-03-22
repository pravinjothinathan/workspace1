/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messengerserver;

import SecFeatures.Key;
import SecFeatures.SecAlgosWrapper;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author alagaranandrajendran
 */
class Client_Data
{
    String Name;
    String IPAddress;
    int Port;
    boolean Logged_in;
    String Existing_Users;//This is used only to get the list of active clients
    Socket Cli_Socket;
    String Password;
}

class Client_Data_Manager
{
    ArrayList<Client_Data> Cli_Data_Array;
    SecAlgosWrapper saw;
    
    public Client_Data_Manager()
    {
        Cli_Data_Array = new ArrayList<Client_Data>();
        saw = new SecAlgosWrapper();
    }
    
    
    public void Send_Client_List()
    {
        int count = 0;
        String Existing_Users = "";

        for(int iLoop = 0; iLoop < Cli_Data_Array.size() ; iLoop++)
        {
            if(Cli_Data_Array.get(iLoop).Logged_in)
            {
                if(count>0)
                {
                   Existing_Users+=",";
                }
                Existing_Users+=Cli_Data_Array.get(iLoop).Name;
                count++;
            }
        }
        
        //Send the existing users list to all the clients
        for (int iLoop = 0; iLoop < Cli_Data_Array.size(); iLoop++)
        {
            if(Cli_Data_Array.get(iLoop).Logged_in)
            {
                try{
                    PrintWriter outToClient = new PrintWriter(Cli_Data_Array.get(iLoop).Cli_Socket.getOutputStream(), true);
                    outToClient.println("Cli_List:"+saw.EncryptMessage(Cli_Data_Array.get(iLoop).Password, Existing_Users));
                    System.out.println("Cli_List:"+saw.EncryptMessage(Cli_Data_Array.get(iLoop).Password, Existing_Users));
                }
                catch(Exception e)
                {
                    System.out.println("Exception occured at sending client list to Client:"+Cli_Data_Array.get(iLoop).Name);
                }
                    
            }
        }
    }
    
    //New_Client;Read_Cli_List;Update_Client;Read_Client;
    public synchronized Client_Data Data_Handler(String Case, String UserName, Client_Data Cli_Data)
    {
        
        Client_Data Return_Data = null;;
        
        if(Case.compareTo("New_Client") == 0)
        {
            int Cli_No =-1;
            
            //Find the Clinet No of this client
            for (int iLoop = 0; iLoop < Cli_Data_Array.size(); iLoop++) {
                if (Cli_Data_Array.get(iLoop).Name.compareTo(UserName) == 0) {
                    Cli_No = iLoop;
                }
            }
            
            if(Cli_No == -1)//New User
            {
                Cli_Data_Array.add(Cli_Data);
                Return_Data = Cli_Data;
            }
            //else Return Data will be null; User already exist
        }
        else if(Case.compareTo("Send_Cli_List") == 0)
        {
            //this will send the client list to all the live clients
            this.Send_Client_List();
            
        }
        /*
        else if(Case.compareTo("Read_Cli_List") == 0)
        {
            int count = 0;
            String Existing_Users = "";
            
            for(int iLoop = 0; iLoop < Cli_Data_Array.size() ; iLoop++)
            {
                if(Cli_Data_Array.get(iLoop).Logged_in && Cli_Data_Array.get(iLoop).Name.compareTo(UserName) != 0)
                {
                    if(count>0)
                    {
                       Existing_Users+=",";
                    }
                    Existing_Users+=Cli_Data_Array.get(iLoop).Name;
                    count++;
                }
            }
            
            Return_Data = Cli_Data;
            Return_Data.Existing_Users = Existing_Users;
            
        }
         * 
         */
        else if(Case.compareTo("Update_Client") == 0)
        {
            int Cli_No =-1;
            
            //Find the Clinet No of this client
            for (int iLoop = 0; iLoop < Cli_Data_Array.size(); iLoop++) {
                if (Cli_Data_Array.get(iLoop).Name.compareTo(UserName) == 0) {
                    Cli_No = iLoop;
                }
            }
            
            if(Cli_No >=0)
            {
                Cli_Data_Array.get(Cli_No).IPAddress = Cli_Data.IPAddress;
                Cli_Data_Array.get(Cli_No).Port = Cli_Data.Port;
                Cli_Data_Array.get(Cli_No).Logged_in = Cli_Data.Logged_in;
                Cli_Data_Array.get(Cli_No).Cli_Socket = Cli_Data.Cli_Socket;
                Cli_Data_Array.get(Cli_No).Password = Cli_Data.Password;
                
                Return_Data = Cli_Data;
            }
            
            //else Return Data will be null; User data not exist
        }
        else if(Case.compareTo("Read_Client") == 0)
        {
            int Cli_No =-1;
            
            //Find the Clinet No of this client
            for (int iLoop = 0; iLoop < Cli_Data_Array.size(); iLoop++) {
                if (Cli_Data_Array.get(iLoop).Name.compareTo(UserName) == 0) {
                    Cli_No = iLoop;
                }
            }
            
            if(Cli_No >=0)
            {
                Return_Data = Cli_Data_Array.get(Cli_No);
            }
             //else Return Data will be null; User data not exist
        }
        
        return Return_Data;
    }
    
}