package com.udacity.jwdnd.course1.cloudstorage;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class Credential {
    private WebDriver driver;

    @FindBy(id="credential-url")
    private WebElement url;
    @FindBy(id="credential-username")
    private WebElement username;
    @FindBy(id="credential-password")
    private WebElement password;
    @FindBy(id="credential-save")
    private WebElement submit;
    @FindBy(id="nav-credentials-tab")
    private WebElement credentialTab;
    @FindBy(id="add-credential")
    private WebElement add;
    @FindBy(id="home-link")
    private WebElement home;



    public Credential(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void createMultipleCredentials(String[] urls, String[] usernames, String[] passwords){
        WebDriverWait wait = new WebDriverWait(driver, 15);
        for (int i = 0; i< urls.length; i++){
            clickCredentialsTab();

            wait.until(ExpectedConditions.elementToBeClickable(add)).click();
            wait.until(ExpectedConditions.visibilityOf(this.url));

            this.url.sendKeys(urls[i]);
            this.username.sendKeys(usernames[i]);
            this.password.sendKeys(passwords[i]);
            wait.until(ExpectedConditions.elementToBeClickable(submit)).click();

            clickHome();
        }

    }

    public void editCredentials(String[] urls, String[] usernames, String[] passwords, String[] unencryptedPasswords){
        WebDriverWait wait = new WebDriverWait(driver, 20);

        for (int i = 0; i < urls.length; i++){
            clickCredentialsTab();
            List<WebElement> editButtons = driver.findElements(By.id("edit-credential"));
            wait.until(ExpectedConditions.elementToBeClickable(editButtons.get(i))).click();
            wait.until(ExpectedConditions.visibilityOf(this.url));

            this.url.clear();
            this.url.sendKeys(urls[i]);
            this.username.clear();
            this.username.sendKeys(usernames[i]);
            verifyUnencryptedPassword(this.password, unencryptedPasswords[i]);
            this.password.clear();
            this.password.sendKeys(passwords[i]);
            submit.click();

            clickHome();
        }
    }

    public void deleteCredential(){
        WebDriverWait wait = new WebDriverWait(this.driver, 15);

        clickCredentialsTab();
        List<WebElement> deleteButtons = driver.findElements(By.id("delete-credential"));

        while(deleteButtons.size()>0){
            wait.until(ExpectedConditions.elementToBeClickable(deleteButtons.get(0))).click();
            clickHome();
            clickCredentialsTab();
            deleteButtons = driver.findElements(By.id("delete-credential"));
        }
    }

    public void verifyUnencryptedPassword(WebElement unencryptedPassword, String password){
        Assertions.assertEquals(password, unencryptedPassword.getAttribute("value"));
    }

    public void verifyCredential(String[] expectedUrls, String[] expectedUsernames, String[] expectedPasswords){
        WebDriverWait wait = new WebDriverWait(this.driver, 15);
        clickCredentialsTab();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("credentialUrl"))));

        List<WebElement> urls = driver.findElements(By.id("credentialUrl"));
        List<WebElement> usernames = driver.findElements(By.id("credentialUsername"));
        List<WebElement> passwords = driver.findElements(By.id("credentialPassword"));

        for (int i = 0; i<expectedUrls.length; i++){
            Assertions.assertEquals(expectedUrls[i], urls.get(i).getText());
            Assertions.assertEquals(expectedUsernames[i], usernames.get(i).getText());
            Assertions.assertNotEquals(expectedPasswords[i], passwords.get(i).getText());
        }
    }

    public boolean isElementPresent(WebDriver driver, String id){
        try{
            driver.findElement(By.id(id));
            return true;
        }catch(NoSuchElementException e){
            return false;
        }
    }

    //Often repeated helper methods that need Thread.sleep()
    public void clickHome(){
        try{
            Thread.sleep(500);
            new WebDriverWait(this.driver, 15)
                    .until(ExpectedConditions.elementToBeClickable(this.home)).click();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void clickCredentialsTab(){
        try{
            Thread.sleep(500);
            new WebDriverWait(this.driver, 15)
                    .until(ExpectedConditions.elementToBeClickable(credentialTab)).click();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
