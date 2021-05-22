package ru.netology;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class AppCardDeliveryTest {
    Date date = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

    @BeforeEach
    public void setUpDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 3);
        date = cal.getTime();
    }
    public void clearDate(){
        //this hack is here because .clear() doesn't work :(
        $("[data-test-id='date'] .input__control").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.BACK_SPACE);
    }
    @Test
    public void positiveTest() {
        open("http://localhost:9999");
        $("[data-test-id='city'] .input__control").sendKeys("Москва");
        clearDate();
        $("[data-test-id='date'] .input__control").sendKeys(formatter.format(date));
        $("[data-test-id='name'] .input__control").setValue("Аа-бБ вВ");
        $("[data-test-id='phone'] .input__control").setValue("+00000000000");
        $("[data-test-id='agreement']").click();
        $(".button").click();
        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    public void autofillTest() {
        open("http://localhost:9999");
        clearDate();
        $("[data-test-id='date'] .input__control").sendKeys(formatter.format(date));
        $("[data-test-id='name'] .input__control").setValue("Аа-бБ вВ");
        $("[data-test-id='phone'] .input__control").setValue("+00000000000");
        $("[data-test-id='agreement']").click();

        $("[data-test-id='city'] .input__control").sendKeys("Мо");
        $$(".menu-item .menu-item__control").find(exactText("Моска")).click();
        $("[data-test-id='city'] .input__control").shouldHave(attribute("value", "Москва"));

        $(".button").click();
        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
    }

}
