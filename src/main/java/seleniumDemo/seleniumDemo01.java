package seleniumDemo;

import common.P;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class seleniumDemo01 {
    public static void main(String[] args){

        /*
        https://chromedriver.storage.googleapis.com/index.html?path=73.0.3683.68/
        * */

        System.setProperty("webdriver.chrome.driver", "c:/drive/chromedriver.exe");

        ChromeOptions chromeOptions = new ChromeOptions();
        //chromeOptions.addArguments("--headless");
        //chromeOptions.addArguments("--window-size=1280,768");

        WebDriver driver = new ChromeDriver(chromeOptions);

        //driver.get("http://www.sqber.com");
        driver.get("http://192.168.25.76:8080/boat?modelid=489053");
        //driver.get("http://www.baidu.com");
        driver.manage().window().maximize();

        File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        String val = srcFile.getPath();

        P.print(val);

        // 与浏览器同步非常重要，必须等待浏览器加载完毕
        //driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        try {
            Files.copy(new File(val).toPath(),new File("d:/a.png").toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        driver.quit();

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
}
