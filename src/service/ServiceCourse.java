package service;

import model.CourseCurrency;

import java.util.List;

public interface ServiceCourse {

    //получить курсы (валют)
    List<CourseCurrency> getCourses();

    //добавить новый курс (валюту)
    CourseCurrency addCourse(CourseCurrency courseCurrency);

    //получить последний курс (валюты)
    CourseCurrency getCourseLast();

    //получить название основной валюты
    String getCurrencyMain();

    //добавить новую валюту
    boolean addNewCurrency(String name, String fullName, double cursCurrency);
}
