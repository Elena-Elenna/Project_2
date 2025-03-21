package model;

import java.time.LocalDate;
import java.util.Objects;

public class User {

    //поля
    private String firstName;//имя
    private String lastName;//фамилия
    private int idUser;
    private String email;
    private String password;
    private LocalDate dateRegistration;
    private LocalDate dateLastEntrance;//дата последнего входа
    private Role role;


    //конструктор
    public User(String firstName, String lastName, int idUser, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.idUser = idUser;
        this.email = email;
        this.password = password;
        this.dateRegistration = LocalDate.now();
        this.dateLastEntrance =  LocalDate.now();
        this.role = Role.USER;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDateRegistration() {
        return dateRegistration;
    }

    public void setDateRegistration(LocalDate dateRegistration) {
        this.dateRegistration = dateRegistration;
    }

    public LocalDate getDateLastEntrance() {
        return dateLastEntrance;
    }

    public void setDateLastEntrance(LocalDate dateLastEntrance) {
        this.dateLastEntrance = dateLastEntrance;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return idUser == user.idUser && Objects.equals(firstName, user.firstName)
                && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email)
                && Objects.equals(password, user.password) && Objects.equals(dateRegistration, user.dateRegistration)
                && Objects.equals(dateLastEntrance, user.dateLastEntrance) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, idUser, email, password, dateRegistration, dateLastEntrance, role);
    }

    @Override
    public String toString() {
        return "User {" +
                "firstName = " + firstName + '\'' +
                "; lastName = " + lastName + '\'' +
                "; idUser = " + idUser +
                "; email = " + email + '\'' +
                "; password = " + password + '\'' +
                "; dateRegistration = " + dateRegistration +
                "; dateLastEntrance = " + dateLastEntrance +
                "; role = " + role +
                '}';
    }
}
