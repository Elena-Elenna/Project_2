package service;

import model.Role;
import model.User;

import java.util.Map;

public interface ServiceUser {

    //получить активного пользователя
    User getActivUser();

    //проверить валидность email
    boolean isEmailValid(String email);

    //проверить валидность пароля
    boolean isPasswordValid(String password);

    //зарегистрировать пользователя
    boolean registerUser(String firstName, String lastName,  String email, String password);

    //авторизоваться
    boolean loginUser(String email, String password);

    //выйти
    void logout();

    //удалить пользователя
    boolean delUser(int idUser);

    //получить всех пользователей
    Map<Integer, User> getUsers();

    //изменить пароль пользователя
    boolean userUpdatePassword(int idUser, String newPassword);

    //изменить статус пользователя
    boolean userStatusUpdate(int idUser, Role role);

    //получить пользователя по id
    User getUserById(int idUser);

    //проверить существует ли email
    boolean isEmailExist(String email);

    //проверить заблокирован ли пользователь
    boolean isUserBlocked(String email);
}
