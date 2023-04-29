package top.pi1grim.ea.component;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import top.pi1grim.ea.common.response.Response;
import top.pi1grim.ea.dto.AvatarDTO;
import top.pi1grim.ea.dto.NumberDTO;
import top.pi1grim.ea.dto.ResultDTO;
import top.pi1grim.ea.exception.CrawlerException;
import top.pi1grim.ea.type.CrawlerStatus;
import top.pi1grim.ea.type.ErrorCode;
import top.pi1grim.ea.type.WebSocketCode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;



@Slf4j
public class Crawler {

    private static final ChromeOptions OPTIONS;

    private static final ConcurrentMap<Long, Crawler> CRAWLER_MAP = new ConcurrentHashMap<>();

    private static final String URL = "https://user.qzone.qq.com/";

    private static final Map<String, Integer> DAY_TO_TIME = Map.of(
            "今天", 0,
            "前天", 2,
            "昨天", 1);

    private CrawlerStatus status;

    private ChromeDriver driver;

    private Long id;

    private Byte step;

    private Long lastUse;

    private Instant timestamp;

    private Map<String, NumberDTO> students;

    private Set<String> filter;

    private String currentQqNumber;

    static {
        OPTIONS = new ChromeOptions();
        OPTIONS.addArguments(
                "--remote-allow-origins=*",
                "--disable-gpu",
                "--no-sandbox"
//                ,"--headless" TODO:推送时移除
                );
        OPTIONS.setPageLoadStrategy(PageLoadStrategy.EAGER);
        System.setProperty("webdriver.chrome.driver", "/usr/local/driver");
    }

    public static Crawler getCrawler(Long id) {
        return CRAWLER_MAP.get(id);
    }

    public static boolean contains(Long id) {
        return CRAWLER_MAP.containsKey(id);
    }

    public static ConcurrentMap<Long, Crawler> getCrawlerMap() {
        return CRAWLER_MAP;
    }

    public static Crawler getInstance() {
        return new Crawler().init();
    }

    public Crawler init() {
        status = CrawlerStatus.OFFLINE;
        driver = new ChromeDriver(OPTIONS);
        timestamp = Instant.now();
        filter = new HashSet<>();
        update();
        log.info("Crawler初始化完成 ====> " + this);
        return this;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void destroy() {
        if (Objects.nonNull(driver)) {
            driver.quit();
        }
        if (CRAWLER_MAP.containsKey(id) &&
                CRAWLER_MAP.get(id).getTimestamp().equals(timestamp)) {
            CRAWLER_MAP.remove(id);
            log.info("Crawler销毁完成 ====> " + id + " : " + this);
        }
    }

    public void update() {
        lastUse = System.currentTimeMillis();
    }

    public void recycle() {
        if (System.currentTimeMillis() - lastUse > 10 * 60 * 1000) {
            log.info("Crawler已被回收 ====> " + id);
            destroy();
        }
    }

    public CrawlerStatus status() {
        return status;
    }

    public void register(Long id, Map<String, NumberDTO> students, Byte step) {
        if (Objects.isNull(id)) {
            log.info("用户Id为空，注册失败，对象已销毁 ====> " + this);
            destroy();
            throw new CrawlerException(ErrorCode.REGISTER_FAIL, null);
        }
        this.id = id;
        this.students = students;
        this.step = step;
        if (CRAWLER_MAP.containsKey(id)) {
            CRAWLER_MAP.get(id).destroy();
        }

        CRAWLER_MAP.put(id, this);
        log.info("注册成功 ====> " + this);
    }

    public static String format(String t) throws ParseException {
        t = t.replace("编辑于", "").trim();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf4 = new SimpleDateFormat("MM-dd HH:mm");
        if(t.startsWith("今天") || t.startsWith("昨天") || t.startsWith("前天")){
            String chinaDate = t.substring(0,2);
            t = t.replace(chinaDate, "").trim();
            return sdf3.format(sdf2.parse(LocalDate.now().toString()).getTime()
                    - (long) DAY_TO_TIME.get(chinaDate) * 24 * 60 * 60 * 1000) + " " + t;
        }else if(t.matches("\\d{1,2}月\\d{1,2}日 \\d{1,2}:\\d{1,2}")){
            SimpleDateFormat sdf = new SimpleDateFormat("M月d日 HH:mm");
            return LocalDate.now().getYear()+ "-" + sdf4.format(sdf.parse(t));
        } else if (t.matches("\\d{2}:\\d{2}")) {
            return sdf3.format(sdf2.parse(LocalDate.now().toString()))+ " " + t;
        } else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日 HH:mm");
            return sdf1.format(sdf.parse(t));
        }
    }

