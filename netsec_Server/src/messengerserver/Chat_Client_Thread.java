/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messengerserver;

import SecFeatures.SecAlgosWrapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 *
 * @author alagaranandrajendran
 */
class Chat_Client_Thread extends Thread
{
    private volatile boolean IsValid = true;
    Socket Cli_Socket;
    Config cfg;
    String ClientName;
    Client_Data_Manager Data_Mgr;
    
    public Chat_Client_Thread(Socket Cli_Socket, Config cfg, String ClientName, Client_Data_Manager Data_Mgr)
    {
        this.Cli_Socket = Cli_Socket;
        this.cfg = cfg;
        this.ClientName = ClientName;
        this.Data_Mgr = Data_Mgr;
    }
    
    public void Exit()
    {
        IsValid = false;
        try{
            Thread.sleep(1000);
            Cli_Socket.close();
        }
        catch(Exception e)
        {
            System.out.println("Exception occured: "+e);
        }
    }
    
    @Override
    public void run()
    {
        IsValid = true;
        BufferedReader inFromClient;
        PrintWriter outToClient;
        String Receive_Data;
        String[] Temp;
        try{
            inFromClient = new BufferedReader(new InputStreamReader(Cli_Socket.getInputStream()));
            outToClient = new PrintWriter(Cli_Socket.getOutputStream(), true);
            Cli_Socket.setSoTimeout(100);
            
            SecAlgosWrapper saw = new SecAlgosWrapper();
            Client_Data This_Cli_data = new Client_Data();
            This_Cli_data = Data_Mgr.Data_Handler("Read_Client", ClientName, This_Cli_data);
            
            while(IsValid)
            {
                try
                {
                    String line = inFromClient.readLine();
                    System.out.println("line ->"+line);
                    System.out.println("This_Cli_data.Password :"+This_Cli_data.Password);
                    Receive_Data = saw.DecryptMessage(This_Cli_data.Password, line);
                    Temp = Receive_Data.split(":"); 
                    //Chat:UserName:RandomNumber(N1)
                    //Logout
                    
                    if(Temp[0].compareTo("Chat") == 0)
                    {
                        Client_Data New_Cli_Data = new Client_Data();
                        New_Cli_Data.Name = Temp[1];
                        //Check whether username already exists or not
                        New_Cli_Data = Data_Mgr.Data_Handler("Read_Client", Temp[1], New_Cli_Data);
                        
                        //int Nonce = Integer.parseInt(Temp[2]);
                        
                        if(New_Cli_Data == null)
                        {
                            outToClient.println(saw.EncryptMessage(This_Cli_data.Password, "NotValid_Client:User details not found"));
                        }
                        else{//Existing user
                            if(New_Cli_Data.Logged_in)
                            {   
                                //Create a session with shared key, Nonce, Ticket for User and username
                                
                                
                                String Shared_Key = saw.KeyGen64().strHexKey;//New random key for client1 and client2
                                
                                //
                                String Message = This_Cli_data.Name + ":"+Shared_Key;
                                
                                String SharedKeyEncbyNewClientKey = saw.EncryptMessage(New_Cli_Data.Password, Message);
                                
                                //String Ticket_String = Shared_Key+":"+This_Cli_data.Name;
                                //Will be the AES encyprtion of Ticket_String with New Client key
                                //String Ticket = saw.EncryptMessage(New_Cli_Data.Password, Ticket_String);
                                
                                //AES-Client1[Session:N1:Client2:KAB:AES-Client2[KAB,Client1]]
                                //String data ="Session:"+Shared_Key; 
                                
                                String Send_Data = New_Cli_Data.Name+":" + Temp[2]+":"+New_Cli_Data.IPAddress+":"+New_Cli_Data.Port+":"+Shared_Key+":"+SharedKeyEncbyNewClientKey;
                                
                                //String data = "Valid_Client:"+Temp[1]+":"+New_Cli_Data.IPAddress+":"+New_Cli_Data.Port;
                                outToClient.println("Session:"+saw.EncryptMessage(This_Cli_data.Password, Send_Data));
                            }
                            else{ //User not logged in
                                outToClient.println("NotValid_Client:"+saw.EncryptMessage(This_Cli_data.Password, "User not logged in"));
                            }
                        }
                    }
                    else if(Temp[0].compareTo("Logout") == 0)
                    {
                        Client_Data New_Cli_Data = new Client_Data();
                        New_Cli_Data.Name = ClientName;
                        New_Cli_Data.IPAddress = "";
                        New_Cli_Data.Port = 0;
                        New_Cli_Data.Logged_in = false;
                        New_Cli_Data.Cli_Socket = null;
                        
                        //Update logout info in the data manager
                        Data_Mgr.Data_Handler("Update_Client", ClientName, New_Cli_Data);
                        
                        //Send the Existing users list to all the clients
                        Data_Mgr.Data_Handler("Send_Cli_List", "", null);
                        
                        outToClient.println("Done_Logout");
                        
                        IsValid= false;
                    }
                    else{
                        outToClient.println("Not Valid");
                    }
                    
                }
                catch(SocketTimeoutException e)
                {
                }
                catch(NullPointerException e)
                {
                    //For handling the Client Crash
                    Client_Data New_Cli_Data = new Client_Data();
                    New_Cli_Data.Name = ClientName;
                    New_Cli_Data.IPAddress = "";
                    New_Cli_Data.Port = 0;
                    New_Cli_Data.Logged_in = false;
                    New_Cli_Data.Cli_Socket = null;
                    
                    //Update logout info in the data manager
                    Data_Mgr.Data_Handler("Update_Client", ClientName, New_Cli_Data);
                    
                    //Send the Existing users list to all the clients
                    Data_Mgr.Data_Handler("Send_Cli_List", "", null);

                    IsValid= false;
                }
                catch(Exception e)
                {
                    System.out.println("Exception occured at Chat Client: "+e);
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Exception occured at Chat Client: "+e);
        }
    }
    
}
