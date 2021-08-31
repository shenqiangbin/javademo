package PDF;

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
}
