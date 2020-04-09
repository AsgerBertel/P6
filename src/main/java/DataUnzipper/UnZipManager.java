package DataUnzipper;

import TweetCleaner.JsonCleaner;
import TweetCleaner.JsonTweet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class UnZipManager {
    String srcDir = "D:/TwitterData";
    File destDir = new File("D:/UnpackedData");
    File tempBzDir = new File("D:/Temp/TempBz");
    File tempTarDir = new File("D:/Temp/TempTar");
    File cleanedDir = new File("D:/CleanedData");
    int j = 0, k = 0;
    SynchronizedCounter syncCounterI = new SynchronizedCounter(destDir);
    ArrayList<Thread> threads = new ArrayList<>();
    public void setGlobals(){
        syncCounterI.set();
        setJ();
        setK();
    }
    public void saveTweets() throws IOException {
        int count = 0;
        File cleanTweet = new File(cleanedDir.getAbsolutePath() + "/" + k++ + ".txt");
        JsonCleaner cleaner = new JsonCleaner();

        ArrayList<JsonTweet> tweets = cleaner.cleanAllFilesInDirectory(destDir.getAbsolutePath());
        BufferedWriter bw = new BufferedWriter(new FileWriter(cleanTweet, true));
        for (JsonTweet t : tweets) {
            bw.write(t.toString());
            bw.newLine();
            count++;
        }
        bw.close();
        System.out.println("\n saved " + count + " tweets");
    }



    private void setJ() {
        if (!(tempTarDir.listFiles() == null)) {
            for (File f : tempTarDir.listFiles()) {
                int a = Integer.parseInt(f.getName());
                if (a > j)
                    j = a;
            }
            if (j != 0)
                j++;
        }

    }

    private void setK() {
        if (!(cleanedDir.listFiles() == null)) {
            for (File f : cleanedDir.listFiles()) {
                int a = Integer.parseInt(f.getName().replace(".txt", ""));
                if (a > k)
                    k = a;
            }
            k++;
        }
    }

    void deleteFilesInDirectory(File dir) {
        for (File file : dir.listFiles())
            file.delete();
    }

    public void unpackAllTars(File directory) {
        if (directory.getName().contains("done"))
            return;
        //for each directory(twitter archive)
        for (File file : directory.listFiles()) {
            //if directory name contains "done", skip
            if (file.getName().contains("tar")) {
                try {
                    File currDir = new File(tempTarDir.getPath() + "/" + j++);
                    currDir.mkdir();
                    UnZipper.unTar(new File(file.getAbsolutePath()), currDir);
                    System.out.println("unTar Fnished");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        directory.renameTo(new File(directory.getPath() + "_done"));
    }

    public void multithreadedUnZip(File[] files) throws InterruptedException {
        for(File f : files){
            BzUnzipRunnable bzRunnable = new BzUnzipRunnable(Integer.parseInt(f.getName()),syncCounterI,destDir,tempBzDir,f.listFiles());
            Thread thread = new Thread(bzRunnable);
            threads.add(thread);
            thread.start();
        }
        for(Thread t : threads){
            //blocking call - means that we wait for
            //all threads to finish before main progresses
            t.join();
        }
        //remove all old threads
        //todo @bau re-use threads instead of making new ones
        threads = new ArrayList<>();
    }
}

class SynchronizedCounter{
    final Object lock = new Object();
    private int i = 0;
    File destDir;

    public SynchronizedCounter(File destDir) {
        this.destDir = destDir;
    }

    public void set() {
        if (!(destDir.listFiles() == null)) {
            for (File f : destDir.listFiles()) {
                int a = Integer.parseInt(f.getName().replace(".json", ""));
                if (a > i)
                    i = a;
            }
            if (i != 0)
                i++;
        }
    }
    public int get(){
        incrementI();
        return i;
    }

    private void incrementI(){
        synchronized (lock){
            i++;
        }
    }
}

class BzUnzipRunnable implements Runnable{
    int id;
    SynchronizedCounter syncCounter;
    File destDir,tempBzDir;
    File[] files;

    public BzUnzipRunnable(int id, SynchronizedCounter syncCounter, File destDir, File tempBzDir, File[] files) {
        this.id = id;
        this.syncCounter = syncCounter;
        this.destDir = destDir;
        this.tempBzDir = tempBzDir;
        this.files = files;
    }

    @Override
    public void run() {
        showFiles(files);
    }

    private void showFiles(File[] files) {
        for (File file : files) {
            if(file.getName().contains("finished"))
                continue;
            if (file.isDirectory()) {
                showFiles(file.listFiles()); // Calls same method again.
            } else {
                System.out.println("File: " + file.getName());
                try {
                    if (file.getName().contains("bz2")) {
                        UnZipper.unZipBz(new File(file.getAbsolutePath()), new File(destDir + "/" + syncCounter.get() + ".json"), tempBzDir,String.valueOf(id));
                    } else {
                        System.out.println("Error, file does not contain bz2, unTar likely failing");
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
            file.renameTo(new File(file.getAbsolutePath() + "finished"));
        }
    }
}
