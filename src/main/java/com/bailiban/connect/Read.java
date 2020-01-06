package com.bailiban.connect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Read extends Thread {

    private Socket socket;

    private String name;

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));){
            while (true) {
                String read = in.readLine();
                if (read == null||read .equals("Bye") ){
                    break;
                }
                System.out.println(name + " >"+read);
            }
        } catch (IOException e) {

        }
    }
}
