/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messengerproject;

import SecFeatures.SecAlgosWrapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author alagaranandrajendran
 */
class Client_Details {

    String Name;
    int Port_No;
    String IPAddress;
    Socket Cli_Socket;
    String History;
    Client_Thread Cli_Thread;
    ClientWindow Cli_Window;
    String SharedKey;
    String Nonce;

    public void Send_Msg(String Msg) {
        try {
            if (Cli_Socket != null) {
                PrintWriter outToClient = new PrintWriter(Cli_Socket.getOutputStream(), true);
                outToClient.println(Msg);
            }
        } catch (Exception e) {
            System.out.println("Exception occured at Sending Message to Client: " + e);
        }
    }
}

class Client_Data_Handler
{
    ArrayList<Client_Details> Cli_Details;
    SecAlgosWrapper saw;
    
    public Client_Data_Handler()
    {
        Cli_Details = new ArrayList<Client_Details>();
        saw = new SecAlgosWrapper();
    }
    
    public String[] Get_Names_List()
    {
        int num_Clients =  Cli_Details.size();
        String[] Names_List = new String[num_Clients];
        for(int iLoop = 0; iLoop< num_Clients; iLoop++)
        {
            Names_List[iLoop] = Cli_Details.get(iLoop).Name;
        }
        
        return Names_List;
    }
    
    public boolean Clients_Alive(String UserName)
    {
        boolean Cli_Alive = false;
        
        //Find the Clinet No of this client
        for (int iLoop = 0; iLoop < Cli_Details.size(); iLoop++) {
            if (Cli_Details.get(iLoop).Name.compareTo(UserName) == 0 && Cli_Details.get(iLoop).Cli_Window != null) {
                Cli_Alive = true;
            }
        }
        return Cli_Alive;
    }
    
    //Check any clients alive or not
    public boolean Clients_Alive()
    {
        boolean Cli_Alive = false;
        for(int iLoop = 0; iLoop< Cli_Details.size(); iLoop++)
        {
            if(Cli_Details.get(iLoop).Cli_Window != null)
            {
                Cli_Alive = true;
            }
        }
        
        return Cli_Alive;
    }
    
