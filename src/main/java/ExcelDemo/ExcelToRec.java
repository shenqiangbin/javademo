package ExcelDemo;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 处理中国知识在线服务平台的 excel 视频数据，生成 rec 文件
 */
public class ExcelToRec {

    public static void main(String[] args) throws IOException, InvalidFormatException {
        buildRec();
    }

    static void buildRec() throws IOException, InvalidFormatException {
        String file = "/Users/adminqian/Desktop/中国知识在线服务平台/处理后.xlsx";
        List<List<String>> objects = new ArrayList<>();

        CommonExcel commonExcel = new CommonExcel(file, new ICommonResultHandler() {

            @Override
            public boolean validateTilte(List<String> titles) {
                return true;
            }

            @Override
            public String store(List<String> cellVals, List<String> titles) {
//                System.out.println(cellVals);
                objects.add(cellVals);
                return "ok";
            }

            @Override
            public void done() {
                System.out.println("done");
            }
        });
        commonExcel.handle();

        StringBuilder builder = new StringBuilder();
        // 拿到数据后，开始处理
        for (List<String> cellVals : objects) {
            String title = cellVals.get(2);
            String video = cellVals.get(4);

            System.out.println(title + " --- " + video);

            if (StringUtils.isEmpty(video)) {
                continue;
            }

            builder.append("<REC>").append("\r\n");
            builder.append("<篇名>=").append(title).append("\r\n");
            builder.append("<作者>=").append("ccro").append("\r\n");
            builder.append("<年>=").append("2023").append("\r\n");
            builder.append("<HANDLEFLAG>=").append("1").append("\r\n");
            builder.append("<会议地点>=").append("Beijing online").append("\r\n");
            builder.append("<数据类型>=").append("4").append("\r\n");
            builder.append("<视频地址>=").append(video).append("\r\n");
            builder.append("<播放量>=").append(280).append("\r\n");
            builder.append("<视频封面地址>=").append(video.replace("mp4", "png")).append("\r\n");
            builder.append("<是否首页展示>=").append(0).append("\r\n");
            builder.append("<首页视频排序>=").append(0).append("\r\n");
            builder.append("<视频时长>=").append("02:28").append("\r\n");
            builder.append("<会议时间>=").append("2023-07-31").append("\r\n");
            builder.append("<SYS_FLD_CHECKSTATE>=").append(2).append("\r\n");

            String md5Str = MD5.md5(title);
            builder.append("<SYS_FLD_MD5>=").append(md5Str).append("\r\n");
            builder.append("\r\n\r\n");

            // 生成的 rec 文件的编码需要是 UTF-8-Bom （或者是 GB2312），可以先转成 UTF-8-Bom 再转成 GB2312。


        }


        System.out.println(builder.toString());
    }


}
