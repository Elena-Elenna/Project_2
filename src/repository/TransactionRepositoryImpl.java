package repository;

import model.Transaction;
import model.TransactionName;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionRepositoryImpl implements TransactionRepository{
    //поля
    private List<Transaction> transactionList = new ArrayList<>();//список транзакций всех пользователей
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

    public void writeTransactionToFile(){
        File path = new File("src/files");
        path.mkdirs();
        File fileTransaction = new File(path,"transaction.txt");
        if(transactionList == null) return;
        if(transactionList.size() == 0)  return;
        if(fileTransaction.exists()) fileTransaction.delete();
        try {
            fileTransaction.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try(BufferedWriter bWriter = new BufferedWriter(new FileWriter(fileTransaction,true)))
        {
            bWriter.write(""+currentId);
            bWriter.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (Transaction tr : transactionList) {
            try(BufferedWriter bWriter = new BufferedWriter(new FileWriter(fileTransaction,true)))
            {
                String str=tr.getIdUser() + "^" + tr.getCurrencyName() + "^" + tr.getTypeTransaction() + "^" +
                        tr.getSumma() + "^" + tr.getDateTransaction() + "^" + tr.getIdUserRecipient() + "^" +
                        tr.getIdActiveUser() + "^" + tr.getNumberTransaction();
                bWriter.write(str);
                bWriter.newLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void readTransactionFromFile(){
        File path = new File("src/files");
        File fileTransaction = new File(path,"transaction.txt");
        if(fileTransaction.exists() == false || fileTransaction.length() == 0) {
            return;
        }
        try(BufferedReader bReader = new BufferedReader(new FileReader(fileTransaction)))
        {
            String line;
            line = bReader.readLine();
            currentId = Integer.parseInt(line);
            while ((line = bReader.readLine()) != null) {
                if(line.length() == 0) continue;
                String [] parts = line.split("\\^");
                int id = Integer.parseInt(parts[0]);
                LocalDateTime dateTr = LocalDateTime.parse(parts[4]);
                String curName = null;
                curName = parts[1];
                TransactionName trName;
                trName = TransactionName.valueOf(parts[2]);
                Transaction tr = new Transaction(Integer.parseInt(parts[0]), curName,trName,
                        Double.parseDouble(parts[3]), dateTr,Integer.parseInt(parts[5]),
                        Integer.parseInt(parts[6]), Integer.parseInt(parts[7]));
                transactionList.add(tr);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

