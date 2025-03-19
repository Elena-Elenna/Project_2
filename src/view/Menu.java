package view;

import model.*;
import repository.CheckRepository;
import repository.CourseRepository;
import repository.TransactionRepository;
import repository.UserRepository;
import service.ServiceCheck;
import service.ServiceCourse;
import service.ServiceUser;
import utils.Validator;
import utils.ValidatorException;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Menu {
    //инициализация
    private final ServiceCheck serviceCheck;
    private final ServiceCourse serviceCourse;
    private final ServiceUser serviceUser;
    private final UserRepository userRepository;
    private final CheckRepository checkRepository;
    private final TransactionRepository transactionRepository;
    private final CourseRepository courseRepository;
    //поля
    private boolean exitAdminMenu;//выход из меню администратора
    private boolean exitUserMenu;//выход из меню пользователя
    private final Scanner scanner = new Scanner(System.in);

    //конструктор
    public Menu(ServiceCheck serviceCheck, ServiceCourse serviceCourse, ServiceUser serviceUser,
                UserRepository userRepository, CheckRepository checkRepository,
                TransactionRepository transactionRepository, CourseRepository courseRepository) {
        this.serviceCheck = serviceCheck;
        this.serviceCourse = serviceCourse;
        this.serviceUser = serviceUser;
        this.userRepository = userRepository;
        this.checkRepository = checkRepository;
        this.transactionRepository = transactionRepository;
        this.courseRepository = courseRepository;
    }

    public void start() {
        InputUser();
    }

    private void showMenu() {
        while(true) {
            System.out.println("\u001B[92m\nМЕНЮ 'ОБМЕНА ВАЛЮТ':\u001B[0m");
            System.out.println("\u001B[93m   1. Меню пользователя\u001B[0m");
            System.out.println("\u001B[93m   2. Меню администратора\u001B[0m");
            System.out.println("\u001B[93m   0. logout\u001B[0m");
            System.out.println((serviceUser.getActivUser().getRole() == Role.ADMIN ? "\u001B[32mАдминистратор: \u001B[0m'"
                    : "\u001B[32mПользователь: \u001B[0m'") + serviceUser.getActivUser().getFirstName() + " "
                    + serviceUser.getActivUser().getLastName() + "'");
            int choice = inputChoiceInt(0, 2, "\u001B[36m\nСделайте выбор: \u001B[0m");
            if (choice == 0) {
                serviceUser.logout();
                break;
            }
            showMenuCase(choice);
        }
    }

    private void showMenuCase(int choice) {
        switch (choice) {
            case 0:
                serviceUser.logout();
                break;
            case 1:
                showUserMenu();
                break;
            case 2:
                if (serviceUser.getActivUser().getRole() == Role.ADMIN) {
                    showAdminMenu();
                } else {
                    System.out.println("\u001B[91mВы не являетесь 'Администратором!'\u001B[0m");
                }
                break;
            default:
                System.out.println("Сделайте корректный выбор...");
                waitRead();
        }
    }

    private void waitRead() {
        System.out.println("\u001B[36m\nДля продолжения нажмите Enter...\u001B[0m");
        scanner.nextLine();
    }

    private boolean authorizationUser() {
        String email;
        String password;
        System.out.print("\u001B[36mВведите Email: \u001B[0m");
        email = scanner.nextLine().trim();
        if (email.length() == 0 || email == null  ) {
            System.out.println("\u001B[91mАвторизация провалена!\u001B[0m");
            return false;
        }
        if (serviceUser.isEmailExist(email) == false) {
            System.out.println("\u001B[33mПользователя с таким Email: \u001B[0m" + email
                    + "\u001B[33m не существует!\u001B[0m");
            return false;
        }
        if (serviceUser.isUserBlocked(email) == true) {
            System.out.println("\u001B[33m Пользоватеть заблокирован!\u001B[0m");
            return false;
        }
        System.out.print("\u001B[36mВведите пароль: \u001B[0m");
        password = scanner.nextLine();
        return   serviceUser.loginUser(email, password);
    }

    private boolean registrationUser() {
        String email;
        String password;
        String str1;
        String str2;
        boolean register;
        System.out.print("\n\u001B[36mВведите Email пользователя: \u001B[0m");
        email = scanner.nextLine().trim();
        if (email.length() == 0 || email == null ) {
            System.out.println("\u001B[91mАвторизация провалена!\u001B[0m");
            return false;
        }
        if (serviceUser.isEmailExist(email) == true) {
            System.out.println("\u001B[31mПользователь с Email: \u001B[0m" + email
                    + "\u001B[31m уже существует!\u001B[0m");
            return false;
        }
        if(serviceUser.isEmailValid(email) == false) {
            System.out.println("\u001B[91mEmail: \u001B[0m" + email
                    + "\u001B[91m не валидный!\u001B[0m");
            return false;
        }
        System.out.print("\u001B[36mВведите пароль пользователя: \u001B[0m");
        password = scanner.nextLine().trim();
        if(serviceUser.isPasswordValid(password) == false) {
            System.out.println("\u001B[91mПароль: \u001B[0m" + password
                    + "\u001B[91mне валидный!\u001B[0m");
            return false;
        }
        System.out.print("\u001B[36mВведите Имя пользователя: \u001B[0m");
        str1 = scanner.nextLine().trim();
        System.out.print("\u001B[36mВведите Фамилию пользователя: \u001B[0m");
        str2 = scanner.nextLine().trim();
        register = serviceUser.registerUser(str1, str2, email, password);
        if (register == true){
            System.out.println("\u001B[33mПользователь: \u001B[0m'" + str1 + " " + str2 +
                    "'\u001B[33m  c Email: \u001B[0m" + email + "\u001B[33m успешно зарегистрирован\u001B[0m");
            return true;
        } else {
            System.out.println("\u001B[91mРегистрация провалена!\u001B[0m");
        }
        return false;
    }


    private int InputUser() {
        boolean isAutorized = false;
        boolean isRegistration = false;
        while (true) {
            System.out.println("\u001B[92m\nДОБРО ПОЖАЛОВАТЬ В 'ОБМЕН ВАЛЮТ!'\u001B[0m");
            System.out.println("\u001B[93m    1. Авторизация\u001B[0m");
            System.out.println("\u001B[93m    0. Выход\u001B[0m");
            int input = inputChoiceInt(0,1,"\u001B[36m\nСделайте выбор: \u001B[0m");
            switch (input) {
                case 0:
                    userRepository.writeUsersToFile();
                    checkRepository. writeChecksToFile();
                    transactionRepository.writeTransactionToFile();
                    courseRepository.writeCurseToFile();
                    courseRepository.writeCurseNameToFile();
//                    System.out.println("До свидания!");
                    System.exit(0);
                    break;
                case 1:
                    isAutorized = authorizationUser();
                    if (isAutorized == true ) {
                        showMenu();
                    } else {
                        System.out.println("\u001B[91mОшибка авторизации!\u001B[0m");
                    }
                    break;
                case 2:
                    isRegistration = registrationUser();
                    if (isRegistration == true ) {
                        showMenu();
                    } else {
                        System.out.println("\u001B[91mОшибка регистрации!\u001B[0m");
                    }
                    break;
                default:
                    System.out.println("Сделайте корректный выбор...");
            }
        }
    }

    private void showUserMenu() {
        while(true) {
            System.out.println("\u001B[92m\nМЕНЮ ПОЛЬЗОВАТЕЛЯ:\u001B[0m");
            System.out.println("\u001B[93m    1. Состояние счета\u001B[0m");
            System.out.println("\u001B[93m    2. Снятие денег\u001B[0m");
            System.out.println("\u001B[93m    3. Внесение денег\u001B[0m");
            System.out.println("\u001B[93m    4. Перевод денег\u001B[0m");
            System.out.println("\u001B[93m    5. История транзакций\u001B[0m");
            System.out.println("\u001B[93m    6. Курс Валют\u001B[0m");
            System.out.println("\u001B[93m    0. Возврат в предыдущее меню\u001B[0m");
            int choice = inputChoiceInt(0,6,"\u001B[36m\nСделайте выбор: \u001B[0m");
            if (choice == 0 || this.exitUserMenu) {
                return;
            }
            this.showUserMenuCase(choice);
        }
    }

    private void showUserMenuCase(int input) {
        int numberCheck = -1;
        int numberCheck2 = -1;
        int idUserTransfer = 0;
        int checkSize = 0;
        double tempSumma = 0.0;
        boolean isTakeMoney = false;
        boolean isDepositMoney = false;
        boolean isTransferMoney = false;
        switch (input) {
            case 0:
                exitUserMenu = true;
                System.out.println("Вы вышли из МЕНЮ ПОЛЬЗОВАТЕЛЯ.");
                break;
            case 1:// Состояние счетов
                System.out.println("\u001B[93m\nСОСТОЯНИЕ СЧЕТА:\u001B[0m");
                printCheckList(serviceUser.getActivUser(),"\u001B[33mСписок счетов: \u001B[0m");
                break;
            case 2: // Снятие денег
                System.out.println("\u001B[93m\nСНЯТИЕ ДЕНЕГ:\u001B[0m");
                if(serviceUser.getActivUser().getRole() == Role.BLOCKED_TRANSACTION) {
                    System.out.println("\u001B[31mОперация не выполнена!\u001B[0m");
                    break;
                }
                checkSize = printCheckList(serviceUser.getActivUser(),"\u001B[33mСписок ваших счетов: \u001B[0m");
                if(checkSize == 0) {
                    break;
                }
                System.out.println("\u001B[92m0 - отмена\u001B[0m");
                numberCheck = inputChoiceInt(0, checkSize,
                        "\u001B[36mВведите номер счета для снятия денег: \u001B[0m");
                if (numberCheck == 0) break;
                Check check = serviceCheck.getCheckByIdUserIdCheck(serviceUser.getActivUser().getIdUser(), numberCheck);
                if(check.isStatus() == false) {
                    System.out.println("\u001B[31mОперация не выполнена! Ваш счет закрыт!\u001B[0m");
                    break;
                }
                tempSumma = inputChoiceDoubl(0, "\u001B[36mВведите сумму: \u001B[0m");
                isTakeMoney = serviceCheck.takeMoney(serviceUser.getActivUser(), numberCheck, tempSumma);
                if (isTakeMoney == true) {
                    System.out.println("\u001B[33mОперация успешно выполнена!\u001B[0m");
                }
//                else {
//                    System.out.println("\u001B[31mОперация не выполнена!\u001B[0m");
//                }
                break;
            case 3://Внесение денег
                System.out.println("\u001B[93m\nВНЕСЕНИЕ ДЕНЕГ:\u001B[0m");
                if(serviceUser.getActivUser().getRole() == Role.BLOCKED_TRANSACTION) {
                    System.out.println("\u001B[31mОперация не выполнена!\u001B[0m");
                    break;
                }
                checkSize = printCheckList(serviceUser.getActivUser(),"\u001B[33mСписок Ваших счетов: \u001B[0m");
                if(checkSize == 0) {
                    break;
                }
                System.out.println("\u001B[92m0 - отмена\u001B[0m");
                numberCheck = inputChoiceInt(0, checkSize,
                        "\u001B[36mВведите номер счета для зачисления денег: \u001B[0m");
                if (numberCheck == 0) break;
                check = serviceCheck.getCheckByIdUserIdCheck(serviceUser.getActivUser().getIdUser(), numberCheck);
                if(check.isStatus() == false) {
                    System.out.println("\u001B[31mОперация не выполнена! Счет закрыт!\u001B[0m");
                    break;
                }
                tempSumma = inputChoiceDoubl(0, "\u001B[36mВведите сумму денег: \u001B[0m");
                isDepositMoney = serviceCheck.depositMoney(serviceUser.getActivUser(), numberCheck, tempSumma);
                if (isDepositMoney == true) {
                    System.out.println("\u001B[33mОперация успешно выполнена!\u001B[0m");
                } else {
                    System.out.println("\u001B[31mОперация не выполнена!\u001B[0m");
                }
                break;
            case 4://Перевод денег
                System.out.println("\u001B[93m\nПЕРЕВОД ДЕНЕГ:\u001B[0m");
                if(serviceUser.getActivUser().getRole() == Role.BLOCKED_TRANSACTION) {
                    System.out.println("\u001B[31mОперация не выполнена!\u001B[0m");
                    break;
                }
                if(serviceCheck.getUserChecks(serviceUser.getActivUser().getIdUser()) == null) {
                    break;
                }
                int choice = inputChoiceInt(0, 2,
                        "\u001B[95m\n1. На свой счет\n2. На счет другого пользователя\n0. Отмена\u001B[0m\u001B[36m\nВаш выбор: \u001B[0m");
                if (choice == 0) break;
                checkSize = printCheckList(serviceUser.getActivUser(),"\u001B[33mСписок Ваших счетов: \u001B[0m");
                System.out.println("\u001B[92m0 - отмена\u001B[0m");
                numberCheck = inputChoiceInt(0, checkSize,
                        "\u001B[36mВведите номер Вашего счета, с которого будут сняты деньги: \u001B[0m");
                if (numberCheck == 0) break;
                check = serviceCheck.getCheckByIdUserIdCheck(serviceUser.getActivUser().getIdUser(), numberCheck);
                if(check.isStatus() == false) {
                    System.out.println("\u001B[31mОперация не выполнена! Счет: \u001B[0m'" + numberCheck
                            + "'\u001B[31m закрыт!\u001B[0m");
                    break;
                }
                tempSumma = inputChoiceDoubl(0,
                        "\u001B[36mВведите сумму для перевода денежных средств: \u001B[0m");
                if(choice == 1) {
                    numberCheck2 = inputChoiceInt(1, checkSize,
                            "\u001B[36mСчет для зачисления средств: \u001B[0m");
                    Check check2 = serviceCheck.getCheckByIdUserIdCheck(serviceUser.getActivUser().getIdUser(),numberCheck2);
                    if(check2.isStatus() == false) {
                        System.out.println("\u001B[31mОперация не выполнена! Счет: \u001B[0m'"
                                + numberCheck2 + "'\u001B[31m закрыт!\u001B[0m");
                        break;
                    }
                    isTransferMoney = serviceCheck.transferMoneyToMe(serviceUser.getActivUser(), numberCheck,
                            numberCheck2, tempSumma);
                    if(isTransferMoney == true) {
                        Check chec = serviceCheck.getCheckByIdUserIdCheck(serviceUser.getActivUser().getIdUser(),numberCheck);
                        System.out.println("\u001B[33mСумма: \u001B[0m" + tempSumma + chec.getCurrencyName() +
                                "\u001B[33m  успешно переведена на Ваш счет: \u001B[0m'" +  numberCheck2 + "'");
                    }
                }
                if(choice == 2) {
                    printUsers(serviceUser.getActivUser());
                    idUserTransfer = inputIdUser("\u001B[36mВведите ID пользователя для перевода средств: \u001B[0m");
                    if(serviceUser.getUserById(idUserTransfer).getRole() == Role.BLOCKED_TRANSACTION) {
                        System.out.println("\u001B[31mОперация не выполнена!\u001B[0m");
                        break;
                    }
                    Optional<List<Check>> userTransfChecks = Optional.ofNullable(serviceCheck.getUserChecks(idUserTransfer));
                    if(userTransfChecks.isEmpty() || userTransfChecks.get().size() == 0) {
                        System.out.println("\u001B[33mУ пользователя с ID: \u001B[0m'" + idUserTransfer
                                + "'\u001B[33m - нет счетов!\u001B[0m");
                        break;
                    }
                    int checkSize2 = printCheckList(serviceUser.getUserById(idUserTransfer),
                            "\u001B[33mСписок счетов пользователя: \u001B[0m'" + serviceUser.getUserById(idUserTransfer).getFirstName() +
                                    " " + serviceUser.getUserById(idUserTransfer).getLastName() + "' : ");
                    System.out.println("\u001B[92m0 - отмена\u001B[0m");
                    numberCheck2 = inputChoiceInt(0, checkSize2,
                            "\u001B[36mВведите номер счета получателя: \u001B[0m");
                    if (numberCheck2 == 0) break;
                    Check check3 = serviceCheck.getCheckByIdUserIdCheck(idUserTransfer, numberCheck2);
                    if(check3.isStatus() == false) {
                        System.out.println("\u001B[31mОперация не выполнена!\u001B[0m");
                        break;
                    }
                    User userRecipient = serviceUser.getUserById(idUserTransfer);
                    isTransferMoney = serviceCheck.transferMoneyToUser(serviceUser.getActivUser(),
                            userRecipient, numberCheck, numberCheck2, tempSumma);
                    if(isTransferMoney == true ) {
                        Check chec = serviceCheck.getCheckByIdUserIdCheck(serviceUser.getActivUser().getIdUser(), numberCheck);
                        System.out.println("\u001B[33mСумма: \u001B[0m" + tempSumma + chec.getCurrencyName() +
                                "\u001B[33m  успешно переведена на счет пользователя: \u001B[0m" + userRecipient.getFirstName() +
                                " " + userRecipient.getLastName());
                    }else {
                        System.out.println("\u001B[31mОперация не выполнена!\u001B[0m");
                    }
                }
                break;
            case 5://История Транзакций
                System.out.println("\u001B[93m\nИСТОРИЯ ТРАНЗАКЦИИ:\u001B[0m");
                printTransactionUser(serviceUser.getActivUser());
                break;
            case 6://Курсы Валют
                System.out.println("\n");
                Optional<CourseCurrency> optCourseLast = Optional.ofNullable(serviceCourse.getCourseLast());
                if(optCourseLast.isEmpty() || optCourseLast.get() == null) {
                    System.out.println("\u001B[31mКурс валют отсутствует!\u001B[0m");
                    break;
                }
                System.out.println("\u001B[93mКУРС ВАЛЮТ НА: \u001B[0m" + optCourseLast.get().getDateCurrency());
                System.out.println("\u001B[33mОсновная валюта: \u001B[0m'" + optCourseLast.get().getCurrencyMain() + "'");
                Map<String ,Double> course = optCourseLast.get().getCourse();
                Map<String,String> courseFullName = optCourseLast.get().getCourseFillName();
                String nameCurr = "";
                for (Map.Entry<String,Double> entry : course.entrySet()) {
                    nameCurr = courseFullName.get(entry.getKey());
                    //   System.out.println(nameCurr+" "+entry.getKey()+" : "+entry.getValue());
                    System.out.println("\u001B[35mВалюта: \u001B[0m'" + nameCurr + "'" + " ".repeat(23 - nameCurr.length())
                            + entry.getKey() + "\u001B[35m Курс -> \u001B[0m" + entry.getValue());
                }
                break;
            default:
                System.out.println("Сделайте корректный выбор...");
        }
        waitRead();
    }

    private void showAdminMenu() {
        exitAdminMenu = false;
        while(true) {
            System.out.println("\u001B[92m       МЕНЮ АДМИНИСТРАТОРА:\u001B[0m");
            System.out.println("\u001B[93m  1. Регистрация нового пользователя\u001B[0m");
            System.out.println("\u001B[93m  2. Изменить пароль пользователя\u001B[0m");
            System.out.println("\u001B[93m  3. Изменить статус пользователя\u001B[0m");
            System.out.println("\u001B[93m  4. Открыть новый счет пользователю\u001B[0m");
            System.out.println("\u001B[93m  5. Заблокировать счет пользователя\u001B[0m");
            System.out.println("\u001B[93m  6. Разблокировать счет пользователя\u001B[0m");
            System.out.println("\u001B[93m  7. Список всех пользователей\u001B[0m");
            System.out.println("\u001B[93m  8. Остаток на счету пользователя\u001B[0m");
            System.out.println("\u001B[93m  9. История транзакций пользователя\u001B[0m");
            System.out.println("\u001B[93m 10. Удалить пользователя\u001B[0m");
            System.out.println("\u001B[93m 11. Курс Валют\u001B[0m");
            System.out.println("\u001B[93m 12. Изменение курса валют\u001B[0m");
            System.out.println("\u001B[93m 13. История курса валют\u001B[0m");
            System.out.println("\u001B[93m 14. Добавить новую валюту\u001B[0m");
            System.out.println("\u001B[93m  0. Возврат в предыдущее меню\u001B[0m");
            int input = inputChoiceInt(0,14,"\u001B[36m\nСделайте выбор: \u001B[0m");
            showAdminMenuCase(input);
            if (exitAdminMenu || input == 0) {
                break;
            }
            waitRead();
        }
    }

    private void showAdminMenuCase(int input) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        int num = 0;
        switch (input) {
            case 0:
                exitAdminMenu = true;
                System.out.println("Вы вышли из МЕНЮ АДМИНИСТРАТОРА.");
                break;
            case 1://Регистрация нового пользователя
                System.out.println("\u001B[93m\nРЕГИСТРАЦИЯ НОВОГО ПОЛЬЗОВАТЕЛЯ:\u001B[0m");
                registrationUser();
                break;
            case 2://Изменение пароля пользователя
                System.out.println("\u001B[93m\nИЗМЕНИТЬ ПАРОЛЬ ПОЛЬЗОВАТЕЛЯ:\u001B[0m");
                printUsers(serviceUser.getActivUser());
                int idUser = inputIdUser("\u001B[36mВведите ID пользователя для изменения пароля: \u001B[0m");
                System.out.print("\u001B[36mВведите новый пароль пользователя: \u001B[0m");
                String password = scanner.nextLine().trim();
                boolean isPassUpdate = serviceUser.userUpdatePassword(idUser, password.trim());
                if(isPassUpdate == true) {
                    System.out.println("\u001B[33mПароль успешно изменен!\u001B[0m");
                } else {
                    System.out.println("\u001B[91mПароль не изменен!\u001B[0m");
                }
                break;
            case 3://Изменение Статуса пользователя
                System.out.println("\u001B[93m\nИЗМЕНИТЬ СТАТУС ПОЛЬЗОВАТЕЛЯ:\u001B[0m");
                printUsers(serviceUser.getActivUser());
                int idUserStat = inputIdUser("\u001B[36mВведите ID пользователя для изменения статуса: \u001B[0m");
                int newStatus = inputChoiceInt(1, 4,
                        "\u001B[36m\nВведите новый статус пользователя\u001B[0m" +
                                "\u001B[92m\n (1 - User,  2 - BLOCKED,  3 - BLOCKED_TRANSACTION,  4 - ADMIN): \u001B[0m");
                Role role = null;
                boolean isUpdate = false;
                if (newStatus == 1) role = Role.USER;
                if (newStatus == 2) role = Role.BLOCKED;
                if (newStatus == 3) role = Role.BLOCKED_TRANSACTION;
                if (newStatus == 4) role = Role.ADMIN;
                if (role != null) {
                    isUpdate = serviceUser.userStatusUpdate(idUserStat, role);
                }
                if(isUpdate == true) {
                    System.out.println("\u001B[33m\nСтатус пользователя: \u001B[0m'" + serviceUser.getUserById(idUserStat).getFirstName() +
                            " " + serviceUser.getUserById(idUserStat).getLastName() + "'\u001B[33m изменен на: \u001B[0m" + role);
                } else {
                    System.out.println("\u001B[91mСтатус пользователя не изменен!\u001B[0m");
                }
                break;
            case 4://Открыть счет пользователю
                System.out.println("\u001B[93m\nОТКРЫТЬ СЧЕТ ПОЛЬЗОВАТЕЛЮ:\u001B[0m");
                printUsers(serviceUser.getActivUser());
                int idUserCheck = inputIdUser("\u001B[36mВведите ID пользователя: \u001B[0m");
                Map<String, Double> lastCourse = serviceCourse.getCourseLast().getCourse();
                List<String> listCurse = new ArrayList<>();
                for (Map.Entry<String, Double> entry : lastCourse.entrySet()) {
                    listCurse.add(entry.getKey());
                }
                System.out.println("\u001B[33m\nСписок валют:\u001B[0m");
                int i = 0;
                for (String name : listCurse) {
                    i++;
                    System.out.println("\u001B[35mID: \u001B[0m" + i + "\u001B[35m  Обозначение: \u001B[0m" + name);
                }
                int idCurrency = inputChoiceInt(1, listCurse.size(),
                        "\u001B[36mВведите ID валюты: \u001B[0m");
                boolean isCheckAdd = serviceCheck.addCheckUser(listCurse.get(idCurrency - 1), true,
                        0, LocalDate.now(), idUserCheck);
                if(isCheckAdd == true){
                    System.out.println("\u001B[33mНовый счет пользователя: \u001B[0m'" + serviceUser.getUserById(idUserCheck).getFirstName()
                           + " " + serviceUser.getUserById(idUserCheck).getLastName() + "'\u001B[33m открыт!\u001B[0m");
                }else{
                    System.out.println("\u001B[91mСчет пользователя не открыт!\u001B[0m");
                }
                break;
            case 5://Закрыть счет пользователю
                System.out.println("\u001B[93m\nЗАБЛОКИРОВАТЬ СЧЕТ ПОЛЬЗОВАТЕЛЯ:\u001B[0m");
                printUsers(serviceUser.getActivUser());
                int idUserClose = inputIdUser("\u001B[36mВведите ID пользователя: \u001B[0m");
                Optional<List<Check>> optCheckList = Optional.ofNullable(serviceCheck.getUserChecks(idUserClose));
                if(optCheckList.isEmpty()) {
                    break;
                }
                int numChecks = printCheckList(serviceUser.getUserById(idUserClose), "\u001B[33mСписок счетов: \u001B[0m");
                int idCheck = inputChoiceInt(1, numChecks, "\u001B[36mВведите ID счета: \u001B[0m");
                boolean isClosed = serviceCheck.closeCheckUser(idUserClose, idCheck);
                if(isClosed == true) {
                    System.out.println("\u001B[31mПользователю: \u001B[0m'" +
                            serviceUser.getUserById(idUserClose).getFirstName() + " " +
                            serviceUser.getUserById(idUserClose).getLastName() + "'\u001B[31m счет № \u001B[0m'" +
                            idCheck + "'\u001B[31m закрыт!\u001B[0m");
                } else {
                    System.out.println("\u001B[91mОперация не выполнена!\u001B[0m" );
                }
                break;
            case 6://Разблокировать счет пользователю
                System.out.println("\u001B[93m\nРАЗБЛОКИРОВАТЬ СЧЕТ ПОЛЬЗОВАТЕЛЯ:\u001B[0m");
                printUsers(serviceUser.getActivUser());
                int idUserUnblock = inputIdUser("\u001B[36mВведите ID пользователя: \u001B[0m");
                int numChecks1 = printCheckList(serviceUser.getUserById(idUserUnblock), "\u001B[33mСписок счетов:\u001B[0m");
                Optional<List<Check>> optCheckList1 = Optional.ofNullable(serviceCheck.getUserChecks(idUserUnblock));
                if(optCheckList1.isEmpty()) {
                    break;
                }
                int idCheck1 = inputChoiceInt(1, numChecks1, "\u001B[36mВведите ID счета: \u001B[0m");
                boolean isUnClosed = serviceCheck.unblockCheckUser(idUserUnblock, idCheck1);
                if(isUnClosed == true) {
                    System.out.println("\u001B[33mПользователю: \u001B[0m'" + serviceUser.getUserById(idUserUnblock).getFirstName() +
                            " " + serviceUser.getUserById(idUserUnblock).getLastName() + "'\u001B[33m  счет № \u001B[0m'" +
                            idCheck1 + "'\u001B[33m  открыт\u001B[0m");
                } else {
                    System.out.println("\u001B[91mОперация не выполнена!\u001B[0m" );
                }
                break;
            case 7://Список всех пользователей
                printUsers(serviceUser.getActivUser());
                break;
            case 8:// Состояние счетов пользователя
                System.out.println("\u001B[93m\nОСТАТОК НА СЧЕТУ ПОЛЬЗОВАТЕЛЯ:\u001B[0m");
                printUsers(serviceUser.getActivUser());
                idUser = inputIdUser("\u001B[36mВведите ID пользователя: \u001B[0m");
                printCheckList(serviceUser.getUserById(idUser), "\u001B[33mСписок счетов:\u001B[0m");
                break;
            case 9://История Транзакций пользователя
                System.out.println("\u001B[93m\nИСТОРИЯ ТРАНЗАКЦИЙ ПОЛЬЗОВАТЕЛЯ:\u001B[0m");
                printUsers(serviceUser.getActivUser());
                idUser = inputIdUser("\u001B[36mВведите ID пользователя: \u001B[0m");
                printTransactionUser(serviceUser.getUserById(idUser));
                break;
            case 10:// Удаление пользователя
                System.out.println("\u001B[93m\nУДАЛИТЬ ПОЛЬЗОВАТЕЛЯ:\u001B[0m");
                printUsers(serviceUser.getActivUser());
                idUser = inputIdUser( "\u001B[36mВведите ID пользователя: \u001B[0m");
                String srtUser = serviceUser.getUserById(idUser).getFirstName() + " " +
                        serviceUser.getUserById(idUser).getLastName();
                boolean isDelete = serviceUser.delUser(idUser);
                if (isDelete == true) {
                    System.out.println("\u001B[33mПользователь: \u001B[0m'" + srtUser + "'\u001B[33m успешно удален.\u001B[0m");
                }else {
                    System.out.println("\u001B[91mОперация не выполнена!\u001B[0m");
                }
                break;
            case 11://Курсы Валют
                System.out.println("\n");
                Optional<CourseCurrency> optCourseLast = Optional.ofNullable(serviceCourse.getCourseLast());
                if(optCourseLast.isEmpty() || optCourseLast.get() == null) {
                    System.out.println("Курс валют отсутствует, сначала создайте новый Курс Валюты.");
                    break;
                }
                System.out.println("\u001B[93mКУРС ВАЛЮТ НА: \u001B[0m"
                        + optCourseLast.get().getDateCurrency().format(formatter));
                System.out.println("\u001B[33mОсновная валюта: \u001B[0m'" + optCourseLast.get().getCurrencyMain() + "'");
                Map<String,Double> course = optCourseLast.get().getCourse();
                Map<String,String> courseFullName = optCourseLast.get().getCourseFillName();
                String nameCurr = "";
                for (Map.Entry<String,Double> entry : course.entrySet()) {
                    nameCurr = courseFullName.get(entry.getKey());
                    System.out.println("\u001B[35mВалюта: \u001B[0m'" + nameCurr + "'" + " ".repeat(23 - nameCurr.length())
                            + entry.getKey() + "\u001B[35m Курс -> \u001B[0m" + entry.getValue());
                }
                break;
            case 12://Изменение курсов Валют
                System.out.println("\u001B[93m\nИЗМЕНЕНИЕ КУРСА ВАЛЮТ:\u001B[0m");
                Map<String,String> cursNames = courseRepository.getNamesCurrency();
                Optional<CourseCurrency> optCourseLastUpdate = Optional.ofNullable(serviceCourse.getCourseLast());
                if(optCourseLastUpdate.isEmpty() || optCourseLastUpdate.get() == null) {
                    System.out.println("Создаем первый курс валют.");
                    System.out.println("Список доступных валют: ");
                    CourseCurrency ccNew = new CourseCurrency();
                    int i1 = 0;
                    List<String> list = new ArrayList<>();
                    for (Map.Entry<String,String> entry : cursNames.entrySet()) {
                        i1++;
                        list.add(entry.getKey());
                        System.out.println(i1 + "\u001B[35m Валюта: \u001B[0m'" + entry.getKey() + "' " + entry.getValue());
                    }
                    System.out.println("\u001B[92m0 - Завершить\u001B[0m");
                    int number = -1;
                    double kurs = 0;
                    System.out.println();
                    number = inputChoiceInt(0, cursNames.size(), "\u001B[36mВведите № ОСНОВНОЙ Валюты: \u001B[0m");
                    ccNew.setCurrencyMain(list.get(number - 1));
                    ccNew.setDateCurrency(LocalDate.now());
                    System.out.println();
                    while (number != 0) {
                        number = inputChoiceInt(0, cursNames.size(), "\u001B[36mВведите № валюты: \u001B[0m");
                        if (number == 0) break;
                        kurs = inputChoiceDoubl(0,"Курс валюты по отношению к '" +
                                ccNew.getCurrencyMain() + "' -> ");
                        ccNew.getCourse().put(list.get(number - 1), kurs);
                        ccNew.getCourseFillName().put(list.get(number - 1),
                                courseRepository.getNamesCurrency().get(list.get(number - 1)));
                    }
                    serviceCourse.addCourse(ccNew);
                }
                if(optCourseLastUpdate.isPresent()) {
                    System.out.println("\u001B[33mОсновная валюта: \u001B[0m'" + serviceCourse.getCurrencyMain() + "'");
                    double curs = 0;
                    Map<String,Double> lastCourse1 = serviceCourse.getCourseLast().getCourse();
                    String cName = serviceCourse.getCurrencyMain();
                    Map<String,Double> newMap = new LinkedHashMap<>();
                    Map<String,String> courseFullName1 = serviceCourse.getCourseLast().getCourseFillName();
                    String nameCurs1 = "";
                    for (Map.Entry<String, Double> entry : lastCourse1.entrySet()) {
                        nameCurs1 = courseFullName1.get(entry.getKey());
                        System.out.print("\u001B[35mВалюта: \u001B[0m'" + nameCurs1 + "' " + entry.getKey());
                        curs = inputChoiceDoubl(0, "\u001B[35m Новый курс -> \u001B[0m");
                        newMap.put(entry.getKey(), curs);
                    }
                    CourseCurrency newCurs = new CourseCurrency();
                    newCurs.setDateCurrency(LocalDate.now());
                    newCurs.setCurrencyMain(cName);
                    newCurs.setCourse(newMap);
                    newCurs.setCourseFillName(courseFullName1);
                    serviceCourse.addCourse(newCurs);
                }
                break;
            case 13://История курсов Валют
                System.out.println("\u001B[93m\nИСТОРИЯ КУРСА ВАЛЮТ: \u001B[0m");
                Optional<List<CourseCurrency>> optCurses = Optional.ofNullable(serviceCourse.getCourses());
                if(optCurses.isEmpty() || optCurses.get().size() == 0) {
                    System.out.println("Курс валюты отсутствует!");
                    break;
                }
                for (CourseCurrency cs:optCurses.get()) {
                    Map<String, Double> map = new LinkedHashMap<>();
                    Map<String, String> mapName = new LinkedHashMap<>();
                    map = cs.getCourse();
                    mapName = cs.getCourseFillName();
                    String nameCurs2 = "";
                    System.out.println("\u001B[93mКурс валют на: \u001B[0m'" + cs.getDateCurrency().format(formatter) + "'");
                    System.out.println("\u001B[33mОсновная валюта: \u001B[0m'" + cs.getCurrencyMain() + "'");
                    for (Map.Entry<String, Double> entry : map.entrySet()) {
                        nameCurs2 = mapName.get(entry.getKey());
                        System.out.println("\u001B[35mВалюта: \u001B[0m'" + nameCurs2 + "'" + " ".repeat(23 - nameCurs2.length())
                                + entry.getKey() + "\u001B[35m Курс -> \u001B[0m" + entry.getValue());
                    }
                    System.out.println();
                }
                break;
            case 14://Добавить новую Валюту в курсы Валют
                System.out.println("\u001B[93m\nДОБАВИТЬ НОВУЮ ВАЛЮТУ:\u001B[0m");
                Optional<CourseCurrency> optCourseLast1 = Optional.ofNullable(serviceCourse.getCourseLast());
                if(optCourseLast1.isEmpty() || optCourseLast1.get() == null) {
                    System.out.println("Курс валют отсутствует. Необходимо создайть новый курс валют");
                    break;
                }
                System.out.println("\u001B[93mСПИСОК И КУРС ВАЛЮТ НА: \u001B[0m" + optCourseLast1.get().getDateCurrency().format(formatter));
                System.out.println("\u001B[33mОсновная валюта: \u001B[0m'" + optCourseLast1.get().getCurrencyMain() + "'");
                Map<String,Double> course1 = optCourseLast1.get().getCourse();
                Map<String,String> coursFullNam = optCourseLast1.get().getCourseFillName();
                String nameCurse = "";
                for (Map.Entry<String,Double> entry : course1.entrySet()) {
                    nameCurse = coursFullNam.get(entry.getKey());
                    System.out.println("\u001B[35mВалюта: \u001B[0m'" + nameCurse + "'" + " ".repeat(23-nameCurse.length())
                            + entry.getKey() + "\u001B[35m Курс -> \u001B[0m" + entry.getValue());
                }
                System.out.print("\u001B[36mВведите обозначение 'Новой Валюты' из трех заглавных латинских букв: \u001B[0m");
                String newCurrName = scanner.nextLine().trim();
                System.out.print("\u001B[36mВведите полное название Новой Валюты \u001B[0m'" + newCurrName + "': ");
                String newCurrFullName = scanner.nextLine().trim();
                double newCursCar = inputChoiceDoubl(0,"\u001B[36mВведите курс валюты: \u001B[0m'"
                        + newCurrName + "'\u001B[36m  по отношению к основной валюте \u001B[0m'"
                        + optCourseLast1.get().getCurrencyMain() + "'\u001B[36m -> \u001B[0m");
                boolean isAddCurr = serviceCourse.addNewCurrency(newCurrName, newCurrFullName, newCursCar);
                if(isAddCurr == true) {
                    System.out.println("\u001B[33mНовая Валюта: \u001B[0m'" + newCurrName
                            + "'\u001B[33m добавлена в Кусы Валют.\u001B[0m");
                } else {
                    System.out.println("\u001B[91mОперация не выполнена!\u001B[0m!");
                }
                break;
            default:
                System.out.println("Сделайте корректный выбор...");
        }
    }

    private int printCheckList(User user, String str){
        Optional<List<Check>> optCheckList = Optional.ofNullable(serviceCheck.getUserChecks(user.getIdUser()));
        try {
            Validator.getUserChecks(optCheckList, user.getIdUser());
        } catch (ValidatorException e) {
            return 0;
        }
        System.out.println(str);
        for (Check check : optCheckList.get()) {
            String sumStr = new DecimalFormat("#0.00").format(check.getSumma());
            System.out.print("\u001B[35mСчет: \u001B[0m" + check.getIdCheck() + "\u001B[35m  Валюта: \u001B[0m"
                    + check.getCurrencyName());
            if (serviceUser.getActivUser().getRole() == Role.ADMIN || user == serviceUser.getActivUser()) {
                System.out.print("\u001B[35m    Сумма на счету: \u001B[0m" + sumStr + " " + check.getCurrencyName()
                        + (check.isStatus() ? "\u001B[35m открыт: \u001B[0m" + check.getOpenDate()
                        : "\u001B[35m открыт: \u001B[0m" + check.getOpenDate() + "\u001B[35m закрыт: \u001B[0m"
                        + check.getCloseDate()));
            }
            System.out.println();
        }
        return optCheckList.get().size();
    }

    private int inputChoiceInt (int min, int max, String comment){
        int choice = -1;
        while (true) {
            System.out.print(comment);
            if (scanner.hasNextInt() == true) {
                choice = scanner.nextInt();
                scanner.nextLine();
                if(choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.println("\u001B[91mНеправильный ввод\u001B[0m");
                }
            }else {
                System.out.println("\u001B[91mНеправильный ввод\u001B[0m");
                scanner.nextLine();
            }
        }
    }

    private double inputChoiceDoubl (double min, String comment){
        String str = "";
        String str1 = "";
        double choice = 0;
        Double choice1 = null;
        while (true) {
            System.out.print(comment);
            str = scanner.nextLine();
            if(str.length() == 0) {
                System.out.println("\u001B[91mНеправильный ввод\u001B[0m");
                continue;
            }
            str1 = str.replace(",", ".");
            try {
                choice1 = Double.parseDouble(str1.trim());
            } catch (NumberFormatException e) {
                System.out.println("\u001B[91mНеправильный ввод\u001B[0m");
                continue;
            }
            choice = Double.valueOf(choice1);
            if(choice > min) {
                return choice;
            } else {
                System.out.println("\u001B[91mНеправильный ввод\u001B[0m");
            }
        }
    }

    private void printTransactionUser(User user){
        Optional<List<Transaction>> optTransactionList = Optional.ofNullable(serviceCheck.getTransactionListByIdUser(user.getIdUser()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        if(optTransactionList.isEmpty() || optTransactionList.get().size() == 0) {
            System.out.println("\u001B[33mЕще не было транзакций ...\u001B[0m");
            return;
        }
        for (Transaction tr : optTransactionList.get()){
            String s = "\u001B[35mТранзакция №: \u001B[0m" + tr.getNumberTransaction() + "\u001B[35m  Валюта: \u001B[0m'"+
                    tr.getCurrencyName() + "'\u001B[35m  Тип: \u001B[0m";
            if(tr.getTypeTransaction() == TransactionName.TAKE_MONEY) s = s + " 'Снятие средств'     ";
            if(tr.getTypeTransaction() == TransactionName.DEPOSIT_MONEY) s = s + " 'Внесение средств'   ";
            if(tr.getTypeTransaction() == TransactionName.TRANSFER_MONEY_IN) s = s + " 'Получение переводом' ";
            if(tr.getTypeTransaction() == TransactionName.TRANSFER_MONEY_OUT) s = s + " 'Снятие переводом'   ";

            if(tr.getIdUserRecipient() == serviceUser.getActivUser().getIdUser() ){
                s = s + "\u001B[35mПолучатель: \u001B[0m'ВЫ'";
            }
            if(tr.getIdUserRecipient() != serviceUser.getActivUser().getIdUser()){
                s = s + "\u001B[35mПолучатель: \u001B[0m'" + serviceUser.getUserById(tr.getIdUserRecipient()).getFirstName() + " " +
                        serviceUser.getUserById(tr.getIdUserRecipient()).getLastName() + "' ";
            }
            if(tr.getIdUserOut() == serviceUser.getActivUser().getIdUser() &&
                    tr.getIdUserOut() !=0){
                s = s + "\u001B[35mОтправитель:\u001B[0m 'ВЫ'";
            }
            if(tr.getIdUserOut() != serviceUser.getActivUser().getIdUser() &&
                    tr.getIdUserOut() != 0 ){
                s = s + "\u001B[35mОтправитель: \u001B[0m'" + serviceUser.getUserById(tr.getIdUserOut()).getFirstName()
                        + " " + serviceUser.getUserById(tr.getIdUserOut()).getLastName() + "' ";
            }
            s = s + "\u001B[35mСумма: \u001B[0m" + tr.getSumma() + tr.getCurrencyName();
            s = s + " " + tr.getDateTransaction().format(formatter);
            System.out.println(s);
        }
    }

    private void printUsers(User user) {
        String s = "";
        String s1 = "";
        Optional<Map<Integer, User>> optUsers = Optional.ofNullable(serviceUser.getUsers());
        if(optUsers.isEmpty() || optUsers.get().size() == 0){
            System.out.println("\u001B[31m\nВ базе нет пользователей!\u001B[0m");
            return;
        }
        System.out.println("\u001B[93m\nСписок всех пользователей:\u001B[0m");
        for (Map.Entry<Integer, User> entry : optUsers.get().entrySet()) {
            int len = entry.getValue().getFirstName().length() + entry.getValue().getLastName().length() + 1;
            if(len < 20) {s = " ".repeat(20 - len);} else {s = " ".repeat(1);}
            len = entry.getValue().getRole().toString().length();
            if(len < 20) {s1 = " ".repeat(20 - len);} else {s1 = " ".repeat(1);}
            System.out.print("\u001B[35mID: \u001B[0m" + entry.getValue().getIdUser() + "  '" + entry.getValue().getFirstName() + " " +
                    entry.getValue().getLastName() + "'");
            if(user.getRole() == Role.ADMIN) {
                System.out.print(s + "\u001B[35mСтатус: \u001B[0m'" + entry.getValue().getRole() + "'" + s1 + "\u001B[35m  Дата регистрации: \u001B[0m" +
                        entry.getValue().getDateRegistration() + "\u001B[35m   Дата посещения: \u001B[0m"
                        + entry.getValue().getDateLastEntrance());
            }
            System.out.println();
        }
    }

    private int inputIdUser(String comment) {
        int choice = -1;
        while (true) {
            System.out.print(comment);
            if (scanner.hasNextInt() == true) {
                choice = scanner.nextInt();
                scanner.nextLine();
                Optional<User> optUser = Optional.ofNullable(serviceUser.getUserById(choice));
                if(optUser.isEmpty()) {
                    System.out.println("\u001B[91mНеправильный ввод\u001B[0m");
                    continue;
                }
                return choice;
            }else {
                System.out.println("\u001B[91mНеправильный ввод\u001B[0m");
                scanner.nextLine();
            }
        }
    }
}
