package service;

import model.Check;
import model.Transaction;
import model.User;

import java.time.LocalDate;
import java.util.List;

public interface ServiceCheck {

    //получить список счетов
    List<Check> getUserChecks (int idUser);

    //закрыть счет пользователя
    boolean closeCheckUser(int idUser,int idCheck);

    //разблокировать счет пользователя
    boolean unblockCheckUser(int idUser,int idCheck);

    //получить список транзакций пользователя
     List<Transaction> getTransactionListByIdUser(int idUser);

    //взять деньги ++
    boolean takeMoney(User user, int idCheck, double summa);

    //внести деньги ++
    boolean depositMoney(User user,int idCheck,double summa);

    //получить счет пользователя (по id счета)
    Check getCheckByIdUserIdCheck(int idUser, int idCheck);

    //перевести деньги пользователю ++
    boolean transferMoneyToUser(User outUser,User inUser,int idOutUserCheck, int idInUserCheck, double summa);

    //перевести деньги себе ++
    boolean transferMoneyToMe(User user,int idOutUserCheck,int idInUserCheck, double summa);

    //добавить счет пользователю
    boolean  addCheckUser(String currencyName, boolean status, double summa,
                                 LocalDate addDate, int idUser);
    //удалить счет пользователя
    boolean delChecksByIdUser(int idUser);
}
