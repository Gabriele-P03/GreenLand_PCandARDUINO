package settings;

import java.io.*;

public class Settings {

    public static int PORT;
    File file = null;
    BufferedReader bufferedReader = null;

    public Settings(){
        loadSettings();
    }

    public void loadSettings(){



        try {
            file = new File("settings.txt");
            bufferedReader = new BufferedReader(new FileReader(file));
            String buffer;
            if((buffer = bufferedReader.readLine()) != null){
                PORT = Integer.valueOf(buffer);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
