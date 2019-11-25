package ExcelDemo;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

import java.io.File;
import java.net.ConnectException;



public class ExcelConvert {
    public static void main(String[] args) throws ConnectException {

        // 另存为可以，但是 - 会变成 0
        String host = "127.0.0.1";
        int port = 8100;
        // 核心代码很简洁,无非就是: 连接 -> 转存为其他格式 -> 断开
        OpenOfficeConnection connection = new SocketOpenOfficeConnection(host, port);
        connection.connect();
        DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
        // 可以直接输入文件名实现转存 ,converter会自行判断格式
        converter.convert(new File("d:/23.xls"), new File("d:/aaaaa.xls"));
        connection.disconnect();
    }
}
