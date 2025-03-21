package utils;

import model.Check;
import model.User;
import service.ServiceUser;

import java.util.List;
import java.util.Optional;

public class Validator {

    //инициализация
    static ServiceUser serviceUser;

    //конструктор
    public Validator(ServiceUser serviceUser) {
        this.serviceUser = serviceUser;
    }


    public static void isEmailValid(String email) throws ValidatorException {
        int index = email.indexOf("@");
        if(index == -1 || index != email.lastIndexOf("@"))
            throw new ValidatorException("\u001B[91mДолжна быть '@'\u001B[0m");
        if(email.indexOf(".", index + 2) == -1)
            throw new ValidatorException("\u001B[91mДолжна быть '.' после '@'\u001B[0m");
        if(email.lastIndexOf(".") >= email.length() - 2)
            throw new ValidatorException("\u001B[91mПосле последней точки должно быть минимум 2 символа\u001B[0m");
        for (int i = 0; i < email.length(); i++){
            char ch = email.charAt(i);
            if(!(Character.isAlphabetic(ch)
                    || Character.isDigit(ch)
                    || ch == '_'
                    || ch == '-'
                    || ch == '.'
                    || ch == '@')){
                throw new ValidatorException("\u001B[91mСимвол не подходит\u001B[0m");
            }
        }
        if(index == 0 ) throw new ValidatorException("\u001B[91mДо '@' должен быть хотя бы 1 символ\u001B[0m");
        if(!Character.isAlphabetic(email.charAt(0)))
            throw new ValidatorException("\u001B[91mПервый символ должен быть буквой\u001B[0m");
    }


    public static void isPasswordValid(String password)throws ValidatorException {
        String special = "! % @ $ & * () []";
        boolean isLow = false;
        boolean isUpp = false;
        boolean isDig = false;
        boolean isSpec = false;
        char ch;
        if (password == null || password.length() < 8) {
            throw new ValidatorException("\u001B[91mНеправильная длина пароля\u001B[0m");
        }
        for (int i = 0; i < password.length(); i++) {
            ch = password.charAt(i);
            if (Character.isLowerCase(ch)) isLow = true;
            if (Character.isUpperCase(ch)) isUpp = true;
            if (Character.isDigit(ch)) isDig = true;
            if (special.indexOf(ch) != -1) isSpec = true;
        }

        if (!isLow)
            throw new ValidatorException("\u001B[91mPassword должен содержать хотя бы одну строчную букву\u001B[0m");
        if (!isUpp)
            throw new ValidatorException("\u001B[91mPassword должен содержать хотя бы одну заглавную букву\u001B[0m");
        if (!isDig)
            throw new ValidatorException("\u001B[91mPassword должен содержать хотя бы одну цифру\u001B[0m");
        if (!isSpec)
            throw new ValidatorException("\u001B[91mPassword должен содержать хотя бы один специальный символ\u001B[0m");

    }


    public static void isUserPresentByEmail_registerUser(Optional<User> optUser,String email) throws ValidatorException {
        if(optUser.isPresent()) {
            throw new ValidatorException("\u001B[91mПользователь с Email: \u001B[0m'" + email
                    + "'\u001B[91m уже существует!\u001B[0m");
        }
    }

    public static void isUserExistById(Optional<User> optUser, int idUser) throws ValidatorException {
        if(optUser.isEmpty() || optUser.get() == null) {
            throw new ValidatorException("\u001B[91mПользователь с ID: \u001B[0m'" + idUser
                    + "'\u001B[91m не существует!\u001B[0m");
        }
    }

    public static void getUserChecks(Optional<List<Check>> optUserChecks, int idUser) throws ValidatorException {
        if(optUserChecks.isEmpty() || optUserChecks.get().size() == 0) {
            String s;
            if(serviceUser.getActivUser().getIdUser() == idUser) {
                s = "\u001B[91mНет счетов!\u001B[0m";
            } else {
                s = "\u001B[91mУ Пользователя: \u001B[0m'" + serviceUser.getUserById(idUser).getFirstName() + " "
                        + serviceUser.getUserById(idUser).getLastName() + "'\u001B[91m нет счетов!\u001B[0m";
            }
            throw new ValidatorException(s);
        }
    }

