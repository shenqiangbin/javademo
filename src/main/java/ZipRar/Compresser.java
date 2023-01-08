package ZipRar;

//import net.lingala.zip4j.exception.brt ;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
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

        /**
         * 测试文件下载地址：链接：https://pan.baidu.com/s/12ugoP2Vx8Ui7chKVGaq-1w 提取码：w6qq
         */
//        String targetDir = "E:\\老子项目\\一手数据模板\\2";
//        String zipFile = "E:\\老子项目\\一手数据模板\\shuju.zip";
//        //doUnArchiver(new File(zipFile), targetDir);
//        decompressor(zipFile, targetDir);
//        decompressor2(zipFile, "E:\\老子项目\\一手数据模板\\22");

        archive("/Users/adminqian/shenqb/book/Dubbo.zip", "/Users/adminqian/shenqb/book/Dubbo.pdf");
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
    public static void decompressor2(String targetFile, String targetDir) throws FileNotFoundException {

//        net.lingala.zip4j.ZipFile zipFile = new net.lingala.zip4j.ZipFile(targetFile);
//        if (testEncoding(targetFile, Charset.forName("UTF-8")) == false) {
//            zipFile.setCharset(Charset.forName("GBK"));
//        }
//        zipFile.extractAll(targetDir);
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

    /**
     * 压缩文件
     *
     * @param zipPath 生成的 zip 文件
     * @param file    要压缩的文件
     * @throws IOException
     */
    static void archive(String zipPath, String file) throws IOException {
        File archive = new File(zipPath);
        try (ZipArchiveOutputStream outputStream = new ZipArchiveOutputStream(archive)) {
            // 可以设置压缩等级
            outputStream.setLevel(5);
            // 可以设置压缩算法，当前支持ZipEntry.DEFLATED和ZipEntry.STORED两种
            outputStream.setMethod(ZipEntry.DEFLATED);

            // 压缩包内不创建文件夹
            ZipArchiveEntry entry = new ZipArchiveEntry(Paths.get(file).getFileName().toString());
            // 在zip中创建一个文件
            outputStream.putArchiveEntry(entry);
            // 并写入内容
            byte[] bytes = Files.readAllBytes(new File(file).toPath());
            outputStream.write(bytes);
            // 完成一个文件的写入
            outputStream.closeArchiveEntry();
        }
    }


}
