package seedu.address.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;

public class ExportManager {

    private static final Logger logger = LogsCenter.getLogger(ExportManager.class);

    private final Path filePath;
    private final String person;

    /**
     * Initializes Export
     */
    public ExportManager(Path filePath, String personDetails) {
        this.filePath = filePath;
        this.person = personDetails;
    }

    /**
     * Export to text file
     */
    public void exportToTxtFile() throws IOException {
        File newFile = new File("data/personDetails.txt");
        newFile.createNewFile();
        logger.info("Created File");
        saveDetailsToLocal(person);
    }

    private void saveDetailsToLocal(String allPerson) {

        if (allPerson.equals("")) {
            return;
        }
        try {
            FileWriter fw = null;
            FileWriter newFw = null;
            String file = "/Details.txt";
            fw = new FileWriter(String.valueOf(filePath) + file);
            fw.write("");
            fw.close();

            newFw = new FileWriter(String.valueOf(filePath) + file, true);
            newFw.write(allPerson);
            newFw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