    public static void getCheckByIdUserIdCheck(Optional<Check> optCheck, int idUser, int idCheck) throws ValidatorException {
        String s;
        if(optCheck.isEmpty() || optCheck.get() == null) {
            s = "\u001B[91mОтсутствует счет: \u001B[0m'" + idCheck + "'";
            throw new ValidatorException(s);
        }
    }

    public static void getUserById( Optional<User> optUser, int idUser) throws ValidatorException {
        String s;
        if (optUser.isEmpty() || optUser.get() == null) {
            s = "\u001B[91mОтсутствует пользователь с ID: \u001B[0m'" + idUser + "'";
            throw new ValidatorException(s);
        }
    }

    public static void checkStatus_false( Optional<Check> optCheck, int idUser, int idCheck) throws ValidatorException{
        if(optCheck.get().isStatus() == false) {
            String s = "\u001B[91mУ пользователя: \u001B[0m'";
            String s1 = serviceUser.getUserById(idUser).getFirstName() + " ";
            String s2 = serviceUser.getUserById(idUser).getLastName() + "'\u001B[91m  Счет: \u001B[0m'" + idCheck
                    + "'\u001B[91m уже закрыт!\u001B[0m";
            throw new ValidatorException(s + s1 + s2);
        }
    }

    public static void checkStatus_true( Optional<Check> optCheck, int idUser, int idCheck) throws ValidatorException{
        if(optCheck.get().isStatus() == true) {
            String s = "\u001B[33mУ пользователя: \u001B[0m'";
            String s1 = serviceUser.getUserById(idUser).getFirstName() + " ";
            String s2 = serviceUser.getUserById(idUser).getLastName() + "'\u001B[33m  Счет: \u001B[0m'" + idCheck
                    + "'\u001B[33m уже открыт!\u001B[0m";
            throw new ValidatorException(s + s1 + s2);
        }
    }

    public static void summaCheck( Optional<Check> optCheck, double summa) throws ValidatorException {
        if (optCheck.get().getSumma() < summa) {
            throw new ValidatorException("\u001B[91mНедостаточно средств на счету!\u001B[0m");
        }
    }

    public static void isChecksEquals(int idCheck1, int idCheck2) throws ValidatorException {
        if(idCheck1 == idCheck2) {
            throw new ValidatorException("\u001B[91mОперация не выполнена!\u001B[0m");
        }
    }

    public static void isSummaLessZero(double summa) throws ValidatorException {
        if(summa < 0){
            throw new ValidatorException("\u001B[91mСумма не может быть меньше '0'\u001B[0m");
        }
    }

    public static void isCheckPresentByUser(String currencyName1, String currencyName2, int idUser ) throws ValidatorException{
        if(currencyName1.equals(currencyName2)){
            throw new ValidatorException("\u001B[33mУ пользователя: \u001B[0m'" + serviceUser.getUserById(idUser).getFirstName() +
                    " " + serviceUser.getUserById(idUser).getLastName() + "'\u001B[33m  Счет в Валюте: \u001B[0m'" +
                    currencyName1 + "'\u001B[33m уже открыт!\u001B[0m");
        }
    }

    public static void isCurrencyPresent(String currencyName1, String currencyName2 ) throws ValidatorException {
        if(currencyName1.equals(currencyName2)){
            throw new ValidatorException("\u001B[33mВалюта: \u001B[0m'" + currencyName1
                    + "'\u001B[33m в списке уже существует!\u001B[0m");
        }
    }

    public static void  isDeleteMe(int idUser) throws ValidatorException {
        if(serviceUser.getActivUser().getIdUser() == idUser){
            throw new ValidatorException("\u001B[91mОперация не выполнена!\u001B[0m");
        }
    }
}
