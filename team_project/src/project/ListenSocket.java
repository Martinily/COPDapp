package project;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONObject;

public class ListenSocket
{
    public static void main(String[] args) throws IOException
    {
        int port=2018;
        int send_port=2019;
        ServerSocket server=new ServerSocket(port);
        System.out.println("<--------------------On hold-------------------->");
        ExecutorService threadPool = Executors.newFixedThreadPool(100);

        while(true)
        {
            Socket socket = server.accept();
            send_port++;

            Runnable runnable=()->
            {
                try
                {
                    String message;//the message received
                    DataInputStream datainputstream=new DataInputStream(socket.getInputStream());
                    System.out.println("!");
                    message=datainputstream.readUTF();
                    System.out.println("!!");
                    System.out.println(message);

                    InputDeal.BasicDeal(message);//entry InputDeal

                    socket.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            };
            threadPool.submit(runnable);
        }
    }
}
