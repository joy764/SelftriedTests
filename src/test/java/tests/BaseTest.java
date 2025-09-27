package tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import org.testng.ITestResult;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class BaseTest {

    protected WebDriver driver;
    protected final Logger log = LogManager.getLogger(this.getClass());

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        // If not set globally, uncomment this line and update the path
        // System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");

        ChromeOptions options = new ChromeOptions();
        options.setBinary("/usr/bin/google-chrome"); // Ensure official Chrome is used
        options.addArguments("--start-maximized");  // Optional: Start browser maximized
        driver = new ChromeDriver(options);
        log.info("Chrome browser launched.");
    }

    public WebDriver getDriver() {
        return driver;
    }

    private void playSound(String soundFilePath) {
        try {
            File soundFile = new File(soundFilePath);
            if (soundFile.exists()) {
                Clip clip = AudioSystem.getClip();
                clip.open(AudioSystem.getAudioInputStream(soundFile));
                clip.start();
            }
        } catch (Exception e) {
            log.warn("Could not play sound: " + e.getMessage());
        }
    }

    private void playBeep() {
        java.awt.Toolkit.getDefaultToolkit().beep();
    }

    private void playSoundOrBeep(String soundFilePath) {
        boolean played = false;
        try {
            File soundFile = new File(soundFilePath);
            if (soundFile.exists()) {
                Clip clip = AudioSystem.getClip();
                clip.open(AudioSystem.getAudioInputStream(soundFile));
                clip.start();
                played = true;
            }
        } catch (Exception e) {
            log.warn("Could not play sound: " + e.getMessage());
        }
        if (!played) {
            playBeep();
        }
    }

    private void playFailBeeps() {
        for (int i = 0; i < 3; i++) {
            playBeep();
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {}
        }
    }

    @AfterMethod(alwaysRun = true)
    public void teardown(org.testng.ITestResult result) {
        if (driver != null) {
            driver.quit();
            log.info("Chrome browser closed.");
        }
        // Play sound based on test result
        if (result.getStatus() == ITestResult.SUCCESS) {
            playSoundOrBeep("src/test/resources/sounds/pass.wav");
        } else if (result.getStatus() == ITestResult.FAILURE) {
            playFailBeeps();
        }
    }
}
