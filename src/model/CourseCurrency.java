package model;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class CourseCurrency {

    //поля
    private Map<String, Double> course = new LinkedHashMap<String, Double>();//курс валют (UA-45,0)
    private Map<String, String> courseFillName = new LinkedHashMap<String, String>();//названия валют (UA-Гривня)
    private String currencyMain;//валюта основная
    private LocalDate dateCurrency;//дата курса

    //конструктор
    public CourseCurrency() {
    }


    public Map<String, Double> getCourse() {
        return course;
    }

    public void setCourse(Map<String, Double> course) {
        this.course = course;
    }

    public Map<String, String> getCourseFillName() {
        return courseFillName;
    }

    public void setCourseFillName(Map<String, String> courseFillName) {
        this.courseFillName = courseFillName;
    }

    public LocalDate getDateCurrency() {
        return dateCurrency;
    }

    public void setDateCurrency(LocalDate dateCurrency) {
        this.dateCurrency = dateCurrency;
    }

    public String getCurrencyMain() {
        return currencyMain;
    }

    public void setCurrencyMain(String currencyMain) {
        this.currencyMain = currencyMain;
    }

    @Override
    public String toString() {
        return "CourseCurrency {" +
                "course = " + course +
                "; currencyMain = " + currencyMain +
                "; dateCurrency = " + dateCurrency +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseCurrency that = (CourseCurrency) o;
        return Objects.equals(course, that.course) && Objects.equals(courseFillName, that.courseFillName)
                && Objects.equals(currencyMain, that.currencyMain) && Objects.equals(dateCurrency, that.dateCurrency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(course, courseFillName, currencyMain, dateCurrency);
    }
}
