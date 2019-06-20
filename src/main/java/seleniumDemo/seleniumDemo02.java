package seleniumDemo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class seleniumDemo02 {
    public static void main(String[] args) throws InterruptedException {

        /*
        https://chromedriver.storage.googleapis.com/index.html?path=73.0.3683.68/
        * */

        System.setProperty("webdriver.chrome.driver", "c:/drive/chromedriver.exe");

        ChromeOptions chromeOptions = new ChromeOptions();
        //chromeOptions.addArguments("--headless");
        //chromeOptions.addArguments("--window-size=1280,768");

        ChromeDriver driver = new ChromeDriver(chromeOptions);

        //driver.get("http://www.sqber.com");
        driver.get("http://oa.cnki.net/web/TTKN/Default.aspx");
        //driver.get("http://www.baidu.com");
        driver.manage().window();

        //WebElement element = driver.findElement(By.name("Account"));
        driver.executeScript("$('input[name=Account]').val('SQB10190')");
        driver.executeScript("$('input[name=Password]').val('SQB534607')");
        driver.executeScript("$('input[name=Account]').val('GQ11223')");
        driver.executeScript("$('input[name=Password]').val('GQ460721')");
        driver.executeScript("$('#login').click()");

        Thread.sleep(1000);

        driver.get("http://oa.cnki.net/web/TTKN/RightList/Right0.aspx");

        Thread.sleep(1000);

        String script = "var result = [];debugger;\n" +
                "var trs =$(\"tr[id=datagrid-row-r1-2-1]\");" +
                "console.log(trs.length);\n" +
                "for(var i=0;i<trs.length;i++){\n" +
                "\tvar tr = trs[i];\n" +
                "\tif(tr){\n" +
                "\t\tvar aEle = $(tr).find(\"a\");\n" +
                "\t\tvar text = aEle.html();\n" +
                "\t\tvar href = aEle.attr('href');\n" +
                "\t\tresult.push({\n" +
                "\t\t\ttext:text,\n" +
                "\t\t\thref:href\n" +
                "\t\t});\n" +
                "\t}\n" +
                "}\n" +
                "console.log(result);\n" +
                "return result;";

        Object result = driver.executeScript(script);

        ArrayList<Map<String, String>> list = (ArrayList<Map<String, String>>) result;

        for (Map<String, String> map : list) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                System.out.println(entry.getKey());
                System.out.println(entry.getValue());
            }
        }

        //driver.get("http://oa.cnki.net/web/PublicManage/Announcement/AnnouncementDetailView?GUID=86fbb95f837146c7be18db1b00f5855d");


//        WebElement element = driver.findElement(By.className("el-pager"));
//
//        List<WebElement> eles = element.findElements(By.tagName("li"));
//        WebElement lastEle = eles.get(eles.size() - 1);
//
//        String text = lastEle.getText();
//
//
//        int currentPage = getCurrentPage(driver);
//
//        while (currentPage <= Integer.parseInt(text)) {
//
//            WebElement ele = null;
//            try {
//                ele = getNextButton(driver);
//            } catch (Exception e) {
//
//            }
//
//            find(driver);
//
//            if (ele != null)
//                ele.click();
//            {
//                break;
//            }
//
//        }

        // 可以滚动截屏 参考：https://www.cnblogs.com/sparkling-ly/p/5466644.html
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String val = srcFile.getPath();

        System.out.println(val);

        //与浏览器同步非常重要，必须等待浏览器加载完毕
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        try {
            Files.copy(new File(val).toPath(), new File("d:/a1.png").toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //driver.quit();

        //FileUtils.copyFile(srcFile,new File("d:\\screenshot.png"));


        //String title = driver.getTitle();
        //System.out.printf(title);

        //driver.close();

        /*
        ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setBinary(chromePath);
//          设置为 headless 模式 （必须）
            chromeOptions.addArguments("--headless");
//          设置浏览器窗口打开大小  （非必须）
//            chromeOptions.addArguments("--window-size=1920,1080");
            chromeOptions.addArguments("--window-size=800,450");
//          -–no-sandbox”参数是让Chrome在root权限下跑
            chromeOptions.addArguments("--no-sandbox");
            //设置窗口最大化
//          chromeOptions.addArguments("--start-maximized");
            driver = new ChromeDriver(chromeOptions);
            //System.setProperty("webdriver.gecko.driver", "C:\\Program Files\\Mozilla Firefox\\firefox.exe");
//          WebDriver driver = new FirefoxDriver();
            //设置窗口最大化
//          driver.manage().window().maximize();
            String title = driver.getTitle();
            driver.get(url);
            // 与浏览器同步非常重要，必须等待浏览器加载完毕
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            WebDriverWait wait = new WebDriverWait(driver, 20);
//            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".map-box")
        * */
    }

    private static WebElement getNextButton(WebDriver driver) {
        WebElement elementPager = driver.findElement(By.className("el-pager"));
        List<WebElement> elesLi = elementPager.findElements(By.tagName("li"));
        for (int i = 0; i < elesLi.size(); i++) {

            WebElement item = elesLi.get(i);
            String cssVal = item.getAttribute("class");

            if (cssVal.contains("active")) {
                return elesLi.get(i + 1);
            }
        }
        return null;
    }

    private static int getCurrentPage(WebDriver driver) {
        WebElement elementActive = driver.findElement(By.className("active"));
        String val = elementActive.getText();
        return Integer.parseInt(val);
    }


    private static void find(WebDriver driver) throws InterruptedException {
        List<WebElement> imgs = driver.findElements(By.tagName("img"));
        for (WebElement img : imgs) {
            if (img.getAttribute("src").contains("/images/img.png")) {
                img.click();

                Thread.sleep(2000);
                driver.findElement(By.className("ht-d")).click();
                Thread.sleep(2000);
            }
        }
    }

}
