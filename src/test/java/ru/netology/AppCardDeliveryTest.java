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
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppCardDeliveryTest {
    Date date = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
    Calendar cal = Calendar.getInstance();

    @BeforeEach
    public void setUpDate() {
        cal.setTime(date);
    }

    public Date datePlusThree() {
        cal.add(cal.DATE, 3);
        date = cal.getTime();
        return date;
    }

    public void clearDate() {
        //this hack is here because .clear() doesn't work :(
        $("[data-test-id='date'] .input__control").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.BACK_SPACE);
    }

    @Test
    public void positiveTest() {
        open("http://localhost:9999");
        $("[data-test-id='city'] .input__control").sendKeys("Москва");
        clearDate();
        datePlusThree();
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
        datePlusThree();
        //fill in all irrelevant fields
        $("[data-test-id='date'] .input__control").sendKeys(formatter.format(date));
        $("[data-test-id='name'] .input__control").setValue("Аа-бБ вВ");
        $("[data-test-id='phone'] .input__control").setValue("+00000000000");
        $("[data-test-id='agreement']").click();

        $("[data-test-id='city'] .input__control").sendKeys("Мо");
        $$(".menu-item .menu-item__control").find(exactText("Москва")).click();
        $("[data-test-id='city'] .input__control").shouldHave(attribute("value", "Москва"));
        $(".button").click();
        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    public void plusWeekTest() {
        open("http://localhost:9999");
        //fill in all irrelevant fields
        $("[data-test-id='city'] .input__control").sendKeys("Москва");
        $("[data-test-id='name'] .input__control").setValue("Аа-бБ вВ");
        $("[data-test-id='phone'] .input__control").setValue("+00000000000");
        $("[data-test-id='agreement']").click();
        $("[data-test-id='date'] .input__control").click();

        Calendar newCal = Calendar.getInstance();//get a new calendar to track the new date
        newCal.add(newCal.DATE, 7);
        String newDate = newCal.get(newCal.DAY_OF_MONTH) + "";
        if (cal.get(cal.MONTH) != newCal.get(newCal.MONTH)) {//click the right arrow if the new and old months don't match
            $$(".calendar__arrow_direction_right").last().click();
        }

        //set up the check of the new month's number
        $$(".calendar__row .calendar__day[data-day]").find(exactText(newDate)).click();//set the new date
        String actual = $("[data-test-id='date'] .input__control").getValue().substring(3,5);//get the numeric value of the new month
        int temp = newCal.get(newCal.MONTH) + 1; //get the numeric value of the expected month (zero-indexed)
        String expected = temp + ""; //convert it to string
        if (temp < 10) {//pad with a zero if September or earlier
            expected = "0" + expected;
        }

        $(".button").click();
        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        assertEquals(actual, 8);
    }
}
