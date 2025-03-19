package utils;

public class ValidatorException extends Exception {

    //конструктор
    public ValidatorException(String message) {
        super(message);
    }


    @Override
    public String getMessage() {
        return "\u001B[91mОшибка: \u001B[0m" + super.getMessage();
    }
}