    //Case: Add_Client; Update_Client; Get_Client;Session_Exist; Start_Session; Send_Msg; Recv_Msg; Close_Socket; Close_Session; Send_Msg
    public synchronized Client_Details Handle_Details(String Case, String Msg, String UserName, Client_Details Client_Detail, String This_Name, ClientWindow Cli_Window)
    {
        int Client_No= -1;
        int iLoop;
        
        Client_Details Ret_Cli_Detail = null;
        
        if(Case.compareTo("Add_Client")==0)
        {
            String[] Temp_UserName;
            int idx;
            
            Temp_UserName = new String[Cli_Details.size()];
            
            //Get the list of User Names from Cli_Details
            for (iLoop = 0; iLoop < Cli_Details.size(); iLoop++) {
                Temp_UserName[iLoop] = Cli_Details.get(iLoop).Name;
            }
            idx = Arrays.binarySearch(Temp_UserName, UserName);
            
            if(idx >=0) //Username already Exists
            {
                //Avoid Adding already existing user name coming from server
                //as a data of existing user list
                if(Client_Detail.Cli_Socket != null)
                {
                    Cli_Details.remove(idx);
                    Cli_Details.add(idx, Client_Detail);
                }
            }
            else{ //Username not found
            
                //Add Client Details based on the Ascending order of the User Name

                Temp_UserName = new String[Cli_Details.size()+1];

                //Get the list of User Names from Cli_Details
                for (iLoop = 0; iLoop < Cli_Details.size(); iLoop++) {
                    Temp_UserName[iLoop] = Cli_Details.get(iLoop).Name;
                }
                Temp_UserName[iLoop] = UserName;

                //sort the UserNames
                Arrays.sort(Temp_UserName);

                int Index = Arrays.binarySearch(Temp_UserName, UserName);
                if(Index >=0 && Index < Cli_Details.size())
                {
                    Cli_Details.add(Index, Client_Detail);
                }
                else{
                    Cli_Details.add(Client_Detail);
                }
            }
        }
        else if(Case.compareTo("Get_Client")==0)
        {
            //Find the Clinet No of this client
            for (iLoop = 0; iLoop < Cli_Details.size(); iLoop++) {
                if (Cli_Details.get(iLoop).Name.compareTo(UserName) == 0) {
                    Client_No = iLoop;
                }
            }
            if(Client_No >= 0)
            {
                Ret_Cli_Detail = Cli_Details.get(Client_No);
            }
            else {
                    System.out.println("Client Details not available: "+UserName);
            }
        }
        else if(Case.compareTo("Update_Client")==0)
        {
            //Find the Clinet No of this client
            for (iLoop = 0; iLoop < Cli_Details.size(); iLoop++) {
                if (Cli_Details.get(iLoop).Name.compareTo(UserName) == 0) {
                    Client_No = iLoop;
                }
            }
            if(Client_No >= 0)
            {
                Cli_Details.remove(Client_No);
                Cli_Details.add(Client_No, Client_Detail);
            }
            else {
                    System.out.println("Client Details not available: "+UserName);
            }
        }
        else if(Case.compareTo("Start_Session")==0)
        {
            //Find the Clinet No of this client
            for (iLoop = 0; iLoop < Cli_Details.size(); iLoop++) {
                if (Cli_Details.get(iLoop).Name.compareTo(UserName) == 0) {
                    Client_No = iLoop;
                }
            }
            if(Client_No >= 0)
            {
                try
                {
                    //Msg Data format---> //Username:Nonce:IPAddress:Port:KAB:Ticket(ThisName:KAB)
                    
                    String[] Temp = Msg.split(":");
                    
                    //Send Data format: Request:Ticket(ThisName:KAB)
                    String SendDataFormat = "Request:"+Temp[5];
                    
                    Socket Cli_Socket = new Socket(Cli_Details.get(Client_No).IPAddress, Cli_Details.get(Client_No).Port_No);
                    Cli_Details.get(Client_No).Cli_Socket = Cli_Socket;
                    
                    BufferedReader inFromClient = new BufferedReader(new InputStreamReader(Cli_Socket.getInputStream()));
                    
                    Cli_Details.get(Client_No).Send_Msg(SendDataFormat);
                    inFromClient.readLine();
                    
                    Client_Thread Cli_Thread = new Client_Thread(this, Cli_Socket, UserName, This_Name);
                    Cli_Details.get(Client_No).Cli_Thread = Cli_Thread;
                    Cli_Thread.start();
                    
                    Cli_Details.get(Client_No).Cli_Window = Cli_Window;
                }
                catch(Exception e)
                {
                    System.out.println("Exception Occured while closing session:"+e);
                }
            }
            else {
                    System.out.println("Client Details not available: "+UserName);
            }
        }
        else if(Case.compareTo("Send_Msg")==0)
        {
            //Find the Clinet No of this client
            for (iLoop = 0; iLoop < Cli_Details.size(); iLoop++) {
                if (Cli_Details.get(iLoop).Name.compareTo(UserName) == 0) {
                    Client_No = iLoop;
                }
            }
            try
            {
                if (Client_No >= 0) {
                    //Send Msg to the Client
                    String SendData = "Msg:"+Msg;
                    String Encrypted_Data = saw.EncryptMessage(Cli_Details.get(Client_No).SharedKey, SendData);
                    
                    Cli_Details.get(Client_No).Send_Msg(Encrypted_Data);
                    //Add it to the History
                    Cli_Details.get(Client_No).History += "\n"+This_Name+": "+ Msg;
                    //Update it on the Client Window
                    Cli_Details.get(Client_No).Cli_Window.Chat_History(Cli_Details.get(Client_No).History);
                }
                else {
                    System.out.println("Client Details not available: "+UserName);
                }
            }
            catch(Exception e)
            {
                System.out.println("Exception Occured while closing session:"+e);
            }
        }
        else if(Case.compareTo("Recv_Msg")==0)
        {
            //Find the Clinet No of this client
            for (iLoop = 0; iLoop < Cli_Details.size(); iLoop++) {
                if (Cli_Details.get(iLoop).Name.compareTo(UserName) == 0) {
                    Client_No = iLoop;
                }
            }
            if (Client_No >= 0) {
                
                //Add it to the History
                Cli_Details.get(Client_No).History += "\n"+UserName+": "+ Msg;
                //Update it on the Client Window
                Cli_Details.get(Client_No).Cli_Window.Chat_History(Cli_Details.get(Client_No).History);
            }
            else {
                System.out.println("Client Details not available: "+UserName);
            }
        }
        else if(Case.compareTo("Close_Socket")==0)
        {
            //Close the Client Socketa and Client Window
            //Find the Client No of this client
            for (iLoop = 0; iLoop < Cli_Details.size(); iLoop++) {
                if (Cli_Details.get(iLoop).Name.compareTo(UserName) == 0) {
                    Client_No = iLoop;
                }
            }
            
            try
            {
                if (Client_No >= 0) {
                    try{
                        Cli_Details.get(Client_No).Cli_Window.Exit_Window();
                    }
                    catch(Exception e)
                    {}
                    try{
                        Cli_Details.get(Client_No).Cli_Socket.close();
                    }
                    catch(Exception e)
                    {
                        System.out.println("Exception Occured at Socket Close: "+e);   
                    }
                    Cli_Details.get(Client_No).Cli_Socket = null;
                    Cli_Details.get(Client_No).Cli_Thread = null;
                    Cli_Details.get(Client_No).Cli_Window = null;
                    Cli_Details.get(Client_No).History = "";
                }
                else {
                    System.out.println("Client Details not available: "+UserName);
                }
            }
            catch(Exception e)
            {
                System.out.println("Exception Occured while closing session:"+e);
            }
        }
        else if(Case.compareTo("Close_Session")==0)
        {
            //Close the Client Thread and Client Window
            //Find the Client No of this client
            for (iLoop = 0; iLoop < Cli_Details.size(); iLoop++)
            {
                if (Cli_Details.get(iLoop).Name.compareTo(UserName) == 0) {
                    Client_No = iLoop;
                }
            }
            
            if (Client_No >= 0) {
                try
                {
                    Cli_Details.get(Client_No).Cli_Thread.Exit();
                    
                    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(Cli_Details.get(Client_No).Cli_Socket.getInputStream()));
                    Cli_Details.get(Client_No).Send_Msg(saw.EncryptMessage(Cli_Details.get(Client_No).SharedKey,"Close"));
                
                    inFromServer.readLine();
                    Cli_Details.get(Client_No).Cli_Socket.close();
                    Cli_Details.get(Client_No).Cli_Socket = null;
                    Cli_Details.get(Client_No).Cli_Thread = null;
                    Cli_Details.get(Client_No).Cli_Window = null;
                    Cli_Details.get(Client_No).History = "";
                }
                catch(Exception e)
                {
                    System.out.println("Exception Occured while closing session:"+e);
                }
            } 
            else {
                System.out.println("Client Details not available: "+UserName);
            }
        }
        else{
            System.out.println("case not matched: "+ Case);
        }
        
        return Ret_Cli_Detail;
    }
}
