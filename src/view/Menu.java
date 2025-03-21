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
    private final Scanner scanner = new Scanner(System.in);

    //поля
    private boolean exitAdminMenu;//выход из меню администратора
    private boolean exitUserMenu;//выход из меню пользователя

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
        String firstName;
        String lastName;
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
        firstName = scanner.nextLine().trim();
        System.out.print("\u001B[36mВведите Фамилию пользователя: \u001B[0m");
        lastName = scanner.nextLine().trim();
        boolean isRegister = serviceUser.registerUser(firstName, lastName, email, password);
        if (isRegister == true){
            System.out.println("\u001B[33mПользователь: \u001B[0m'" + firstName + " " + lastName +
                    "'\u001B[33m  c Email: \u001B[0m" + email + "\u001B[33m успешно зарегистрирован\u001B[0m");
            return true;
        } else {
            System.out.println("\u001B[91mРегистрация провалена!\u001B[0m");
        }
        return false;
    }

    private int InputUser() {
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
                    System.exit(0);
                    break;
                case 1:
                    boolean isAutorized = authorizationUser();
                    if (isAutorized == true ) {
                        showMenu();
                    } else {
                        System.out.println("\u001B[91mОшибка авторизации!\u001B[0m");
                    }
                    break;
                case 2:
                    boolean isRegistration = registrationUser();
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
        switch (input) {
            case 0:
                exitUserMenu = true;
                System.out.println("Вы вышли из МЕНЮ ПОЛЬЗОВАТЕЛЯ.");
                break;
            case 1:
                System.out.println("\u001B[93m\nСОСТОЯНИЕ СЧЕТА:\u001B[0m");
                printCheckList(serviceUser.getActivUser(),"\u001B[33mСписок счетов: \u001B[0m");
                break;
            case 2:
                System.out.println("\u001B[93m\nСНЯТИЕ ДЕНЕГ:\u001B[0m");
                if(serviceUser.getActivUser().getRole() == Role.BLOCKED_TRANSACTION) {
                    System.out.println("\u001B[31mОперация не выполнена!\u001B[0m");
                    break;
                }
                int checkSize = printCheckList(serviceUser.getActivUser(),"\u001B[33mСписок ваших счетов: \u001B[0m");
                if(checkSize == 0) {
                    break;
                }
                System.out.println("\u001B[92m0 - отмена\u001B[0m");
                int idCheck = inputChoiceInt(0, checkSize,
                        "\u001B[36mВведите номер счета для снятия денег: \u001B[0m");
                if (idCheck == 0) break;
                Check check = serviceCheck.getCheckByIdUserIdCheck(serviceUser.getActivUser().getIdUser(), idCheck);
                if(check.isStatus() == false) {
                    System.out.println("\u001B[31mОперация не выполнена! Ваш счет закрыт!\u001B[0m");
                    break;
                }
                double summa = inputChoiceDouble(0, "\u001B[36mВведите сумму: \u001B[0m");
                boolean isTakeMoney = serviceCheck.takeMoney(serviceUser.getActivUser(), idCheck, summa);
                if (isTakeMoney == true) {
                    System.out.println("\u001B[33mОперация успешно выполнена!\u001B[0m");
                }
                break;
            case 3:
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
                idCheck = inputChoiceInt(0, checkSize,
                        "\u001B[36mВведите номер счета для зачисления денег: \u001B[0m");
                if (idCheck == 0) break;
                check = serviceCheck.getCheckByIdUserIdCheck(serviceUser.getActivUser().getIdUser(), idCheck);
                if(check.isStatus() == false) {
                    System.out.println("\u001B[31mОперация не выполнена! Счет закрыт!\u001B[0m");
                    break;
                }
                summa = inputChoiceDouble(0, "\u001B[36mВведите сумму денег: \u001B[0m");
                boolean isDepositMoney = serviceCheck.depositMoney(serviceUser.getActivUser(), idCheck, summa);
                if (isDepositMoney == true) {
                    System.out.println("\u001B[33mОперация успешно выполнена!\u001B[0m");
                } else {
                    System.out.println("\u001B[31mОперация не выполнена!\u001B[0m");
                }
                break;
            case 4:
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
                idCheck = inputChoiceInt(0, checkSize,
                        "\u001B[36mВведите номер Вашего счета, с которого будут сняты деньги: \u001B[0m");
                if (idCheck == 0) break;
                check = serviceCheck.getCheckByIdUserIdCheck(serviceUser.getActivUser().getIdUser(), idCheck);
                if(check.isStatus() == false) {
                    System.out.println("\u001B[31mОперация не выполнена! Счет: \u001B[0m'" + idCheck
                            + "'\u001B[31m закрыт!\u001B[0m");
                    break;
                }
                summa = inputChoiceDouble(0,
                        "\u001B[36mВведите сумму для перевода денежных средств: \u001B[0m");
                if(choice == 1) {
                    int idCheck2 = inputChoiceInt(1, checkSize,
                            "\u001B[36mСчет для зачисления средств: \u001B[0m");
                    Check check2 = serviceCheck.getCheckByIdUserIdCheck(serviceUser.getActivUser().getIdUser(),idCheck2);
                    if(check2.isStatus() == false) {
                        System.out.println("\u001B[31mОперация не выполнена! Счет: \u001B[0m'"
                                + idCheck2 + "'\u001B[31m закрыт!\u001B[0m");
                        break;
                    }
                    boolean isTransferMoney = serviceCheck.transferMoneyToMe(serviceUser.getActivUser(), idCheck,
                            idCheck2, summa);
                    if(isTransferMoney == true) {
                        Check chec = serviceCheck.getCheckByIdUserIdCheck(serviceUser.getActivUser().getIdUser(),idCheck);
                        System.out.println("\u001B[33mСумма: \u001B[0m" + summa + chec.getCurrencyName() +
                                "\u001B[33m  успешно переведена на Ваш счет: \u001B[0m'" +  idCheck2 + "'");
                    }
                }
                if(choice == 2) {
                    printUsers(serviceUser.getActivUser());
                    int idUserTransfer = inputIdUser("\u001B[36mВведите ID пользователя для перевода средств: \u001B[0m");
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
                    int idCheck2 = inputChoiceInt(0, checkSize2,
                            "\u001B[36mВведите номер счета получателя: \u001B[0m");
                    if (idCheck2 == 0) break;
                    Check check3 = serviceCheck.getCheckByIdUserIdCheck(idUserTransfer, idCheck2);
                    if(check3.isStatus() == false) {
                        System.out.println("\u001B[31mОперация не выполнена!\u001B[0m");
                        break;
                    }
                    User userRecipient = serviceUser.getUserById(idUserTransfer);
                    boolean isTransferMoney = serviceCheck.transferMoneyToUser(serviceUser.getActivUser(),
                            userRecipient, idCheck, idCheck2, summa);
                    if(isTransferMoney == true ) {
                        Check chec = serviceCheck.getCheckByIdUserIdCheck(serviceUser.getActivUser().getIdUser(), idCheck);
                        System.out.println("\u001B[33mСумма: \u001B[0m" + summa + chec.getCurrencyName() +
                                "\u001B[33m  успешно переведена на счет пользователя: \u001B[0m" + userRecipient.getFirstName() +
                                " " + userRecipient.getLastName());
                    }else {
                        System.out.println("\u001B[31mОперация не выполнена!\u001B[0m");
                    }
                }
                break;
            case 5:
                System.out.println("\u001B[93m\nИСТОРИЯ ТРАНЗАКЦИИ:\u001B[0m");
                printTransactionUser(serviceUser.getActivUser());
                break;
            case 6://Курс валют
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
                for (Map.Entry<String,Double> entry : course.entrySet()) {
                    String currencyName = courseFullName.get(entry.getKey());
                    System.out.println("\u001B[35mВалюта: \u001B[0m'" + currencyName + "'" +
                            " ".repeat(23 - currencyName.length()) + entry.getKey() +
                            "\u001B[35m Курс -> \u001B[0m" + entry.getValue());
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
        switch (input) {
            case 0:
                exitAdminMenu = true;
                System.out.println("Вы вышли из МЕНЮ АДМИНИСТРАТОРА.");
                break;
            case 1:
                System.out.println("\u001B[93m\nРЕГИСТРАЦИЯ НОВОГО ПОЛЬЗОВАТЕЛЯ:\u001B[0m");
                registrationUser();
                break;
            case 2:
                System.out.println("\u001B[93m\nИЗМЕНИТЬ ПАРОЛЬ ПОЛЬЗОВАТЕЛЯ:\u001B[0m");
                printUsers(serviceUser.getActivUser());
                int idUser = inputIdUser("\u001B[36mВведите ID пользователя для изменения пароля: \u001B[0m");
                System.out.print("\u001B[36mВведите новый пароль пользователя: \u001B[0m");
                String newPassword = scanner.nextLine().trim();
                boolean isPassUpdate = serviceUser.userUpdatePassword(idUser, newPassword.trim());
                if(isPassUpdate == true) {
                    System.out.println("\u001B[33mПароль успешно изменен!\u001B[0m");
                } else {
                    System.out.println("\u001B[91mПароль не изменен!\u001B[0m");
                }
                break;
            case 3:
                System.out.println("\u001B[93m\nИЗМЕНИТЬ СТАТУС ПОЛЬЗОВАТЕЛЯ:\u001B[0m");
                printUsers(serviceUser.getActivUser());
                int idUserStatus = inputIdUser("\u001B[36mВведите ID пользователя для изменения статуса: \u001B[0m");
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
                    isUpdate = serviceUser.userStatusUpdate(idUserStatus, role);
                }
                if(isUpdate == true) {
                    System.out.println("\u001B[33m\nСтатус пользователя: \u001B[0m'" +
                            serviceUser.getUserById(idUserStatus).getFirstName() + " " +
                            serviceUser.getUserById(idUserStatus).getLastName() +
                            "'\u001B[33m изменен на: \u001B[0m" + role);
                } else {
                    System.out.println("\u001B[91mСтатус пользователя не изменен!\u001B[0m");
                }
                break;
            case 4:
                System.out.println("\u001B[93m\nОТКРЫТЬ СЧЕТ ПОЛЬЗОВАТЕЛЮ:\u001B[0m");
                printUsers(serviceUser.getActivUser());
                int idUserCheck = inputIdUser("\u001B[36mВведите ID пользователя: \u001B[0m");
                Map<String, Double> lastCourse = serviceCourse.getCourseLast().getCourse();
                List<String> listCurrency = new ArrayList<>();
                for (Map.Entry<String, Double> entry : lastCourse.entrySet()) {
                    listCurrency.add(entry.getKey());
                }
                System.out.println("\u001B[33m\nСписок валют:\u001B[0m");
                int i = 0;
                for (String name : listCurrency) {
                    i++;
                    System.out.println("\u001B[35mID: \u001B[0m" + i + "\u001B[35m  Обозначение: \u001B[0m" + name);
                }
                int idCurrency = inputChoiceInt(1, listCurrency.size(),
                        "\u001B[36mВведите ID валюты: \u001B[0m");
                boolean isCheckAdd = serviceCheck.addCheckUser(listCurrency.get(idCurrency - 1), true,
                        0, LocalDate.now(), idUserCheck);
                if(isCheckAdd == true){
                    System.out.println("\u001B[33mНовый счет пользователя: \u001B[0m'" +
                            serviceUser.getUserById(idUserCheck).getFirstName() + " " +
                            serviceUser.getUserById(idUserCheck).getLastName() + "'\u001B[33m открыт!\u001B[0m");
                }else{
                    System.out.println("\u001B[91mСчет пользователя не открыт!\u001B[0m");
                }
                break;
            case 5:
                System.out.println("\u001B[93m\nЗАБЛОКИРОВАТЬ СЧЕТ ПОЛЬЗОВАТЕЛЯ:\u001B[0m");
                printUsers(serviceUser.getActivUser());
                int idUserClose = inputIdUser("\u001B[36mВведите ID пользователя: \u001B[0m");
                Optional<List<Check>> listChecks = Optional.ofNullable(serviceCheck.getUserChecks(idUserClose));
                if(listChecks.isEmpty()) {
                    break;
                }
                int numberChecks = printCheckList(serviceUser.getUserById(idUserClose),"\u001B[33mСписок счетов: \u001B[0m");
                int idCheck = inputChoiceInt(1, numberChecks, "\u001B[36mВведите ID счета: \u001B[0m");
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
            case 6:
                System.out.println("\u001B[93m\nРАЗБЛОКИРОВАТЬ СЧЕТ ПОЛЬЗОВАТЕЛЯ:\u001B[0m");
                printUsers(serviceUser.getActivUser());
                int idUserUnblock = inputIdUser("\u001B[36mВведите ID пользователя: \u001B[0m");
                int numberChecks1 = printCheckList(serviceUser.getUserById(idUserUnblock),"\u001B[33mСписок счетов:\u001B[0m");
                Optional<List<Check>> optCheckList1 = Optional.ofNullable(serviceCheck.getUserChecks(idUserUnblock));
                if(optCheckList1.isEmpty()) {
                    break;
                }
                int idCheck1 = inputChoiceInt(1, numberChecks1, "\u001B[36mВведите ID счета: \u001B[0m");
                boolean isUnclosed = serviceCheck.unblockCheckUser(idUserUnblock, idCheck1);
                if(isUnclosed == true) {
                    System.out.println("\u001B[33mПользователю: \u001B[0m'" + serviceUser.getUserById(idUserUnblock).getFirstName()
                           + " " + serviceUser.getUserById(idUserUnblock).getLastName() +
                            "'\u001B[33m  счет № \u001B[0m'" + idCheck1 + "'\u001B[33m  открыт\u001B[0m");
                } else {
                    System.out.println("\u001B[91mОперация не выполнена!\u001B[0m" );
                }
                break;
            case 7://Список всех пользователей
                printUsers(serviceUser.getActivUser());
                break;
            case 8:
                System.out.println("\u001B[93m\nОСТАТОК НА СЧЕТУ ПОЛЬЗОВАТЕЛЯ:\u001B[0m");
                printUsers(serviceUser.getActivUser());
                idUser = inputIdUser("\u001B[36mВведите ID пользователя: \u001B[0m");
                printCheckList(serviceUser.getUserById(idUser), "\u001B[33mСписок счетов:\u001B[0m");
                break;
            case 9:
                System.out.println("\u001B[93m\nИСТОРИЯ ТРАНЗАКЦИЙ ПОЛЬЗОВАТЕЛЯ:\u001B[0m");
                printUsers(serviceUser.getActivUser());
                idUser = inputIdUser("\u001B[36mВведите ID пользователя: \u001B[0m");
                printTransactionUser(serviceUser.getUserById(idUser));
                break;
            case 10:
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
            case 11://Курс валют
                System.out.println("\n");
                Optional<CourseCurrency> courseLast = Optional.ofNullable(serviceCourse.getCourseLast());
                if(courseLast.isEmpty() || courseLast.get() == null) {
                    System.out.println("Курс валют отсутствует, сначала создайте новый Курс Валюты.");
                    break;
                }
                System.out.println("\u001B[93mКУРС ВАЛЮТ НА: \u001B[0m"
                        + courseLast.get().getDateCurrency().format(formatter));
                System.out.println("\u001B[33mОсновная валюта: \u001B[0m'" + courseLast.get().getCurrencyMain() + "'");
                Map<String,Double> course = courseLast.get().getCourse();
                Map<String,String> courseFullName = courseLast.get().getCourseFillName();
                for (Map.Entry<String,Double> entry : course.entrySet()) {
                    String nameCurrency = courseFullName.get(entry.getKey());
                    System.out.println("\u001B[35mВалюта: \u001B[0m'" + nameCurrency + "'" +
                            " ".repeat(23 - nameCurrency.length()) + entry.getKey() +
                            "\u001B[35m Курс -> \u001B[0m" + entry.getValue());
                }
                break;
            case 12:
                System.out.println("\u001B[93m\nИЗМЕНЕНИЕ КУРСА ВАЛЮТ:\u001B[0m");
                Map<String,String> currencyNames = courseRepository.getNamesCurrency();
                Optional<CourseCurrency> courseLastUpdate = Optional.ofNullable(serviceCourse.getCourseLast());
                if(courseLastUpdate.isEmpty() || courseLastUpdate.get() == null) {
                    System.out.println("Создаем первый курс валют.");
                    System.out.println("Список доступных валют: ");
                    CourseCurrency newCourseCurrency = new CourseCurrency();
                    int i1 = 0;
                    List<String> list = new ArrayList<>();
                    for (Map.Entry<String,String> entry : currencyNames.entrySet()) {
                        i1++;
                        list.add(entry.getKey());
                        System.out.println(i1 + "\u001B[35m Валюта: \u001B[0m'" + entry.getKey() + "' " + entry.getValue());
                    }
                    System.out.println("\u001B[92m0 - Завершить\u001B[0m");
                    System.out.println();
                    int number = inputChoiceInt(0,currencyNames.size(),"\u001B[36mВведите № ОСНОВНОЙ Валюты: \u001B[0m");
                    newCourseCurrency.setCurrencyMain(list.get(number - 1));
                    newCourseCurrency.setDateCurrency(LocalDate.now());
                    System.out.println();
                    while (number != 0) {
                        number = inputChoiceInt(0,currencyNames.size(),"\u001B[36mВведите № валюты: \u001B[0m");
                        if (number == 0) break;
                        double kurs = inputChoiceDouble(0,"Курс валюты по отношению к '" +
                                newCourseCurrency.getCurrencyMain() + "' -> ");
                        newCourseCurrency.getCourse().put(list.get(number - 1), kurs);
                        newCourseCurrency.getCourseFillName().put(list.get(number - 1),
                                courseRepository.getNamesCurrency().get(list.get(number - 1)));
                    }
                    serviceCourse.addCourse(newCourseCurrency);
                }
                if(courseLastUpdate.isPresent()) {
                    System.out.println("\u001B[33mОсновная валюта: \u001B[0m'" + serviceCourse.getCurrencyMain() + "'");
                    Map<String,Double> lastCourse1 = serviceCourse.getCourseLast().getCourse();
                    String cName = serviceCourse.getCurrencyMain();
                    Map<String,Double> newMap = new LinkedHashMap<>();
                    Map<String,String> courseFullName1 = serviceCourse.getCourseLast().getCourseFillName();
                    for (Map.Entry<String, Double> entry : lastCourse1.entrySet()) {
                        String nameCurs1 = courseFullName1.get(entry.getKey());
                        System.out.print("\u001B[35mВалюта: \u001B[0m'" + nameCurs1 + "' " + entry.getKey());
                        double curs = inputChoiceDouble(0, "\u001B[35m Новый курс -> \u001B[0m");
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
            case 13:
                System.out.println("\u001B[93m\nИСТОРИЯ КУРСА ВАЛЮТ: \u001B[0m");
                Optional<List<CourseCurrency>> curses = Optional.ofNullable(serviceCourse.getCourses());
                if(curses.isEmpty() || curses.get().size() == 0) {
                    System.out.println("Курс валюты отсутствует!");
                    break;
                }
                for (CourseCurrency cs:curses.get()) {
                    Map<String, Double> map = new LinkedHashMap<>();
                    Map<String, String> mapName = new LinkedHashMap<>();
                    map = cs.getCourse();
                    mapName = cs.getCourseFillName();
                    System.out.println("\u001B[93mКурс валют на: \u001B[0m'" + cs.getDateCurrency().format(formatter) + "'");
                    System.out.println("\u001B[33mОсновная валюта: \u001B[0m'" + cs.getCurrencyMain() + "'");
                    for (Map.Entry<String, Double> entry : map.entrySet()) {
                        String nameCurs2 = mapName.get(entry.getKey());
                        System.out.println("\u001B[35mВалюта: \u001B[0m'" + nameCurs2 + "'" +
                                " ".repeat(23 - nameCurs2.length()) + entry.getKey() +
                                "\u001B[35m Курс -> \u001B[0m" + entry.getValue());
                    }
                    System.out.println();
                }
                break;
            case 14:
                System.out.println("\u001B[93m\nДОБАВИТЬ НОВУЮ ВАЛЮТУ:\u001B[0m");
                Optional<CourseCurrency> courseLast1 = Optional.ofNullable(serviceCourse.getCourseLast());
                if(courseLast1.isEmpty() || courseLast1.get() == null) {
                    System.out.println("Курс валют отсутствует. Необходимо создайть новый курс валют");
                    break;
                }
                System.out.println("\u001B[93mСПИСОК И КУРС ВАЛЮТ НА: \u001B[0m" + courseLast1.get().getDateCurrency().format(formatter));
                System.out.println("\u001B[33mОсновная валюта: \u001B[0m'" + courseLast1.get().getCurrencyMain() + "'");
                Map<String,Double> course1 = courseLast1.get().getCourse();
                Map<String,String> courseFullName1 = courseLast1.get().getCourseFillName();
                for (Map.Entry<String,Double> entry : course1.entrySet()) {
                    String nameCurse = courseFullName1.get(entry.getKey());
                    System.out.println("\u001B[35mВалюта: \u001B[0m'" + nameCurse + "'" + " ".repeat(23-nameCurse.length())
                            + entry.getKey() + "\u001B[35m Курс -> \u001B[0m" + entry.getValue());
                }
                System.out.print("\u001B[36mВведите обозначение 'Новой Валюты' из трех заглавных латинских букв: \u001B[0m");
                String newCurrencyName = scanner.nextLine().trim();
                System.out.print("\u001B[36mВведите полное название Новой Валюты \u001B[0m'" + newCurrencyName + "': ");
                String newCurrencyFullName = scanner.nextLine().trim();
                double newCource = inputChoiceDouble(0,"\u001B[36mВведите курс валюты: \u001B[0m'"
                        + newCurrencyName + "'\u001B[36m  по отношению к основной валюте \u001B[0m'"
                        + courseLast1.get().getCurrencyMain() + "'\u001B[36m -> \u001B[0m");
                boolean isAddCurr = serviceCourse.addNewCurrency(newCurrencyName, newCurrencyFullName, newCource);
                if(isAddCurr == true) {
                    System.out.println("\u001B[33mНовая Валюта: \u001B[0m'" + newCurrencyName
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
        Optional<List<Check>> checkList = Optional.ofNullable(serviceCheck.getUserChecks(user.getIdUser()));
        try {
            Validator.getUserChecks(checkList, user.getIdUser());
        } catch (ValidatorException e) {
            return 0;
        }
        System.out.println(str);
        for (Check check : checkList.get()) {
            String strings = new DecimalFormat("#0.00").format(check.getSumma());
            System.out.print("\u001B[35mСчет: \u001B[0m" + check.getIdCheck() + "\u001B[35m  Валюта: \u001B[0m"
                    + check.getCurrencyName());
            if (serviceUser.getActivUser().getRole() == Role.ADMIN || user == serviceUser.getActivUser()) {
                System.out.print("\u001B[35m    Сумма на счету: \u001B[0m" + strings + " " + check.getCurrencyName()
                        + (check.isStatus() ? "\u001B[35m открыт: \u001B[0m" + check.getOpenDate()
                        : "\u001B[35m открыт: \u001B[0m" + check.getOpenDate() + "\u001B[35m закрыт: \u001B[0m"
                        + check.getCloseDate()));
            }
            System.out.println();
        }
        return checkList.get().size();
    }

    private int inputChoiceInt (int min, int max, String comment){
        while (true) {
            System.out.print(comment);
            if (scanner.hasNextInt() == true) {
                int choice = scanner.nextInt();
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

    private double inputChoiceDouble(double min, String comment){
        Double choice1 = null;
        while (true) {
            System.out.print(comment);
            String str = scanner.nextLine();
            if(str.length() == 0) {
                System.out.println("\u001B[91mНеправильный ввод\u001B[0m");
                continue;
            }
            str = str.replace(",", ".");
            try {
                choice1 = Double.parseDouble(str.trim());
            } catch (NumberFormatException e) {
                System.out.println("\u001B[91mНеправильный ввод\u001B[0m");
                continue;
            }
            double choice = Double.valueOf(choice1);
            if(choice > min) {
                return choice;
            } else {
                System.out.println("\u001B[91mНеправильный ввод\u001B[0m");
            }
        }
    }

    private void printTransactionUser(User user){
        Optional<List<Transaction>> transactionList = Optional.ofNullable(serviceCheck.getTransactionListByIdUser(user.getIdUser()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        if(transactionList.isEmpty() || transactionList.get().size() == 0) {
            System.out.println("\u001B[33mЕще не было транзакций ...\u001B[0m");
            return;
        }
        for (Transaction tr : transactionList.get()){
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
                s = s + "\u001B[35mПолучатель: \u001B[0m'" + serviceUser.getUserById(tr.getIdUserRecipient()).getFirstName()
                        + " " + serviceUser.getUserById(tr.getIdUserRecipient()).getLastName() + "' ";
            }
            if(tr.getIdActiveUser() == serviceUser.getActivUser().getIdUser() &&
                    tr.getIdActiveUser() !=0){
                s = s + "\u001B[35mОтправитель:\u001B[0m 'ВЫ'";
            }
            if(tr.getIdActiveUser() != serviceUser.getActivUser().getIdUser() &&
                    tr.getIdActiveUser() != 0 ){
                s = s + "\u001B[35mОтправитель: \u001B[0m'" + serviceUser.getUserById(tr.getIdActiveUser()).getFirstName()
                        + " " + serviceUser.getUserById(tr.getIdActiveUser()).getLastName() + "' ";
            }
            s = s + "\u001B[35mСумма: \u001B[0m" + tr.getSumma() + tr.getCurrencyName();
            s = s + " " + tr.getDateTransaction().format(formatter);
            System.out.println(s);
        }
    }

    private void printUsers(User user) {
        String s = "";
        String s1 = "";
        Optional<Map<Integer, User>> users = Optional.ofNullable(serviceUser.getUsers());
        if(users.isEmpty() || users.get().size() == 0){
            System.out.println("\u001B[31m\nВ базе нет пользователей!\u001B[0m");
            return;
        }
        System.out.println("\u001B[93m\nСписок всех пользователей:\u001B[0m");
        for (Map.Entry<Integer, User> entry : users.get().entrySet()) {
            int length = entry.getValue().getFirstName().length() + entry.getValue().getLastName().length() + 1;
            if(length < 20) {s = " ".repeat(20 - length);} else {s = " ".repeat(1);}
            length = entry.getValue().getRole().toString().length();
            if(length < 20) {s1 = " ".repeat(20 - length);} else {s1 = " ".repeat(1);}
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
        while (true) {
            System.out.print(comment);
            if (scanner.hasNextInt() == true) {
                int choice = scanner.nextInt();
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
