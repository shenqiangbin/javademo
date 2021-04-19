package ZipRar;

import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Compresser {

    public static void main(String[] args) throws IOException, ArchiveException {


        String targetDir = "E:\\老子项目\\一手数据模板\\2";
        String zipFile = "E:\\老子项目\\一手数据模板\\shuju.zip";
        //doUnArchiver(new File(zipFile), targetDir);
        decompressor(zipFile, targetDir);
        decompressor2(zipFile, "E:\\老子项目\\一手数据模板\\22");
    }

    // 不行
    public static void doUnArchiver(File srcfile, String destpath) throws IOException {
        byte[] buf = new byte[1024];
        FileInputStream fis = new FileInputStream(srcfile);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ZipInputStream zis = new ZipInputStream(bis);
        ZipEntry zn = null;
        while ((zn = zis.getNextEntry()) != null) {
            File f = new File(destpath + "/" + zn.getName());
            if (zn.isDirectory()) {
                f.mkdirs();
            } else {
                /*
                 * 父目录不存在则创建
                 */
                File parent = f.getParentFile();
                if (!parent.exists()) {
                    parent.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(f);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                int len;
                while ((len = zis.read(buf)) != -1) {
                    bos.write(buf, 0, len);
                }
                bos.flush();
                bos.close();
            }
            zis.closeEntry();
        }
        zis.close();
    }

    // 可以（推荐使用）
    public static void decompressor(String zipFile, String targetDir) throws IOException, ArchiveException {

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
            String name = Paths.get(targetDir, entry.getName()).toString();
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

    // 解压方式2（某些也会有问题）
    public static void decompressor2(String targetFile, String targetDir) throws FileNotFoundException, ZipException {

        net.lingala.zip4j.ZipFile zipFile = new net.lingala.zip4j.ZipFile(targetFile);
        if (testEncoding(targetFile, Charset.forName("UTF-8")) == false) {
            zipFile.setCharset(Charset.forName("GBK"));
        }
        zipFile.extractAll(targetDir);
    }

    static boolean testEncoding(String filepath, Charset charset) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(new File(filepath));
        BufferedInputStream bis = new BufferedInputStream(fis);
        ZipInputStream zis = new ZipInputStream(bis, charset);
        ZipEntry zn = null;
        try {
            while ((zn = zis.getNextEntry()) != null) {
                // do nothing
            }
        } catch (Exception e) {
            return false;
        } finally {
            try {
                zis.close();
                bis.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
