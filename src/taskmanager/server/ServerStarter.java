package taskmanager.server;

import taskmanager.Manager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerStarter implements Runnable {
    private ServerSocket serverSocket;
    private Manager journalTask;

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
                    Runnable run = new ServerThread(clientSocket, journalTask);
                    Thread serverThread = new Thread(run);
                    serverThread.start();
                } catch (IOException ex) {
                    clientSocket.close();
                }
            }
        } catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
