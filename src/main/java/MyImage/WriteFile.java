package MyImage;

import java.io.*;

public class WriteFile {

    /**
     * 将内容写进文件，文件不存在则创建
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String dir = "d:/test/ttest/"; // 注意结尾的斜线
        String fileName = "dic.txt";
        String content = getContent();

        removeFile(dir+fileName);

        createIfNotExist(dir, false);
        createIfNotExist(dir + fileName, true);
        writeFile(dir + fileName, content);
    }

    private static String getContent() {
        StringBuilder builder = new StringBuilder();

        builder.append("学校系统\t100\r\n");

        builder.append("学校表\t50\r\n");
        builder.append("学生表\t50\r\n");

        builder.append("成绩\t20\r\n");
        builder.append("学生姓名\t20\r\n");
        builder.append("学科\t20\r\n");
        builder.append("分数\t20\r\n");
        builder.append("学校名称\t20\r\n");
        builder.append("创建年份\t20\r\n");
        builder.append("规模\t20\r\n");
        builder.append("级别\t20\r\n");
        builder.append("类型\t20\r\n");

        builder.append("沙流小学\t10\r\n");
        builder.append("北京大学\t10\r\n");
        builder.append("实验小学\t10\r\n");
        builder.append("新华中学\t10\r\n");
        builder.append("定州一中\t10\r\n");
        builder.append("定州二中\t10\r\n");
        builder.append("北京大学\t10\r\n");
        builder.append("清华大学\t10\r\n");
        builder.append("无名大学\t10\r\n");

        builder.append("小李\t10\r\n");
        builder.append("小明\t10\r\n");

        return builder.toString();
    }

    public static void createIfNotExist(String path, boolean isFile) throws IOException {
        File file = new File(path);
        //如果文件不存在则创建
        if (!file.exists()) {
            if (!isFile) {
                //如果是目录
                file.mkdirs();
            } else {
                //如果是文件
                file.createNewFile();
            }
        }
    }

    public static void writeFile(String path, String content) throws IOException {
        FileOutputStream fileOutputStream = null;
        BufferedWriter bufferedWriter = null;
        try {
            fileOutputStream = new FileOutputStream(path, true);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

            bufferedWriter.write(content);
        } finally {
            bufferedWriter.flush();
            bufferedWriter.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        }
    }

    public static boolean removeFile(String path){
        File file = new File(path);
        return file.delete();
    }

}
