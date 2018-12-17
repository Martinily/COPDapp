package project;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public interface GetJsonArray
{
    static String GetMessage(int port) throws IOException//need free port
    {
        System.out.println(port);
        ServerSocket server=new ServerSocket(port);
        System.out.println(port+"!");
        Socket socket=server.accept();
        System.out.println(port+"!!");
        DataInputStream in = new DataInputStream(socket.getInputStream());
        String jsonArray=in.readUTF();
        System.out.println(port+"!!!");
        server.close();
        socket.close();
        return jsonArray;
    }
}
