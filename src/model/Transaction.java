package model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction {

    //поля
    private int idUser;
    private String currencyName;//название валюты
    private TransactionName typeTransaction;//название транзакции(enum)
    private double summa;
    private LocalDateTime dateTransaction;
    private int idUserRecipient;// получатель
    private int idActiveUser;//отправитель
    private int numberTransaction;

    //конструктор
    public Transaction(int idUser, String currencyName, TransactionName typeTransaction, double summa,
                       LocalDateTime dateTransaction, int idUserRecipient, int idUserOut, int  numberTransaction) {
        this.idUser = idUser;
        this.currencyName = currencyName.trim();
        this.typeTransaction = typeTransaction;
        this.summa = summa;
        this.dateTransaction = dateTransaction;
        this.idUserRecipient = idUserRecipient;
        this.idActiveUser = idUserOut;
        this. numberTransaction = numberTransaction;
    }



    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getCurrencyName() {
        return currencyName.trim();
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName.trim();
    }

    public TransactionName getTypeTransaction() {
        return typeTransaction;
    }

    public void setTypeTransaction(TransactionName typeTransaction) {
        this.typeTransaction = typeTransaction;
    }

    public double getSumma() {
        return summa;
    }

    public void setSumma(double summa) {
        this.summa = summa;
    }

    public LocalDateTime getDateTransaction() {
        return dateTransaction;
    }

    public void setDateTransaction(LocalDateTime dateTransaction) {
        this.dateTransaction = dateTransaction;
    }

    public int getNumberTransaction() {
        return numberTransaction;
    }

    public void setNumberTransaction(int numberTransaction) {
        this.numberTransaction = numberTransaction;
    }

    public int getIdUserRecipient() {
        return idUserRecipient;
    }

    public void setIdUserRecipient(int idUserRecipient) {
        this.idUserRecipient = idUserRecipient;
    }

    public int getIdActiveUser() {
        return idActiveUser;
    }

    public void setIdActiveUser(int idActiveUser) {
        this.idActiveUser = idActiveUser;
    }

    @Override
    public String toString() {
        return "Transaction {" +
                "idUser = " + idUser +
                "; currencyName = " + currencyName +
                "; typeTransaction = " + typeTransaction +
                "; summa = " + summa +
                "; dateTransaction = " + dateTransaction +
                "; idUserRecipient = " + idUserRecipient +
                "; idUserOut = " + idActiveUser +
                "; numberTransaction = " + numberTransaction +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return idUser == that.idUser && Double.compare(summa, that.summa) == 0
                && idUserRecipient == that.idUserRecipient && idActiveUser == that.idActiveUser
                && numberTransaction == that.numberTransaction && Objects.equals(currencyName, that.currencyName)
                && typeTransaction == that.typeTransaction && Objects.equals(dateTransaction, that.dateTransaction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, currencyName, typeTransaction, summa, dateTransaction,
                idUserRecipient, idActiveUser, numberTransaction);
    }
}
