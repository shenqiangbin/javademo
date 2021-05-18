package fileDemo;

import org.mozilla.universalchardet.UniversalDetector;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

// 类似 commons-io 下的 FileUtils
public class FileHelper {

    public static File getDirectory(File f) {
        if (f != null) {
            String path = f.getPath();
            int len = path.length();
            int num2 = len;

            while (true) {
                --num2;
                if (num2 < 0) {
                    break;
                }

                char ch1 = path.charAt(num2);
                if (ch1 == File.separatorChar) {
                    return new File(path.substring(0, num2));
                }
            }
        }

        return null;
    }

    public static void copyFile(File in, File out) throws IOException {
        FileInputStream fis = new FileInputStream(in);
        FileOutputStream fos = new FileOutputStream(out);
        FileChannel sourceChannel = fis.getChannel();
        FileChannel destinationChannel = fos.getChannel();
        sourceChannel.transferTo(0L, sourceChannel.size(), destinationChannel);
        sourceChannel.close();
        destinationChannel.close();
        fos.close();
        fis.close();
    }

    public static String getFilename(File file) {
        if (file != null) {
            String path = file.getPath();
            int len = path.length();
            int num2 = len;

            while (true) {
                --num2;
                if (num2 < 0) {
                    break;
                }

                char ch1 = path.charAt(num2);
                if (ch1 == File.separatorChar) {
                    return path.substring(num2 + 1, len);
                }
            }
        }

        return "";
    }

    /**
     * 在指定目录按文件名字查找文件（包括子目录）
     * @param dir
     * @param name
     * @return 返回找到得对象集合
     * @throws FileNotFoundException
     */
    public static List<File> findFile(String dir, String name) throws FileNotFoundException {
        List<File> list = new ArrayList<>();
        File file = new File(dir);
        if (!file.exists()) {
            throw new FileNotFoundException("目录不存在");
        }
        if (file.isFile()) {
            if(file.getName().equalsIgnoreCase(name)){
                list.add(file);
            }
        } else {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                String root = files[i].getAbsolutePath();
                list.addAll(FileHelper.findFile(root, name));
            }
        }
        return list;
    }

    /**
     * 创建文件
     *
     * @param filePath
     * @return
     */
    public static boolean createFile(String filePath) throws Exception {

        boolean flag = false;
        File fileName = new File(filePath);
        try {
            if (!fileName.exists()) {
                fileName.createNewFile();
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 读TXT文件内容
     *
     * @param filePath
     * @return
     */
    public static String readTxtFile(String filePath) throws Exception {
        String result = null;
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            File fileName = new File(filePath);
            fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);
            try {
                String read = null;
                while ((read = bufferedReader.readLine()) != null) {
                    result = result + read + "\r\n";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (fileReader != null) {
                fileReader.close();
            }
        }
        System.out.println("读取出来的文件内容是：" + "\r\n" + result);
        return result;
    }

    public static boolean writeTxtFile(String content, String filePath, boolean isUnicode, boolean append) throws Exception {
        RandomAccessFile mm = null;
        boolean flag = false;
        FileOutputStream o = null;
        try {
            File fileName = new File(filePath);
            if (!fileName.exists()) {
                fileName.createNewFile();
            }
            o = new FileOutputStream(fileName, append);
            if (isUnicode) {
                o.write(content.getBytes("GBK"));
            } else {
                o.write(content.getBytes("UTF-8"));
            }
            o.close();
            flag = true;
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            if (mm != null) {
                mm.close();
            }
        }
        return flag;
    }

    public static boolean deleteTxtFile(String filePath) throws Exception {
        File fileName = new File(filePath);
        try {
            if (!fileName.exists()) {
                return false;
            }
            fileName.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 将本地文件转换成字节形式的字符串，并通过 base64 加密
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static String fileToByteConent(String path) throws IOException {
        FileInputStream fileInputStream = null;
        fileInputStream = new FileInputStream(path);
        //ByteArrayInputStream inputStream = new ByteArrayInputStream(fileInputStream);

        StringBuilder builder = new StringBuilder();
        int ch = 0;
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        while ((ch = fileInputStream.read()) != -1) {
            arrayOutputStream.write(ch);
        }
        fileInputStream.close();
        String val = arrayOutputStream.toString();
        BASE64Encoder encoder = new BASE64Encoder();
        String str = encoder.encode(arrayOutputStream.toByteArray());
        return str;
    }

    /**
     * 将本地文件转换成字节形式的字符串，并通过 base64 加密
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static String fileToByteConentWithCharater(String path) throws IOException {
        FileInputStream fileInputStream = null;
        ByteArrayOutputStream arrayOutputStream = null;
        try {
            fileInputStream = new FileInputStream(path);
            //ByteArrayInputStream inputStream = new ByteArrayInputStream(fileInputStream);

            StringBuilder builder = new StringBuilder();
            int ch = 0;
            arrayOutputStream = new ByteArrayOutputStream();
            while ((ch = fileInputStream.read()) != -1) {
                arrayOutputStream.write(ch);
            }
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }

        String charsetName = detector(path);
        String val = arrayOutputStream.toString(charsetName);
        //String abc = new String(arrayOutputStream.toByteArray(),"UTF-8");
        // abc.getBytes();

        BASE64Encoder encoder = new BASE64Encoder();
        //String str = encoder.encode(arrayOutputStream.toByteArray());
        String str = encoder.encode(val.getBytes());
        return str;
    }

    public static String detector(String fileName) throws IOException {
        String encode = null;
        BufferedInputStream bis = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(fileName));
            int readSize;
            byte[] buffer = new byte[8 * 4096];
            UniversalDetector detector = new UniversalDetector(null);
            while ((readSize = bis.read(buffer)) > 0 && !detector.isDone()) {
                detector.handleData(buffer, 0, readSize);
            }
            detector.dataEnd();
            encode = detector.getDetectedCharset();
            detector.reset();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //return encode;
        return encode.equalsIgnoreCase("UTF-8") ? "UTF-8" : "GBK";
    }

    /**
     * 将 base64 加密后的字节字符串转换成本地文件
     *
     * @param path
     * @param content
     * @throws IOException
     */
    public static void byteContentToFile(String path, String content) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] newBy = decoder.decodeBuffer(content);
        FileOutputStream fileOutputStream = new FileOutputStream(path, false);
        fileOutputStream.write(newBy);
    }
}