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

public class Note {
    private WebDriver driver;

    @FindBy(id="note-title")
    private WebElement noteTitle;
    @FindBy(id="note-description")
    private WebElement noteDescription;
    @FindBy(id="save")
    private WebElement submitNote;
    @FindBy(id="add-note")
    private WebElement addNote;
    @FindBy(id="nav-notes-tab")
    private WebElement noteTab;
    @FindBy(id="edit-note")
    private WebElement edit;
    @FindBy(id="delete-note")
    private WebElement delete;
    @FindBy(id="home-link")
    private WebElement home;

    public Note(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    void clickAddNote(){
        addNote.click();
    }

    public void submitNote(String noteTitle, String noteDescription){
        WebDriverWait wait = new WebDriverWait(this.driver, 15);
        clickNoteTab();
        wait.until(ExpectedConditions.elementToBeClickable(addNote)).click();
        wait.until(ExpectedConditions.visibilityOf(this.noteTitle)).sendKeys(noteTitle);
        wait.until(ExpectedConditions.visibilityOf(this.noteDescription)).sendKeys(noteDescription);

        wait.until(ExpectedConditions.elementToBeClickable(submitNote)).click();

        clickHome();
    }

    public void editNote(String title, String description){
        WebDriverWait wait = new WebDriverWait(this.driver, 20);
        clickNoteTab();
        wait.until(ExpectedConditions.elementToBeClickable(edit)).click();
        wait.until(ExpectedConditions.visibilityOf(this.noteTitle));
        this.noteTitle.clear();
        this.noteTitle.sendKeys(title);
        this.noteDescription.clear();
        this.noteDescription.sendKeys(description);
        submitNote.click();
        clickHome();
    }

    public void deleteNote(){
        WebDriverWait wait = new WebDriverWait(this.driver, 20);
        clickNoteTab();
        wait.until(ExpectedConditions.elementToBeClickable(delete)).click();
        clickHome();
        clickNoteTab();
    }

    public boolean isElementPresent(String id){
        try{
            this.driver.findElement(By.id(id));
            return true;
        }catch(NoSuchElementException e){
            return false;
        }
    }

    public void verifyNote(String noteTitle, String noteDescription){
        WebDriverWait wait = new WebDriverWait(this.driver, 20);

        clickNoteTab();

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("noteTitle"))));

        WebElement title = driver.findElement(By.id("noteTitle"));
        WebElement description = driver.findElement(By.id("noteDescription"));

        Assertions.assertEquals(noteTitle, title.getText());
        Assertions.assertEquals(noteDescription, description.getText());
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

    public void clickNoteTab(){
        try{
            Thread.sleep(500);
            new WebDriverWait(this.driver, 15)
                    .until(ExpectedConditions.elementToBeClickable(noteTab)).click();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
