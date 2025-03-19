package service;

import model.CourseCurrency;
import model.User;
import repository.CheckRepository;
import repository.CourseRepository;
import repository.TransactionRepository;
import repository.UserRepository;
import utils.Validator;
import utils.ValidatorException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ServiceCourseImpl implements ServiceCourse {
    //инициализация
    private final CourseRepository courseRepository;

    //конструктор
    public ServiceCourseImpl(CourseRepository courseRepository ) {
        this.courseRepository = courseRepository;
    }


    public List<CourseCurrency> getCourses(){//COURSE
        return courseRepository.getCourses();
    }

    public CourseCurrency addCourse(CourseCurrency courseCurrency){
        courseRepository.addCourse(courseCurrency);
        return courseCurrency;
    }

    public CourseCurrency getCourseLast(){
        return courseRepository.getCourseLast();
    }

    public String getCurrencyMain(){
        return courseRepository.getCurrencyMain();
    }

    public boolean addNewCurrency(String name, String fullName, double cursCurrency){
        if(name == null) return false;
        if(name.length() == 0) return false;
        if(fullName == null) return false;
        if(fullName.length() == 0) return false;
        Optional<Map<String,String>> optMapNames = Optional.ofNullable(courseRepository.getNamesCurrency());
        if(optMapNames.isPresent()) {
            for (Map.Entry<String,String> entry : optMapNames.get().entrySet()) {
                try {
                    Validator.isCurrencyPresent(entry.getKey(),name);
                } catch (ValidatorException e) {
                    System.out.println(e.getMessage());
                    return false;
                }
            }
        }
        courseRepository.addNameCurrency(name, fullName);
        CourseCurrency cc = courseRepository.getCourseLast();
        Optional<Map<String,Double>> optCourses = Optional.ofNullable(cc.getCourse());
        Optional<Map<String,String>> optCoursesFillName = Optional.ofNullable(cc.getCourseFillName());
        if(!optCourses.isEmpty() && !optCoursesFillName.isEmpty()) {
            optCourses.get().put(name, cursCurrency);
            optCoursesFillName.get().put(name, fullName);
        }
        return true;
    }
}
