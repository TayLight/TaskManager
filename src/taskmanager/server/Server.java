package taskmanager.server;

import taskmanager.Manager;
import taskmanager.task.JournalTask;
import taskmanager.task.Task;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;


public class Server {
    /**
     * Путь к файлу с параметрами подключения
     */
    private static final String PATH_TO_PROPERTIES = "./serverConnection.properties";

    public static void main(String[] args) {
        try {
            Manager journalTask = new JournalTask<Task>();
            FileInputStream fileInputStream = new FileInputStream(PATH_TO_PROPERTIES);
            Properties prop = new Properties();
            prop.load(fileInputStream);
            int port = Integer.parseInt(prop.getProperty("server4.port"));
            ServerSocket server = new ServerSocket(port);
            System.out.println("Сервер запущен."); //оповещение сервером о том, что он запущен
            while (true) {
                Socket clientSocket = server.accept(); //ожидаем, пока кто-нибудь не захочет подключиться
                try {
                    Runnable run = new ServerThread(clientSocket, journalTask);
                    Thread serverThread = new Thread(run);
                    serverThread.start();
                } catch (IOException ex) {
                    clientSocket.close();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
