package service;

import model.Check;
import model.Transaction;
import model.TransactionName;
import model.User;
import repository.CheckRepository;
import repository.CourseRepository;
import repository.TransactionRepository;
import repository.UserRepository;
import utils.Validator;
import utils.ValidatorException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ServiceCheckImpl implements ServiceCheck {
    //инициализация
    private final UserRepository userRepository;
    private final CheckRepository checkRepository;
    private final CourseRepository courseRepository;
    private final TransactionRepository transactionRepository;

    //конструктор
    public ServiceCheckImpl(UserRepository userRepository, CheckRepository checkRepository,
                            CourseRepository courseRepository, TransactionRepository transactionRepository ) {
        this.userRepository = userRepository;
        this.checkRepository = checkRepository;
        this.courseRepository = courseRepository;
        this.transactionRepository = transactionRepository;
    }


    public List<Check> getUserChecks (int idUser){
        Optional<User> optUser = Optional.ofNullable(userRepository.isUserExistById(idUser));
        try {
            Validator.isUserExistById(optUser, idUser);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            return null;
        }
        Optional<List<Check>> optUserChecks = Optional.ofNullable(checkRepository.getUserChecks(idUser));
        try {
            Validator.getUserChecks(optUserChecks, idUser);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return checkRepository. getUserChecks(idUser);
    }

    public boolean closeCheckUser(int idUser, int idCheck){
        Optional<Check> optCheck = Optional.ofNullable(checkRepository.getCheckByIdUserIdCheck(idUser, idCheck));
        Optional<User> optUser = Optional.ofNullable(userRepository.getUserById(idUser));
        try {
            Validator.getUserById(optUser, idUser);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            return false;
        }
        try {
            Validator.getCheckByIdUserIdCheck(optCheck, idUser, idCheck);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            return false;
        }
        try {
            Validator.checkStatus_false(optCheck, idUser,idCheck);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            return false;
        }
        checkRepository.closeCheckUser(idUser,idCheck);
        return true;
    }

    public boolean unblockCheckUser(int idUser, int idCheck) {
        Check check = checkRepository.getCheckByIdUserIdCheck(idUser, idCheck);
        Optional<Check> optCheck = Optional.ofNullable(check);
        Optional<User> optUser = Optional.ofNullable(userRepository.getUserById(idUser));
        try {
            Validator.getUserById(optUser, idUser);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            return false;
        }
        try {
            Validator.getCheckByIdUserIdCheck(optCheck, idUser, idCheck);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            return false;
        }
        try {
            Validator.checkStatus_true(optCheck, idUser,idCheck);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            return false;
        }
        checkRepository.unblockCheckUser(idUser,idCheck);
        return true;
    }

    public List<Transaction> getTransactionListByIdUser(int idUser){
        return transactionRepository.getTransactionListByIdUser(idUser);
    }

    public boolean takeMoney(User activeUser, int idCheck, double summa){
        Optional<User> optUser = Optional.ofNullable(userRepository.isUserExistById(activeUser.getIdUser()));
        try {
            Validator.isUserExistById(optUser, activeUser.getIdUser());
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            return false;
        }
        Optional<List<Check>> optChecks = Optional.ofNullable(getUserChecks(activeUser.getIdUser()));
        try {
            Validator.getUserChecks(optChecks, activeUser.getIdUser());
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            return false;
        }
        Optional<Check> optCheck = Optional.ofNullable(checkRepository.getCheckByIdUserIdCheck(activeUser.getIdUser(),idCheck));
        try {
            Validator.getCheckByIdUserIdCheck(optCheck, activeUser.getIdUser(), idCheck);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            return false;
        }
        try {
            Validator.summaCheck(optCheck, summa);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            return false;
        }
        Optional<Check> optCheck1 = Optional.ofNullable(checkRepository.takeMoney(activeUser.getIdUser(), idCheck,summa));
        Transaction transaction = new Transaction(activeUser.getIdUser(), optCheck1.get().getCurrencyName(),
                TransactionName.TAKE_MONEY, summa, LocalDateTime.now(), activeUser.getIdUser(),0,0);
        transactionRepository.addTransaction(transaction);
        Optional<Transaction> optTrans = Optional.ofNullable(transaction);
        if(optCheck1.isEmpty() || optCheck1.get() == null || optTrans.isEmpty() || optTrans.get() == null ){
            return false;
        }
        return true;
    }

    public boolean depositMoney(User ativeUser, int idCheck, double summa) {
        Optional<User> optUser = Optional.ofNullable(userRepository.isUserExistById(ativeUser.getIdUser()));
        try {
            Validator.isUserExistById(optUser, ativeUser.getIdUser());
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            return false;
        }
        Optional<List<Check>> optChecks = Optional.ofNullable(getUserChecks(ativeUser.getIdUser()));
        try {
            Validator.getUserChecks(optChecks, ativeUser.getIdUser());
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            return false;
        }
        Optional<Check> optCheck = Optional.ofNullable(checkRepository.getCheckByIdUserIdCheck(ativeUser.getIdUser(),idCheck));
        try {
            Validator.getCheckByIdUserIdCheck(optCheck, ativeUser.getIdUser(), idCheck);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            return false;
        }
        Optional<Check> optCheckDepo = Optional.ofNullable(checkRepository.depositMoney(ativeUser.getIdUser(),idCheck,summa));
        Transaction transaction = new Transaction(ativeUser.getIdUser(), optCheckDepo.get().getCurrencyName(),
                TransactionName.DEPOSIT_MONEY, summa, LocalDateTime.now(), ativeUser.getIdUser(),0,0);
        transactionRepository.addTransaction(transaction);
        Optional<Transaction> optTrans = Optional.ofNullable(transaction);
        if(optCheckDepo.isEmpty() || optCheckDepo.get() == null || optTrans.isEmpty() || optTrans.get() == null ){
            return false;
        }
        return true;
    }

    public Check getCheckByIdUserIdCheck(int idUser, int idCheck){
        return checkRepository.getCheckByIdUserIdCheck(idUser,idCheck);
    }

    public boolean transferMoneyToUser(User activeUser, User userRecipient, int idCheck, int idCheck2, double summa) {
        Optional<List<Check>> optCheckLis = Optional.ofNullable(getUserChecks(activeUser.getIdUser()));
        try {
            Validator.getUserChecks(optCheckLis, activeUser.getIdUser());
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            return false;
        }
        Optional<Check> optCheck = Optional.ofNullable(checkRepository.getCheckByIdUserIdCheck(activeUser.getIdUser(),idCheck));
        try {
            Validator.summaCheck(optCheck, summa);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            return false;
        }
        Optional<List<Check>> optiNUserChecks = Optional.ofNullable(checkRepository.getUserChecks(userRecipient.getIdUser()));
        try {
            Validator.getUserChecks(optiNUserChecks, userRecipient.getIdUser());
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            return false;
        }
        checkRepository.takeMoney(activeUser.getIdUser(), idCheck, summa);
        Optional<Check> optCheck1 = Optional.ofNullable(optiNUserChecks.get().get(idCheck2 - 1));
        double currencyOut = courseRepository.getCourseByCurrencyName(optCheck.get().getCurrencyName());
        double currencyIn = courseRepository.getCourseByCurrencyName(optCheck1.get().getCurrencyName());
        double currencyMain = courseRepository.getCourseByCurrencyName(courseRepository.getCurrencyMain());
        double summaIn = ((summa / currencyOut) * currencyIn);
        checkRepository.depositMoney(userRecipient.getIdUser(), optCheck1.get().getIdCheck(), summaIn);

        Transaction transaction1 = new Transaction(activeUser.getIdUser(), optCheck.get().getCurrencyName(),
                TransactionName.TRANSFER_MONEY_OUT, summa, LocalDateTime.now(), userRecipient.getIdUser(),
                activeUser.getIdUser(),0);
        transactionRepository.addTransaction(transaction1);
        Transaction transaction2 = new Transaction(userRecipient.getIdUser(), optCheck1.get().getCurrencyName(),
                TransactionName.TRANSFER_MONEY_IN, summaIn, LocalDateTime.now(), userRecipient.getIdUser(),
                activeUser.getIdUser(),0);
        transactionRepository.addTransaction(transaction2);
        return true;
    }

    public boolean transferMoneyToMe(User activeUser, int idCheck, int idCheck2, double summa) {
        Optional<Check> optCheck = Optional.ofNullable(checkRepository.getCheckByIdUserIdCheck(activeUser.getIdUser(),idCheck));
        Optional<Check> optCheck2 = Optional.ofNullable(checkRepository.getCheckByIdUserIdCheck(activeUser.getIdUser(),idCheck2));
        try {
            Validator.getCheckByIdUserIdCheck(optCheck, activeUser.getIdUser(), idCheck);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            return false;
        }
        try {
            Validator.getCheckByIdUserIdCheck(optCheck2, activeUser.getIdUser(), idCheck2);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            return false;
        }
        try {
            Validator.summaCheck(optCheck, summa);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            return false;
        }
        try {
            Validator.isChecksEquals(idCheck, idCheck2);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            return false;
        }
        checkRepository.takeMoney(activeUser.getIdUser(), idCheck, summa);
        double currencyOut = courseRepository.getCourseByCurrencyName(optCheck.get().getCurrencyName());
        double currencyIn = courseRepository.getCourseByCurrencyName(optCheck2.get().getCurrencyName());
        double currencyMain = courseRepository.getCourseByCurrencyName(courseRepository.getCurrencyMain());
        double summaIn = ((summa / currencyOut) * currencyIn);
        checkRepository.depositMoney(activeUser.getIdUser(), optCheck2.get().getIdCheck(), summaIn);

        Transaction transaction1 = new Transaction(activeUser.getIdUser(), optCheck2.get().getCurrencyName(),
                TransactionName.TRANSFER_MONEY_OUT, summa, LocalDateTime.now(), activeUser.getIdUser(),
                activeUser.getIdUser(),0);
        transactionRepository.addTransaction(transaction1);
        return true;
    }

    public boolean  addCheckUser(String currencyName, boolean status, double summa,
                                 LocalDate openDate, int idUser){
        try {
            Validator.isSummaLessZero(summa);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            return false;
        }
        for (Check check : checkRepository.getUserChecks(idUser)) {
            try {
                Validator.isCheckPresentByUser(check.getCurrencyName(), currencyName, idUser);
            } catch (ValidatorException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }
        Optional<User> isUser = Optional.ofNullable(userRepository.isUserExistById(idUser));
        try {
            Validator.isUserExistById(isUser, idUser);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            return false;
        }
        checkRepository.addCheckUser(currencyName, status, summa, openDate, idUser);
        return true;
    }

    public boolean deleteChecksByIdUser(int idUser){
        Optional<List<Check>> optUserChecks = Optional.ofNullable(checkRepository.getUserChecks(idUser));
        if(optUserChecks.isPresent()) {
            checkRepository.deleteCheckByIdUser(idUser);
            return true;
        }
        return false;
    }
}
