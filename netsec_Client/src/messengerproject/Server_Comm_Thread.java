/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messengerproject;

import SecFeatures.SecAlgosWrapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 *
 * @author alagaranandrajendran
 */
class Server_Comm_Thread extends Thread
{
    private volatile boolean IsValid = true;
    MessengerProject App;
    Socket Server_Socket;
    String This_Name;

    public Server_Comm_Thread(MessengerProject App, Socket Server_Socket, String This_Name) {
        this.App = App;
        this.Server_Socket = Server_Socket;
        this.This_Name = This_Name;
    }

    public void Exit() {
        
        IsValid = false;
    }

    @Override
    public void run() {
        try{
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(Server_Socket.getInputStream()));
            Server_Socket.setSoTimeout(100);
            String Receive_Data;
            String[] Temp;
            IsValid = true;
            
            SecAlgosWrapper saw = new SecAlgosWrapper();
            
            while(IsValid)
            {
                try{
                    
                    Receive_Data = inFromServer.readLine();
                    //Receive_Data = 
                    
                    System.out.println(Receive_Data);
                    Temp = Receive_Data.split(":");
                    
                    if(Temp[0].compareTo("User_Valid") == 0)
                    {
                        //User_Valid
                        App.Start_Messenger();;
                    }
                    else if(Temp[0].compareTo("User_NotValid") == 0)
                    {
                        //User_NotValid:Warning Message
                        App.Set_Warning_Login(Temp[1]);
                        //user name is not valid; so, this thread will be closed and opened again
                        IsValid = false;
                    }
                    else if(Temp[0].compareTo("Cli_List") == 0)
                    {
                        String CliList = saw.DecryptMessage(App.Get_CliSvr_Key(), Temp[1]);
                        
                        System.out.println("CliList after decryption ->"+CliList);
                        
                        //Cli_List:User1,User2...
                        String[] Temp_CliList = CliList.split(",");
                        String[] NameList = new String[Temp_CliList.length - 1]; //Excluding This Client Name
                        int Counter = 0;
                        for(int iLoop = 0; iLoop<Temp_CliList.length; iLoop++)
                        {
                            if(Temp_CliList[iLoop].compareTo(This_Name)!= 0)
                            {
                                NameList[Counter] = Temp_CliList[iLoop];
                                Counter++;
                            }
                        }
                        
                        //update the nameslist on Chat messenger and update client details
                        App.Update_ClientList(NameList);
                        
                    }
                    else if(Temp[0].compareTo("Session") == 0)
                    {
                 
                        String Session_String = saw.DecryptMessage(App.Get_CliSvr_Key(), Temp[1]);   
                        //Username:Nonce:IPAddress:Port:KAB:Ticket(ThisName:KAB)
                        App.Open_Cli_window(Session_String);
                    }
                    else if(Temp[0].compareTo("NotValid_Client") == 0)
                    {
                        String Warning = saw.DecryptMessage(App.Get_CliSvr_Key(), Temp[1]);
                        App.Set_Warning_Chat_Wndw(Warning);
                    }
                    else if(Temp[0].compareTo("Done_Logout") == 0)
                    {
                        App.Close_Application();
                        IsValid = false;
                    }
                }
                catch(SocketTimeoutException e)
                {
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception Occured at Server Communication Thread:"+e);
        }
    }
}

