package com.bailiban.helloworld;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTest {

    public static void main(String[] args) throws IOException {
        //新建Server，监控8080端口
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("服务已开启，等待客户端连接");
        // 监听并获取客户端Socket，为阻塞方法
        Socket socket = serverSocket.accept();
        System.out.println("客户端已连接: "+socket.getInetAddress().getHostAddress());
        try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true))
        {
            // 等待客户端信息
            System.out.println("Client > " + in.readLine());
            // 向客户端发送信息
            String line = sin.readLine();
            out.println(line);

        }
    }
}
