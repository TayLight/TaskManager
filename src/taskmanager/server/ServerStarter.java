package taskmanager.server;

import taskmanager.Manager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class ServerStarter implements Runnable {
    private ServerSocket serverSocket;
    private Manager journalTask;
    public static LinkedList<ServerThread> clientList = new LinkedList<>();

    public ServerStarter(ServerSocket serverSocket, Manager journalTask){
        this.serverSocket = serverSocket;
        this.journalTask = journalTask;
    }

    @Override
    public void run(){
        try{
            while (true) {
                Socket clientSocket = serverSocket.accept(); //ожидаем, пока кто-нибудь не захочет подключиться
                try {
                    clientList.add(new ServerThread(clientSocket, journalTask));
                } catch (IOException ex) {
                    clientSocket.close();
                }
            }
        } catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
