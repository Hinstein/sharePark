package com.hinstein.android.experiment.test3;

/**
 * @BelongsProject: androidexperiment
 * @BelongsPackage: com.hinstein.android.experiment.test3
 * @Author: Hinstein
 * @CreateTime: 2019-12-09 12:33
 * @Description:
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

public class Server extends Thread {
    ServerSocket server = null;
    Socket socket = null;

    public void push(String s) {
        blockingQueue.offer(s);
    }

    private LinkedBlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>();

    public Server(int port) {
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        try {
            System.out.println(getdate() + "  等待客户端连接...");
            socket = server.accept();
            new SendMessThread().start();// 连接并返回socket后，再启用发送消息线程
            System.out.println(getdate() + "  客户端 （" + socket.getInetAddress().getHostAddress() + "） 连接成功...");
            InputStream in = socket.getInputStream();
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = in.read(buf)) != -1) {
                System.out.println(getdate() + "  客户端: （"
                        + socket.getInetAddress().getHostAddress() + "）说："
                        + new String(buf, 0, len, "UTF-8"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getdate() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String result = format.format(date);
        return result;
    }

    class SendMessThread extends Thread {

        @Override
        public void run() {
            super.run();
            Scanner scanner = null;
            OutputStream out = null;
            try {
                if (socket != null) {
                    scanner = new Scanner(System.in);
                    out = socket.getOutputStream();
                    String in = "";
                    do {
                        try {
                            in = blockingQueue.take();
                            out.write(("" + in).getBytes("UTF-8"));
                            out.flush();// 清空缓存区的内容
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } while (!in.equals("q"));
                    scanner.close();
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


}