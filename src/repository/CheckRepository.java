package repository;

import model.Check;

import java.time.LocalDate;
import java.util.List;

public interface CheckRepository {

    //Добавить счет пользователю
    void  addCheckUser (String currencyName, boolean status, double summa,
                               LocalDate openDate, int idUser);

    //Закрыть счет пользователю
    void closeCheckUser(int idUser,int idCheck);

    //Разблокировать счет пользоватяля
    void unblockCheckUser(int idUser,int idCheck);

    //Получить счета пользователя
    List<Check> getUserChecks(int idUser);

    //Получить счет (по id пользователя и по id счета)
    Check getCheckByIdUserIdCheck(int idUser,int idCheck);

    //удалить счет пользователя (по id пользователя)
    void deleteCheckByIdUser(int idUser);

    //взять деньги
    Check takeMoney(int idUser, int idCheck, double summa);

    //внести деньги
    Check depositMoney(int idUser, int idCheck, double summa);

    //записывать счета в файл ++
    void writeChecksToFile();

    //считывать счета из файла ++
    void readChecksFromFile();
}
