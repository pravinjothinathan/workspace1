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
class Client_Thread extends Thread {

    Socket Cli_Socket;
    private volatile boolean IsValid = true;
    Client_Data_Handler Cli_Handler;
    BufferedReader inFromServer;
    String Name;
    String This_Name;

    public Client_Thread(Client_Data_Handler Cli_Handler, Socket Cli_Socket, String Name, String This_Name) {
        this.Cli_Socket = Cli_Socket;
        this.Cli_Handler = Cli_Handler;
        this.Name = Name;
        this.This_Name = This_Name;
    }

    public void Exit() {
        IsValid = false;
        try {
            Thread.sleep(1000);
        }
        catch (Exception e)
        {
            System.out.println("Exception Occured at Client Thread: " + e);
        }
    }

    @Override
    public void run() {
        IsValid = true;
        String[] Temp;
        String Receive_Data, Send_Data;
        BufferedReader input;
        int Seq_No = 0, iLoop = 0, Server_No = -1;
        
        SecAlgosWrapper saw = new SecAlgosWrapper();
        
        try {
            Cli_Socket.setSoTimeout(100);
            inFromServer = new BufferedReader(new InputStreamReader(Cli_Socket.getInputStream()));
            int Client_No =-1;
            while (IsValid) {
                try
                {
                    Client_Details CliDetail = Cli_Handler.Handle_Details("Get_Client", "", Name, null, This_Name, null);
                    
                    if(CliDetail==null)
                        System.out.println("CliDetail in null !!!");
                    //System.out.println("CliDetail.SharedKey "+CliDetail.SharedKey);
                    String Message = inFromServer.readLine();
                    System.out.println("inFromServer.readLine()"+Message);
                    
                    Receive_Data = saw.DecryptMessage(CliDetail.SharedKey,Message);
                    System.out.println("Recieved Data--->"+Receive_Data);
                    Temp = Receive_Data.split(":");

                    if (Temp[0].compareTo("Close") == 0)
                    {
                        //if close message if received from the client
                        System.out.println(Receive_Data + " from Client");
                        Send_Data = "Done";
                        Cli_Handler.Handle_Details("Send_Msg", Send_Data, Name, null, This_Name, null);
                        Thread.sleep(1000);
                        
                        Cli_Handler.Handle_Details("Close_Socket", "", Name, null, This_Name, null);
                        IsValid = false;
                    }
                    else if(Temp[0].compareTo("Msg") == 0)
                    {
                        System.out.println("Received String:-->"+Temp[1]);
                        String ReceivedMsg = Temp[1];
                        Cli_Handler.Handle_Details("Recv_Msg", ReceivedMsg, Name, null, This_Name, null);
                    }
                }
                catch (SocketTimeoutException e) {
                }
                catch(NullPointerException e)
                {
                    Cli_Handler.Handle_Details("Close_Socket", "", Name, null, This_Name, null);
                    IsValid = false;
                }
                catch (Exception e)
                {
                    System.out.println("Exception occured: " + e);
                }
            }
        }
        catch (Exception e) {
            System.out.println("Exception occured: " + e);
        }
    }
}

class test{
    
}