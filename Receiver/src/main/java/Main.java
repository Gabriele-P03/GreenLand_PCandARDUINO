import settings.Settings;
import sync.SyncThread;
import tab.Tab;
import tab.TabUtils;

import java.sql.Date;
import java.time.Instant;
import java.util.Calendar;

/**
 *
 * @version 1.0.0
 *
 * This program has to be used with the GreenLand application for android
 *
 * When run, this program will wait 'till some data are ready to be read on the
 * input stream of the socket. Store them onto a text file then on a spreadsheet
 * called as the current year.
 *
 * This program needs all the libs of the ApachePOI. The libs have to be stored in a directory,
 * called "libs" into the root directory of the program.
 * Text file and spreadsheet files instead must be stored in an another directory called "resources", this
 * directory has to be stored always inside the program's root directory
 *
 * To "break" the loop (waiting the data available to be read on th input stream), press "SYNC" button on the
 * SyncLayout of the application, when some surveys can be synchronized.
 *
 *
 * @author GABRIELE-P03
 * @gitHub https://github.com/Gabriele-P03/GreenLand_PCandARDUINO/tree/master/Receive
 */
public class Main {

    private static Settings settings;

    public static void main(String[] args) {
        settings = new Settings();
        settings.loadSettings();
        new Thread(new SyncThread()).start();
    }
}
