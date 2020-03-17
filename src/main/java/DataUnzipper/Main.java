package DataUnzipper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    //  static File filezip = new File("C:/Users/madsf/Desktop/Merge_test");
    // static File destDir = new File("C:/Users/madsf/Desktop/Merge_test/unziptest");
    static String srcDir = "F:/TwitterData";
    static File destDir = new File("F:/TESTFOLDER/unpacked");
    static File tempBzDir = new File("F:/Temp/TempBz");
    static File tempTarDir = new File("F:/TESTFOLDER/tempTar");
    static int i=0,j=0;

    //todo @Bau add functionality to show progression
    public static void main(String[] args) {
        File[] directories = new File(srcDir).listFiles(File::isDirectory);
        //ALWAYS SET j before calling unpackAllTars
        setJ();
        unpackAllTars(directories);
        //ALWAYS SET i before calling showFiles
        setI();
        showFiles(tempTarDir.listFiles());
        deleteFinishedFilesInDirectory(tempTarDir);
    }

    private static void setI(){
        if (!(destDir.listFiles() == null)){
            for(File f : destDir.listFiles()){
                int a = Integer.parseInt(f.getName().replace(".json",""));
                if(a > i)
                    i = a;
            }
            if(i!=0)
                i++;
        }
    }
    private static void setJ(){
        if(!(tempTarDir.listFiles() == null)){
            for (File f : tempTarDir.listFiles()){
                int a = Integer.parseInt(f.getName());
                if(a > j)
                    j = a;
            }
            if(j!=0)
                j++;
        }

    }

    private static void deleteFinishedFilesInDirectory(File dir){
        for(File file: dir.listFiles())
            if (file.getName().contains("finished"))
                file.delete();
    }

    public static void unpackAllTars(File[] directories) {
        boolean isTar = false;
        //for each directory(twitter archive)
        for (File folder : directories) {
            //if directory name contains "done", skip
            if (folder.getName().contains("done"))
                continue;
            if(folder.isDirectory()){
                isTar = false;
                for(File f : folder.listFiles()){
                    if(f.getName().contains("tar")){
                        isTar =true;
                        File unzippedFile = new File(f.getName().replace(".tar","folder"));
                        try {
                            File currDir = new File(tempTarDir.getPath()+"/" + j++);
                            currDir.mkdir();
                            ZipManager.unGzip(new File(f.getAbsolutePath()), currDir, unzippedFile, tempTarDir);
                            System.out.println("unTar Fnished");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (isTar)
                    folder.renameTo(new File(folder.getPath() + "_done"));
            }
            //unpack
            //append done when finished
            //f.renameTo(new File(f.getName() + "_done"));
        }
    }

    public static void showFiles(File[] files) {
        for (File file : files) {
            if (file.isDirectory()) {
                //System.out.println("Directory: " + file.getName());
                /*if (file.getName().contains("bz2") || file.getName().contains("tar")) {
                    try {
                        ZipManager.unGzip(new File(file.getAbsolutePath()), destDir, new File(destDir + "/" + i + ".json"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }*/
                showFiles(file.listFiles()); // Calls same method again.
            }
            else {
                System.out.println("File: " + file.getName());
                try {
                    if (file.getName().contains("bz2")) {
                        ZipManager.unGzip(new File(file.getAbsolutePath()), destDir, new File(destDir + "/" + i + ".json"),tempBzDir);
                    }else{
                        System.out.println("Error, file does not contain bz2, unTar likely failing");
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
            i++;
            file.renameTo(new File(file.getName() + "finished"));
        }
    }
}