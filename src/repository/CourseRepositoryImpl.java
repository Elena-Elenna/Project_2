package repository;

import model.CourseCurrency;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CourseRepositoryImpl  implements CourseRepository{

    //поля
    private List<CourseCurrency> courses = new ArrayList<>();//история всех курсов валют
    private Map<String,String> namesCurrency = new LinkedHashMap<>();//список наименований доступных валют

    //конструктор
    public CourseRepositoryImpl() {
        readCurseFromFile();
        readCurseNameFromFile();
    }


    @Override
    public Map<String, String> getNamesCurrency() {
        return namesCurrency;
    }

    public void addNameCurrency(String newCurrencyName, String newCurrencyFullName){
        namesCurrency.put(newCurrencyName, newCurrencyFullName);
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
        File path = new File("src/files");
        path.mkdirs();
        File fileCurses = new File(path,"curses.txt");
        if(courses == null) return;
        if(courses.size() == 0)  return;
        if(fileCurses.exists()) fileCurses.delete();
        try {
            fileCurses.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try(BufferedWriter bWriter1 = new BufferedWriter(new FileWriter(fileCurses,true)))
        {
            int i = 0;
            for (CourseCurrency cs : courses) {
                i++;
                Map<String,Double> map = new LinkedHashMap<>();
                Map<String,String> mapName = new LinkedHashMap<>();
                map = cs.getCourse();
                mapName = cs.getCourseFillName();
                String  curMainName = cs.getCurrencyMain().trim();
                LocalDate dateCurr = cs.getDateCurrency();
                if(i == 1) {
                    bWriter1.write("**");
                    bWriter1.newLine();
                }
                bWriter1.write("" + curMainName.trim());
                bWriter1.newLine();
                bWriter1.write("" + dateCurr.toString().trim());
                bWriter1.newLine();
                for (Map.Entry<String,Double> entry : map.entrySet()) {
                    String str1 = entry.getKey().trim() + "^" + entry.getValue().toString().trim();
                    bWriter1.write(str1);
                    bWriter1.newLine();
                }
                bWriter1.write("*");
                bWriter1.newLine();
                for (Map.Entry<String,String> entry : mapName.entrySet()) {
                    String str1 = entry.getKey().trim() + "^" + entry.getValue().trim();
                    bWriter1.write(str1);
                    bWriter1.newLine();
                }
                bWriter1.write("**");
                bWriter1.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readCurseFromFile(){
        File path = new File("src/files");
        File fileCurses = new File(path,"curses.txt");
        if(fileCurses.exists() == false || fileCurses.length() == 0) {
            return;
        }
        Map<String,Double> map = new LinkedHashMap<>();
        Map<String,String> mapName = new LinkedHashMap<>();
        try(BufferedReader bReader = new BufferedReader(new FileReader(fileCurses)))
        {
            String line;
            int flagMap = 0;
            int numStr = 0;
            String cName = null;
            LocalDate dateCur = null;
            while ((line = bReader.readLine()) != null) {
                if(line.length() == 0) continue;
                numStr++;
                if(line.equals("**")){
                    numStr = 0;
                    if(flagMap == 1) {
                        CourseCurrency cc = new CourseCurrency();
                        cc.setDateCurrency(dateCur);
                        cc.setCurrencyMain(cName);
                        Map<String,Double> map_1 = new LinkedHashMap<>(map);
                        Map<String,String> mapName_1 = new LinkedHashMap<>(mapName);
                        cc.setCourse(map_1);
                        cc.setCourseFillName(mapName_1);
                        courses.add(cc);
                        map.clear();
                        mapName.clear();
                    }
                    flagMap = 0;
                    continue;
                }
                if(line.equals("*")){
                    flagMap = 1;
                    continue;
                }
                if(flagMap == 0 && numStr == 1){
                    cName= line.trim();
                    continue;
                }
                if(flagMap == 0 && numStr == 2){
                    dateCur = LocalDate.parse(line);
                    continue;
                }
                if(flagMap == 0 && numStr > 2){
                    String[] part = line.split("\\^");
                    map.put(part[0].trim(), Double.parseDouble(part[1]));
                    continue;
                }
                if(flagMap == 1 && numStr > 2){
                    String[] part = line.split("\\^");
                    String cn = part[0].trim();
                    mapName.put(cn,part[1]);
                    continue;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeCurseNameToFile() {
        File path = new File("src/files");
        path.mkdirs();
        File fileCurrName = new File(path, "currencyName.txt");
        if (namesCurrency == null) return;
        if (namesCurrency.size() == 0) return;
        if (fileCurrName.exists()) fileCurrName.delete();
        try {
            fileCurrName.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (BufferedWriter bWriter1 = new BufferedWriter(new FileWriter(fileCurrName, true)))
        {
            for (Map.Entry<String, String> entry : namesCurrency.entrySet()) {
                bWriter1.write("" + entry.getKey().trim() + "^" + entry.getValue().trim());
                bWriter1.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readCurseNameFromFile() {
        File path = new File("src/files");
        File fileCurrName = new File(path, "currencyName.txt");
        if(fileCurrName.exists() == false || fileCurrName.length() == 0) {
            this.namesCurrency.put("USD","Доллар");
            this.namesCurrency.put("EUR","Евро");
            this.namesCurrency.put("PLN","Злотый");
            this.namesCurrency.put("JPY","Йена");
            this.namesCurrency.put("CZK","Крона");
            return;
        }
        Map<String, String> mapName = new LinkedHashMap<>();
        try(BufferedReader bReader = new BufferedReader(new FileReader(fileCurrName)))
        {
            String line;
            while ((line = bReader.readLine()) != null) {
                if (line.length() == 0) continue;
                String[] part = line.split("\\^");
                namesCurrency.put(part[0].trim(),part[1].trim());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}