    public LocalDateTime formatToLocalDateTime(String time) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Instant instant;
        ZoneId zoneId;

        try {
            String format = format(time);
            Date date = sdf.parse(format);
            instant = date.toInstant();
            zoneId = ZoneId.systemDefault();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return instant.atZone(zoneId).toLocalDateTime();
    }

    public File getQuick() {
        update();
        if (!status.equals(CrawlerStatus.OFFLINE)) {
            //只有离线状态才能调用这个方法
            throw new CrawlerException(ErrorCode.WRONG_EXECUTE_TIMING, status);
        }
        driver.get(URL);
        File quickFile = null;
        try {
            Thread.sleep(2000);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement iframe = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("login_frame")));

            int iframeX = iframe.getLocation().getX();
            int iframeY = iframe.getLocation().getY();

            driver.switchTo().frame(iframe);
            quickFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            BufferedImage image = ImageIO.read(quickFile);
            WebElement quick = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("/html/body/div[1]/div[4]/div[8]/div/span/img")));

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
        update();
        return quickFile;
    }

    public void checkLogin() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("head-info")));

            currentQqNumber = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("QM_OwnerInfo_Icon")))
                    .getAttribute("src").split("/")[4];

            log.info("当前的QQ号 " + currentQqNumber + " ====> " + id);

        } catch (RuntimeException e) {
            log.error("登录超时 ====> " + id);
            WebSocketServer.sendInfo(Response.success(WebSocketCode.UPDATE_STATUS, id), id);
            throw new CrawlerException(ErrorCode.LOGIN_OVERTIME, id);
        }
        //登录成功
        log.info("Crawler登录成功 ====> " + id);
        WebSocketServer.sendInfo(Response.success(WebSocketCode.UPDATE_STATUS, id), id);
        status = CrawlerStatus.LEAVE_UNUSED;
    }

    public void scrollToBottom() {
        try {

            Thread.sleep(3000);
            JavascriptExecutor executor = driver;
            executor.executeScript("window.scrollTo(0, document.body.scrollHeight);");

        } catch (InterruptedException e) {
            log.error("线程错误 ====> " + id, e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("JS执行出错，公开数据不足 ====> " + id, e);
        }
    }

    public List<ResultDTO> deepSearch() {
        update();

        status = CrawlerStatus.DEEP_SEARCH;
        WebSocketServer.sendInfo(Response.success(WebSocketCode.UPDATE_STATUS, id), id);
        List<ResultDTO> results = new ArrayList<>();

        int headCount = 0;

        for (Map.Entry<String, NumberDTO> entry : students.entrySet()) {

            WebSocketServer.sendInfo(Response.success(WebSocketCode.HEAD_COUNT, headCount++), id);

            driver.get(URL + entry.getKey());

            try {
                WebDriverWait wait10s = new WebDriverWait(driver, Duration.ofSeconds(20));

                String beforeSrc;
                String afterSrc;

                List<WebElement> li;

                do {
                    driver.switchTo().defaultContent();
                    beforeSrc = driver.getPageSource();

                    scrollToBottom();

                    afterSrc = driver.getPageSource();
                    driver.switchTo().frame("QM_Feeds_Iframe");
                    li = wait10s.until(ExpectedConditions.presenceOfElementLocated(By.id("host_home_feeds")))
                            .findElements(By.className("f-single"));

                } while (li.size() < step && !beforeSrc.equals(afterSrc));

                int currentStep = li.size() > step ? step : li.size();
                log.info("当前好友" + entry.getValue().getNotes() + "最远步数 : " + currentStep + " ====> " + id);

                for (int i = 0; i < currentStep; i++) {
                    WebElement item = li.get(i);

                    WebElement time = item.findElement(By.className("f-single-head"))
                            .findElement(By.className("user-info"))
                            .findElement(By.className("info-detail"))
                            .findElement(By.xpath("span"));

                    WebElement content = item.findElement(By.className("f-single-content"))
                            .findElement(By.className("f-item"))
                            .findElement(By.className("f-info"));

                    ResultDTO dto = new ResultDTO()
                            .setUserId(id)
                            .setStuId(entry.getValue().getId())
                            .setNotes(entry.getValue().getNotes())
                            .setQqNumber(entry.getKey())
                            .setContent(content.getText())
                            .setPostTime(formatToLocalDateTime(time.getText()))
                            .setDataType(false);

                    results.add(dto);
                }

            } catch (NoSuchElementException e) {
                log.error("元素未找到 ====> " + id, e);
            } finally {
                status = CrawlerStatus.LEAVE_UNUSED;
            }

        }
        update();
        status = CrawlerStatus.LEAVE_UNUSED;
        WebSocketServer.sendInfo(Response.success(WebSocketCode.UPDATE_STATUS, id), id);
        log.info("深度搜索结束 ====> " + id);
        return results;
    }

    public void listen() {
        status = CrawlerStatus.LISTEN;
        WebSocketServer.sendInfo(Response.success(WebSocketCode.UPDATE_STATUS, id), id);
        log.info("进入监听模式 ====> " + id);
    }

    public ResultDTO scan() {
        update();
        driver.get(URL + currentQqNumber);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement userPto = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("user-pto")));

        String content = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("f-info")))
                .getText();

        String time = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("info-detail")))
                .findElement(By.xpath("span"))
                .getText();
        LocalDateTime dateTime = formatToLocalDateTime(time);

        String[] url = userPto.findElement(By.xpath("a"))
                .getAttribute("href")
                .split("/");
        String number = url[url.length - 1];

        NumberDTO student = students.get(number);
        if (Objects.isNull(student) || filter.contains(content)) {
            return null;
        }

        filter.add(content);
        log.info("侦测到目标 ====> " + id);

        update();

        return new ResultDTO().setUserId(id)
                .setStuId(student.getId())
                .setNotes(student.getNotes())
                .setQqNumber(number)
                .setContent(content)
                .setPostTime(dateTime)
                .setDataType(true);
    }

    public List<AvatarDTO> updateAvatar() {

        update();
        status = CrawlerStatus.UPDATE_AVATAR;
        WebSocketServer.sendInfo(Response.success(WebSocketCode.UPDATE_STATUS, id), id);
        List<AvatarDTO> avatars = new ArrayList<>();

        for (Map.Entry<String, NumberDTO> entry : students.entrySet()) {
            String url = URL + entry.getKey();
            NumberDTO stu = entry.getValue();
            driver.get(url);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            String avatar = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("QM_OwnerInfo_Icon")))
                    .getAttribute("src");

            AvatarDTO avatarDto = new AvatarDTO().setStuId(stu.getId())
                    .setAvatar(avatar);

            avatars.add(avatarDto);
        }

        update();
        status = CrawlerStatus.LEAVE_UNUSED;
        WebSocketServer.sendInfo(Response.success(WebSocketCode.UPDATE_STATUS, id), id);
        log.info("头像采集完成 ====> " + id);
        return avatars;
    }
}
