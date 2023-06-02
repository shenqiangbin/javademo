package seleniumDemo;

import ExcelDemo.Excel2007Utils;
import common.P;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.*;

public class seleniumDemo01 {

    /* 数据集合 */
    static List<List<Object>> dataList = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException, IOException {

        /*
        https://chromedriver.storage.googleapis.com/index.html?path=73.0.3683.68/
        * */

        //System.setProperty("webdriver.chrome.driver", "c:/chromedriver_win32/chromedriver.exe");
        System.setProperty("webdriver.chrome.driver", "/Users/adminqian/shenqb/soft/chromeDriver/chromedriver_mac64/chromedriver");

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36");
        chromeOptions.addArguments("--window-size=1280,768");

        WebDriver driver = new ChromeDriver(chromeOptions);

        driver.get("https://www.qctsw.com/complaint/search?brandId=32");
        //driver.manage().window().maximize();

        Thread.sleep(2000);

        WebElement element = driver.findElement(By.className("el-pager"));
        List<WebElement> eles = element.findElements(By.tagName("li"));

        int currentPage = getCurrentPage(eles);

        WebElement lastPageEle = eles.get(eles.size() - 1);
        String lastPageStr = lastPageEle.getText();
        while (currentPage <= Integer.parseInt(lastPageStr) && currentPage <= 25) {
            System.out.println("currentPage：" + currentPage);
            if (currentPage >= 1) {
                handleCurrentPage(driver);
            }

            WebElement ele = null;
            try {
                ele = getNextButton(driver);
            } catch (Exception e) {

            }

            if (ele != null) {
                ele.click();
                Thread.sleep(3000);
            } else {
                break;
            }


            WebElement element2 = driver.findElement(By.className("el-pager"));
            List<WebElement> eles2 = element2.findElements(By.tagName("li"));
            currentPage = getCurrentPage(eles2);

        }

        saveToExcel();

//        File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
//        String val = srcFile.getPath();
//
//        P.print(val);

        // 与浏览器同步非常重要，必须等待浏览器加载完毕
        //driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

//        try {
//            Files.copy(new File(val).toPath(),new File("d:/a.png").toPath());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //driver.quit();

        //FileUtils.copyFile(srcFile,new File("d:\\screenshot.png"));


        //String title = driver.getTitle();
        //System.out.printf(title);

        driver.close();

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

    // 得到下一个按钮
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

    private static int getCurrentPage(List<WebElement> eles) {
        for (WebElement ele : eles) {
            String cssVal = ele.getAttribute("class");
            if (cssVal.contains("is-active")) {
                return Integer.parseInt(ele.getText());
            }
        }
        throw new RuntimeException("not found");
    }


//    private static void find(WebDriver driver) throws InterruptedException {
//        List<WebElement> imgs = driver.findElements(By.tagName("img"));
//        for (WebElement img : imgs) {
//            if (img.getAttribute("src").contains("/images/img.png")) {
//                img.click();
//
//                Thread.sleep(2000);
//                driver.findElement(By.className("ht-d")).click();
//                Thread.sleep(2000);
//            }
//        }
//    }

    private static void handleCurrentPage(WebDriver driver) throws InterruptedException {

        List<WebElement> nameEles = driver.findElements(By.className("public-hover"));
        for (WebElement nameEle : nameEles) {

            nameEle.click();

            Thread.sleep(3000);

            handleDetailPage(driver);

            Thread.sleep(3000);
        }
    }

    private static void handleDetailPage(WebDriver driver) {

        List<Object> rowData = new ArrayList<>();

        String winStr = driver.getWindowHandles().toArray()[1].toString();
        driver.switchTo().window(winStr);

        String currentUrl = driver.getCurrentUrl();
        String pageSource = driver.getPageSource();
        System.out.println(currentUrl);
        rowData.add(currentUrl);

        WebElement titleEle = driver.findElement(By.className("text-title"));
        String content = titleEle.getText();

        System.out.println(content);
        rowData.add(content);

        List<WebElement> contentEles = driver.findElements(By.className("table-content"));
        for (int i = 0; i < contentEles.size(); i++) {
            WebElement ele = contentEles.get(i);
            if (i == 3 || i == 6) {
                String text = ele.getText();
                System.out.println(text);
                rowData.add(text);
            }
        }

//        WebElement contentEle = driver.findElement(By.className("mtb-24"));
//        String contentEleText = contentEle.getText();
//        rowData.add(contentEleText);

        // 关闭当前 tab 页
        driver.close();

        winStr = driver.getWindowHandles().toArray()[0].toString();
        driver.switchTo().window(winStr);

        dataList.add(rowData);
    }

    static void saveToExcel() throws IOException {
        Excel2007Utils.writeExcelData("/Users/adminqian/shenqb", "data.xls", "sheet1", dataList);
    }
}

/**
 * 1、selenium 关闭当前 tab 页
 * <p>
 * 再切换回第一个 tab 页
 * winStr = driver.getWindowHandles().toArray()[0].toString();
 * driver.switchTo().window(winStr);
 **/
