import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class HepsiburadaLoginPage {
    private WebDriver driver;
    private String baseUrl;
    private String mail;
    private String pw;

    @BeforeClass
    public static void setupWebDriverChromeDriver() {
        System.setProperty("webdriver.chrome.driver","src/test/resources/chromedriver");
    }

    @Before
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        baseUrl = "https://www.hepsiburada.com/";
        mail = "hb.hafizeyildirim.testcase@gmail.com";
        pw = "HepsiburadaCaseTest123";
    }

    @After
    public void removeObject()
    {
        if(driver != null)
        {
            driver.quit();
        }
    }

    @Test
    public void verifyAddTwoItemInBasketWithoutLogin()
    {
        driver.get(baseUrl);

        //Arama inputuna ilgili string değer yazılır ve arama butonuna tıklanır.
        WebElement searchInput = driver.findElement(By.className("desktopOldAutosuggestTheme-input"));
        searchInput.click();
        searchInput.sendKeys("probis");

        driver.findElement(By.className("SearchBoxOld-buttonContainer")).click();

        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }

        //Arama yapıldıktan sonra, yönlendirilen ekran liste ekranı olmalıdır.
        assertThat(driver.getCurrentUrl(),containsString(baseUrl+"ara?q=probis"));

        //Birinci eleman alınıyor
        WebElement firstItem = driver.findElement(By.id("i0"));
        //Action sınıfıyla, birinci elemanın üzerine gelinip, "Sepete Ekle" butonunu yakalıyoruz.
        Actions action = new Actions(driver);
        action.moveToElement(firstItem).build().perform();

        WebElement sepeteEkleBtnItem1 = firstItem.findElement(By.xpath("//*[@id=\"i0\"]/div/a/div[3]/div[4]/button"));
        sepeteEkleBtnItem1.click();

        //İkinci eleman
        WebElement secondItem = driver.findElement(By.id("i1"));
        action.moveToElement(secondItem).build().perform();

        WebElement sepeteEkleBtnItem2 = firstItem.findElement(By.xpath("//*[@id=\"i1\"]/div/a/div[3]/div[4]/button"));
        sepeteEkleBtnItem2.click();

        //Sepet işlemleri
        driver.get("https://checkout.hepsiburada.com/sepetim");

        try{
            Thread.sleep(3000);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }

        //Sepetteki ürün adedini bildiren span etiketini çekiyoruz.
        WebElement basketCountspan = driver.findElement(By.id("basket-item-count"));

        int productCount = Integer.parseInt(basketCountspan.getText().split(" ")[0]);

        //2 adet ürün olduğunu doğruluyorum.
        Assert.assertTrue(productCount == 2);
    }

    @Test
    public void verifyAddTwoItemInBasketWithLogin()
    {
        Actions action = new Actions(driver);
        driver.get(baseUrl);

        //Hesabım butonunun üzerine gelip, Giriş yap butonuna tıklıyoruz.
        WebElement myAccountElement = driver.findElement(By.id("myAccount"));
        action.moveToElement(myAccountElement).build().perform();
        driver.findElement(By.id("login")).click();

        try{
            Thread.sleep(3000);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }

        //Yönlendirilen sayfa aşağıdaki gibi olmalıdır.
        assertThat(driver.getCurrentUrl(),containsString("giris.hepsiburada.com"));

        //Email inputu ve şifre inputu doldurultuktan sonra giriş yapma butonuna tıklıyoruz.
        WebElement mailInput = driver.findElement(By.id("txtUserName"));
        mailInput.sendKeys(mail);
        driver.findElement(By.id("btnLogin")).click();

        WebElement pwInput = driver.findElement(By.id("txtPassword"));
        pwInput.sendKeys(pw);
        driver.findElement(By.id("btnEmailSelect")).click();

        try{
            Thread.sleep(3000);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }

        //Giriş başarılıysa, yönlendirilen URL, anasayfa olmalıdır.
        assertThat(driver.getCurrentUrl(),equals(baseUrl));

        //Login işlemi bitiyor.

        WebElement searchInput = driver.findElement(By.className("desktopOldAutosuggestTheme-input"));
        searchInput.click();
        searchInput.sendKeys("probis");

        driver.findElement(By.className("SearchBoxOld-buttonContainer")).click();

        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }

        assertThat(driver.getCurrentUrl(),containsString(baseUrl+"ara?q=probis"));

        //Birinci eleman
        WebElement firstItem = driver.findElement(By.id("i0"));

        action.moveToElement(firstItem).build().perform();

        WebElement sepeteEkleBtnItem1 = firstItem.findElement(By.xpath("//*[@id=\"i0\"]/div/a/div[3]/div[4]/button"));
        sepeteEkleBtnItem1.click();

        //İkinci eleman
        WebElement secondItem = driver.findElement(By.id("i1"));
        action.moveToElement(secondItem).build().perform();

        WebElement sepeteEkleBtnItem2 = firstItem.findElement(By.xpath("//*[@id=\"i1\"]/div/a/div[3]/div[4]/button"));
        sepeteEkleBtnItem2.click();

        driver.get("https://checkout.hepsiburada.com/sepetim");

        try{
            Thread.sleep(3000);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }

        WebElement basketCountspan = driver.findElement(By.id("basket-item-count"));

        int productCount = Integer.parseInt(basketCountspan.getText().split(" ")[0]);

        Assert.assertTrue(productCount == 2);

    }

}
