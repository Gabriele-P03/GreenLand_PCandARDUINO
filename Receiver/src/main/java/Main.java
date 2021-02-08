import settings.Settings;
import sync.SyncThread;
import tab.Tab;
import tab.TabUtils;

import java.sql.Date;
import java.time.Instant;
import java.util.Calendar;

public class Main {

    private static Settings settings;

    public static void main(String[] args) {
        settings = new Settings();
        new Thread(new SyncThread()).start();
    }
}
