import java.util.*;
import java.io.*;

class Person {
    private String firstname;
    private String secondname;
    private int age;

    public Person() {}

    public Person(String firstname, String secondname, int age) {
        this.firstname = firstname;
        this.secondname = secondname;
        this.age = age;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSecondname() {
        return this.secondname;
    }

    public void setSecondname(String secondname) {
        this.secondname = secondname;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFullName() {
        return this.getFirstname() + " " + this.getSecondname();
    }
}

class Employer extends Person {
    private String INN;
    private String SNILS;

    public Employer() {}

    public Employer(String firstname, String secondname, int age, String INN, String SNILS) {
        super(firstname,secondname,age);
        this.INN = INN;
        this.SNILS = SNILS;
    }

    public void setINN(String INN) {
        this.INN = INN;
    }

    public String getINN() {
        return this.INN;
    }

    public void setSNILS(String SNILS) {
        this.SNILS = SNILS;
    }


    public String getSNILS() {
        return this.SNILS;
    }

}

class Developer extends Employer {
    private String level;
    private String language;
    private int fee;

    public Developer() {
    }

    public Developer(String firstname, String secondname, int age, String INN,
                     String SNILS, String level, String language, int fee) {
        super(firstname, secondname, age, INN, SNILS);
        this.level = level;
        this.language = language;
        this.fee = fee;
    }

    public String getLevel() {
        return this.level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getFee() {
        return this.fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

}

public class Lab4 {

    public static ArrayList<Developer> FileInput(String filename) throws FileNotFoundException {
        ArrayList<Developer> developers = new ArrayList<Developer>();
        File file = recursiveFileSearch(new File(System.getProperty("user.dir")), filename);
        if (file == null) return null;
        FileReader fReader = new FileReader(file);
        BufferedReader bReader = new BufferedReader(fReader);

        try {
            String line = bReader.readLine();
            while (true) {
                line = bReader.readLine();
                if (line == null) break;
                String[] values = line.split(";");
                developers.add(new Developer(values[0], values[1], Integer.parseInt(values[2]), values[3], values[4],
                        values[5], values[6], Integer.parseInt(values[7])));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("File read error");
        }

        return developers;
    }

    public static File recursiveFileSearch(File directory, String filename) {
        File result = null;
        File[] directoryList = directory.listFiles();

        for (int i = 0; i < directoryList.length; i++) {
            if (directoryList[i].isDirectory()) {
                result = recursiveFileSearch(directoryList[i], filename);
                if (result != null) break;
            } else if (directoryList[i].getName().startsWith(filename)) {
                return directoryList[i];
            }
        }
        return result;
    }

    public static ArrayList<String> ParseLanguages(ArrayList<Developer> devs) {
        ArrayList<String> langs = new ArrayList<>();
        for (Developer dev : devs) {
            String language = dev.getLanguage();
            if (!langs.contains(language)) {
                langs.add(language);
            }
        }
        return langs;
    }

    public static ArrayList<String> ParseLevels(ArrayList<Developer> devs) {
        ArrayList<String> levels = new ArrayList<>();
        for (Developer dev : devs) {
            String level = dev.getLevel();
            if (!levels.contains(level)) {
                levels.add(level);
            }
        }
        return levels;
    }

    public static void Menu() {
        System.out.println("Выберите действие:");
        System.out.println("1.Посмотреть полный список разработчиков");
        System.out.println("2.Поиск разработчиков по уровню");
        System.out.println("3.Поиск разработчиков по языку");
        System.out.println("4.Поиск разработчиков по З/П");
        System.out.println("5.Выход");
    }

    public static void PrintArray(ArrayList<Developer> developers) {
        System.out.printf("%18s %11s %7s %11s %11s %11s %11s\n", "ФИО", "Возраст", "ИНН", "СНИЛС", "Уровень", "Язык", "З/П");
        for (Developer dev : developers) {
            System.out.printf("%18s %11s %7s %11s %11s %11s %11s\n", dev.getFullName(), dev.getAge(), dev.getINN(),
                    dev.getSNILS(), dev.getLevel(), dev.getLanguage(), dev.getFee());
        }
    }

    public static ArrayList<Developer> FindByLevel(ArrayList<Developer> devs, String level) {
        ArrayList<Developer> LevelDevs = new ArrayList<>();
        for (Developer dev : devs) {
            String DevLevel = dev.getLevel();
            if (DevLevel.equals(level)) {
                LevelDevs.add(dev);
            }
        }
        return LevelDevs;
    }

    public static ArrayList<Developer> FindByLanguage(ArrayList<Developer> devs, String lang) {
        ArrayList<Developer> LanguageDevs = new ArrayList<>();
        for (Developer dev : devs) {
            String DevLang = dev.getLanguage();
            if (DevLang.equals(lang)) {
                LanguageDevs.add(dev);
            }
        }
        return LanguageDevs;
    }

    public static ArrayList<Developer> FindByFee(ArrayList<Developer> devs, int regime, int left, int right) {
        ArrayList<Developer> DevFees = new ArrayList<>();
        for (Developer dev : devs) {
            int DevFee = dev.getFee();
            if (DevFee <= left && regime == 1) {
                DevFees.add(dev);
            } else if (DevFee >= left && regime == 2) {
                DevFees.add(dev);
            } else if (DevFee >= left && DevFee <= right && regime == 3) {
                DevFees.add(dev);
            }
        }
        return DevFees;
    }

    public static void RunApplication() throws IOException {
        ArrayList<Developer> developers;
        developers = FileInput("devs.csv");
        ArrayList<String> languages = new ArrayList<>();
        languages = ParseLanguages(developers);
        ArrayList<String> levels = new ArrayList<>();
        levels = ParseLevels(developers);
        Scanner in = new Scanner(System.in);
        while (true) {
            Menu();
            int mode = in.nextInt();
            if (mode < 1 || mode > 5) {
                System.out.println("Выберите действие в диапозоне от 1 до 5.\n");
                continue;
            }
            if (mode == 1) {
                PrintArray(developers);
                System.out.println("\nНажмите Enter, чтобы продолжить...");
                System.in.read();
            } else if (mode == 2) {
                System.out.println("\nВыберите уровень:");
                int i = 1;
                for (String level : levels) {
                    System.out.println(Integer.toString(i) + "." + level);
                    i++;
                }
                int level;
                while(true) {
                    level = in.nextInt();
                    if (level < 1 || level > levels.size()) {
                        System.out.println("Введите значение в диапазоне от 1 до " + Integer.toString(levels.size()));
                    } else {
                        break;
                    }
                }
                ArrayList<Developer> LevelDevs = new ArrayList<>();
                LevelDevs = FindByLevel(developers, levels.get(level - 1));
                PrintArray(LevelDevs);
                System.out.println("\nНажмите Enter, чтобы продолжить...");
                System.in.read();
            } else if (mode == 3) {
                System.out.println("\nВыберите язык:");
                int i = 1;
                for (String lang : languages) {
                    System.out.println(Integer.toString(i) + "." + lang);
                    i++;
                }
                int language;
                while(true) {
                    language = in.nextInt();
                    if (language < 1 || language > languages.size()) {
                        System.out.println("Введите значение в диапазоне от 1 до " + Integer.toString(languages.size()));
                    } else {
                        break;
                    }
                }
                ArrayList<Developer> LanguageDevs = new ArrayList<>();
                LanguageDevs = FindByLanguage(developers, languages.get(language - 1));
                PrintArray(LanguageDevs);
                System.out.println("\nНажмите Enter, чтобы продолжить...");
                System.in.read();
            } else if (mode == 4) {
                System.out.println("\nВыберите режим поиска:");
                int regime;
                while(true) {
                    System.out.println("1.Меньше вводимого значения\n2.Больше вводимого значения\n3.В вводимом диапазоне");
                    regime = in.nextInt();
                    if(regime < 1 || regime > 3) {
                        System.out.println("Введите значение в диапазоне от 1 до 3\n");
                    }
                    else {
                        break;
                    }
                }
                int left = 0, right = 0;
                if (regime == 1 || regime == 2) {
                    System.out.println("\nВведите значение");
                    left = in.nextInt();
                } else if (regime == 3) {
                    System.out.println("\nВведите первое значение");
                    left = in.nextInt();
                    System.out.println("\nВведите второе значение");
                    right = in.nextInt();
                    if (left > right) {
                        int tmp = right;
                        right = left;
                        left = tmp;
                    }
                }
                ArrayList<Developer> DevFees = FindByFee(developers, regime, left, right);
                if (DevFees.isEmpty()) {
                    System.out.println("Список пуст\n");
                } else {
                    System.out.println("\nВыберите режим сортровки:");
                    int sortregime;
                    while(true) {
                        System.out.println("1.По возрастанию\n2.По убыванию\n3.Без сортировки");
                        sortregime = in.nextInt();
                        if (sortregime < 1 || sortregime > 3) {
                            System.out.println("Введите значение в диапазоне от 1 до 3");
                        } else {
                            break;
                        }
                    }
                    if (sortregime == 1) {
                        Comparator<Developer> AscendingOrder = new Comparator<Developer>() {
                            @Override
                            public int compare(Developer o1, Developer o2) {
                                return Integer.compare(o1.getFee(), o2.getFee());
                            }
                        };
                        DevFees.sort(AscendingOrder);
                        PrintArray(DevFees);
                    } else if (sortregime == 2) {
                        Comparator<Developer> DescendingOrder = new Comparator<Developer>() {
                            @Override
                            public int compare(Developer o1, Developer o2) {
                                return Integer.compare(o2.getFee(), o1.getFee());
                            }
                        };
                        DevFees.sort(DescendingOrder);
                        PrintArray(DevFees);
                    } else if (sortregime == 3) {
                        PrintArray(DevFees);
                    }
                }
                System.out.println("\nНажмите Enter, чтобы продолжить...");
                System.in.read();
            } else if (mode == 5) {
                in.close();
                break;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Lab4.RunApplication();
    }
}