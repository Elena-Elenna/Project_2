package repository;

import model.Transaction;
import model.TransactionName;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionRepositoryImpl implements TransactionRepository{
    //поля (список транзакций всех пользователей)
    private List<Transaction> transactionList = new ArrayList<>();
    private int currentId = 0;//id счетчик транзакций

    //конструктор
    public TransactionRepositoryImpl() {
        readTransactionFromFile();
    }


    public List<Transaction> getTransactionListByIdUser(int idUser) {
        return transactionList.stream().filter(l -> l.getIdUser() == idUser).collect(Collectors.toList());
    }

    public void addTransaction(Transaction transaction){
        currentId = currentId + 1;
        transaction.setNumberTransaction(currentId);
        transactionList.add(transaction);
    }

    @Override
    public String toString() {
        return "TransactionRepositoryImpl {" +
                "transactionList = " + transactionList +
                '}';
    }

//    public void writeTransactionToFile(){
//        File path = new File("src/files");
//        path.mkdirs();
//        File fileTransaction = new File(path,"transaction.txt");
//        if(transactionList == null) return;
//        if(transactionList.size() == 0)  return;
//        if(fileTransaction.exists()) fileTransaction.delete();
//        //создать файл если его еще нет
//        try {
//            fileTransaction.createNewFile();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        try(BufferedWriter bWriter = new BufferedWriter(new FileWriter(fileTransaction,true)))
//        {
//            bWriter.write(""+currentId);
//            bWriter.newLine();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        for (Transaction tr : transactionList) {
//            try(BufferedWriter bWriter = new BufferedWriter(new FileWriter(fileTransaction,true)))
//            {
//                String str=tr.getIdUser() + "^" + tr.getCurrencyName() + "^" + tr.getTypeTransaction() + "^" +
//                        tr.getSumma() + "^" + tr.getDateTransaction() + "^" + tr.getIdUserRecipient() + "^" +
//                        tr.getIdUserOut() + "^" + tr.getNumberTransaction();
//                bWriter.write(str);
//                bWriter.newLine();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }

    public void writeTransactionToFile() {
        File path = new File("src/files");
        path.mkdirs(); // Создаем директорию, если она не существует
        File fileTransaction = new File(path, "transaction.txt");

        // Если список транзакций пуст или не инициализирован, выходим
        if (transactionList == null || transactionList.isEmpty()) {
            return;
        }

        // Удаляем файл, если он существует, и создаем новый
        if (fileTransaction.exists()) {
            fileTransaction.delete();
        }

        // Создание нового файла, если его нет
        try {
            fileTransaction.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Записываем currentId в файл
        try (BufferedWriter bWriter = new BufferedWriter(new FileWriter(fileTransaction, true))) {
            bWriter.write(String.valueOf(currentId));
            bWriter.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Записываем транзакции
        for (Transaction tr : transactionList) {
            String transactionData = formatTransaction(tr);
            writeTransactionToFile(fileTransaction, transactionData);
        }
    }

    // Метод для записи одной транзакции в файл
    private void writeTransactionToFile(File file, String transactionData) {
        try (BufferedWriter bWriter = new BufferedWriter(new FileWriter(file, true))) {
            bWriter.write(transactionData);
            bWriter.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Метод для форматирования строки транзакции
    private String formatTransaction(Transaction tr) {
        return tr.getIdUser() + "^" +
                tr.getCurrencyName() + "^" +
                tr.getTypeTransaction() + "^" +
                tr.getSumma() + "^" +
                tr.getDateTransaction() + "^" +
                tr.getIdUserRecipient() + "^" +
                tr.getIdUserOut() + "^" +
                tr.getNumberTransaction();
    }


//    public void readTransactionFromFile(){
//        File path = new File("src/files");
//        File fileTransaction = new File(path,"transaction.txt");
//        if(fileTransaction.exists() == false || fileTransaction.length() == 0) {
//            return;
//        }
//        try(BufferedReader bReader = new BufferedReader(new FileReader(fileTransaction)))
//        {
//            String line;
//            line = bReader.readLine();
//            currentId = Integer.parseInt(line);
//            while ((line = bReader.readLine()) != null) {
//                if(line.length() == 0) continue;
//                String [] parts = line.split("\\^");
//                int id = Integer.parseInt(parts[0]);
//                LocalDateTime dateTr = LocalDateTime.parse(parts[4]);
//                String curName = null;
//                curName = parts[1];
//                TransactionName trName;
//                trName = TransactionName.valueOf(parts[2]);
//                Transaction tr = new Transaction(Integer.parseInt(parts[0]), curName,trName,
//                        Double.parseDouble(parts[3]), dateTr,Integer.parseInt(parts[5]),
//                        Integer.parseInt(parts[6]), Integer.parseInt(parts[7]));
//                transactionList.add(tr);
//            }
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public void readTransactionFromFile() {
        File fileTransaction = new File("src/files", "transaction.txt");

        // Проверяем, существует ли файл и не является ли он пустым
        if (!fileTransaction.exists() || fileTransaction.length() == 0) {
            return;
        }

        try (BufferedReader bReader = new BufferedReader(new FileReader(fileTransaction))) {
            // Читаем и устанавливаем текущий ID
            String line = bReader.readLine();
            if (line != null) {
                currentId = Integer.parseInt(line.trim());
            }

            // Читаем транзакции из файла
            while ((line = bReader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Transaction transaction = parseTransaction(line);
                    transactionList.add(transaction);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Метод для парсинга строки в объект Transaction
    private Transaction parseTransaction(String line) {
        String[] parts = line.split("\\^");
        if (parts.length != 8) {
            throw new IllegalArgumentException(line);
        }

        int idUser = Integer.parseInt(parts[0].trim());
        String currencyName = parts[1].trim();
        TransactionName transactionType = TransactionName.valueOf(parts[2].trim());
        double sum = Double.parseDouble(parts[3].trim());
        LocalDateTime dateTransaction = LocalDateTime.parse(parts[4].trim());
        int idUserRecipient = Integer.parseInt(parts[5].trim());
        int idUserOut = Integer.parseInt(parts[6].trim());
        int numberTransaction = Integer.parseInt(parts[7].trim());

        return new Transaction(idUser, currencyName, transactionType, sum, dateTransaction, idUserRecipient, idUserOut, numberTransaction);
    }
}

