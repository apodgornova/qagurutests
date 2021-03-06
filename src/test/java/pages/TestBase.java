package pages;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import config.CredentialsConfig;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.openqa.selenium.remote.DesiredCapabilities;

import static java.lang.String.format;

public class TestBase {

    public static CredentialsConfig credentialsConfig =
            ConfigFactory.create(CredentialsConfig.class);

    @BeforeAll
        static void setup() {
            SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
            Configuration.startMaximized = true;

            //https://user1:1234@selenoid.autotests.cloud/wd/hub/
            Configuration.remote = format("https://%s:%s@%s",
                credentialsConfig.login(),
                credentialsConfig.password(),
                System.getProperty("selenoidUrl"));
            Configuration.browserSize = "1920x1080";

            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("enableVNC", true);
            capabilities.setCapability("enableVideo", true);

            Configuration.browserCapabilities = capabilities;
        }

        @AfterEach
        public void tearDown() {
            Attach.screenshotAs("Last screenshot");
            Attach.pageSource();
            Attach.browserConsoleLogs();
            Attach.addVideo();
        }
}
