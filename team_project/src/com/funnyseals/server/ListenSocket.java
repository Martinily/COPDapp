package com.funnyseals.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ListenSocket {
    public static void main(String[] args) throws IOException {
        int port = 2018;
        ServerSocket server = new ServerSocket(port,100);
        System.out.println("<--------------------On hold-------------------->");
        ExecutorService threadPool = Executors.newCachedThreadPool();

        while (true) {
            Socket socket = server.accept();
            if(socket!=null){
                Runnable runnable = () ->
                {
                    try {
                        String message;
                        DataInputStream datainputstream = new DataInputStream(socket.getInputStream());
                        message = datainputstream.readUTF();
                        System.out.println(message);

                        InputDeal.BasicDeal(message);

                        socket.shutdownInput();
                        socket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                };
                threadPool.submit(runnable);
            }
        }
    }
}
