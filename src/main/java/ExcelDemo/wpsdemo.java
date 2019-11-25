package ExcelDemo;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class wpsdemo {
    public static void main(String[] args) {
        //调用 wps 保存
        Ex2PDF("d:/23.xls","d:/233.xlsx");
    }

    private static void Ex2PDF(String inputFile, String pdfFile) {
        try {

            ComThread.InitSTA(true);
            ActiveXComponent ax = new ActiveXComponent("KET.Application");
            //ax.setProperty("Visible", false);
            ax.setProperty("AutomationSecurity", new Variant(3)); // 禁用宏
            Dispatch excels = ax.getProperty("Workbooks").toDispatch();

            Dispatch excel = Dispatch.invoke(excels, "Open", Dispatch.Method, new Object[]{inputFile, new Variant(false), new Variant(false)}, new int[9]).toDispatch();
// 转换格式
            // excel另存为 excel 方法不知道
            Dispatch.call(excel, "SaveAs",pdfFile);
//            Dispatch.invoke(excel, "ExportAsFixedFormat", Dispatch.Method, new Object[]{new Variant(0), // PDF格式=0
//                    pdfFile, new Variant(0) // 0=标准 (生成的PDF图片不会变模糊) 1=最小文件
//                    // (生成的PDF图片糊的一塌糊涂)
//            }, new int[1]);

// 这里放弃使用SaveAs
            /*
             * Dispatch.invoke(excel,"SaveAs",Dispatch.Method,new Object[]{ outFile, new
             * Variant(57), new Variant(false), new Variant(57), new Variant(57), new
             * Variant(false), new Variant(true), new Variant(57), new Variant(true), new
             * Variant(true), new Variant(true) },new int[1]);
             */

            Dispatch.call(excel, "Close", new Variant(false));

            if (ax != null) {
                ax.invoke("Quit", new Variant[]{});
                ax = null;
            }
            ComThread.Release();

        } catch (Exception e) {
            e.printStackTrace();
            //return -1;
        }
    }
}
