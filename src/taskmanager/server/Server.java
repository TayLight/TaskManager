package taskmanager.server;

import taskmanager.Manager;
import taskmanager.task.JournalTask;
import taskmanager.task.Task;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Properties;
import java.util.Scanner;


public class Server {
    /**
     * Путь к файлу с параметрами подключения
     */
    private static final String PATH_TO_PROPERTIES = "./serverConnection.properties";
    private static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            Manager journalTask = new JournalTask<Task>();
            FileInputStream fileInputStream = new FileInputStream(PATH_TO_PROPERTIES);
            Properties prop = new Properties();
            prop.load(fileInputStream);
            int numPort = 0;
            while (true){
                System.out.println("Номер сервера:");
                numPort = in.nextInt();
                if (numPort >= 1 && numPort <= 4) break;
            }
            int port = Integer.parseInt(prop.getProperty("server" + numPort + ".port"));
            ServerSocket server = new ServerSocket(port);
            System.out.println("Сервер запущен."); //оповещение сервером о том, что он запущен
            Runnable run = new ServerStarter(server, journalTask);
            Thread serverStarter = new Thread(run);
            serverStarter.setDaemon(true);
            serverStarter.start();
            while (true) {
                if (in.nextLine().equals("stop")) {
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
