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

    public static ArrayList<Operators> FileInput(String filename) throws FileNotFoundException {
        ArrayList<Operators> operators = new ArrayList<Operators>();
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
                operators.add(new Operators(values[0], Integer.parseInt(values[1])));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("File read error");
        }

        return operators;
    }

    public static void printOperators(ArrayList<Operators> operators) {
        System.out.printf("%18s %11s\n", "Name", "Code");
        for (Operators ops : operators) {
            System.out.printf("%18s %11s\n", ops.getOperatorsName(), ops.getOperatorCode());
        }
    }

    public static void RunApplication() throws IOException {
        Scanner sc = new Scanner(System.in);
        ArrayList<Operators> operators;
        operators = FileInput("ops.csv");

        while (true) {
            System.out.println("Choose an action: \n" + "\t1.Show all operators \n" + "\t2. \n");
            int firstInput = sc.nextInt();
            if (firstInput == 1) {
                printOperators(operators);
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