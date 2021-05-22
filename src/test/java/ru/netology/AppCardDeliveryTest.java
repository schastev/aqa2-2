package ru.netology;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AppCardDeliveryTest {

    @Test
    public void positiveTest() {
        open("http://localhost:9999");
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        cal.add(Calendar.DATE, 3);
        date = cal.getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        $("[data-test-id='city'] .input__control").sendKeys("Москва");
        //this hack is here because .clear() doesn't work :(
        $("[data-test-id='date'] .input__control").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.BACK_SPACE);
        //end of hack
        $("[data-test-id='date'] .input__control").sendKeys(formatter.format(date));
        $("[data-test-id='name'] .input__control").setValue("Аа-бБ вВ");
        $("[data-test-id='phone'] .input__control").setValue("+00000000000");
        $("[data-test-id='agreement']").click();
        $(".button").click();
        $(withText("Успешо!")).shouldBe(visible, Duration.ofSeconds(15));
    }


}
