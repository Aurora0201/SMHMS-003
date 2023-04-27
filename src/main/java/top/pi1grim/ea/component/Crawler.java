package top.pi1grim.ea.component;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import top.pi1grim.ea.exception.CrawlerException;
import top.pi1grim.ea.type.CrawlerStatus;
import top.pi1grim.ea.type.ErrorCode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;



@Slf4j
public class Crawler {

    private static final ChromeOptions OPTIONS;

    private static final ConcurrentMap<Long, Crawler> CRAWLER_MAP = new ConcurrentHashMap<>();

    private static final String URL = "https://user.qzone.qq.com/";

    private CrawlerStatus status;

    private ChromeDriver driver;

    private Long id;

    private Long lastUse;

    private Map<String, String> students;

    static {
        OPTIONS = new ChromeOptions();
        OPTIONS.addArguments(
                "--remote-allow-origins=*",
                "--disable-gpu",
                "--no-sandbox"
//                ,"--headless"
                );
        OPTIONS.setPageLoadStrategy(PageLoadStrategy.EAGER);
        System.setProperty("webdriver.chrome.driver", "/usr/local/driver");
    }

    public static boolean contains(Long id) {
        return CRAWLER_MAP.containsKey(id);
    }

    public static Crawler getCrawler(Long id) {
        return CRAWLER_MAP.get(id);
    }

    public Crawler init() {
        status = CrawlerStatus.OFFLINE;
        driver = new ChromeDriver(OPTIONS);
        update();
        log.info("Crawler初始化完成 ====> " + this);
        return this;
    }

    public void destroy() {
        if (Objects.nonNull(driver)) {
            driver.quit();
        }
        CRAWLER_MAP.remove(id);
        log.info("Crawler销毁完成 ====> " + id);
    }

    public void update() {
        lastUse = System.currentTimeMillis();
    }

    public void recovery() {
        //TODO：超时回收
    }

    public CrawlerStatus status() {
        return status;
    }

    public void register(Long id, Map<String, String> students) {
        if (Objects.isNull(id)) {
            log.info("用户Id为空，注册失败，对象已销毁 ====> " + this);
            destroy();
            throw new CrawlerException(ErrorCode.REGISTER_FAIL, null);
        }
        this.id = id;
        this.students = students;

        if (CRAWLER_MAP.containsKey(id)) {
            CRAWLER_MAP.get(id).destroy();
        }

        CRAWLER_MAP.put(id, this);
        log.info("注册成功 ====> " + this);
    }

    public File getQuick() {
        if (!status.equals(CrawlerStatus.OFFLINE)) {
            //只有离线状态才能调用这个方法
            throw new CrawlerException(ErrorCode.WRONG_LOGIN_TIMING, status);
        }
        driver.get(URL);
        File quickFile = null;
        try {
            Thread.sleep(3000);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement iframe = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("login_frame")));
            int iframeX = iframe.getLocation().getX();
            int iframeY = iframe.getLocation().getY();
            driver.switchTo().frame(iframe);
            quickFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            BufferedImage image = ImageIO.read(quickFile);
            WebElement quick = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div[4]/div[8]/div/span/img")));
            int left = iframeX + quick.getLocation().getX();
            int top = iframeY + quick.getLocation().getY();
            int right = quick.getSize().getWidth();
            int bottom = quick.getSize().getHeight();
            BufferedImage img = image.getSubimage(left,top,right,bottom);
            ImageIO.write(img, "png", quickFile);
            driver.switchTo().defaultContent();
        } catch (IOException e) {
            log.error("异常发生",e);
        } catch (InterruptedException e) {
            log.error("异常发生", e);
            Thread.currentThread().interrupt();
        }
        log.info("获取二维码成功 ====> " + id);
        return quickFile;
    }

    public void checkLogin() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("head-info")));
        } catch (RuntimeException e) {
            destroy();
            log.error("登录超时，销毁Crawler ====> " + id);
            throw new CrawlerException(ErrorCode.LOGIN_OVERTIME, id);
        }
        //登录成功
        log.info("Crawler登录成功 ====> " + id);
        status = CrawlerStatus.LEAVE_UNUSED;

    }

}
