package ZipRar;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Compresser {

    public static void main(String[] args) throws IOException, ArchiveException {
        decompressor();
    }

    public static void decompressor() throws IOException, ArchiveException {

        String targetDir = "H:\\1";
        String zipFile = "H:\\博客.zip";

        File archiveFile = new File(zipFile);
        // 文件不存在，跳过
        if (!archiveFile.exists())
            return;

        ArchiveInputStream input = new ArchiveStreamFactory().createArchiveInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
        ArchiveEntry entry = null;
        while ((entry = input.getNextEntry()) != null) {
            if (!input.canReadEntryData(entry)) {
                // log something?
                continue;
            }
            String name = fileName(targetDir, entry);
            File f = new File(name);
            if (entry.isDirectory()) {
                if (!f.isDirectory() && !f.mkdirs()) {
                    throw new IOException("failed to create directory " + f);
                }
            } else {
                File parent = f.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("failed to create directory " + parent);
                }
                try (OutputStream o = Files.newOutputStream(f.toPath())) {
                    IOUtils.copy(input, o);
                }
            }
        }
        input.close();

    }

    public static String fileName(String targetDir,ArchiveEntry entry){
        return Paths.get(targetDir,entry.getName()).toString();
    }
}
