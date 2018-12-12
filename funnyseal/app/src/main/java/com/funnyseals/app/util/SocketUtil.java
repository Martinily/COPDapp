package com.funnyseals.app.util;

import java.io.IOException;
import java.net.Socket;

/**
 * <pre>
 *     author : marin
 *     time   : 2018/12/08
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class SocketUtil {
    private static final String IP_ADDR   = "10.63.60.135";//服务器地址  这里要改成服务器的ip
    private static final int    PORT_SEND = 2018;//服务器端口号
    private static final int    PORT_GET  = 2019;//服务器端口号

    public static Socket getSendSocket() throws IOException {
        return new Socket(IP_ADDR, PORT_SEND);
    }

    public static Socket getGetSocket() throws IOException{
        return new Socket(IP_ADDR,PORT_GET);
    }
}
