package DataUnzipper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

public class ZipManager {

    public static File unGzip(final File inputFile, final File outputDir, File renamedFile) throws FileNotFoundException, IOException {
        System.out.println(String.format("Ungzipping %s to dir %s.", inputFile.getAbsolutePath(), outputDir.getAbsolutePath()));

        final File outputFile = new File(outputDir, inputFile.getName().substring(0, inputFile.getName().length() - 3));
        InputStream in2 = Files.newInputStream(Paths.get(inputFile.toString()));
        // final GZIPInputStream in = new GZIPInputStream(new FileInputStream(inputFile));
        BZip2CompressorInputStream in = new BZip2CompressorInputStream(in2);

        final FileOutputStream out = new FileOutputStream(outputFile);

        IOUtils.copy(in, out);

        in.close();
        out.close();

        if (outputFile.renameTo(renamedFile)) {
            System.out.println("File moved successfully");
        } else {
            System.out.println("Failed to move file");
        }
        return outputFile;
    }
}