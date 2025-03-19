package repository;

import model.CourseCurrency;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CourseRepositoryImpl  implements CourseRepository{

    //поле (история всех курсов валют)
    private List<CourseCurrency> courses = new ArrayList<>();
    // (список наименований доступных валют)
    private Map<String,String> namesCurrency = new LinkedHashMap<>();


    //конструктор
    public CourseRepositoryImpl() {
        readCurseFromFile();
        readCurseNameFromFile();
    }

    @Override
    public Map<String, String> getNamesCurrency() {
        return namesCurrency;
    }

    public void addNameCurrency(String name, String fullName){
        namesCurrency.put(name,fullName);
    }

    public List<CourseCurrency> getCourses() {
        return courses;
    }

    public CourseCurrency getCourseLast(){
        if(courses == null) return null;
        if(courses.size() == 0) return null;
        return courses.get(courses.size() - 1);
    }

    public String  getCurrencyMain(){
        if(courses == null) return null;
        if(courses.size() == 0) return null;
        return courses.get(courses.size() - 1).getCurrencyMain();
    }

    public void addCourse(CourseCurrency courseCurrency) {
        courses.add(courseCurrency);
    }

    public double getCourseByCurrencyName(String currencyName){
        CourseCurrency courseCurrency = courses.get(courses.size() - 1);
        return courseCurrency.getCourse().get(currencyName);
    }

    @Override
    public String toString() {
        return "CourseRepositoryImpl {" +
                "courses = " + courses +
                "; namesCurrency = " + namesCurrency +
                '}';
    }

    public void writeCurseToFile(){
//        File path = new File("src/files");
//        path.mkdirs();
//        File fileCurses = new File(path,"curses.txt");
//        if(courses == null) return;
//        if(courses.size() == 0)  return;
//        if(fileCurses.exists()) fileCurses.delete();
//        //создать файл если его еще нет
//        try {
//            fileCurses.createNewFile();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        try(BufferedWriter bWriter1 = new BufferedWriter(new FileWriter(fileCurses,true))) //флаг
//        {
//            int i = 0;
//            for (CourseCurrency cs : courses) {
//                i++;
//                Map<String,Double> map = new LinkedHashMap<>();
//                Map<String,String> mapName = new LinkedHashMap<>();
//                map = cs.getCourse();
//                mapName = cs.getCourseFillName();
//                String  curMainName = cs.getCurrencyMain().trim();
//                LocalDate dateCurr = cs.getDateCurrency();
//                if(i == 1) {
//                    bWriter1.write("**");
//                    bWriter1.newLine();// перевод каретки
//                }
//                bWriter1.write("" + curMainName.trim());
//                bWriter1.newLine();
//                bWriter1.write("" + dateCurr.toString().trim());
//                bWriter1.newLine();
//                for (Map.Entry<String,Double> entry : map.entrySet()) {
//                    String str1 = entry.getKey().trim() + "^" + entry.getValue().toString().trim();
//                    bWriter1.write(str1);
//                    bWriter1.newLine();
//                }
//                bWriter1.write("*");
//                bWriter1.newLine();
//                for (Map.Entry<String,String> entry : mapName.entrySet()) {
//                    String str1 = entry.getKey().trim() + "^" + entry.getValue().trim();
//                    bWriter1.write(str1);
//                    bWriter1.newLine();
//                }
//                bWriter1.write("**");
//                bWriter1.newLine();
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        if (courses == null || courses.isEmpty()) return;
        File fileCurses = new File("src/files/curses.txt");
        fileCurses.getParentFile().mkdirs();
        if (fileCurses.exists()) fileCurses.delete();
        try {
            fileCurses.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (BufferedWriter bWriter = new BufferedWriter(new FileWriter(fileCurses, true))) {
            boolean isFirst = true;
            for (CourseCurrency cs : courses) {
                if (isFirst) {
                    bWriter.write("**");
                    bWriter.newLine();
                    isFirst = false;
                }
                bWriter.write(cs.getCurrencyMain().trim());
                bWriter.newLine();
                bWriter.write(cs.getDateCurrency().toString().trim());
                bWriter.newLine();
                for (Map.Entry<String, Double> entry : cs.getCourse().entrySet()) {
                    bWriter.write(entry.getKey().trim() + "^" + entry.getValue().toString().trim());
                    bWriter.newLine();
                }
                bWriter.write("*");
                bWriter.newLine();
                for (Map.Entry<String, String> entry : cs.getCourseFillName().entrySet()) {
                    bWriter.write(entry.getKey().trim() + "^" + entry.getValue().trim());
                    bWriter.newLine();
                }
                bWriter.write("**");
                bWriter.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readCurseFromFile(){
//        File path = new File("src/files");
//        File fileCurses = new File(path,"curses.txt");
//        if(fileCurses.exists() == false || fileCurses.length() == 0) {
//            return;
//        }
//        Map<String,Double> map = new LinkedHashMap<>();
//        Map<String,String> mapName = new LinkedHashMap<>();
//        try(BufferedReader bReader = new BufferedReader(new FileReader(fileCurses)))
//        {
//            String line;
//            int flagMap = 0;
//            int numStr = 0;
//            String cName = null;
//            LocalDate dateCur = null;
//            while ((line = bReader.readLine()) != null) {
//                if(line.length() == 0) continue;
//                numStr++;
//                if(line.equals("**")){
//                    numStr = 0;
//                    if(flagMap == 1) {
//                        CourseCurrency cc = new CourseCurrency();
//                        cc.setDateCurrency(dateCur);
//                        cc.setCurrencyMain(cName);
//                        Map<String,Double> map_1 = new LinkedHashMap<>(map);
//                        Map<String,String> mapName_1 = new LinkedHashMap<>(mapName);
//                        cc.setCourse(map_1);
//                        cc.setCourseFillName(mapName_1);
//                        courses.add(cc);
//                        map.clear();
//                        mapName.clear();
//                    }
//                    flagMap = 0;
//                    continue;
//                }
//                if(line.equals("*")){
//                    flagMap = 1;
//                    continue;
//                }
//                if(flagMap == 0 && numStr == 1){
//                    cName= line.trim();
//                    continue;
//                }
//                if(flagMap == 0 && numStr == 2){
//                    dateCur = LocalDate.parse(line);
//                    continue;
//                }
//                if(flagMap == 0 && numStr > 2){
//                    String[] part = line.split("\\^");
//                    map.put(part[0].trim(), Double.parseDouble(part[1]));
//                    continue;
//                }
//                if(flagMap == 1 && numStr > 2){
//                    String[] part = line.split("\\^");
//                    String cn = part[0].trim();
//                    mapName.put(cn,part[1]);
//                    continue;
//                }
//            }
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        File fileCurses = new File("src/files/curses.txt");
        if (!fileCurses.exists() || fileCurses.length() == 0) {
            return;
        }
        Map<String, Double> map = new LinkedHashMap<>();
        Map<String, String> mapName = new LinkedHashMap<>();
        try (BufferedReader bReader = new BufferedReader(new FileReader(fileCurses))) {
            String line;
            boolean isReadingNames = false;
            int lineCount = 0;
            String currencyName = null;
            LocalDate dateCurrency = null;
            while ((line = bReader.readLine()) != null) {
                if (line.isEmpty()) continue;
                if (line.equals("**")) {
                    if (!map.isEmpty() || !mapName.isEmpty()) {
                        CourseCurrency cc = new CourseCurrency();
                        cc.setDateCurrency(dateCurrency);
                        cc.setCurrencyMain(currencyName);
                        cc.setCourse(new LinkedHashMap<>(map));
                        cc.setCourseFillName(new LinkedHashMap<>(mapName));
                        courses.add(cc);
                        map.clear();
                        mapName.clear();
                    }
                    isReadingNames = false;
                    lineCount = 0;
                    continue;
                }
                if (line.equals("*")) {
                    isReadingNames = true;
                    continue;
                }
                lineCount++;
                if (!isReadingNames) {
                    if (lineCount == 1) {
                        currencyName = line.trim();
                    } else if (lineCount == 2) {
                        dateCurrency = LocalDate.parse(line.trim());
                    } else {
                        String[] part = line.split("\\^");
                        map.put(part[0].trim(), Double.parseDouble(part[1]));
                    }
                } else {
                    String[] part = line.split("\\^");
                    mapName.put(part[0].trim(), part[1]);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void writeCurseNameToFile() {
//        File path = new File("src/files");
//        path.mkdirs();
//        File fileCurrName = new File(path, "currencyName.txt");
//        if (namesCurrency == null) return;
//        if (namesCurrency.size() == 0) return;
//        if (fileCurrName.exists()) fileCurrName.delete();
//        //создать файл если его еще нет
//        try {
//            fileCurrName.createNewFile();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        try (BufferedWriter bWriter1 = new BufferedWriter(new FileWriter(fileCurrName, true))) //флаг
//        {
//            for (Map.Entry<String, String> entry : namesCurrency.entrySet()) {
//                bWriter1.write("" + entry.getKey().trim() + "^" + entry.getValue().trim());
//                bWriter1.newLine();
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        if (namesCurrency == null || namesCurrency.isEmpty()) return;
        File fileCurrName = new File("src/files/currencyName.txt");
        fileCurrName.getParentFile().mkdirs();
        if (fileCurrName.exists()) fileCurrName.delete();
        try {
            fileCurrName.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileCurrName, true))) {
            for (Map.Entry<String, String> entry : namesCurrency.entrySet()) {
                writer.write(entry.getKey().trim() + "^" + entry.getValue().trim());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
//TODO

//    public void readCurseNameFromFile() {
//        File path = new File("src/files");
//        File fileCurrName = new File(path, "currencyName.txt");
//        if(fileCurrName.exists() == false || fileCurrName.length() == 0) {
//            this.namesCurrency.put("USD","Доллар");
//            this.namesCurrency.put("EUR","Евро");
//            this.namesCurrency.put("PLN","Злотый");
//            this.namesCurrency.put("JPY","Йена");
//            this.namesCurrency.put("CZK","Крона");
//            return;
//        }
//        Map<String, String> mapName = new LinkedHashMap<>();
//        try(BufferedReader bReader = new BufferedReader(new FileReader(fileCurrName)))
//        {
//            String line;
//            while ((line = bReader.readLine()) != null) {
//                if (line.length() == 0) continue;
//                String[] part = line.split("\\^");
//                namesCurrency.put(part[0].trim(),part[1].trim());
//            }
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public void readCurseNameFromFile() {
        File fileCurrName = new File("src/files", "currencyName.txt");
        // Если файл не существует или пустой, устанавливаем стандартные значения
        if (!fileCurrName.exists() || fileCurrName.length() == 0) {
            setDefaultCurrencyNames();
            return;
        }
        // Чтение данных из файла и добавление их в карту
        try (BufferedReader bReader = new BufferedReader(new FileReader(fileCurrName))) {
            String line;
            while ((line = bReader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split("\\^");
                    if (parts.length == 2) {
                        namesCurrency.put(parts[0].trim(), parts[1].trim());
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении файла", e);
        }
    }

    // Метод для установки стандартных значений валют
    private void setDefaultCurrencyNames() {
        namesCurrency.put("USD", "Доллар");
        namesCurrency.put("EUR", "Евро");
        namesCurrency.put("PLN", "Злотый");
        namesCurrency.put("JPY", "Йена");
        namesCurrency.put("CZK", "Крона");
    }
}