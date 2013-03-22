/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messengerproject;


import SecFeatures.SecAlgosWrapper;
import java.net.Socket;
import java.io.*;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;

/**
 * The main class of the application.
 */
public class MessengerProject{
    
    private Client_Data_Handler Cli_Handler;
    private MessengerApp_View Msg_App_View;
    private LoginForm lgn_Form;
    
    private Socket This_Socket;
    
    private int This_Port;
    
    private String This_Name;
    private Server_Thread svr_Thread;
    private String CliSvr_Key;
    
    private SecAlgosWrapper saw;
    
    public MessengerProject()
    {
        saw = new SecAlgosWrapper();
    }
    
    public String Get_UserName()
    {
        return This_Name;
    }
    
    public String Get_CliSvr_Key()
    {
        return CliSvr_Key;
    }

    public void Start_Application()
    {   
        lgn_Form = new LoginForm(this);
        //show(lgn_Form);
        lgn_Form.setVisible(true);
    }
    
    
    public void User_Login(int Option, String UserName, String Password)
    {
        //option ---> 1- Register 0-Login
        
        //Data += ":127.0.0.1:"+This_Port;
        this.This_Name = UserName;
        String ServerIPAddress = "127.0.0.1";
        int Port = 6500;
        
        String Send_Data;
        
        if(Option == 1)
        {
            String Data = saw.RegisterEncrypt(UserName, Password, This_Port);
            Send_Data = "Register:"+This_Name+":"+Data;
        }
        else{
            String Data = saw.LoginEncrypt(UserName, Password,This_Port);
            Send_Data = "Login:"+This_Name+":"+Data;
        }
        
        System.out.println("Send Data: "+ Send_Data);
        
        CliSvr_Key = saw.Get_CliSvr_Key(UserName, Password);
        
        try{
            This_Socket = new Socket(InetAddress.getByName(ServerIPAddress), Port);
            
            //Start the Server Communication Thread
            Server_Comm_Thread ServerCommThread = new Server_Comm_Thread(this, This_Socket, This_Name);
            ServerCommThread.start();
            
            //BufferedReader inFromServer = new BufferedReader(new InputStreamReader(This_Socket.getInputStream()));
            PrintWriter outToServer = new PrintWriter(This_Socket.getOutputStream(), true);
            
            outToServer.println(Send_Data);
            //String Receive_Data = inFromServer.readLine();
            //String[] Temp = Receive_Data.split(":");
        }
        catch(Exception e)
        {
            System.out.println("Exception occured at User Login: " + e);
        }
        
    }
    
    public void Set_Warning_Login(String Msg)
    {
        try{
            This_Socket.close();
            lgn_Form.Set_Warning(Msg);
        }
        catch(Exception e)
        {
            System.out.println("Exception occured at Set Warning Messgaer for Login: " + e);
        }

    }
    
    public void Start_Messenger()
    {
        String[] Nameslist = new String[0];
        try{
            lgn_Form.setVisible(false);
        }
        catch(Exception e)
        {
        }

        this.Cli_Handler = new Client_Data_Handler();

        Server_Thread svr_Thread = new Server_Thread(this.Cli_Handler, This_Port, this.This_Name, this);
        this.svr_Thread = svr_Thread;

        svr_Thread.start();

        Msg_App_View = new MessengerApp_View(this);
        //show(Msg_App_View);
        Msg_App_View.setVisible(true);
    }
    
