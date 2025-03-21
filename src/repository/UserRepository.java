package repository;

import model.Role;
import model.User;
import java.util.Map;

public interface UserRepository {

    //добавить пользователя
    User addUser(String firstName, String lastName, String email, String password);

    //существует ли пользователь по id
    User isUserExistById(int idUser);

    // существует ли email
    boolean isEmailExist(String email);

    //получить пользователя по id
    User getUserById(int idUser);

    //получить пользователя по email
    User getUserByEmail(String email);

    //изменить пароль пользователя
    boolean userUpdatePassword(int idUser, String newPassword);

    //удалить пользователя
    User deleteUser(int idUser);

    //получить всех пользователей
    Map<Integer,User> getUsers();

    //изменить статус пользователя
    boolean userStatusUpdate(int idUser, Role role);

    //записать пользователей в файл ++
    void writeUsersToFile();

    //читать пользователей из файла ++
    void  readUsersFromFile();
}
