package repository;

import model.Check;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CheckRepositoryImpl implements CheckRepository {
    //поле (общий список счетов всех пользователей)
    private List<Check> userChecks = new ArrayList<>();

    //конструктор
    public CheckRepositoryImpl() {
        readChecksFromFile();
    }


    public void  addCheckUser (String currencyName, boolean status, double summa,
                               LocalDate addDate, int idUser){
//        int idCheck = 0;
//        int i = userChecks.size();
//        if(i == 0) {
//            idCheck = 0;
//        } else {
//            for (Check ch : userChecks){
//                if(ch.getIdUser() == idUser) idCheck = idCheck + 1;
//            }
//        }
//        Check check = new Check(currencyName, status, summa, addDate,null,idCheck + 1, idUser);
//        userChecks.add(check);
        int idCheck = 0;
        if (!userChecks.isEmpty()) {
            for (Check ch : userChecks) {
                if (ch.getIdUser() == idUser) idCheck++;
            }
        }
        Check check = new Check(currencyName, status, summa, addDate, null, idCheck + 1, idUser);
        userChecks.add(check);
    }

    public void closeCheckUser(int idUser, int idCheck){
        Check check = getCheckByIdUserIdCheck(idUser, idCheck);
        check.setStatus(false);
        check.setCloseDate(LocalDate.now());
    }

    public void unblockCheckUser(int idUser, int idCheck){
        Check check = getCheckByIdUserIdCheck(idUser, idCheck);
        check.setStatus(true);
        check.setCloseDate(null);
    }

    public List<Check> getUserChecks(int idUser) {
        return userChecks.stream().filter(ch -> ch.getIdUser() == idUser).collect(Collectors.toList());
    }

    public Check getCheckByIdUserIdCheck(int idUser, int idCheck) {
//        Check check = null;
//        for (Check ch : userChecks){
//            if(ch.getIdUser() == idUser && ch.getIdCheck() == idCheck) check = ch;
//        }
//        return check;
        for (Check ch : userChecks) {
            if (ch.getIdUser() == idUser && ch.getIdCheck() == idCheck) return ch;
        }
        return null;
    }

    public void delCheckByIdUser(int idUser) {
//        int i = -1;
//        for (Check ch : userChecks){
//            i++;
//            if(ch.getIdUser() == idUser ) {
//                userChecks.remove(i - 1);
//            }
//        }
        userChecks.removeIf(ch -> ch.getIdUser() == idUser);
    }

    public Check takeMoney(int idUser, int idCheck, double summa){
//        Check check = null;
//        for (Check ch : userChecks){
//            if(ch.getIdUser() == idUser && ch.getIdCheck() == idCheck) {
//                double summaCheck = ch.getSumma();
//                summaCheck = summaCheck - summa;
//                ch.setSumma(summaCheck);
//                check = ch;
//            }
//        }
//        return check;
        for (Check ch : userChecks) {
            if (ch.getIdUser() == idUser && ch.getIdCheck() == idCheck) {
                ch.setSumma(ch.getSumma() - summa);
                return ch;
            }
        }
        return null;
    }

    public Check depositMoney(int idUser, int idCheck, double summa){
//        Check check = null;
//        for (Check ch : userChecks){
//            if(ch.getIdUser() == idUser && ch.getIdCheck() == idCheck) {
//                double summaCheck = ch.getSumma();
//                summaCheck = summaCheck + summa;
//                ch.setSumma(summaCheck);
//                check = ch;
//            }
//        }
//        return check;
        for (Check ch : userChecks) {
            if (ch.getIdUser() == idUser && ch.getIdCheck() == idCheck) {
                ch.setSumma(ch.getSumma() + summa);
                return ch;
            }
        }
        return null;
    }

    public void writeChecksToFile(){
//        List<Check> checks = new ArrayList<>(userChecks);
//        File path = new File("src/files");
//        path.mkdirs();
//        File fileChecks = new File(path,"checks.txt");
//        if(checks == null) return;
//        if(checks.size() == 0)  return;
//        if(fileChecks.exists()) fileChecks.delete();
//        //создать файл если его еще нет
//        try {
//            fileChecks.createNewFile();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        for (Check check : checks) {
//            try(BufferedWriter bWriter = new BufferedWriter(new FileWriter(fileChecks,true))) //флаг
//            {
//                String str = check.getCurrencyName() + "^" + check.isStatus() + "^" + check.getSumma()
//                        + "^" + check.getOpenDate() + "^" + check.getCloseDate() + "^" + check.getIdCheck()
//                        + "^" + check.getIdUser();
//                bWriter.write(str);
//                bWriter.newLine();// перевод каретки
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }

        if (userChecks.isEmpty()) return;
        File path = new File("src/files");
        path.mkdirs();
        File fileChecks = new File(path, "checks.txt");
        if (fileChecks.exists()) fileChecks.delete();
        try {
            fileChecks.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (BufferedWriter bWriter = new BufferedWriter(new FileWriter(fileChecks, true))) {
            for (Check check : userChecks) {
                String str = check.getCurrencyName() + "^" + check.isStatus() + "^" + check.getSumma()
                        + "^" + check.getOpenDate() + "^" + check.getCloseDate() + "^" + check.getIdCheck()
                        + "^" + check.getIdUser();
                bWriter.write(str);
                bWriter.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readChecksFromFile(){
//        File path = new File("src/files");
//        File fileChecks = new File(path,"checks.txt");
//        if(fileChecks.exists() == false || fileChecks.length() == 0) {
//            return;
//        }
//        try(BufferedReader bReader = new BufferedReader(new FileReader(fileChecks)))
//        {
//            String line;
//            while ((line = bReader.readLine()) != null) {
//                if(line.length() == 0) continue;
//                String [] parts = line.split("\\^");
//                String name = null;
//                name = parts[0];
//                LocalDate openDate = null;
//                LocalDate closeDate = null;
//                if (!parts[3].equals("null")) { openDate = LocalDate.parse(parts[3]);}
//                if (!parts[4].equals("null")) { closeDate = LocalDate.parse(parts[4]);}
//                Check check = new Check(name, Boolean.parseBoolean(parts[1]), Double.parseDouble(parts[2]),
//                        openDate, closeDate, Integer.parseInt(parts[5]),
//                        Integer.parseInt(parts[6]));
//                userChecks.add(check);
//            }
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        File fileChecks = new File("src/files/checks.txt");
        if (!fileChecks.exists() || fileChecks.length() == 0) return;
        try (BufferedReader bReader = new BufferedReader(new FileReader(fileChecks))) {
            String line;
            while ((line = bReader.readLine()) != null) {
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\^");
                LocalDate openDate = parts[3].equals("null") ? null : LocalDate.parse(parts[3]);
                LocalDate closeDate = parts[4].equals("null") ? null : LocalDate.parse(parts[4]);
                Check check = new Check(parts[0], Boolean.parseBoolean(parts[1]), Double.parseDouble(parts[2]),
                        openDate, closeDate, Integer.parseInt(parts[5]), Integer.parseInt(parts[6]));
                userChecks.add(check);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
