package model;

import java.time.LocalDate;
import java.util.Objects;

public class Check {

    private String currencyName;//название валюты
    private boolean status;//статус счета
    private double summa;
    private LocalDate openDate;//дата открытия счета
    private LocalDate closeDate;//дата закрытия счета
    private int idCheck;
    private int idUser;

    //конструктор
    public Check(String currencyName, boolean status, double summa, LocalDate openDate,
                 LocalDate closeDate, int idCheck, int idUser) {
        this.currencyName = currencyName.trim();
        this.status = status;
        this.summa = summa;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.idCheck = idCheck;
        this.idUser = idUser;
    }



    public int getIdCheck() {
        return idCheck;
    }

//    public void setIdCheck(int idCheck) {
//        this.idCheck = idCheck;
//    }

    public String getCurrencyName() {
        return currencyName.trim();
    }

//    public void setCurrencyName(String currencyName) {
//        this.currencyName = currencyName.trim();
//    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public double getSumma() {
        return summa;
    }

    public void setSumma(double summa) {
        this.summa = summa;
    }

    public LocalDate getOpenDate() {
        return openDate;
    }

//    public void setOpenDate(LocalDate openDate) {
//        this.openDate = openDate;
//    }

    public LocalDate getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(LocalDate closeDate) {
        this.closeDate = closeDate;
    }

    public int getIdUser() {
        return idUser;
    }

    @Override
    public String toString() {
        return "Check {" +
                "CurrencyCheck = " + currencyName +
                "; status = " + status +
                "; summa = " + summa +
                "; openDate = " + openDate +
                "; closeDate = " + closeDate +
                "; idCheck = " + idCheck +
                "; idUser = " + idUser +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Check check = (Check) o;
        return status == check.status && Double.compare(summa, check.summa) == 0 && idCheck == check.idCheck
                && idUser == check.idUser && Objects.equals(currencyName, check.currencyName)
                && Objects.equals(openDate, check.openDate) && Objects.equals(closeDate, check.closeDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currencyName, status, summa, openDate, closeDate, idCheck, idUser);
    }
}