    public void Update_ClientList(String[] NameList)
    {
        for(int iLoop = 0; iLoop < NameList.length; iLoop++)
        {
           Client_Details Cli_Detail = new Client_Details();
           Cli_Detail.Name = NameList[iLoop];
           Cli_Detail.IPAddress = "";
           Cli_Detail.Port_No = 0;
           Cli_Detail.History = "";
           Cli_Detail.Cli_Socket = null;
           Cli_Detail.Cli_Thread = null;

           this.Cli_Handler.Handle_Details("Add_Client", "", Cli_Detail.Name, Cli_Detail, this.This_Name, null);
        }
        
        Msg_App_View.Update_Clients(NameList);
    }
    
    
    public void Start_Session(String UserName) {
        //Start the Session with getting the IPAddress and Port for the selected user
        try
        {
            if(!Cli_Handler.Clients_Alive(UserName))
            {
                //BufferedReader inFromServer = new BufferedReader(new InputStreamReader(This_Socket.getInputStream()));
                PrintWriter outToServer = new PrintWriter(This_Socket.getOutputStream(), true);
                
                Client_Details Cli_Detail = Cli_Handler.Handle_Details("Get_Client", "", UserName, null, This_Name, null);
                
                BigDecimal big = new BigDecimal(Math.random());
                String Nonce = big.toString();
                Cli_Detail.Nonce = Nonce;
                String Message = "Chat:"+UserName+":"+Nonce;
                
                System.out.println("this.CliSvr_Key -> "+this.CliSvr_Key);
                
                String eMessage = saw.EncryptMessage(this.CliSvr_Key, Message);
                
                System.out.println("eMessage ->"+eMessage);

                outToServer.println(eMessage);
                
                

                //String ReceiveData = inFromServer.readLine();
                //String[] Temp = ReceiveData.split(":");
            }
        }
        catch(Exception e)
        {
            System.out.println("Exception occured at Main: " + e);
        }
    }
    
    public void Open_Cli_window(String ReceiveData)
    {
        //Username:Nonce:IPAddress:Port:KAB:Ticket(ThisName:KAB)
        String[] Temp = ReceiveData.split(":");
        
        System.out.println("receive data ->"+ReceiveData);                          
        Client_Details Cli_Detail = Cli_Handler.Handle_Details("Get_Client", ReceiveData, Temp[0], null, This_Name, null);
        
        System.out.println("Nonce Stored:"+Cli_Detail.Nonce);
        System.out.println("Nonce from Server:"+Temp[1]);
        
        if(Cli_Detail.Nonce.equals(Temp[1]))
        {
            System.out.println("I am executing");
            Cli_Detail.IPAddress  = Temp[2];
            Cli_Detail.Port_No = Integer.parseInt(Temp[3]);
            Cli_Detail.SharedKey = Temp[4];

            Cli_Handler.Handle_Details("Update_Client", "", Temp[0], Cli_Detail, This_Name, null);

            //System.out.println("Selected Value:" + UserName);
            ClientWindow Cli_Window = new ClientWindow(this, Temp[0]);
            Cli_Handler.Handle_Details("Start_Session", ReceiveData, Temp[0], null, This_Name, Cli_Window);

            //show(Cli_Window);
            Cli_Window.setVisible(true);
        }
    }
    
    public void Set_Warning_Chat_Wndw(String Msg)
    {
        //NotValid_Client
        this.Msg_App_View.Set_Warning(Msg);
    }
    
    public void Start_Cli_Window(String UserName, Client_Details Cli_Detail)
    {
        //Called from Server Thread
        //System.out.println("Selected Value:" + UserName);
        ClientWindow Cli_Window = new ClientWindow(this, UserName);
        Cli_Detail.Cli_Window = Cli_Window;
        Cli_Handler.Handle_Details("Add_Client", "", UserName, Cli_Detail, This_Name, Cli_Window);
        Msg_App_View.Update_Clients(Cli_Handler.Get_Names_List());
        
        //show(Cli_Window);
        Cli_Window.setVisible(true);
    }
    
    public void Send_Msg(String UserName, String Msg)
    {
        Cli_Handler.Handle_Details("Send_Msg", Msg, UserName, null, This_Name, null);
    }
    
    public void Close_Session(String UserName)
    {
        Cli_Handler.Handle_Details("Close_Session", "", UserName, null, This_Name, null);
    }
    
