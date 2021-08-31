package PDF;

import org.apache.commons.io.FilenameUtils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathUtil {

    /**
     * 返回当前项目的 folderName 路径
     * @param folderName
     * @return
     */
    public static String getPath(String folderName) {
        String userDir = System.getProperty("user.dir");
        Path path = Paths.get(userDir, folderName);
        return path.toString();
    }

    /**
     * 在文件所在路径生成一个新的文件，返回文件路径
     * @param origiFile 文件位置
     * @param other 新文件名是原文件名加后缀，other 是后缀
     * @return 新的文件路径
     */
    public static String getNewFileName(String origiFile, String other) {
        String path = FilenameUtils.getFullPath(origiFile);
        String filenameWithoutExt = FilenameUtils.getBaseName(origiFile);
        String extension = FilenameUtils.getExtension(origiFile);
        String newFileName = filenameWithoutExt + "_" + other + "." + extension;

        return FilenameUtils.concat(path, newFileName);
    }
}
