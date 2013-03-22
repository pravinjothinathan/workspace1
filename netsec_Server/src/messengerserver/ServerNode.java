/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messengerserver;

import SecFeatures.PortKey;
import SecFeatures.SecAlgosWrapper;
import SecFeatures.Sha512;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 *
 * @author alagaranandrajendran
 */
public class ServerNode {
    
    public static void main(String args[])
    {
        try{
            System.out.println("Enter the Port No.:");
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            int Port = Integer.parseInt(inFromUser.readLine());
            
            //Start the Server Thread
            Login_Server_Thread Svr_Thread = new Login_Server_Thread(Port);
            Svr_Thread.start();
            
            System.out.println("Press Enter to Exit");
            inFromUser.readLine();
            
            //Exit the server thread
            Svr_Thread.Exit();
            System.out.println("Server Terminated...");
        }
        catch(Exception e)
        {
            System.out.println("Exception occured at Main: "+e);
        }
    }
    
}

class Login_Server_Thread extends Thread
{
    private volatile boolean IsValid = true;
    int Port;
    ServerSocket Server;
    Config cfg;
    Client_Data_Manager Data_Mgr;
    
    public Login_Server_Thread(int Port)
    {
        this.Port = Port;
    }
    
    public void Exit()
    {
        IsValid = false;
        try{
            Thread.sleep(1000);
            Server.close();
        }
        catch(Exception e)
        {
            System.out.println("Exception occured at Server Exit: "+e);
        }
    }
    
