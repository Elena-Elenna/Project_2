package service;

import model.Role;
import model.User;
import repository.UserRepository;
import utils.Validator;
import utils.ValidatorException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

public class ServiceUserImpl implements ServiceUser {
    //инициализация
    private final UserRepository userRepository;
    private final ServiceCheck serviceCheck;

    //поля (авторизованный пользователь)
    private User activUser;

    //конструктор
    public ServiceUserImpl(UserRepository userRepository,ServiceCheck serviceCheck) {
        this.userRepository = userRepository;
        this.serviceCheck = serviceCheck;
    }


    public User getActivUser() {
        return this.activUser;
    }

    public boolean isEmailValid(String email) {
        try {
            Validator.isEmailValid(email.trim());
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean isPasswordValid(String password) {
        try {
            Validator.isPasswordValid(password.trim());
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean registerUser(String firstName, String lastName,  String email, String password) {
        boolean valid;
        valid = isEmailValid(email);
        if(valid == false) return false;
        valid = isPasswordValid(password);
        if(valid == false) return false;
        Optional<User> optUser = Optional.ofNullable(userRepository.getUserByEmail(email.trim()));
        try {
            Validator.isUserPresentByEmail_registerUser(optUser, email);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            return false;
        }
        userRepository.addUser(firstName.trim(), lastName.trim(), email.trim(), password.trim());
        return true;
    }
    //TODO
    public boolean loginUser(String email, String password) {
        if (activUser != null) {
            logout();
        }
        for (Map.Entry<Integer, User> entry : userRepository.getUsers().entrySet()) {
            if (entry.getValue().getEmail().equals(email.trim()) &&
                    entry.getValue().getPassword().equals(password.trim())) {
                if (entry.getValue().getRole() == Role.ADMIN) {
                    activUser = entry.getValue();
                    activUser.setRole(Role.ADMIN);
                    entry.getValue().setDateLastEntrance(LocalDate.now());
                    return true;
                }
                if (entry.getValue().getRole() == Role.USER) {
                    activUser = entry.getValue();
                    activUser.setRole(Role.USER);
                    entry.getValue().setDateLastEntrance(LocalDate.now());
                    return true;
                }
                if (entry.getValue().getRole() == Role.BLOCKED_TRANSACTION) {
                    activUser = entry.getValue();
                    activUser.setRole(Role.BLOCKED_TRANSACTION);
                    entry.getValue().setDateLastEntrance(LocalDate.now());
                    return true;
                }
                if (entry.getValue().getRole() == Role.BLOCKED) {
//                    System.out.println("Вы ЗАБЛОКИРОВАНЫ ! Обратитесь к администратору");
                    return false;
                }
            }
        }
        return true;
}

    public void logout() {
        this.activUser = null;
    }

    public boolean delUser(int idUser) {
        Optional<User> optUser = Optional.ofNullable(userRepository.isUserExistById(idUser));
        try {
            Validator.isUserExistById(optUser,idUser);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            return false;
        }
        try {
            Validator.isDeleteMe(idUser);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            return false;
        }
        serviceCheck.delChecksByIdUser(idUser);
        userRepository.delUser(idUser);
        return true;
    }

    public Map<Integer,User> getUsers() {
        return this.userRepository.getUsers();
    }

    public boolean userUpdatePassword(int idUser, String password) {
        boolean exit = false;
        Optional<User> optUser = Optional.ofNullable(userRepository.isUserExistById(idUser));
        try {
            Validator.isUserExistById(optUser, idUser);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            return false;
        }
        try {
            Validator.isPasswordValid(password.trim());
        } catch (ValidatorException e) {
            exit = true;
            System.out.println(e.getMessage());
        }
        if(exit == true) return false;
        this.userRepository.userUpdatePassword(idUser, password);
        return true;
    }

    public boolean userStatusUpdate(int idUser, Role role) {
        Optional<User> optUser = Optional.ofNullable(userRepository.isUserExistById(idUser));
        try {
            Validator.isUserExistById(optUser, idUser);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
            return false;
        }
        boolean isUpdate = this.userRepository.userStatusUpdate(idUser, role);
        if (isUpdate) {
            return true;
        }
        return false;
    }

    public User getUserById(int idUser) {
        return userRepository.getUserById(idUser);
    }

    public boolean isEmailExist(String email){
        return userRepository.isEmailExist(email);
    }
//Todo
    public boolean isUserBlocked(String email){
        Optional<User> optUser = Optional.ofNullable(userRepository.getUserByEmail(email));
        if (optUser.get().getRole() == Role.BLOCKED) return true;
        return false;
    }
}
