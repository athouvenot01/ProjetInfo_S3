
import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.html.testbench.ParagraphElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.BrowserTest;
import com.vaadin.testbench.BrowserTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.Keys;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author loicrosian
 */
public class UIIT extends BrowserTestBase {
    
    private static String getDeploymentHostname() {
        String hostname = System.getenv("HOSTNAME");
        if (hostname != null && !hostname.isEmpty()) {
            return hostname;
        }
        return "localhost";
    }
    
     @BeforeEach
    public void open() {
        getDriver().get("http://"+getDeploymentHostname()+":8080/");
    }

    @BrowserTest
    public void clickingButtonShowsNotification() {
        Assertions.assertFalse($(ParagraphElement.class).exists());
        $(ButtonElement.class).waitForFirst().click();
        Assertions.assertTrue($(ParagraphElement.class).exists());
    }

    @BrowserTest
    public void clickingButtonTwiceShowsTwoNotifications() {
        Assertions.assertFalse($(ParagraphElement.class).exists());
        ButtonElement button = $(ButtonElement.class).waitForFirst();
        button.click();
        button.click();
        $(ParagraphElement.class).waitForFirst();
        Assertions.assertEquals(2, $(ParagraphElement.class).all().size());
    }

    @BrowserTest
    public void testClickButtonShowsHelloAnonymousUserNotificationWhenUserNameIsEmpty() {
        ButtonElement button = $(ButtonElement.class).waitForFirst();
        button.click();
        ParagraphElement msg = $(ParagraphElement.class).waitForFirst();
        Assertions.assertEquals("Hello anonymous user", msg.getText());
    }

    @BrowserTest
    public void testClickButtonShowsHelloUserNotificationWhenUserIsNotEmpty() {
        TextFieldElement textField = $(TextFieldElement.class).waitForFirst();
        textField.setValue("Vaadiner");
        $(ButtonElement.class).waitForFirst().click();
        ParagraphElement msg = $(ParagraphElement.class).waitForFirst();
        Assertions.assertEquals("Hello Vaadiner", msg.getText());
    }

    @BrowserTest
    public void testEnterShowsHelloUserNotificationWhenUserIsNotEmpty() {
        TextFieldElement textField = $(TextFieldElement.class).waitForFirst();
        textField.setValue("Vaadiner");
        textField.sendKeys(Keys.ENTER);
        ParagraphElement msg = $(ParagraphElement.class).waitForFirst();
        Assertions.assertEquals("Hello Vaadiner", msg.getText());
    }
}
