package taskmanager.server;

import taskmanager.server.ServerThread;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    /**Серверный порт
     *
     */
    private static int PORT = 1024;

    public static void main(String[] args){
        try{
            ServerSocket server = new ServerSocket(PORT);
            System.out.println("Server is started!"); //оповещение сервером о том, что он запущен
            while (true) {
                Socket clientSocket = server.accept(); //ожидаем, пока кто-нибудь не захочет подключиться
                try{
                    Runnable run = new ServerThread(clientSocket);
                    Thread serverThread = new Thread(run);
                    serverThread.start();
                } catch(IOException ex){
                    clientSocket.close();
                }
            }
        } catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
