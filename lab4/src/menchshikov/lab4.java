import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

class Operators {
    private String operatorsName;
    private int operatorCode;

    public Operators() {
    }

    public Operators(String operatorsName, int operatorCode) {
        this.operatorsName = operatorsName;
        this.operatorCode = operatorCode;
    }

    public String getOperatorsName() {
        return this.operatorsName;
    }

    public void setOperatorsName(String operatorsName) {
        this.operatorsName = operatorsName;
    }

    public int getOperatorCode() {
        return this.operatorCode;
    }

    public void setOperatorCode(int operatorCode) {
        this.operatorCode = operatorCode;
    }
}

class Services extends Operators {
    private int internet4g;
    private int internet5g;
    private int internetDist;

    public Services() {
    }

    public Services(String operatorsName, int operatorCode, int internet4g, int internet5g, int internetDist) {
        super(operatorsName, operatorCode);
        this.internet4g = internet4g;
        this.internet5g = internet5g;
        this.internetDist = internetDist;
    }

    public int getInternet4g() {
        return this.internet4g;
    }

    public void setInternet4g(int internet4g) {
        this.internet4g = internet4g;
    }

    public int getInternet5g() {
        return this.internet5g;
    }

    public void setInternet5g(int internet5g) {
        this.internet5g = internet5g;
    }

    public int getInternetDist() {
        return this.internetDist;
    }

    public void setInternetDist(int internetDist) {
        this.internetDist = internetDist;
    }
}

class lab4 {
    public static File searchFile(File directory, String filename) {
        File result = null;
        File[] directoryList = directory.listFiles();

        for (int i = 0; i < directoryList.length; i++) {
            if (directoryList[i].isDirectory()) {
                result = searchFile(directoryList[i], filename);
                if (result != null)
                    break;
            } else if (directoryList[i].getName().startsWith(filename)) {
                return directoryList[i];
            }
        }
        return result;
    }

    public static ArrayList<Services> FileInput(String filename) throws FileNotFoundException {
        ArrayList<Services> services = new ArrayList<Services>();
        File file = searchFile(new File(System.getProperty("user.dir")), filename);
        if (file == null)
            return null;
        FileReader fReader = new FileReader(file);
        BufferedReader bReader = new BufferedReader(fReader);

        try {
            String line = bReader.readLine();
            while (true) {
                line = bReader.readLine();
                if (line == null)
                    break;
                String[] values = line.split(";");
                services.add(new Services(values[0], Integer.parseInt(values[1]), Integer.parseInt(values[2]),
                        Integer.parseInt(values[3]), Integer.parseInt(values[4])));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("File read error");
        }

        return services;
    }

    public static void printOperators(ArrayList<Services> operators) {
        System.out.printf("%18s %11s\n", "Name", "Code");
        for (Operators ops : operators) {
            System.out.printf("%18s %11s\n", ops.getOperatorsName(), ops.getOperatorCode());
        }
    }

    public static void printServices(ArrayList<Services> services) {
        System.out.printf("%18s %11s %10s %10s %20s\n", "Name", "Code", "4G", "5G", "Inernet Distribution");
        for (Services serv : services) {
            if (serv.getInternet4g() == 0) {
                System.out.printf("%18s %11s %10s %10s %18s\n", serv.getOperatorsName(), serv.getOperatorCode(),
                        "-", serv.getInternet5g(), serv.getInternetDist());
            } else if (serv.getInternet5g() == 0) {
                System.out.printf("%18s %11s %10s %10s %18s\n", serv.getOperatorsName(), serv.getOperatorCode(),
                        serv.getInternet4g(), "-", serv.getInternetDist());
            } else if (serv.getInternetDist() == 0) {
                System.out.printf("%18s %11s %10s %10s %18s\n", serv.getOperatorsName(), serv.getOperatorCode(),
                        serv.getInternet4g(), serv.getInternet5g(), "-");
            } else {
                System.out.printf("%18s %11s %10s %10s %18s\n", serv.getOperatorsName(), serv.getOperatorCode(),
                        serv.getInternet4g(), serv.getInternet5g(), serv.getInternetDist());
            }
        }
    }

    public static void printSpecificServices(ArrayList<Services> services, int serviceNumber) {
        System.out.printf("%18s %11s %10s\n", "Name", "Code", "Coast");
        if (serviceNumber == 1) {
            for (Services serv : services) {
                if (serv.getInternet4g() == 0) {
                    continue;
                }
                System.out.printf("%18s %11s %10s\n", serv.getOperatorsName(), serv.getOperatorCode(),
                        serv.getInternet4g());
            }
        } else if (serviceNumber == 2) {
            for (Services serv : services) {
                if (serv.getInternet5g() == 0) {
                    continue;
                }
                System.out.printf("%18s %11s %10s\n", serv.getOperatorsName(), serv.getOperatorCode(),
                        serv.getInternet5g());
            }
        } else if (serviceNumber == 2) {
            for (Services serv : services) {
                if (serv.getInternetDist() == 0) {
                    continue;
                }
                System.out.printf("%18s %11s %10s\n", serv.getOperatorsName(), serv.getOperatorCode(),
                        serv.getInternetDist());
            }
        }
    }

    public static void RunApplication() throws IOException {
        Scanner sc = new Scanner(System.in);
        ArrayList<Services> services;
        services = FileInput("ops.csv");

        while (true) {
            System.out.println(
                    "Choose an action: \n" + "\t1.Show all operators \n" + "\t2.Show all operators with services \n"
                            + "\t3.Show operators with a specific service\n");
            int firstInput = sc.nextInt();
            if (firstInput == 1) {
                printOperators(services);
                System.out.println("\nPress Enter to continue...");
                System.in.read();
            } else if (firstInput == 2) {
                printServices(services);
                System.out.println("\nPress Enter to continue...");
                System.in.read();
            } else if (firstInput == 3) {
                System.out.println("Choose a service:\n" + "\t1.4G\n" + "\t2.5G\n" + "\t3.Internet Distribution");
                int serviceNumber = sc.nextInt();
                printSpecificServices(services, serviceNumber);
                System.out.println("\nPress Enter to continue...");
                System.in.read();
            } else {
                break;
            }
        }
        sc.close();
    }

    public static void main(String[] args) throws IOException {
        lab4.RunApplication();
    }
}