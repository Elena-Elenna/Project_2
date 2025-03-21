package repository;

import model.CourseCurrency;

import java.util.List;
import java.util.Map;

public interface CourseRepository {

    //получить названия валют
    Map<String, String> getNamesCurrency();

    //добавить название валюты
    void addNameCurrency(String newCurrencyName, String newCurrencyFullName);

    //получить курсы (валют)
    List<CourseCurrency> getCourses();

    //получить последний курс (валюты)
    CourseCurrency getCourseLast();

    //получить основную валюту
    String  getCurrencyMain();

    //добавить новый курс (валюту)
    void addCourse(CourseCurrency courseCurrency);

    //получить курс по названию валюты
    double getCourseByCurrencyName(String currencyName);

    //написать курс в файл
    void writeCurseToFile();

    //читать курс из файла
    void readCurseFromFile();

    //написать названия валют в файл ++
    void writeCurseNameToFile();

    //читать названия валют из файла ++
    void readCurseNameFromFile();
}
