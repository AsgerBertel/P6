package DataUnzipper;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class Main {

    //todo @Bau add functionality to show progression
    public static void main(String[] args) throws IOException, InterruptedException {
        UnZipManager unZipManager = new UnZipManager();
        File[] directories = new File(unZipManager.srcDir).listFiles(File::isDirectory);
        //Set globals to ensure that no files are overwritten
        unZipManager.setGlobals();
        for (File f : directories) {
            unZipManager.unpackAllTars(f);
            unZipManager.multithreadedUnZip(unZipManager.tempTarDir.listFiles());
            //extract all useful information from the .json files and store them in new files
            unZipManager.saveTweets();
            FileUtils.cleanDirectory(unZipManager.tempTarDir);
            FileUtils.cleanDirectory(unZipManager.destDir);
        }
    }
}