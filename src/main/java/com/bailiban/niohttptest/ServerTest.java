package com.bailiban.niohttptest;

import com.bailiban.httptest.HttpServer;
import com.bailiban.niohttptest.controller.UserController;
import com.bailiban.niohttptest.model.MethodInfo;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ServerTest {

    public static Map<String,Object> beanMap = new HashMap<>();
    static {
        beanMap.put("userController",new UserController());
    }
    public static Map<String, MethodInfo> methodMap = new HashMap<>();





    public static void main(String[] args) throws IOException {
        UserController.main(null);

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(80));
        serverSocketChannel.configureBlocking(false);
        Selector selector =Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true){
            if (selector.select(3000)==0){
                continue;
            }
            Iterator<SelectionKey> keyIterable = selector.selectedKeys().iterator();
            while (keyIterable.hasNext()){
                SelectionKey key = keyIterable.next();
                httpHandle(key);
                keyIterable.remove();
            }
        }
    }

    private static void httpHandle(SelectionKey key) throws IOException {
        if (key.isAcceptable()){
            acceptHandle(key);
        }else if (key.isReadable()) {
            requestHandle(key);
        }
    }

    private static void acceptHandle(SelectionKey key) throws IOException {
        SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(key.selector(),SelectionKey.OP_READ, ByteBuffer.allocate(1024));
    }

    private static void requestHandle(SelectionKey key) throws IOException {

        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
        byteBuffer.clear();
        if (socketChannel.read(byteBuffer)==-1){
            socketChannel.close();
            return;
        }
        byteBuffer.flip();
        String requestMsg = new String(byteBuffer.array());

        String url = requestMsg.split("\r\n")[0].split(" ")[1];

        System.out.println("Request: " + url);
        System.out.println("----------------------------");

        if (url.contains("get?")) {
            String params = url.split("\\?")[1];
            url = url.split("\\?")[0];
            MethodInfo methodInfo = methodMap.get(url);
            String content = null;
            try {
                content = (String) methodInfo.getMethod().invoke(beanMap.get(methodInfo.getClassName()),
                        Integer.valueOf(params.split("=")[1]));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            close(socketChannel,content);
        }
        else if (url.contains("getAll")){
            MethodInfo methodInfo = methodMap.get(url);
            String content = null;
            try {
                content = (String) methodInfo.getMethod().invoke(beanMap.get(methodInfo.getClassName()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            close(socketChannel,content);
        }else{
            socketChannel.close();
            return;
        }

    }

    private static void close(SocketChannel socketChannel,String content) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("HTTP/1.1 200 OK\r\n");
        stringBuilder.append("Content-Type:text/html;charset=utf-8\r\n");
        stringBuilder.append("\r\n");
        stringBuilder.append("<html><head><title>HttpTest</title></head><body>");
//        String content = HttpServer.contentMap.get(url);
        stringBuilder.append(content != null ? content : "404");
        stringBuilder.append("</body></html>");
        socketChannel.write(ByteBuffer.wrap(stringBuilder.toString().getBytes()));
        socketChannel.close();
    }


}
