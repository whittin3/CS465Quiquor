package client;

import java.io.*;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Neal Eric
 * Date: 11/10/13
 */

/**
 * todo: @whittin3 Please note that this entire class was written by me a long time ago and I haven't checked to see if it works
 */
public class UserSettings {
    private Boolean StartInFullScreen;
    private Boolean EnableAddNewDrinkButton;
    private Boolean AlwaysStartWithAnEmptyBar;

    public void parseConfigFile() throws FileNotFoundException, URISyntaxException {
        CodeSource codeSource = Main.class.getProtectionDomain().getCodeSource();
        String path = new File(codeSource.getLocation().toURI().getPath()).getParentFile().getAbsolutePath().replaceAll("\\\\", "/");
        File file = new File(path + "/config.txt");
        if (file.exists()) {
            try (Scanner scanner = new Scanner(file).useDelimiter("[=\\n]")) {
                boolean errorFlag = false;
                while (scanner.hasNext()) {
                    String line = scanner.next().replaceAll("\\s", "");
                    if (line.equals("StartInFullScreen")) {
                        StartInFullScreen = Boolean.valueOf(scanner.next().replaceAll("\\s", ""));
                    }
                    if (line.equals("EnableAddNewDrinkButton")) {
                        EnableAddNewDrinkButton = Boolean.valueOf(scanner.next().replaceAll("\\s", ""));
                    }
                    if (line.equals("AlwaysStartWithAnEmptyBar")) {
                        AlwaysStartWithAnEmptyBar = Boolean.valueOf(scanner.next().replaceAll("\\s", ""));
                    }
                }
            }
        }
        else {
            try {
                writeConfigFile();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void writeConfigFile() throws URISyntaxException, IOException {
        CodeSource codeSource = Main.class.getProtectionDomain().getCodeSource();
        String path = new File(codeSource.getLocation().toURI().getPath()).getParentFile().getAbsolutePath().replaceAll("\\\\", "/");
        FileWriter fstream = new FileWriter(path + "/config.txt");
        try (BufferedWriter out = new BufferedWriter(fstream)) {
            out.write("StartInFullScreen = " + String.valueOf(StartInFullScreen));
            out.newLine();
            out.write("EnableAddNewDrinkButton = " + String.valueOf(EnableAddNewDrinkButton));
            out.newLine();
            out.write("AlwaysStartWithAnEmptyBar = " + String.valueOf(AlwaysStartWithAnEmptyBar));
            out.newLine();
        }
    }
}
