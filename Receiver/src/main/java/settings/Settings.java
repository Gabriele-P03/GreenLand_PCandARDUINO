package settings;

import java.io.*;

/**
 * This class instance a new file which contains
 * every settings for the running of this program.
 * Actually PORT (networking) is the only one available
 *
 * It is used by SyncThread to instance a new Server Socket listening
 * on PORT port. By default should be 2020
 *
 * @author GABRIELE-P03
 * @gitHub https://github.com/Gabriele-P03/GreenLand_PCandARDUINO/tree/master/Receive
 */
public class Settings {

    public static int PORT;
    File file = null;
    BufferedReader bufferedReader = null;

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
