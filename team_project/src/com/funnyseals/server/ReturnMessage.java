package com.funnyseals.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public interface ReturnMessage {
    static void ReturnMessage(String string, int port) throws IOException//need free port
    {
        System.out.println(port);
        ServerSocket server = new ServerSocket(port);
        Socket socket = server.accept();
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeUTF(string);
        server.close();
        socket.shutdownOutput();
        socket.close();
    }
}
