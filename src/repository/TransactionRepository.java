package repository;

import model.Transaction;

import java.util.List;

public interface TransactionRepository {

    //получить список транзакций по id пользователя
    List<Transaction> getTransactionListByIdUser(int idUser);

    //добавить транзакцию
    void addTransaction(Transaction transaction);

    //записать транзакцию в файл ++
    void writeTransactionToFile();

    //чтение транзакций из файла ++
    void readTransactionFromFile();
}
