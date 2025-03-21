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
    boolean takeMoney(User activeUser, int idCheck, double summa);

    //внести деньги ++
    boolean depositMoney(User ativeUser,int idCheck,double summa);

    //получить счет пользователя (по id счета)
    Check getCheckByIdUserIdCheck(int idUser, int idCheck);

    //перевести деньги пользователю ++
    boolean transferMoneyToUser(User activeUser,User userRecipient,int idCheck, int idCheck2, double summa);

    //перевести деньги себе ++
    boolean transferMoneyToMe(User activeUser,int idCheck,int idCheck2, double summa);

    //добавить счет пользователю
    boolean  addCheckUser(String currencyName, boolean status, double summa,
                                 LocalDate openDate, int idUser);
    //удалить счет пользователя
    boolean deleteChecksByIdUser(int idUser);
}