    @Override
    public void run()
    {
        IsValid = true;
        Socket Connected;
        BufferedReader inFromClient;
        String Receive_Data;
        String[] Temp;
        String Data;
        String File_Password, Decrpyted_Pass;
        
        
        try{
            Server = new ServerSocket(Port);
            Server.setSoTimeout(100);
            
            cfg = new Config("ServerData.cfg");
            Data_Mgr = new Client_Data_Manager();
            
            SecAlgosWrapper saw = new SecAlgosWrapper();
            //String Server_Key = saw.KeyGen();
            
            //Key generation for getting server alone key to use in storing passwords
            String Key_Sample = "Encyrpted_Message";
            Sha512 sha = new Sha512();
            byte[] hash = sha.GetDigestinByte(Key_Sample);
            
            String Server_Key = saw.asHex(hash);//new String(hash,"ISO-8859-1");
             
            
            System.out.println("Server_Key: "+ Server_Key.length());
            
            String Return_Msg;
            
            while(IsValid)
            {
                try{
                    Connected = Server.accept();
                    //Connected.
                    
                    inFromClient = new BufferedReader(new InputStreamReader(Connected.getInputStream()));
                    PrintWriter outToClient = new PrintWriter(Connected.getOutputStream(), true);
                    Receive_Data = inFromClient.readLine();
                    
                    System.out.println("Received Data: "+ Receive_Data);
                    
                    System.out.println("ReceiveData Length: "+ Receive_Data.length());
                    
                    Temp = Receive_Data.split(":"); 
                    //Register:UserName:Hash(UserName+Password)
                    //Login:UserName:Hash(UserName+Password)
                    
                    if(Temp[0].compareTo("Register") == 0)
                    {   
                        
                        PortKey KeyPass = saw.RegisterDecrypt(Receive_Data);
                        
                        if(KeyPass != null)
                        {
                            //Check if password register not available
                            if(cfg.Access_File("Key_Exist", "", Temp[1]+".Password").compareTo("true") != 0)
                            {
                                Client_Data New_Cli_Data = new Client_Data();
                                New_Cli_Data.Name = Temp[1];
                                New_Cli_Data.IPAddress = Connected.getInetAddress().getHostAddress();
                                New_Cli_Data.Port = KeyPass.PortNo;
                                New_Cli_Data.Logged_in = true;
                                New_Cli_Data.Cli_Socket = Connected;
                                New_Cli_Data.Password = KeyPass.key.strHexKey;
                                //New_Client;Read_Cli_List;Update_Client;Read_Client;
                                New_Cli_Data = Data_Mgr.Data_Handler("New_Client", Temp[1], New_Cli_Data);


                                if(New_Cli_Data == null)//Not valid; Existing Username
                                {
                                    outToClient.println("User_NotValid:Username already exist");
                                }
                                else{//New user Valid
                                    //Data Format = Username.Password = ""\n

                                    String Encrypted_Pass = saw.EncryptMessage(Server_Key, KeyPass.key.strHexKey);

                                    Data= Temp[1]+".Password="+Encrypted_Pass+"\n";

                                    cfg.Access_File("Write_Data", Data, "");

                                    Chat_Client_Thread Cli_Thread = new Chat_Client_Thread(Connected, cfg, Temp[1], Data_Mgr);
                                    Cli_Thread.start();

                                    outToClient.println(/*saw.EncryptMessage(KeyPass.key.strKey, */"User_Valid");

                                    Thread.sleep(1000);
                                    //Send the Existing users list to all the clients
                                    Data_Mgr.Data_Handler("Send_Cli_List", "", null);

                                }
                            }
                            else{//Password is already registered for this user
                                outToClient.println("User_NotValid:Username already exist");
                            }
                        }
                        else{
                            outToClient.println("User_NotValid:User not valid");
                        }
                    }
                    else if(Temp[0].compareTo("Login") == 0)
                    {
                        Client_Data New_Cli_Data = new Client_Data();
                        New_Cli_Data.Name = Temp[1];
                        //Check whether username already exists or not
                        New_Cli_Data = Data_Mgr.Data_Handler("Update_Client", Temp[1], New_Cli_Data);
                        
                        if(New_Cli_Data == null) //Username not valid
                        {
                            //User already Exist; but server restarted
                            if(cfg.Access_File("Key_Exist", "", Temp[1]+".Password").compareTo("true") == 0)
                            {
                                String Key = saw.DecryptMessage(Server_Key,cfg.Access_File("Read_Key", "", Temp[1]+".Password"));
                                PortKey data = saw.LoginDecrypt(Key, Receive_Data);
                                if(data != null)
                                {
                                    New_Cli_Data = new Client_Data();
                                    New_Cli_Data.Name = Temp[1];
                                    New_Cli_Data.IPAddress = Connected.getInetAddress().getHostAddress();
                                    New_Cli_Data.Port = data.PortNo;
                                    New_Cli_Data.Logged_in = true;
                                    New_Cli_Data.Cli_Socket = Connected;
                                    New_Cli_Data.Password = data.key.strHexKey;
                                    
                                    New_Cli_Data = Data_Mgr.Data_Handler("New_Client", Temp[1], New_Cli_Data);
                                    
                                    Chat_Client_Thread Cli_Thread = new Chat_Client_Thread(Connected, cfg, Temp[1], Data_Mgr);
                                    Cli_Thread.start();
                                    
                                    outToClient.println("User_Valid");
                                    
                                    Thread.sleep(1000);
                                    //Send the Existing users list to all the clients
                                    Data_Mgr.Data_Handler("Send_Cli_List", "", null);
                                    
                                }
                                else{
                                    outToClient.println("User_NotValid:Username and password not matched");
                                }
                           }
                            else{
                                outToClient.println("User_NotValid:Username not valid");
                            }
                            
                        }
                        else{//Existing user
                            
                            File_Password = cfg.Access_File("Read_Key", "", Temp[1]+".Password");
                            Decrpyted_Pass = saw.DecryptMessage(Server_Key, File_Password);
                            PortKey data = saw.LoginDecrypt(Decrpyted_Pass, Receive_Data);
                            
                            if(data != null)
                            {
                                New_Cli_Data.Name = Temp[1];
                                New_Cli_Data.IPAddress = Connected.getInetAddress().getHostAddress();
                                New_Cli_Data.Port = data.PortNo;
                                New_Cli_Data.Logged_in = true;
                                New_Cli_Data.Cli_Socket = Connected;
                                New_Cli_Data.Password = data.key.strHexKey;
                                New_Cli_Data = Data_Mgr.Data_Handler("Update_Client", Temp[1], New_Cli_Data);
                                
                                Chat_Client_Thread Cli_Thread = new Chat_Client_Thread(Connected, cfg, Temp[1], Data_Mgr);
                                Cli_Thread.start();
                                
                                outToClient.println("User_Valid");
                                
                                Thread.sleep(1000);
                                //Send the Existing users list to all the clients
                                Data_Mgr.Data_Handler("Send_Cli_List", "", null);
                            }
                            else{//Password not matched
                                outToClient.println("User_NotValid:Username and password not matched");
                            }
                        }
                    }
                }
                catch(SocketTimeoutException e)
                {
                }
                catch(Exception e)
                {
                    System.out.println("Exception occured at Chat Server: "+e);
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Exception occured at Chat Server: "+e);
        }
    }
    
}






