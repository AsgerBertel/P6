package DataUnzipper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

public class ZipManager {

    public static void unGzip(final File inputFile, final File outputDir, File renamedFile, File tempDir) throws IOException {
        if (inputFile.getName().contains("bz2")) {
            unZipBz(inputFile, outputDir, renamedFile,tempDir);
        } else {
            unTar(inputFile, outputDir);
        }
    }

    private static void unZipBz(final File inputFile, final File outputDir, File renamedFile, File tempFileDir) throws IOException {
        //System.out.println(String.format("Ungzipping %s to dir %s.", inputFile.getAbsolutePath(), outputDir.getAbsolutePath()));
        final File outputFile = new File(tempFileDir,inputFile.getName());
        InputStream in2 = Files.newInputStream(Paths.get(inputFile.toString()));
        final FileOutputStream out = new FileOutputStream(outputFile);
        BZip2CompressorInputStream in = new BZip2CompressorInputStream(in2);
        IOUtils.copy(in, out);
        in.close();
        out.close();
        if (outputFile.renameTo(renamedFile)) {
            //System.out.println("File moved successfully");
        } else {
            System.out.println("Failed to move file");
        }
    }

    private static void unTar(final File inputFile, final File outputDir) throws IOException {
        FileInputStream fis = new FileInputStream(inputFile);
        TarArchiveInputStream tis = new TarArchiveInputStream(fis);
        TarArchiveEntry tarEntry = null;
        while ((tarEntry = tis.getNextTarEntry()) != null) {
            File outputFile = new File(outputDir + File.separator + tarEntry.getName());
            if (tarEntry.isDirectory()) {
                //System.out.println("outputFile Directory ---- "
                //        + outputFile.getAbsolutePath());
                if (!outputFile.exists()) {
                    outputFile.mkdirs();
                }
            } else {
                //File outputFile = new File(destFile + File.separator + tarEntry.getName());
               // System.out.println("outputFile File ---- " + outputFile.getAbsolutePath());
                outputFile.getParentFile().mkdirs();
                //outputFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(outputFile);
                IOUtils.copy(tis, fos);
                fos.close();
            }
        }
        tis.close();
    }
}