package sync;

import settings.Settings;
import tab.Tab;
import tab.TabUtils;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author GABRIELE PACE
 * @git
 *
 */
public class SyncThread implements Runnable{

    private ServerSocket serverSocket = null;
    private Socket clientSocket = null;
    private BufferedReader bufferedReader = null;


    @Override
    public void run() {

            try {

                serverSocket = new ServerSocket();
                serverSocket.setReuseAddress(true);
                serverSocket.bind(new InetSocketAddress(Settings.PORT));
                clientSocket = serverSocket.accept();
                bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                File file = new File("resources","surveys.txt");
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                String buffer;

                while((buffer = bufferedReader.readLine()) != null){
                    System.out.println(buffer);
                    bufferedWriter.write(buffer+"\n");
                }

                bufferedWriter.flush();
                bufferedWriter.close();
                clientSocket.close();
                serverSocket.close();

                //Tab.generate();
            } catch (IOException e) {
                e.printStackTrace();
            }
        Tab.instance();
        TabUtils.append();
    }

}
