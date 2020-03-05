package DataUnzipper;

import java.io.File;
import java.io.IOException;

public class Main {
    //  static File filezip = new File("C:/Users/madsf/Desktop/Merge_test");
    // static File destDir = new File("C:/Users/madsf/Desktop/Merge_test/unziptest");
    static File filezip = new File("E:/Twitter data/archiveteam-twitter-stream-2019-05/twitter_stream_2019_05_28");
    static File destDir = new File("E:/Twitter_Data_Unzipped");
    static int i = 0;
    public static void main(String[] args) {

        File[] files = new File(filezip.toString()).listFiles();
        showFiles(files);

    }

    public static void showFiles(File[] files) {

        for (File file : files) {

            if (file.isDirectory()) {
                System.out.println("Directory: " + file.getName());
                if (file.getName().contains("bz2") || file.getName().contains("tar")) {
                    try {
                        ZipManager.unGzip(new File(file.getAbsolutePath()), destDir, new File(destDir + "/" + i + ".json"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                showFiles(file.listFiles()); // Calls same method again.
            } else {
                System.out.println("File: " + file.getName());
                if (file.getName().contains("bz2") || file.getName().contains("tar")) {
                    try {
                        ZipManager.unGzip(new File(file.getAbsolutePath()), destDir, new File(destDir + "/" + i + ".json"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            i++;
        }
    }
}