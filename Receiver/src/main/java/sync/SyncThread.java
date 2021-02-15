package sync;

import settings.Settings;
import tab.Tab;
import tab.TabUtils;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * When this runnable is started, a new thread is fired.
 * The new one allows to set a new ServerSocket listening on
 * the port declared in the settings.txt file. By default I use 2020.
 *
 * When a connection is instanced, it will read the input stream 'till its end.
 * The IS contains the daily average of every survey sent by the client(android in this case).
 * These data as stored onto different file in the phone, but on this server on a unique file;
 * 'cause data should be stored in a spreadsheet, but in order to prevent any losing-data, I prefer
 * storing them before onto a text file and then onto spreadsheet.
 * @see Tab
 * @see TabUtils
 *
 * REMEMBER THAT EVERY TIME IT READS THE INPUT STREAM, IT WILL OVERWRITE THE TEXT FILE!!!
 * So be sure that every data has been written onto spreadsheet!!! if not call the right method to
 * just write data onto spreadsheet without read again the inputstream, or else make a backup of the data
 * not transporter from text to ss and contact me. MOST IF IT IS HAPPENS OFTEN!
 *
 * @author GABRIELE-P03
 * @gitHub https://github.com/Gabriele-P03/GreenLand_PCandARDUINO/tree/master/Receiver
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

            } catch (IOException e) {
                e.printStackTrace();
            }
        Tab.instance();
        TabUtils.append();
    }

}
