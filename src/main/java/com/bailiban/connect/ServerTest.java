package com.bailiban.connect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTest {
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("服务已开启，等待客户端连接");
        Socket socket = serverSocket.accept();


        //读
        new Thread(() ->{
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));){
                while (true) {
                    String read = in.readLine();
                    if (read == null||read.equals("Bye") ){
                        break;
                    }
                    System.out.println("Client >"+read);
                }

            } catch (IOException e) {

            }

        }).start();


        //写
        new Thread(() ->{

            try (BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(),true))
            {

                while (true){

                    String line = sin.readLine();
                    if (line == null ||line.equals("Bye")){
                        break;
                    }
                    out.println(line);
                    System.out.println("Server >"+line);

                }
            } catch (IOException e) {

            }
        }).start();
    }

}
