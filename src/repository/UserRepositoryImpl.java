package repository;

import model.Role;
import model.User;

import java.io.*;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class UserRepositoryImpl implements UserRepository {
    //поля список всех пользователей (id-пользователь)
    private Map<Integer, User> users = new LinkedHashMap<>();
    private int numberUsers = 0; //id счетчик пользователя

    //конструктор
    public UserRepositoryImpl() {
        this.readUsersFromFile();
        if(users.size() == 0) {
            numberUsers++;
            User user= new User("Иван","Иванов",numberUsers,"1","1");
            user.setRole(Role.ADMIN);
            user.setDateLastEntrance(LocalDate.now());
            user.setDateRegistration(LocalDate.now());
            users.put(numberUsers,user);
        }
    }

    public User addUser(String firstName, String lastName,  String email, String password) {
        numberUsers++;
        User user = new User(firstName, lastName, numberUsers, email, password);
        this.users.put(numberUsers, user);
        return user;
    }

    public User isUserExistById(int idUser) {
        return this.users.get(idUser);
    }

    public boolean isEmailExist(String email){
        for (Map.Entry<Integer,User> entry : users.entrySet()) {
            if(entry.getValue().getEmail().equals(email)) return true;
        }
        return false;
    }

    public User getUserById(int idUser) {
        return this.users.get(idUser);
    }

    public User getUserByEmail(String email) {
        for (Map.Entry<Integer,User> entry : users.entrySet()) {
            if(entry.getValue().getEmail().equals(email)) return entry.getValue();
        }
        return null;
    }


    public boolean userUpdatePassword(int idUser, String newPassword) {
        User user = users.get(idUser);
        user.setPassword(newPassword);
        return true;
    }

    public User delUser(int idUser) {
        return users.remove(idUser);
    }

    public Map<Integer,User> getUsers() {
        return this.users;
    }

    public boolean userStatusUpdate(int idUser, Role role) {
        User user = users.get(idUser);
        if(user != null) {
            user.setRole(role);
            return true;
        }
        return false;
    }

//    public void writeUsersToFile(){
//        Map<Integer,User> usersToFile = getUsers();
//        File path = new File("src/files");
//        path.mkdirs();
//        File fileUsers = new File(path,"users.txt");
//        if(usersToFile == null) return;
//        if(usersToFile.size() == 0)  return;
//        if(fileUsers.exists()) fileUsers.delete();
//        //создать файл если его еще нет
//        try {
//            fileUsers.createNewFile();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        try(BufferedWriter bWriter1=new BufferedWriter(new FileWriter(fileUsers,true)))
//        {
//            String str = "" + numberUsers;
//            bWriter1.write(str);
//            bWriter1.newLine();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        for (Map.Entry<Integer, User> entry : usersToFile.entrySet()) {
//            try(BufferedWriter bWriter = new BufferedWriter(new FileWriter(fileUsers,true)))
//            {
//                String str1 = entry.getKey() + "^" + entry.getValue().getFirstName() + "^"
//                        + entry.getValue().getLastName() + "^" + entry.getValue().getIdUser() + "^"
//                        + entry.getValue().getEmail() + "^" + entry.getValue().getPassword() + "^"
//                        + entry.getValue().getDateRegistration() + "^" + entry.getValue().getDateLastEntrance()
//                        + "^" + entry.getValue().getRole();
//                bWriter.write(str1);
//                bWriter.newLine();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }

    public void writeUsersToFile() {
        Map<Integer, User> usersToFile = getUsers();
        if (usersToFile == null || usersToFile.isEmpty()) {
            return;
        }

        File fileUsers = new File("src/files", "users.txt");
        fileUsers.getParentFile().mkdirs(); // Создаем директорию, если её нет

        // Удаляем старый файл и создаем новый
        if (fileUsers.exists()) {
            fileUsers.delete();
        }
        try {
            fileUsers.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileUsers, true))) {
            // Записываем количество пользователей
            writer.write(String.valueOf(numberUsers));
            writer.newLine();

            // Записываем каждого пользователя
            for (User user : usersToFile.values()) {
                writer.write(formatUser(user));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Метод для форматирования пользователя в строку
    private String formatUser(User user) {
        return user.getIdUser() + "^" +
                user.getFirstName() + "^" +
                user.getLastName() + "^" +
                user.getEmail() + "^" +
                user.getPassword() + "^" +
                user.getDateRegistration() + "^" +
                user.getDateLastEntrance() + "^" +
                user.getRole();
    }

//    public void  readUsersFromFile(){
//        File path = new File("src/files");
//        File fileUsers = new File(path,"users.txt");
//        if(fileUsers.exists() == false || fileUsers.length() == 0) {
//            return;
//        }
//        try(BufferedReader bReader = new BufferedReader(new FileReader(fileUsers)))
//        {
//            String line;
//            line = bReader.readLine();
//            numberUsers = Integer.parseInt(line);
//            while ((line = bReader.readLine()) != null) {
//                if(line.length() == 0) continue;
//                String [] parts = line.split("\\^");
//                int id = Integer.parseInt(parts[0]);
//                User user = new User(parts[1], parts[2], Integer.parseInt(parts[3]), parts[4], parts[5]);
//                LocalDate dateRegistration = LocalDate.parse(parts[6]);
//                LocalDate dateLastEntrance = LocalDate.parse(parts[7]);
//                Role role = null;
//                if (parts[8].equals("ADMIN")) role = Role.ADMIN;
//                if (parts[8].equals("USER")) role = Role.USER;
//                if (parts[8].equals("BLOCKED")) role = Role.BLOCKED;
//                if (parts[8].equals("BLOCKED_TRANSACTION")) role = Role.BLOCKED_TRANSACTION;
//                user.setRole(role);
//                user.setDateRegistration(dateRegistration);
//                user.setDateLastEntrance(dateLastEntrance);
//                users.put(id,user);
//            }
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
public void readUsersFromFile() {
    File fileUsers = new File("src/files", "users.txt");

    // Проверяем, существует ли файл и не является ли он пустым
    if (!fileUsers.exists() || fileUsers.length() == 0) {
        return;
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(fileUsers))) {
        // Читаем и устанавливаем количество пользователей
        String line = reader.readLine();
        if (line != null) {
            numberUsers = Integer.parseInt(line.trim());
        }

        // Читаем пользователей из файла
        while ((line = reader.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                User user = parseUser(line);
                users.put(user.getIdUser(), user);
            }
        }
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
    // Метод для парсинга строки в объект User
    private User parseUser(String line) {
        String[] parts = line.split("\\^");
        if (parts.length != 9) {
            throw new IllegalArgumentException(line);
        }
        int idUser = Integer.parseInt(parts[0].trim());
        String firstName = parts[1].trim();
        String lastName = parts[2].trim();
        int parsedIdUser = Integer.parseInt(parts[3].trim());
        String email = parts[4].trim();
        String password = parts[5].trim();
        LocalDate dateRegistration = LocalDate.parse(parts[6].trim());
        LocalDate dateLastEntrance = LocalDate.parse(parts[7].trim());
        Role role = Role.valueOf(parts[8].trim()); // Преобразование строки в enum
        User user = new User(firstName, lastName, parsedIdUser, email, password);
        user.setDateRegistration(dateRegistration);
        user.setDateLastEntrance(dateLastEntrance);
        user.setRole(role);
        return user;
    }
}
