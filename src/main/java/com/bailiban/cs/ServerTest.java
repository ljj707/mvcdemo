package com.bailiban.cs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerTest {


    private static Map<String, String> contentMap = new HashMap<>();

    static {
        contentMap.put("index", "Welcome!");
        contentMap.put("hello", "How are you?");
    }

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(8080);
        Socket socket = serverSocket.accept();
        System.out.println("Server成功连接");

        new Thread(()->{
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream())) {
                while (true) {
                    String line = in.readLine();
                    if (line == null || line.equals("bye")) {
                        break;
                    }
                    String content = contentMap.get(line.trim());
                    out.println(content != null ? content : "404");
                    out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                System.out.println("客户端断开。");
            }
        }).start();
    }

}