    public boolean Exit_Application()
    {
        boolean valid = false;
        if(!this.Cli_Handler.Clients_Alive()) //if no client session is going on
        {
            try{
                //BufferedReader inFromServer = new BufferedReader(new InputStreamReader(This_Socket.getInputStream()));
                PrintWriter outToServer = new PrintWriter(This_Socket.getOutputStream(), true);

                outToServer.println(saw.EncryptMessage(CliSvr_Key, "Logout"));

                //inFromServer.readLine();

                valid = true;
            }
            catch (Exception e)
            {
                System.out.println("Exception occured at Exit_Application: " + e);
            }
        }
        return valid;
    }
    
    public void Close_Application()
    {
        try
        {
            This_Socket.close();
            this.svr_Thread.Exit();
        }
        catch(Exception e) {
                System.out.println("Exception occured at Close_Application: " + e);
        }
    }
    
    
    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        //launch(MessengerProApp.class, args);
        try{
            
            String Name,IPAddress;
            int Port, incr =0;
            
            MessengerProject Messenger = new MessengerProject();
            
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            
            System.out.println("Enter the Port No.:");
            Messenger.This_Port = Integer.parseInt(inFromUser.readLine());
            
            //Start the Login Screen
            Messenger.Start_Application();
            
        }
        catch(Exception e)
        {
            System.out.println("Exception occured at Main: " + e);
        }
    }
}



class Server_Thread extends Thread {

    Client_Data_Handler Cli_Handler;
    int TCP_Port;
    ServerSocket Server;
    private volatile boolean IsValid = true;
    String This_Name;
    MessengerProject App;

    public Server_Thread(Client_Data_Handler Cli_Handler, int TCP_Port, String This_Name, MessengerProject App) {
        this.Cli_Handler = Cli_Handler;
        this.TCP_Port = TCP_Port;
        this.This_Name = This_Name;
        this.App = App;
    }

    public void Exit() {
        
        IsValid = false;
        try {
            Thread.sleep(1000);
            Server.close();
        }
        catch(Exception e) {
            System.out.println("Exception occured at Server Thread Exit: " + e);
        }
    }

    @Override
    public void run() {
        IsValid = true;
        Socket Connected;
        BufferedReader inFromClient;
        String Receive_Data;
        int Cli_No;
        String[] Temp;
        try {
            Server = new ServerSocket(TCP_Port);
            Server.setSoTimeout(100);
            SecAlgosWrapper saw = new SecAlgosWrapper();
            while (IsValid) {
                try {
                    Connected = Server.accept();
                    inFromClient = new BufferedReader(new InputStreamReader(Connected.getInputStream()));
                    Receive_Data = inFromClient.readLine();
                    System.out.println(Receive_Data);
                    Temp = Receive_Data.split(":"); //Request:Ticket(ThisName:KAB)
                    
                    if(Temp[0].compareTo("Request") == 0)
                    {
                        String Ticket = saw.DecryptMessage(App.Get_CliSvr_Key(), Temp[1]);
                        
                        //Split the Ticket
                        String[] Ticket_Split = Ticket.split(":");
                        
                        Client_Details Cli_Detail = new Client_Details();
                        Cli_Detail.Cli_Socket = Connected;
                        Cli_Detail.History = "";
                        Cli_Detail.Name = Ticket_Split[0];
                        Cli_Detail.Cli_Window = null;
                        Cli_Detail.SharedKey = Ticket_Split[1];
                        
                        Client_Thread New_Client = new Client_Thread(Cli_Handler, Connected, Cli_Detail.Name, This_Name);
                        Cli_Detail.Cli_Thread = New_Client;
                        New_Client.start();
                        
                        //Add Client Detail to arraylist
                        App.Start_Cli_Window(Cli_Detail.Name, Cli_Detail);
                        
                        //Send Response to Client
                        Cli_Detail.Send_Msg("Request accpted by Client.");
                    }
                }
                catch (SocketTimeoutException e)
                {
                }
            }
        } catch (IOException e) {
            System.out.println("Exception occured at Server Thread: " + e);
        }
    }
}



