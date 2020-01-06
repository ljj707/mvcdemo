package com.bailiban.cs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientTest {


    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("localhost",8080);
        System.out.println("Client成功连接");

        new Thread(()->{
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream());
                 BufferedReader sin = new BufferedReader(new InputStreamReader(System.in))) {
                while (true) {
                    String request = sin.readLine();
                    if (request.equals("bye")) {
                        break;
                    }
                    out.println(request);
                    out.flush();
                    String content = in.readLine();
                    System.out.println(content);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

}
