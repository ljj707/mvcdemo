package com.bailiban.connect;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;




public class ClientTest {
    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("localhost",8080);
        System.out.println("服务开启，可以发送消息");

        //读
        new Thread(() ->{
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));){
                while (true) {
                    String read = in.readLine();
                    if (read == null||read .equals("Bye") ){
                        break;
                    }
                    System.out.println("Server >"+read);
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
                    if (line == null || line.equals("Bye")){
                        break;
                    }
                    out.println(line);
                    System.out.println("Client >"+line);
                }
            } catch (IOException e) {

            }
        }).start();
    }
}
