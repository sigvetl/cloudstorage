package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UserTest {
    private WebDriver driver;

    //signup
    @FindBy(id="inputFirstName")
    private WebElement firstName;

    @FindBy(id="inputLastName")
    private WebElement lastName;

    @FindBy(id="inputUsername")
    private WebElement username;

    @FindBy(id="inputPassword")
    private WebElement password;

    @FindBy(id="signup-submit")
    private WebElement signupButton;

    @FindBy(id="login-link")
    private WebElement loginLink;

    //login
    @FindBy(id="inputUsername")
    private WebElement loginUsername;

    @FindBy(id="inputPassword")
    private WebElement loginPassword;

    @FindBy(id="login-submit")
    private WebElement loginButton;

    @FindBy(id="signup-link")
    private WebElement signupLink;

    //logout
    @FindBy(id="logout-button")
    private WebElement logout;

    public UserTest(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void signup(String firstName, String lastName, String username, String password){
        try{
            Thread.sleep(500);
            new WebDriverWait(this.driver, 15)
                    .until(ExpectedConditions.elementToBeClickable(this.signupButton));
            this.firstName.sendKeys(firstName);
            this.lastName.sendKeys(lastName);
            this.username.sendKeys(username);
            this.password.sendKeys(password);
            signupButton.click();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void login(String username, String password){
        try{
            Thread.sleep(500);
            new WebDriverWait(this.driver, 15)
                    .until(ExpectedConditions.elementToBeClickable(this.loginButton));
            loginUsername.sendKeys(username);
            loginPassword.sendKeys(password);
            loginButton.click();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void loginPage(){
        try{
            Thread.sleep(500);
            new WebDriverWait(this.driver, 15)
                    .until(ExpectedConditions.elementToBeClickable(this.loginLink))
                    .click();
            Thread.sleep(500);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void signupPage(){
        try{
            Thread.sleep(500);
            new WebDriverWait(this.driver, 15)
                    .until(ExpectedConditions.elementToBeClickable(this.signupLink))
                    .click();
            Thread.sleep(500);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void logout(){
        try{
            Thread.sleep(500);
            new WebDriverWait(this.driver, 15)
                    .until(ExpectedConditions.elementToBeClickable(this.logout))
                    .click();
            Thread.sleep(500);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void signupAndLogin(String firstName, String lastName, String username, String password){
        this.signupPage();
        this.signup(firstName, lastName, username, password);
        this.loginPage();
        this.login(username, password);
    }
}
